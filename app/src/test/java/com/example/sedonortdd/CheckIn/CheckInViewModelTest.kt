import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.sedonortdd.data.models.Location
import com.example.sedonortdd.data.repositories.CheckInRepository
import com.example.sedonortdd.util.getOrAwaitValue
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CheckInViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: CheckInViewModel
    private val mockRepository: CheckInRepository = mockk()

    @Before
    fun setUp() {
        viewModel = CheckInViewModel(mockRepository)
    }

    @Test
    fun `fetchLocation should update LiveData when repository returns data`() = runTest {
        // Arrange
        val locationId = "valid_id"
        val expectedLocation = Location(name = "Test Location", address = "Test Address", photo = "test.jpg")
        coEvery { mockRepository.getLocationById(locationId) } returns expectedLocation

        // Act
        viewModel.fetchLocation(locationId)  // Panggil fungsi, tapi jangan langsung diuji return-nya

        // Assert
        assertEquals(expectedLocation, viewModel.location.getOrAwaitValue()) // Uji LiveData
    }

    @Test
    fun `fetchLocation should update LiveData to null when repository returns null`() = runTest {
        // Arrange
        val locationId = "invalid_id"
        coEvery { mockRepository.getLocationById(locationId) } returns null

        // Act
        viewModel.fetchLocation(locationId)

        // Assert
        assertEquals(null, viewModel.location.getOrAwaitValue())  // LiveData harus null
    }
}
