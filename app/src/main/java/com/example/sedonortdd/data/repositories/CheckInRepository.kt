package com.example.sedonortdd.data.repositories

import com.example.sedonortdd.data.models.Location
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.tasks.await

class CheckInRepository(private val db: FirebaseFirestore) {
    suspend fun getLocationById(locationId: String): Location? {
        return try {
            val document = db.collection("locations").document(locationId).get().await()
            if (document.exists()) {
                val data = document.data
                println("DEBUG Firestore Data: $data") // <- Tambahin ini
                document.toObject(Location::class.java)
            } else {
                println("DEBUG Firestore: Document tidak ditemukan.")
                null
            }
        } catch (e: Exception) {
            println("DEBUG Firestore Exception: ${e.message}")
            null
        }
    }

//    suspend fun getLocationById(locationId: String): Location? {
//        return try {
//            val document = db.collection("locations").document(locationId).get().await()
//            document.toObject(Location::class.java)
//        } catch (e: Exception) {
//            null
//        }
//    }
}
