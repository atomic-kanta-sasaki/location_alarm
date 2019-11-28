package com.example.locationalarmproject

import android.annotation.SuppressLint
import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.icu.util.Calendar
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcel
import android.os.PersistableBundle
import android.text.format.DateFormat
import android.text.format.DateFormat.*
import android.view.View
import android.view.WindowManager.LayoutParams.*
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.startActivity
import com.google.android.material.snackbar.Snackbar
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_schedule_edit.*
import java.text.ParseException
import java.util.*
import java.text.SimpleDateFormat

class ScheduleEditActivity : AppCompatActivity() , TimeAlertDialog.Listener
    , TimeAlertDialog.DatePickerFragment.OnDateSelectedListener
    , TimeAlertDialog2.DatePickerFragment.OnDateSelectedListener {

    override fun onSelected2(year2: Int, month2: Int, date2: Int) {
        val str = String.format(Locale.US, "%d/%d/%d", year2, month2+1, date2)
        date2Text.text = str
    }

    override fun onSelected(year: Int, month: Int, date: Int) {
        val sdf = String.format(Locale.US, "%d/%d/%d", year, month+1, date)
        dateText.text = sdf
    }



    private lateinit var realm: Realm
    /**
     * 更新処理
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule_edit)
        realm = Realm.getDefaultInstance()


        val scheduleId = intent?.getLongExtra("schedule_id", -1L)
        if (scheduleId != -1L) {

            val schedule = realm.where<Schedule>()
                .equalTo("id", scheduleId).findFirst()

            titleEdit.setText(schedule?.title)
            detailEdit.setText(schedule?.detail)
            dateText.setText(DateFormat.format("yyyy/MM/dd", schedule?.date))
            date2Text.setText(DateFormat.format("yyyy/MM/dd", schedule?.date2))
            delete.visibility = View.VISIBLE

        } else {
            delete.visibility = View.INVISIBLE
        }

        save.setOnClickListener { view: View ->
            when (scheduleId) {
                -1L -> {
                    realm.executeTransaction { db: Realm ->
                        val maxId = db.where<Schedule>().max("id")
                        val nextId = (maxId?.toLong() ?: 0L) + 1
                        val schedule = db.createObject<Schedule>(nextId)
                        val date = dateText.text.toString().toDate("yyyy/MM/dd")
                        if (date != null) schedule.date = date
                        val date2 = date2Text.text.toString().toDate("yyyy/MM/dd")
                        if (date2 != null) schedule.date2 = date2
                        schedule.title = titleEdit.text.toString()
                        schedule.detail = detailEdit.text.toString()
                    }
                    finish()
                }
                else -> {
                    realm.executeTransaction { db: Realm ->
                        val schedule = db.where<Schedule>()
                            .equalTo("id", scheduleId).findFirst()
                        val date = dateText.text.toString()
                            .toDate("yyyy/MM/dd")
                        val date2 = date2Text.text.toString()
                            .toDate("yyyy/MM/dd")
                        schedule?.title = titleEdit.text.toString()
                        schedule?.detail = detailEdit.text.toString()
                    }
                    finish()
                }
            }
        }

        delete.setOnClickListener { view: View ->
            realm.executeTransaction { db: Realm ->
                db.where<Schedule>().equalTo("id", scheduleId)
                    ?.findFirst()
                    ?.deleteFromRealm()
            }
            finish()
        }

        address.setOnClickListener { onaddressButtonTapped(it) }
    }

    @SuppressLint("SimpleDateFormat")
    private fun String.toDate(pattern: String = "yyyy/MM/dd "): Date? {
        return try {
            SimpleDateFormat(pattern).parse(this)
        } catch (e: IllegalArgumentException) {
            return null
        } catch (e: ParseException) {
            return null
        }
    }

    fun describeContents(): Int {
        return 0
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        /**
         * スリープ解除
         */
        if (intent?.getBooleanExtra("onReceive", false) == true) {
            when {

                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1 -> {
                    setShowWhenLocked(true)
                    setTurnScreenOn(true)
                    val keyguardManager =
                        getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
                    keyguardManager.requestDismissKeyguard(this, null)
                }

                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                    window.addFlags(
                        FLAG_TURN_SCREEN_ON or FLAG_SHOW_WHEN_LOCKED
                    )
                    val keyguardManager =
                        getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
                    keyguardManager.requestDismissKeyguard(this, null)
                }
                else ->
                    window.addFlags(
                        FLAG_TURN_SCREEN_ON or
                                FLAG_SHOW_WHEN_LOCKED or FLAG_DISMISS_KEYGUARD
                    )
            }
            val dialog = TimeAlertDialog()
            dialog.show(supportFragmentManager, "alert_dialog")
        }
        setContentView(R.layout.activity_schedule_edit)

        dateText.setOnClickListener {
            val dialog = TimeAlertDialog.DatePickerFragment()
            dialog.show(supportFragmentManager, "date_dialog")
        }
        date2Text.setOnClickListener {
            val dialog2 = TimeAlertDialog2.DatePickerFragment()
            dialog2.show(supportFragmentManager, "date_dialog")
        }

    }


    /**
     * 住所テキストをタップした処理
     */
    fun onaddressButtonTapped(view: View?) {
        val intent = Intent(this, MapsActivity::class.java)
        startActivity(intent)
    }
    /**
     * データベースを終了
     */
    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    private fun Calendar.get(year: Int, month: Int, date: Int) {
    }
}


