package com.example.locationalarmproject

import android.app.Application
import io.realm.Realm

class SchedulerApplication : Application() {
    override  fun onCreate() {
        super.onCreate()
        Realm.init(this)
    }

}