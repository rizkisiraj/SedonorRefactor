package com.example.sedonortdd.CheckIn

import com.example.sedonortdd.data.models.Location
import com.example.sedonortdd.data.repositories.CheckInRepository
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import io.mockk.*
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class CheckInRepositoryTest {
    private lateinit var mockFirestore: FirebaseFirestore
    private lateinit var mockDocumentReference: DocumentReference
    private lateinit var mockDocumentSnapshot: DocumentSnapshot
    private lateinit var repository: CheckInRepository

    @Before
    fun setUp() {
        mockFirestore = mockk(relaxed = true)
        mockDocumentReference = mockk(relaxed = true)
        mockDocumentSnapshot = mockk(relaxed = true)
        repository = CheckInRepository(mockFirestore)
    }

    @Test
    fun `getLocationById returns valid Location`() = runBlocking {

        val locationId = "NuaDS0zpRi1flLAfier9"
        val mockLocation = Location(name = "Test Location", description = "Test Desc")

        every { mockFirestore.collection("locations").document(locationId) } returns mockDocumentReference
        every { mockDocumentReference.get() } returns mockk(relaxed = true)

        mockkStatic("kotlinx.coroutines.tasks.TasksKt")
        coEvery { mockDocumentReference.get().await() } returns mockDocumentSnapshot
        every { mockDocumentSnapshot.toObject(Location::class.java) } returns mockLocation

        val result = repository.getLocationById(locationId)

        assertNotNull(result)
        assertEquals("Test Location", result?.name)
    }

    @Test
    fun `getLocationById should return null on failure`() = runTest {

        val locationId = "invalid_id"

        every { mockFirestore.collection("locations").document(locationId) } returns mockDocumentReference
        every { mockDocumentReference.get() } throws RuntimeException("Firestore fetch error")

        val result = repository.getLocationById(locationId)

        assertNull(result)
    }
}
