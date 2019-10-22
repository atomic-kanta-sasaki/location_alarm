package com.example.locationalarmproject


//import com.google.maps.android.SphericalUtil
import android.Manifest
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
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*


class MapsActivity : AppCompatActivity(), LocationListener,OnMapReadyCallback, OnMarkerClickListener{

    private var lastLatLng: LatLng? = null
    private var pinList: Map<Int, Map<String, Any>> = mapOf()
    var TAG = "hoge"

    // lateinit: late initialize to avoid checking null
    private lateinit var locationManager: LocationManager
    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
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
//        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lastLatLng, zoomValue))

        // マーカーを表示させる.
        googleMap.addMarker(
            MarkerOptions()
                .position(tokyoStation)             // 地図上のマーカーの位置
                .title("Marker in tokyoStation")    // マーカーをタップ時に表示するテキスト文字列
                .snippet("Australian cities") // タイトルの下に表示される追加のテキスト
                .icon(BitmapDescriptorFactory.defaultMarker(
                    BitmapDescriptorFactory.HUE_BLUE)) // アイコン
        )

        val radius = 1000 *1.0 // 1km

        // 円を描画
        googleMap.addCircle(
            CircleOptions()
                .center(tokyoStation)          // 円の中心位置
                .radius(radius)          // 半径 (メートル単位)
                .strokeColor(Color.BLUE) // 線の色
                .strokeWidth(2f)         // 線の太さ
                .fillColor(0x400080ff)   // 円の塗りつぶし色
        )

        // 一旦例として東京駅と大阪駅の緯度と経度を示す
        val latLngA = LatLng(35.681236, 139.767125)
        val latLngB = LatLng(34.7331, 135.5002)

        // 距離をメートル単位で返す
//        val distance = SphericalUtil.computeDistanceBetween(latLngA, latLngB)

    }

    override fun onMarkerClick(p0: Marker?): Boolean {

        Log.w(TAG, "hoge")
        return true
    }

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

    override fun onLocationChanged(location: Location) {
        // Latitude
        val textView1 = findViewById<TextView>(R.id.text_view1)
        val str1 = "Latitude:" + location.getLatitude()
        textView1.text = str1

        // Longitude
        val textView2 = findViewById<TextView>(R.id.text_view2)
        val str2 = "Longtude:" + location.getLongitude()
        textView2.text = str2
    }

    override fun onProviderEnabled(provider: String) {

    }

    override fun onProviderDisabled(provider: String) {

    }

//    /**
//     * ピン差しロジックを実装しているメソッド
//     */
//    private fun putPin(location: Location) {
//
//        val sdf = SimpleDateFormat("yyyy/MM/dd")
//        val latLng = LatLng(location.latitude, location.longitude)
//        val accuracy = location.accuracy.toString()
//        val accColor: Float = if (location.accuracy < 100.0F ) 200.0F - location.accuracy * 2 else 0.0F
//
//
//        // put pin
//        mMap.addMarker(MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(accColor)).title("$dateStr / acc: $accuracy m"))
//
//        // draw line between now and last LatLng
//        if ( lastLatLng != null ) {
//            val straight = PolylineOptions()
//            straight.color(Color.BLUE)
//            straight.width(6F)
//            straight.add(lastLatLng)
//            straight.add(latLng)
//
//            mMap.addPolyline(straight)
//        }
//        lastLatLng = latLng
//
//        // move camera
//        val camerapos = CameraPosition.Builder()
//        val camerazoom = if (pincount == 0) 18.0F else mMap.cameraPosition.zoom
//        camerapos.target(latLng)
//        camerapos.zoom(camerazoom)
//        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(camerapos.build()))
//
//        // add pin count
//        pincount++
//        val pincountView: TextView = findViewById(R.id.txt_pincount)
//        pincountView.text = Integer.toString(pincount)
//
//        // add pin to list
//        pinList += pincount to mapOf(
//            "date" to dateStr,
//            "latitude" to location.latitude,
//            "longitude" to location.longitude,
//            "accuracy" to location.accuracy
//        )
//
//        if ( pincount == MAX_PINCOUNT ) {
//            showToast("pincount has reached maximum.")
//            stopLocationUpdates()
//        }
//    }
//
//
//
//    private fun showToast( msg: String, length: Int = Toast.LENGTH_SHORT ) {
//        Toast.makeText(this, msg, length).show()
//    }
}
