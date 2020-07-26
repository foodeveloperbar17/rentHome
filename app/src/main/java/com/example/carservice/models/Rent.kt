package com.example.carservice.models

data class Rent (
    var uid: String,
    var user: User,
    var apartment: Apartment,
    var rentDays: ArrayList<Long>,
    var moneySpent: Double
)