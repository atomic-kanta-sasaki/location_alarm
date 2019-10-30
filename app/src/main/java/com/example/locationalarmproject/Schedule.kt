package com.example.locationalarmproject

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Schedule  : RealmObject() {
    @PrimaryKey
    var id : Long = 0
    var title: String = ""
    var detail: String =""

}