package com.example.locationalarmproject

//import com.google.maps.android.SphericalUtil
import android.Manifest
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.location.LocationProvider
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.GoogleMap.OnMapClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import java.util.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_maps.*
import kotlinx.android.synthetic.main.activity_schedule_edit.*

class MapsActivity : AppCompatActivity(), LocationListener,OnMapReadyCallback, OnMarkerClickListener, OnMapClickListener {

    private var lastLatLng: LatLng? = null
    private var pinList: Map<Int, Map<String, Any>> = mapOf()
    var TAG = "hoge"
    private var pincount: Int = 0
    private val MAX_PINCOUNT: Int = 1000

    // lateinit: late initialize to avoid checking null
    private lateinit var locationManager: LocationManager
    private lateinit var mMap: GoogleMap
    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)


        val realmCofigration = RealmConfiguration.Builder()
            .deleteRealmIfMigrationNeeded()
            .schemaVersion(0)
            .build()

        realm = Realm.getInstance(realmCofigration)

        var scheduler = realm.where(Schedule::class.java).findAll()
        var latest_schedule: Long = scheduler.max("id").toString().toLong()

        button3.setOnClickListener{

            val intent = Intent(this, ScheduleEditActivity::class.java).putExtra("schedule_id", latest_schedule)
            startActivity(intent)
        }

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1000)
        } else {
            locationStart()

            if (::locationManager.isInitialized) {
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    1000,
                    50f,
                    this)
            }

        }
    }

    /**
     * mapを使用するための設定情報
     */

    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val tokyoStation = LatLng(35.681236, 139.767125) // 東京駅

        // スワイプで地図を平行移動
        googleMap.uiSettings.isScrollGesturesEnabled = true

        // ピンチイン、ピンチアウトでズーム
        googleMap.uiSettings.isZoomGesturesEnabled = true

        // 現在地の表示
        googleMap.uiSettings.isCompassEnabled = true

        //現在位置の取得を許可
        googleMap.setMyLocationEnabled(true);

        // 自分の現在地に移動するアイコンの追加
        googleMap.isMyLocationEnabled = true
        val zoomValue = 14.0f // 1.0f 〜 21.0f を指定
        var lastLatLng: LatLng? = null


         var zoomSize = 14
        var str = null;
             // tapされた位置の緯度経度
            mMap!!.setOnMapClickListener(object : GoogleMap.OnMapClickListener {

                 override fun onMapClick(tapLocation: LatLng) {
                     // tapされた位置の緯度経度
                     val location = LatLng(tapLocation.latitude, tapLocation.longitude);
                     mMap.clear()
                     val str: String = String.format(Locale.US, "%f, %f", tapLocation.latitude, tapLocation.longitude);
                     mMap.addMarker(MarkerOptions().position(location).title(str));
                     mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 14.toFloat()))

                     val realmCofigration = RealmConfiguration.Builder()
                         .deleteRealmIfMigrationNeeded()
                         .schemaVersion(0)
                         .build()

                     realm = Realm.getInstance(realmCofigration)

                     // Obtain the SupportMapFragment and get notified when the map is ready to be used.
                     button2.setOnClickListener {
                             realm.executeTransaction { db: Realm ->

                                 var scheduler = realm.where(Schedule::class.java).findAll()
                                 var latest_schedule: Long = scheduler.max("id").toString().toLong()

                                 val schedule = db.where<Schedule>()
                                     .equalTo("id", latest_schedule).findFirst()
                                 schedule?.address = location.toString()
                             }
                         }
                 }


             })

    }

    override fun onMapClick(tapLocation: LatLng) {
        // tapされた位置の緯度経度
        val location = LatLng(tapLocation.latitude, tapLocation.longitude)
    }

    /**
     * ログをはかせる
     */
    override fun onMarkerClick(p0: Marker?): Boolean {

        Log.w(TAG, "hoge")
        return true
    }

    /**
     * GPSの使用をスタートする
     */
    private fun locationStart() {
        Log.d("debug", "locationStart()")

        // Instances of LocationManager class must be obtained using Context.getSystemService(Class)
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Log.d("debug", "location manager Enabled")
        } else {
            // to prompt setting up GPS
            val settingsIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(settingsIntent)
            Log.d("debug", "not gpsEnable, startActivity")
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1000)

            Log.d("debug", "checkSelfPermission false")
            return
        }

        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            1000,
            50f,
            this)



    }

    /**
     * Android Quickstart:
     * https://developers.google.com/sheets/api/quickstart/android
     *
     * Respond to requests for permissions at runtime for API 23 and above.
     * @param requestCode The request code passed in
     * requestPermissions(android.app.Activity, String, int, String[])
     * @param permissions The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     * which is either PERMISSION_GRANTED or PERMISSION_DENIED. Never null.
     */
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == 1000) {
            // 使用が許可された
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("debug", "checkSelfPermission true")

                locationStart()

            } else {
                // それでも拒否された時の対応
                val toast = Toast.makeText(this,
                    "これ以上なにもできません", Toast.LENGTH_SHORT)
                toast.show()
            }
        }
    }

    /**
     * 状態ログをはかせる
     */
    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
        when (status) {
            LocationProvider.AVAILABLE ->
                Log.d("debug", "LocationProvider.AVAILABLE")
            LocationProvider.OUT_OF_SERVICE ->
                Log.d("debug", "LocationProvider.OUT_OF_SERVICE")
            LocationProvider.TEMPORARILY_UNAVAILABLE ->
                Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE")
        }
    }

    /**
     * 現在位置が変更された場合に発火するメソッド
     */
    override fun onLocationChanged(location: Location) {
//
//        // Latitude
//        val textView1 = findViewById<TextView>(R.id.text_view1)
//        val str1 = "Latitude:" + location.getLatitude()
//        textView1.text = str1
//
//        // Longitude
//        val textView2 = findViewById<TextView>(R.id.text_view2)
//        val str2 = "Longtude:" + location.getLongitude()
//        textView2.text = str2

        // 通知の設定
        val builder = Notification.Builder(this).apply {
            setSmallIcon(R.drawable.notification_template_icon_bg)// 必須
            setContentTitle("目的地に到着しました")
            setContentText("こうこうこういう予定です")
            setAutoCancel(true)
            setDefaults(Notification.DEFAULT_ALL)
        }

        // 親となるアクティビティを指定 マニフェストに追記が必要
        val stackBuilder = TaskStackBuilder.create(this)
        stackBuilder.addParentStack(MapsActivity::class.java)

        // 表示するアクティビティ
        stackBuilder.addNextIntent(Intent(this, MapsActivity::class.java))

        // 通知をタップした時に開くインテントを設定
        val pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        builder.setContentIntent(pendingIntent)

        // 通知を送信
        val notificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, builder.build())

    }




    /**
     * status保持用メソッド
     */
    override fun onProviderEnabled(provider: String) {

    }

    /**
     * status保持用メソッド
     */
    override fun onProviderDisabled(provider: String) {

    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }


}
