package com.example.sedonortdd.Location

import com.example.sedonortdd.data.models.Article
import com.example.sedonortdd.data.models.Location
import com.example.sedonortdd.data.repositories.LocationRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
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
    private lateinit var mockDonorHistoryTask: Task<DocumentReference>
    private lateinit var mockQuerySnapshot: QuerySnapshot
    private lateinit var mockDocumentReference: DocumentReference
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
        val location = Location(name = "TestName", description = "this is a test")
        every { mockFirestore.collection("locations") } returns mockCollectionReference
        every { mockCollectionReference.get() } returns mockTask

        mockkStatic("kotlinx.coroutines.tasks.TasksKt")
        coEvery { mockTask.await() } returns mockQuerySnapshot

        every { mockQuerySnapshot.toObjects(Location::class.java) } returns listOf(location)

        val result = repository.fetchLocations()

        assertTrue(result.isSuccess)
        assertEquals(listOf(location)[0].name, result.getOrNull()!![0].name)
    }


    @Test
    fun `fetch locations should throw exception`() = runTest {

        every { mockFirestore.collection("locations") } returns mockCollectionReference
        every { mockCollectionReference.get() } throws RuntimeException("Firestore fetch error")

        val result = repository.fetchLocations()

        assertTrue(result.isFailure)
        assertEquals("Firestore fetch error", result.exceptionOrNull()?.message)
    }

    @Test
    fun `create location successfully in firestore`() = runBlocking {
        // Arrange
        val location = Location(name = "New Location", description = "This is a new test location")
        every { mockFirestore.collection("locations") } returns mockCollectionReference
        every { mockCollectionReference.add(location) } returns mockDonorHistoryTask

        mockkStatic("kotlinx.coroutines.tasks.TasksKt")
        coEvery { mockDonorHistoryTask.await() } returns mockDocumentReference

        // Act
        val result = repository.createLocation(location)

        // Assert
        assertTrue(result.isSuccess)
        assertEquals(null, result.getOrNull()) // Void return type represented as null
    }
}