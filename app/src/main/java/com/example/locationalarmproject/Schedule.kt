package com.example.locationalarmproject

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import com.google.android.gms.maps.model.LatLng

open class Schedule  : RealmObject(){
    @PrimaryKey
    var id: Long? = 0
    var latitudeAddress: Double? = 0.0
    var longtudeAdress: Double? = 0.0
    var title: String? = ""
    var detail: String? = ""
    var stg: String?= ""
    var str: String?=""

}