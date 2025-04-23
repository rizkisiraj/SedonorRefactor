package com.example.sedonortdd.data.models

import com.google.firebase.firestore.GeoPoint

data class Location(
    val address: String = "",
    val description: String = "",
    val photo: String = "",
    val schedule: String = "",
    val coordinates:  GeoPoint = generateRandomGeoPoint(),
    val location: String? = null,
    val name: String? = null
)
fun generateRandomGeoPoint(): GeoPoint {
    val latitude = (-90..90).random() + Math.random()
    val longitude = (-180..180).random() + Math.random()
    return GeoPoint(latitude, longitude)
}

