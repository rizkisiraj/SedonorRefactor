package com.example.sedonortdd.data.repositories

import com.example.sedonortdd.data.models.DonorHistory
import com.example.sedonortdd.data.models.Location
import com.google.firebase.firestore.FirebaseFirestore

class DonorHistoryRepository(private val firestore: FirebaseFirestore) {
    suspend fun createHistory(history: DonorHistory): Result<Nothing?> {
        return Result.success(null)
    }

    suspend fun fetchDonorHistories(): Result<List<DonorHistory>> {
        return try {
            Result.success(listOf())
        } catch (e: Exception) {
            Result.success(listOf())
        }
    }
}