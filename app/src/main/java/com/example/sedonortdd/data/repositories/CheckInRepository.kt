package com.example.sedonortdd.data.repositories

import com.example.sedonortdd.data.models.Location
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.tasks.await

class CheckInRepository(private val db: FirebaseFirestore) {

    suspend fun getLocationById(locationId: String): Location? {
        return try {
            val document = db.collection("locations").document(locationId).get().await()
            document.toObject(Location::class.java)
        } catch (e: Exception) {
            null
        }
    }
}
