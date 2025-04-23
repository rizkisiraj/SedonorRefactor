import com.example.sedonortdd.data.models.Choice
import com.example.sedonortdd.data.models.Message
import com.example.sedonortdd.data.models.OpenAiRequest
import com.example.sedonortdd.data.models.OpenAiResponse
import com.example.sedonortdd.data.models.Usage
import com.example.sedonortdd.data.repositories.ChatbotRepository
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatbotRepositoryTest {

    private lateinit var openAiApi: OpenAiApi
    private lateinit var repository: ChatbotRepository
    private lateinit var mockCall: Call<OpenAiResponse>

    @Before
    fun setUp() {

        openAiApi = mock(OpenAiApi::class.java)

        repository = ChatbotRepository()

        mockCall = mock(Call::class.java) as Call<OpenAiResponse>
    }

    @Test
    fun `getGptResponse should make a successful API call`() {
        val fakeResponse = OpenAiResponse(
            id = "test123",
            `object` = "chat.completion",
            created = 0,
            model = "gpt-4.1",
            usage = Usage(1, 1, 2),
            choices = listOf(
                Choice(
                    message = Message("assistant", "Hello!"),
                    finish_reason = "stop",
                    index = 0
                )
            )
        )

        // Mock the API response
        `when`(openAiApi.getGptResponse(any(), any())).thenReturn(mockCall)
        `when`(mockCall.enqueue(any(Callback::class.java) as Callback<OpenAiResponse>)).thenAnswer {
            val callback = it.getArgument<Callback<OpenAiResponse>>(0)
            val response = Response.success(fakeResponse)
            callback.onResponse(mockCall, response)
        }

        // Call the repository method
        val request = OpenAiRequest(model = "gpt-4.1", messages = listOf(Message("user", "Hi")), temperature = 0.7)
        val authorizationHeader = "APIKEY"
        // Execute the request
        repository.getGptResponse(authorizationHeader, request, object : Callback<OpenAiResponse> {
            override fun onResponse(call: Call<OpenAiResponse>, response: Response<OpenAiResponse>) {
                assertTrue(response.isSuccessful)
                val responseBody = response.body()
                assertNotNull(responseBody)
                assertEquals("test123", responseBody?.id)
            }

            override fun onFailure(call: Call<OpenAiResponse>, t: Throwable) {
                fail("API call failed: ${t.message}")
            }
        })
    }

    @Test
    fun `getGptResponse should handle API failure`() {
        // Mock the API response failure
        `when`(openAiApi.getGptResponse(any(), any())).thenReturn(mockCall)
        `when`(mockCall.enqueue(any(Callback::class.java) as Callback<OpenAiResponse>)).thenAnswer {
            val callback = it.getArgument<Callback<OpenAiResponse>>(0)
            callback.onFailure(mockCall, Throwable("Network error"))
        }

        // Call the repository method
        val request = OpenAiRequest(model = "gpt-4.1", messages = listOf(Message("user", "Hi")), temperature = 0.7)
        val authorizationHeader = "APIKEY"
        repository.getGptResponse(authorizationHeader, request, object : Callback<OpenAiResponse> {
            override fun onResponse(call: Call<OpenAiResponse>, response: Response<OpenAiResponse>) {
                fail("API call should have failed")
            }

            override fun onFailure(call: Call<OpenAiResponse>, t: Throwable) {
                assertEquals("Network error", t.message)
            }
        })
    }
}
