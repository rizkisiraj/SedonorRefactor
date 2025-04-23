package com.example.sedonortdd.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.sedonortdd.data.models.Message
import com.example.sedonortdd.data.models.OpenAiRequest
import com.example.sedonortdd.data.models.OpenAiResponse
import com.example.sedonortdd.data.repositories.ChatbotRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatBotViewModel(application: Application) : AndroidViewModel(application) {

    private val chatbotRepository = ChatbotRepository()

    val conversation = MutableLiveData<MutableList<String>>(mutableListOf())
    val isLoading = MutableLiveData<Boolean>(false)
    val errorMessage = MutableLiveData<String>("")

    private val apiKey = "APIIKEY"
    val authorizationHeader = "Bearer $apiKey"

    fun sendMessageToChatbot(userInput: String) {
        isLoading.value = true
        val request = OpenAiRequest(
            model = "gpt-4.1",
            messages = listOf(Message(role = "user", content = userInput)),
            temperature = 0.7
        )

        viewModelScope.launch(Dispatchers.IO) {
            chatbotRepository.getGptResponse(authorizationHeader, request, object : Callback<OpenAiResponse> {
                override fun onResponse(
                    call: Call<OpenAiResponse>,
                    response: Response<OpenAiResponse>
                ) {
                    isLoading.postValue(false)
                    if (response.isSuccessful) {
                        val gpt3Answer = response.body()?.choices?.get(0)?.message?.content
                        gpt3Answer?.let {
                            updateConversation(it)
                        }
                    } else {
                        errorMessage.postValue("Error: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<OpenAiResponse>, t: Throwable) {
                    isLoading.postValue(false)
                    errorMessage.postValue("Failed: ${t.message}")
                }
            })
        }
    }

    private fun updateConversation(answer: String) {
        val currentConversation = conversation.value ?: mutableListOf()
        currentConversation.add("User: $answer")
        conversation.postValue(currentConversation)
    }
}
