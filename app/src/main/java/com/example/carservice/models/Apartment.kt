package com.example.carservice.models

import com.google.android.gms.maps.model.LatLng
import java.util.*

data class Apartment(
    var uuid: String?,
    val name: String?,
    val description: String?,
    val price: Double,
    val location: LatLng?,
    val imagesPaths: ArrayList<String> = ArrayList(),
    val userRating: Double = 0.0,
    val overAllRating: Double = 0.0,
    val rentHistory: ArrayList<Long> = ArrayList(),

// not for firebase storing.
    var isFavouriteForCurrentUser: Boolean = false

)