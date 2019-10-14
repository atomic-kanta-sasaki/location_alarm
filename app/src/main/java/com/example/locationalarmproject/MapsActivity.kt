package com.example.locationalarmproject

import android.Manifest
import android.os.Bundle
import android.location.LocationManager
import android.content.pm.PackageManager
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.widget.TextView
import android.location.LocationProvider
import android.provider.Settings
import android.widget.Toast
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.GoogleMap


import android.graphics.Color
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.SphericalUtil


class MapsActivity : AppCompatActivity(), LocationListener,OnMapReadyCallback {

    // lateinit: late initialize to avoid checking null
    private lateinit var locationManager: LocationManager
    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

//        Obtain the SupportMapFragment and get notified when the map is ready to be used.
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
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        // スワイプで地図を平行移動
        googleMap.uiSettings.isScrollGesturesEnabled = true

        // ピンチイン、ピンチアウトでズーム
        googleMap.uiSettings.isZoomGesturesEnabled = true

        // 回転
        googleMap.uiSettings.isRotateGesturesEnabled = true

        // ツールバー
        googleMap.uiSettings.isZoomGesturesEnabled = true

        // 2本指でスワイプで視点を傾けることができます。
        googleMap.uiSettings.isMapToolbarEnabled = true

        // コンパスの表示
        googleMap.uiSettings.isTiltGesturesEnabled = true

        // 現在地の表示
        googleMap.uiSettings.isCompassEnabled = true

        //現在位置の取得を許可
        googleMap.setMyLocationEnabled(true);


        // 自分の現在地に移動するアイコンの追加
        googleMap.isMyLocationEnabled = true
        val zoomValue = 14.0f // 1.0f 〜 21.0f を指定
        var lastLatLng: LatLng? = null
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lastLatLng, zoomValue))

        // マーカーを表示させる.
        googleMap.addMarker(
            MarkerOptions()
                .position(sydney)             // 地図上のマーカーの位置
                .title("Marker in Sydney")    // マーカーをタップ時に表示するテキスト文字列
                .snippet("Australian cities") // タイトルの下に表示される追加のテキスト
                .icon(BitmapDescriptorFactory.defaultMarker(
                    BitmapDescriptorFactory.HUE_BLUE)) // アイコン
        )

        val latLng = LatLng(35.681236, 139.767125) // 東京駅
        val radius = 1000 *1.0 // 1km

        // 円を描画
        googleMap.addCircle(
            CircleOptions()
                .center(latLng)          // 円の中心位置
                .radius(radius)          // 半径 (メートル単位)
                .strokeColor(Color.BLUE) // 線の色
                .strokeWidth(2f)         // 線の太さ
                .fillColor(0x400080ff)   // 円の塗りつぶし色
        )

        // 一旦例として東京駅と大阪駅の緯度と経度を示す
        val latLngA = LatLng(35.681236, 139.767125)
        val latLngB = LatLng(34.7331, 135.5002)

        // 距離をメートル単位で返す
        val distance = SphericalUtil.computeDistanceBetween(latLngA, latLngB)


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
}
