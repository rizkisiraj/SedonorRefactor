import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.sedonortdd.data.models.Location
import com.example.sedonortdd.data.repositories.CheckInRepository
import com.example.sedonortdd.util.getOrAwaitValue
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import org.junit.After
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

    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = CheckInViewModel(mockRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `fetchLocation should update LiveData when repository returns data`() = runTest {

        val locationId = "NuaDS0zpRi1flLAfier9"
        val expectedLocation = Location(
            name = "Apotek Jalan Seturan",
            address = "Jalan Seturan",
            photo = "https://goalkes-images.s3.ap-southeast-1.amazonaws.com/media/5289/FbXaFridLSuGNYNVmpHPQpY6jI9U7WwBPkv2DJnm.jpg"
        )

        coEvery { mockRepository.getLocationById(locationId) } returns expectedLocation

        viewModel.fetchLocation(locationId)

        assertEquals(expectedLocation, viewModel.location.getOrAwaitValue())
    }

    @Test
    fun `fetchLocation should update LiveData to null when repository returns null`() = runTest {

        val locationId = "invalid_id"
        coEvery { mockRepository.getLocationById(locationId) } returns null

        viewModel.fetchLocation(locationId)

        assertEquals(null, viewModel.location.getOrAwaitValue())
    }
}
