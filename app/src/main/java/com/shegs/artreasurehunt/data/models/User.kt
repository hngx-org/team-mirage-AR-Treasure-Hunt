package com.shegs.artreasurehunt.data.models

data class User(
    val id: String,
    val email: String,
    val userName: String,
    val lat: Double? = 0.0,
    val lng: Double? = 0.0,
)
