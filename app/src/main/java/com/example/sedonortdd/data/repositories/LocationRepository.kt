package com.example.sedonortdd.data.repositories

import com.example.sedonortdd.data.models.Location
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await

class LocationRepository(private val firestore: FirebaseFirestore) {
    suspend fun fetchLocations(): Result<List<Location>> {
        return try {
            val snapshot: QuerySnapshot = firestore.collection("locations").get().await()
            Result.success(snapshot.toObjects(Location::class.java))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createLocation(location: Location): Result<Nothing?> {
        return Result.success(null)
    }
}