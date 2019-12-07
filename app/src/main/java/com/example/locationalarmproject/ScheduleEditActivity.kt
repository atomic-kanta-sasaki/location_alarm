package com.example.locationalarmproject

import android.annotation.SuppressLint
import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager.LayoutParams.*
import android.widget.RadioButton
import androidx.annotation.RequiresApi
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_schedule_edit.*
import java.text.ParseException
import java.util.*
import java.text.SimpleDateFormat

class ScheduleEditActivity : AppCompatActivity() , TimeAlertDialog.Listener
    , TimeAlertDialog.DatePickerFragment.OnDateSelectedListener
    ,TimeAlertDialog2.DatePickerFragment.OnDateSelectedListener {

    /**
     * カレンダー
     * タップした日付をテキストに表示
     */
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onSelected(year: Int, month: Int, date: Int) {
        val stg = String.format(Locale.US, "%d/%d/%d", year, month+1, date)
        dateText.text = stg
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onSelected2(year2: Int, month2: Int, date2: Int) {
        val str = String.format(Locale.US, "%d/%d/%d", year2, month2+1, date2)
        date2Text.text = str
    }



    fun describeContents(): Int {
        return 0
    }
    private lateinit var realm: Realm
    /**
     * 更新処理
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule_edit)
        var realmConfigration = RealmConfiguration.Builder()
            .deleteRealmIfMigrationNeeded()
            .schemaVersion(0)
            .build()

        realm = Realm.getInstance(realmConfigration)
        /**
         * ラジオボタン
         */
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            rangeText.text = findViewById<RadioButton>(checkedId).text

            /**
             * 通知範囲処理分け
             */
            when (findViewById<RadioButton>(checkedId)) {
                range50 -> {}
                range100 -> {}
                range200 -> {}
            }

        }

        dateText.setOnClickListener{
            val dialog = TimeAlertDialog.DatePickerFragment()
            dialog.show(supportFragmentManager, "date_dialog")
        }
        date2Text.setOnClickListener{
            val dialog2 = TimeAlertDialog2.DatePickerFragment()
            dialog2.show(supportFragmentManager, "date_dialog")
        }

        val scheduleId = intent?.getLongExtra("schedule_id", -1L)
        if (scheduleId != -1L) {

            val  schedule = realm.where<Schedule>()
                .equalTo("id", scheduleId).findFirst()

            titleEdit.setText(schedule?.title)
            dateText.setText(schedule?.stg)
            date2Text.setText(schedule?.str)
            detailEdit.setText(schedule?.detail)
            rangeText.setText(schedule?.range)
            delete.visibility = View.VISIBLE

        }else{
            delete.visibility = View.INVISIBLE
        }
        /**
         * 保存タップ処理
         */
        save.setOnClickListener {view: View ->
            when (scheduleId){
                -1L -> {
                    realm.executeTransaction { db: Realm ->
                        val maxId = db.where<Schedule>().max("id")
                        val nextId = (maxId?.toLong() ?: 0L)  + 1
                        val schedule = db.createObject<Schedule>(nextId)

                        schedule.title = titleEdit.text.toString()
                        schedule.detail = detailEdit.text.toString()
                        schedule.stg=dateText.text.toString()
                        schedule.str = date2Text.text.toString()
                        schedule.range = rangeText.text.toString()
                    }
                    finish()
                }
                else -> {
                    realm.executeTransaction { db: Realm ->
                        val schedule = db.where<Schedule>()
                            .equalTo("id", scheduleId).findFirst()
                        schedule?.title = titleEdit.text.toString()
                        schedule?.detail = detailEdit.text.toString()
                        schedule?.stg=dateText.text.toString()
                        schedule?.str = date2Text.text.toString()
                        schedule?.range = rangeText.text.toString()
                    }
                    finish()
                }
            }
        }

        delete.setOnClickListener{ view: View ->
            realm.executeTransaction{db: Realm ->
                db.where<Schedule>().equalTo("id",scheduleId)
                    ?.findFirst()
                    ?.deleteFromRealm()
            }
            finish()
        }

        addAdress.setOnClickListener {
            val intent = Intent(this,MapsActivity::class.java)
            startActivity(intent)
        }
        /**
         * スリープ解除
         */
        if (intent?.getBooleanExtra("onReceive", false) == true){
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
    }
    /**
     * 設定日時に通知システム起動
     */

    @SuppressLint("SimpleDateFormat")
    private fun String.toDate(pattern: String = "yyyy/MM/dd "): Date?{
        return try {
            SimpleDateFormat(pattern).parse(this)
        } catch (e: IllegalArgumentException) {
            return null
        } catch (e: ParseException) {
            return null
        }
    }

    /**
     * データベースを終了
     */
    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }


}

private fun Calendar.get(year: Int, month: Int, date: Int) {
}