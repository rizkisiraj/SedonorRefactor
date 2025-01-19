package com.example.sedonortdd.DonorHistory

import com.example.sedonortdd.data.models.DonorHistory
import com.example.sedonortdd.data.repositories.DonorHistoryRepository
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

class DonorRepositoryTest {

    private lateinit var mockFirestore: FirebaseFirestore
    private lateinit var mockCollectionReference: CollectionReference
    private lateinit var mockTask: Task<QuerySnapshot>
    private lateinit var mockQuerySnapshot: QuerySnapshot
    private lateinit var repository: DonorHistoryRepository

    @Before
    fun setUp() {
        mockFirestore = mockk(relaxed = true)
        mockCollectionReference = mockk(relaxed = true)
        mockTask = mockk(relaxed = true)
        mockQuerySnapshot = mockk(relaxed = true)
        repository = DonorHistoryRepository(mockFirestore)
    }

    @Test
    fun `fetch donorHistories successfully from firestore`() = runBlocking {
        // Arrange
        val history = DonorHistory(
            weight = 80,
            heartRate = 120,
            hemoglobinLevel = 54,
            status = true,
            temperature = 35,
            diastolicPressure = 120,
            systolicPressure = 8,
            donorLocation = "Dutamas"
        )
        every { mockFirestore.collection("donorHistories") } returns mockCollectionReference
        every { mockCollectionReference.get() } returns mockTask

        mockkStatic("kotlinx.coroutines.tasks.TasksKt")
        coEvery { mockTask.await() } returns mockQuerySnapshot

        every { mockQuerySnapshot.toObjects(DonorHistory::class.java) } returns listOf(history)

        val result = repository.fetchDonorHistories()

        assertTrue(result.isSuccess)
        assertEquals(listOf(history)[0].donorLocation, result.getOrNull()!![0].donorLocation)
    }


    @Test
    fun `fetch donorHistories should throw exception`(): Unit = runTest {

        every { mockFirestore.collection("locations") } returns mockCollectionReference
        every { mockCollectionReference.get() } throws RuntimeException("Firestore fetch error")

        val result = repository.fetchDonorHistories()

        assertTrue(result.isFailure)
        assertEquals("Firestore fetch error", result.exceptionOrNull()?.message)
    }
}