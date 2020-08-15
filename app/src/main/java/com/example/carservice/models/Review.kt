package com.example.carservice.models

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Review (
    var uid: String,
    val userReview: String,
    val numStars : Int,
    val userName: String,
    @ServerTimestamp
    var creationDate: Date?
)