package com.example.sedonortdd.Location

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.sedonortdd.data.models.Location
import com.example.sedonortdd.data.repositories.LocationRepository
import com.example.sedonortdd.viewModel.LocationViewModel
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.*

class LocationViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule() // For LiveData testing

    private lateinit var viewModel: LocationViewModel
    private val testDispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = LocationViewModel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadLocations should update loading to true initially`() = runTest {
        val mockRepository: LocationRepository = mockk(relaxed = true)
        viewModel.repository = mockRepository

        coEvery { mockRepository.fetchLocations() } returns Result.success(emptyList())

        val loadingObserver: Observer<Boolean> = mockk(relaxed = true)
        viewModel.loading.observeForever(loadingObserver)

        viewModel.loadLocations()

        verify { loadingObserver.onChanged(true) }
        viewModel.loading.removeObserver(loadingObserver)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `loadLocations should update LiveData with locations on success`() = runTest {
        val mockRepository: LocationRepository = mockk(relaxed = true)
        viewModel.repository = mockRepository

        val locations = listOf(Location(name = "Maryam"), Location(name = "Maryam1"))
        coEvery { mockRepository.fetchLocations() } returns Result.success(locations)

        val locationsObserver: Observer<List<Location>> = mockk(relaxed = true)
        val loadingObserver: Observer<Boolean> = mockk(relaxed = true)

        viewModel.locations.observeForever(locationsObserver)
        viewModel.loading.observeForever(loadingObserver)

        viewModel.loadLocations()
        advanceUntilIdle()

        verify { locationsObserver.onChanged(locations) }
        verify { loadingObserver.onChanged(false) }

        viewModel.locations.removeObserver(locationsObserver)
        viewModel.loading.removeObserver(loadingObserver)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `loadArticles should update error message on failure`() = runTest {
        val mockRepository: LocationRepository = mockk(relaxed = true)
        viewModel.repository = mockRepository

        val exception = Exception("Network Error")
        coEvery { mockRepository.fetchLocations() } returns Result.failure(exception)

        val errorObserver: Observer<String?> = mockk(relaxed = true)
        val loadingObserver: Observer<Boolean> = mockk(relaxed = true)

        viewModel.error.observeForever(errorObserver)
        viewModel.loading.observeForever(loadingObserver)

        viewModel.loadLocations()
        advanceUntilIdle()

        verify { errorObserver.onChanged("Network Error") }
        verify { loadingObserver.onChanged(false) }

        viewModel.error.removeObserver(errorObserver)
        viewModel.loading.removeObserver(loadingObserver)
    }

    @Test
    fun `loadLocations should throw an error if repository is not initialized`() = runTest {

        val exception = Assert.assertThrows(IllegalStateException::class.java) {
            viewModel.loadLocations()
        }

        assertEquals("Repository must be initialized before loading locations", exception.message)
    }
}