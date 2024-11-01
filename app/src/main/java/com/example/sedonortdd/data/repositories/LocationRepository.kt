package com.example.sedonortdd.repositories

import com.example.sedonortdd.models.Article
import com.example.sedonortdd.models.Location
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
}