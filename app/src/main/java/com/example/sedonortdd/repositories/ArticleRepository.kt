package com.example.sedonortdd.repositories

import com.example.sedonortdd.models.Article
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await
import kotlin.reflect.KClass

class ArticleRepository(private val firestore: FirebaseFirestore) {
    suspend fun fetchArticles(): Result<List<Article>> {
        return try {
            val snapshot: QuerySnapshot = firestore.collection("articles").get().await()
            Result.success(snapshot.toObjects(Article::class.java))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}