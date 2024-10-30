package com.example.sedonortdd

import com.example.sedonortdd.models.Article
import com.example.sedonortdd.repositories.ArticleRepository
import com.example.sedonortdd.repositories.LocationRepository
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
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class LocationRepositoryTest {
    private lateinit var mockFirestore: FirebaseFirestore
    private lateinit var mockCollectionReference: CollectionReference
    private lateinit var mockTask: Task<QuerySnapshot>
    private lateinit var mockQuerySnapshot: QuerySnapshot
    private lateinit var repository: LocationRepository

    @Before
    fun setUp() {
        mockFirestore = mockk(relaxed = true)
        mockCollectionReference = mockk(relaxed = true)
        mockTask = mockk(relaxed = true)
        mockQuerySnapshot = mockk(relaxed = true)
        repository = LocationRepository(mockFirestore)
    }

    @Test
    fun `fetch locations successfully from firestore`() = runBlocking {
        // Arrange
        val article = Article("Test Title", "Test Content")
        every { mockFirestore.collection("locations") } returns mockCollectionReference
        every { mockCollectionReference.get() } returns mockTask

        mockkStatic("kotlinx.coroutines.tasks.TasksKt")
        coEvery { mockTask.await() } returns mockQuerySnapshot

        every { mockQuerySnapshot.toObjects(Article::class.java) } returns listOf(article)

        val result = repository.fetchLocations()

        assertTrue(result.isSuccess)
        assertEquals(listOf(article), result.getOrNull())
    }


    @Test
    fun `fetch locations should throw exception`() = runTest {

        every { mockFirestore.collection("locations") } returns mockCollectionReference
        every { mockCollectionReference.get() } throws RuntimeException("Firestore fetch error")

        val result = repository.fetchLocations()

        assertTrue(result.isFailure)
        assertEquals("Firestore fetch error", result.exceptionOrNull()?.message)
    }
}