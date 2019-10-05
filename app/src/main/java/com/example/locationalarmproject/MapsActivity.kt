package com.example.locationalarmproject

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.SphericalUtil


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
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
}
