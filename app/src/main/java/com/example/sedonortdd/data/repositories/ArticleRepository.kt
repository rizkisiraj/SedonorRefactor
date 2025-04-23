package com.example.sedonortdd.data.repositories

import com.example.sedonortdd.data.models.Article
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await

class ArticleRepository(private val firestore: FirebaseFirestore) {
    suspend fun fetchArticles(): Result<List<Article>> {
        return try {
            val collection = firestore.collection("articles")
            val snapshot: QuerySnapshot = collection.get().await()
            Result.success(snapshot.toObjects(Article::class.java))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}