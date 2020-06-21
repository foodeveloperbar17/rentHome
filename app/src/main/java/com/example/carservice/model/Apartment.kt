package com.example.carservice.model

import com.google.android.gms.maps.model.LatLng

data class Apartment(
    val name: String?,
    val description: String?,
    val price: Double,
    val location: LatLng?,
    val imagePath: Int = 0,
    val userRating: Int = 0,
    val overAllRating: Double = 0.0
)