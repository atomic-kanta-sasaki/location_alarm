package com.example.locationalarmproject

import android.app.Application
import io.realm.Realm

/**
 * データベース処理
 */
class MySchedulerApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
    }
}