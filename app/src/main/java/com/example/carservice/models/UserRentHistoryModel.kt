package com.example.carservice.models

data class UserRentHistoryModel (
    val uid: String,
    val userId: String, // for firebase security rules
    val apartmentId: String,
    val apartmentName: String,
    val apartmentDescription: String,
    val moneySpent: Double,
    val daysSpent: Int,
    val days: ArrayList<Long>
)