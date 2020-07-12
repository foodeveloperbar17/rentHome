package com.example.carservice.presenters

import com.example.carservice.R
import com.example.carservice.ui.activities.MapsActivity
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.google.maps.DirectionsApi
import com.google.maps.GeoApiContext

class MapsPresenter(mapsActivity: MapsActivity) {
    private var mapsActivity: MapsActivity? = null


    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var geoContext: GeoApiContext
    private var isCurrLocationEnabled = false
    private var currLocationResult: LocationResult? = null

    init {
        this.mapsActivity = mapsActivity
    }

    companion object {
        private var mapsPresenter: MapsPresenter? = null
        fun getInstance(mapsActivity: MapsActivity): MapsPresenter {
            if (mapsPresenter == null) {
                mapsPresenter =
                    MapsPresenter(mapsActivity)
            }
            return mapsPresenter!!
        }
    }

    fun onActivityCreated(mapsActivity: MapsActivity) {
        if (this.mapsActivity == null){
            this.mapsActivity = mapsActivity
        }
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(mapsActivity)
        initLocationCallback()
        initGeoApiContext()
    }

    fun getCurrentLocationPeriodically() {
        val locationRequest = LocationRequest()
        locationRequest.interval = 5000 //10 seconds
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null
        )
    }

    private fun initGeoApiContext() {
        geoContext = GeoApiContext()
        geoContext.setApiKey(mapsActivity?.resources?.getString(R.string.geo_api_key))
    }


    private fun initLocationCallback() {
        locationCallback = object : LocationCallback() {
            @Override
            override fun onLocationAvailability(locationAvailability: LocationAvailability) {
                isCurrLocationEnabled = locationAvailability.isLocationAvailable
                super.onLocationAvailability(locationAvailability)
            }

            @Override
            override fun onLocationResult(locationResult: LocationResult?) {
                super.onLocationResult(locationResult)
                currLocationResult = locationResult
            }
        }
    }

    fun getCurrentLocation() {
        if (currLocationResult?.lastLocation == null) {
            return
        }
        mapsActivity?.animateCamera(
            LatLng(
                currLocationResult?.lastLocation?.latitude!!,
                currLocationResult?.lastLocation?.longitude!!
            ), 14F
        )
    }


    fun getPathForPositions(startLocation: LatLng, endLocation: LatLng): MutableList<LatLng> {
        val path = mutableListOf<LatLng>()
        val directionRequest = DirectionsApi.getDirections(
            geoContext, "${startLocation.latitude},${startLocation.longitude}",
            "${endLocation.latitude},${endLocation.longitude}"
        )
        try {
            val directionResult = directionRequest.await()
            if (directionResult.routes != null && directionResult.routes.isNotEmpty()) {
                val route = directionResult.routes[0]

                if (route.legs != null) {
                    for (leg in route.legs) {
                        if (leg.steps != null) {
                            for (step in leg.steps) {
                                if (step.steps != null && step.steps.isNotEmpty()) {
                                    for (smallerStep in step.steps) {
                                        val encodedPolyline = smallerStep.polyline
                                        if (encodedPolyline != null) {
                                            val coords = encodedPolyline.decodePath()
                                            for (coord in coords) {
                                                path.add(LatLng(coord.lat, coord.lng))
                                            }

                                        }
                                    }
                                } else {
                                    val encodedPolyline = step.polyline
                                    if (encodedPolyline != null) {
                                        val coords = encodedPolyline.decodePath()
                                        for (coord in coords) {
                                            path.add(LatLng(coord.lat, coord.lng))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return path
    }


    fun onActivityStop() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }


    fun onActivityDestroy() {
        mapsActivity = null
    }
}