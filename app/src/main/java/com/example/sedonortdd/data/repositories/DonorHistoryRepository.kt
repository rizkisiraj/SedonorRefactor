package com.example.sedonortdd.data.repositories

import com.example.sedonortdd.data.models.DonorHistory
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await

class DonorHistoryRepository(private val firestore: FirebaseFirestore) {
    suspend fun createHistory(history: DonorHistory): Result<Nothing?> {
        return try {
            firestore.collection("donorHistories")
                .add(history)
                .await()
            Result.success(null)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun fetchDonorHistories(): Result<List<DonorHistory>> {
        return try {
            val snapshot: QuerySnapshot = firestore.collection("donorHistories").get().await()
            Result.success(snapshot.toObjects(DonorHistory::class.java))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}