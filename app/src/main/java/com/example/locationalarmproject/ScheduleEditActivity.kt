package com.example.locationalarmproject

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.material.snackbar.Snackbar
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_schedule_edit.*

class ScheduleEditActivity : AppCompatActivity() {

    private lateinit var realm: Realm
    /**
     *更新処理
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule_edit)
        realm = Realm.getDefaultInstance()

        val scheduleId = intent?.getLongExtra("schedule_id", -1L)

        if (scheduleId != -1L) {

            val  schedule = realm.where<Schedule>()
                .equalTo("id", scheduleId).findFirst()

            titleEdit.setText(schedule?.title)
            detailEdit.setText(schedule?.detail)
            delete.visibility = View.VISIBLE

        }else{
            delete.visibility = View.INVISIBLE
        }

        save.setOnClickListener {view: View ->
            when (scheduleId){
                -1L -> {


                    realm.executeTransaction { db: Realm ->
                        val maxId = db.where<Schedule>().max("id")
                        val nextId = (maxId?.toLong() ?: 0L) + 1
                        val schedule = db.createObject<Schedule>(nextId)

                        schedule.title = titleEdit.text.toString()
                        schedule.detail = detailEdit.text.toString()
                    }
                    Snackbar.make(view, "追加しました", Snackbar.LENGTH_SHORT)
                        .setAction("戻る") { finish() }
                        .setActionTextColor(Color.YELLOW)
                        .show()
                }
                else -> {
                    realm.executeTransaction { db: Realm ->
                        val schedule = db.where<Schedule>()
                            .equalTo("id", scheduleId).findFirst()
                        schedule?.title = titleEdit.text.toString()
                        schedule?.detail = detailEdit.text.toString()
                    }
                    Snackbar.make(view, "修正しました", Snackbar.LENGTH_SHORT)
                        .setAction("戻る") { finish() }
                        .setActionTextColor(Color.YELLOW)
                        .show()
                }
            }
        }

        delete.setOnClickListener{ view: View ->
            realm.executeTransaction{db: Realm ->
                db.where<Schedule>().equalTo("id",scheduleId)
                    ?.findFirst()
                    ?.deleteFromRealm()
            }
            Snackbar.make(view,"削除しました",Snackbar.LENGTH_SHORT)
                .setAction("戻る"){finish()}
                .setActionTextColor(Color.YELLOW)
                .show()
        }
    }

    /**
     * アクティビティの終了
     */
    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }



}
