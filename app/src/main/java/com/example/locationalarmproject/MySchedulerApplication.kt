package com.example.locationalarmproject

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T



/**
 * データベース処理
 */
class MySchedulerApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
//        val builder = RealmConfiguration.Builder()
//        builder.schemaVersion(1L).migration(Migration())
//        val config = builder.build()
//        Realm.setDefaultConfiguration(config)
    }
}