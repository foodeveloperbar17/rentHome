package com.example.carservice.model

import com.google.android.gms.maps.model.LatLng

data class AppartmentsModel(
    val name: String?,
    val description: String?,
    val price: Int?,
    val location: LatLng?,
    val imagePath: Int?,
    val userRating: Int?,
    val overAllRating: Double = 0.0
)