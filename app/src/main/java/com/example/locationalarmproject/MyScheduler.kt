package com.example.locationalarmproject

import android.content.Intent
import android.graphics.drawable.ClipDrawable
import android.graphics.drawable.ClipDrawable.VERTICAL
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.LinearLayout

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.actions.ItemListIntents
import com.google.android.material.tabs.TabLayout
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_my_scheduler.*
import kotlinx.android.synthetic.main.activity_schedule_edit.*
import kotlinx.android.synthetic.main.content_my_scheduler.*
import androidx.recyclerview.widget.DividerItemDecoration as DividerItemDecoration1
import io.realm.OrderedRealmCollection as RealmOrderedRealmCollection
import androidx.appcompat.widget.SearchView


class MyScheduler : AppCompatActivity() {
    private lateinit var realm: Realm
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private var recyclerView:RecyclerView?=null
    private var searchView: SearchView? = null

    /**
     * Realmクラスインスタンスの取得
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_scheduler)
        setSupportActionBar(toolbar)

        recyclerView = findViewById(R.id.list)
        searchView = findViewById(R.id.menu_search)
        viewManager = LinearLayoutManager(this)
       /*
       viewAdapter = ScheduleAdapter(data = RealmOrderedRealmCollection<Schedule>)
        */



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

        /*
        リストの線の表示
         */
        val divider = androidx.recyclerview.widget.DividerItemDecoration(list.context,LinearLayoutManager(this).orientation)
        list.addItemDecoration(divider)


    }




    /*
    検索ボックスの表示
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search, menu)

        return super.onCreateOptionsMenu(menu)
    }


}
