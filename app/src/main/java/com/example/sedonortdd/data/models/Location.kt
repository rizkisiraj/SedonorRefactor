package com.example.sedonortdd.data.models

import com.google.firebase.firestore.GeoPoint

data class Location(
    val address: String = "",
    val description: String = "",
    val photo: String = "",
    val schedule: String? = "",
    val location: String? = null,
    val name: String? = null
)

