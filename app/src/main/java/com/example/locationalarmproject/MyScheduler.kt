package com.example.locationalarmproject

import android.content.Intent
import android.graphics.drawable.ClipDrawable
import android.graphics.drawable.ClipDrawable.VERTICAL
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.actions.ItemListIntents
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_my_scheduler.*
import kotlinx.android.synthetic.main.activity_schedule_edit.*
import kotlinx.android.synthetic.main.content_my_scheduler.*
import androidx.recyclerview.widget.DividerItemDecoration as DividerItemDecoration1


class MyScheduler : AppCompatActivity() {
    private lateinit var realm: Realm

    /**
     * Realmクラスインスタンスの取得
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_scheduler)
        setSupportActionBar(toolbar)
        val realmCofigration = RealmConfiguration.Builder()
            .deleteRealmIfMigrationNeeded()
            .schemaVersion(0)
            .build()

        realm = Realm.getInstance(realmCofigration)


        list.layoutManager = LinearLayoutManager(this)
        val schedules = realm.where<Schedule>().findAll()
        val adapter = ScheduleAdapter(schedules)
        list.adapter = adapter
        fab.setOnClickListener { view ->
            val intent = Intent(this, ScheduleEditActivity::class.java)
            startActivity(intent)
        }
        adapter.setOnItemClickListener { id ->
            val intent = Intent(this, ScheduleEditActivity::class.java)
                .putExtra("schedule_id", id)
            startActivity(intent)

        }

    }




    /*
    検索ボックスの表示
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search, menu)


        return super.onCreateOptionsMenu(menu)
    }
}
