package com.example.locationalarmproject

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class Schedule  : RealmObject(){
    @PrimaryKey
    var id: Long? = 0
    var address: String? =""
    var title: String? = ""
    var detail: String? = ""
    var stg: String?= ""
    var str: String?=""
    var range:String?=""

}