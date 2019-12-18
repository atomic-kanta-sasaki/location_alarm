package com.example.locationalarmproject

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ClipDrawable
import android.graphics.drawable.ClipDrawable.VERTICAL
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import androidx.core.view.isNotEmpty
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
        /*
        区切り線の追加
         */
        val divider = androidx.recyclerview.widget.DividerItemDecoration(list.context,LinearLayoutManager(this).orientation)
        list.addItemDecoration(divider)



    }




    /*
    検索ボックスの表示

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.search, menu)
        val searchItem = menu?.findItem(R.id.menu_search)
        val searchView = searchItem?.actionView as SearchView
        searchView.queryHint = "Search View Hint"
        val searchViewiew = menu.findItem(R.id.menu_search).actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {



            override fun onQueryTextSubmit(text: String?): Boolean {

                return true
            }

            override fun onQueryTextChange(text: String?): Boolean {
                val ScheduleAdapter = menu.findItem(R.id.list)

                return false
            }



        })


        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu.findItem(R.id.menu_search).actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
        }

        return super.onCreateOptionsMenu(menu)
    }
    
     */
}
