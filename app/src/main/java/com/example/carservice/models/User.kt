package com.example.carservice.models

data class User(
    val uid: String,
    val name: String,
    var phoneNumber: String?,
    var email: String?,
    var pid: String?,
    var favourites: ArrayList<String>,
    var userRole: String,
    var rentHistory: ArrayList<UserRentHistoryModel>?
)