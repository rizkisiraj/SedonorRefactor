// ChatBotViewModelTest.kt
package com.example.sedonortdd.viewModel

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.sedonortdd.data.models.*
import com.example.sedonortdd.data.repositories.ChatbotRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.*
import org.mockito.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@ExperimentalCoroutinesApi
class ChatBotViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    lateinit var repository: ChatbotRepository

    @Mock
    lateinit var call: Call<OpenAiResponse>

    @Mock
    lateinit var conversationObserver: Observer<MutableList<String>>

    @Mock
    lateinit var loadingObserver: Observer<Boolean>

    private lateinit var viewModel: ChatBotViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)

        viewModel = ChatBotViewModel(Application())
        viewModel.conversation.observeForever(conversationObserver)
        viewModel.isLoading.observeForever(loadingObserver)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `sendMessageToChatbot should update conversation on success`() {
        val response = OpenAiResponse(
            id = "1",
            `object` = "chat.completion",
            created = System.currentTimeMillis(),
            model = "gpt-4.1",
            usage = Usage(0, 0, 0),
            choices = listOf(Choice(Message("assistant", "Halo!"), "stop", 0))
        )

        val request = OpenAiRequest("gpt-4.1", listOf(Message("user", "Halo")), 0.7)

        Mockito.`when`(repository.getGptResponse(Mockito.anyString(), Mockito.eq(request), Mockito.any()))
            .thenAnswer {
                val callback = it.getArgument<Callback<OpenAiResponse>>(2)
                callback.onResponse(call, Response.success(response))
                null
            }

        viewModel.sendMessageToChatbot("Halo")
        testDispatcher.scheduler.advanceUntilIdle()

        Assert.assertTrue(viewModel.conversation.value!!.contains("User: Halo!"))
    }
}
