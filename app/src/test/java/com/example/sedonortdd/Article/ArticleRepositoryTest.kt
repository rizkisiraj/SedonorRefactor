package com.example.sedonortdd.Article

import com.example.sedonortdd.data.models.Article
import com.example.sedonortdd.data.repositories.ArticleRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */



class ArticleRepositoryTest {

    private lateinit var mockFirestore: FirebaseFirestore
    private lateinit var mockCollectionReference: CollectionReference
    private lateinit var mockTask: Task<QuerySnapshot>
    private lateinit var mockQuerySnapshot: QuerySnapshot
    private lateinit var repository: ArticleRepository

    @Before
    fun setUp() {
        mockFirestore = mockk(relaxed = true)
        mockCollectionReference = mockk(relaxed = true)
        mockTask = mockk(relaxed = true)
        mockQuerySnapshot = mockk(relaxed = true)
        repository = ArticleRepository(mockFirestore)
    }

    @Test
    fun `fetch articles successfully from firestore`() = runBlocking {
        val article = Article("Test Title", "Test Content")
        every { mockFirestore.collection("articles") } returns mockCollectionReference
        every { mockCollectionReference.get() } returns mockTask

        mockkStatic("kotlinx.coroutines.tasks.TasksKt")
        coEvery { mockTask.await() } returns mockQuerySnapshot

        every { mockQuerySnapshot.toObjects(Article::class.java) } returns listOf(article)

        val result = repository.fetchArticles()

        assertTrue(result.isSuccess)
        assertEquals(listOf(article), result.getOrNull())
    }


    @Test
    fun `fetch articles should throw exception`() = runTest {

        val repository = ArticleRepository(mockFirestore)

        every { mockFirestore.collection("articles") } returns mockCollectionReference
        every { mockCollectionReference.get() } throws RuntimeException("Firestore fetch error")

        val result = repository.fetchArticles()

        assertTrue(result.isFailure)
        assertEquals("Firestore fetch error", result.exceptionOrNull()?.message)
    }
}
