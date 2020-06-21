package com.example.carservice.ui

import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.carservice.presenter.MapsPresenter
import com.example.carservice.R
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import java.util.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    private lateinit var userMarker: Marker

    private lateinit var currLocationButton: Button


    private lateinit var fromAutoCompleteFragment: AutocompleteSupportFragment
    private lateinit var toAutoCompleteFragment: AutocompleteSupportFragment


    private val PERMISSION_REQUEST_CODE = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        initUI()
        initUIActions()
        initPlacesApi()
        MapsPresenter.getInstance(this).onActivityCreated(this)
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
        val tbilisi = LatLng(41.73, 44.77)
        userMarker = mMap.addMarker(MarkerOptions().position(tbilisi).title("Marker in Tbilisi"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(tbilisi, 10F))

        if (isLocationGranted()) {
            mMap.isMyLocationEnabled = true
            MapsPresenter.getInstance(this).getCurrentLocationPeriodically()
        }
        setLongClickListener(mMap)
    }

    private fun initUI() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fromAutoCompleteFragment = supportFragmentManager
            .findFragmentById(R.id.from_autocomplete_fragment_id) as AutocompleteSupportFragment
        fromAutoCompleteFragment.setHint("Start Location")


        toAutoCompleteFragment = supportFragmentManager
            .findFragmentById(R.id.to_autocomplete_fragment_id) as AutocompleteSupportFragment
        toAutoCompleteFragment.setHint("End Location")

        currLocationButton = findViewById(R.id.curr_location_button_id)
    }

    private fun initUIActions() {
        currLocationButton.setOnClickListener {
            if (mMap.isMyLocationEnabled) {
                MapsPresenter.getInstance(this).getCurrentLocation()
            } else {
                requestLocationPermissions()
            }
        }
    }

    fun animateCamera(latLng: LatLng, zoom: Float){
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))
    }

    private fun initPlacesApi() {
        Places.initialize(applicationContext, "AIzaSyBRQllVOX1FsbVFyN3-NxtDDInkAFEpzLs")
        fromAutoCompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG))
        fromAutoCompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            @Override
            override fun onPlaceSelected(place: Place) {
                Toast.makeText(applicationContext, "L ${place.name} $place", Toast.LENGTH_LONG)
                    .show()
            }

            override fun onError(status: Status) {
                Log.e("places api on error", "error $status")
            }
        })

        toAutoCompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG))
        toAutoCompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            @Override
            override fun onPlaceSelected(place: Place) {
                Toast.makeText(applicationContext, "L ${place.name} $place", Toast.LENGTH_LONG)
                    .show()
            }

            override fun onError(status: Status) {
                Log.e("places api on error", "error $status")
            }
        })
    }

    private fun setLongClickListener(mMap: GoogleMap) {
        mMap.setOnMapLongClickListener {
            val startPosition = userMarker.position
            userMarker.remove()
            userMarker = mMap.addMarker(
                MarkerOptions()
                    .position(it)
                    .title("You clicked here")
                    .snippet("This is your snippet")
            )
            mMap.animateCamera(CameraUpdateFactory.newLatLng(userMarker.position))
//            detectStreet(userMarker.position)

            val coordinates = MapsPresenter.getInstance(
                this
            ).getPathForPositions(startPosition, userMarker.position)
            drawPath(coordinates)
        }
    }

    private fun drawPath(path: MutableList<LatLng>){
        mMap.clear()
        if (path.isNotEmpty()) {
            val polylineOpts = PolylineOptions().addAll(path).color(Color.BLUE).width(10F)
            mMap.addPolyline(polylineOpts)
        }
    }

    private fun isLocationGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }


    private fun requestLocationPermissions() {
        val permissionsArray = arrayOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )
        ActivityCompat.requestPermissions(this, permissionsArray, PERMISSION_REQUEST_CODE)
    }

    private fun detectStreet(latLng: LatLng) {
        val geoCoder = Geocoder(this, Locale("Geo", "Geo"))
        val res = geoCoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
        Toast.makeText(this, res[0].getAddressLine(0) + "" + res[0].toString(), Toast.LENGTH_LONG)
            .show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            for (i in permissions.indices) {
                if (permissions[i] == android.Manifest.permission.ACCESS_FINE_LOCATION || permissions[i] == android.Manifest.permission.ACCESS_COARSE_LOCATION) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        mMap.isMyLocationEnabled = true
                        MapsPresenter.getInstance(
                            this
                        ).getCurrentLocationPeriodically()
                        return
                    }
                }
            }
        }
    }



    @Override
    override fun onStop() {
        super.onStop()
        MapsPresenter.getInstance(this).onActivityStop()
    }

    @Override
    override fun onDestroy() {
        super.onDestroy()
        MapsPresenter.getInstance(this).onActivityDestroy()
    }
}
