package com.example.sedonortdd.data.repositories

import OpenAiApi
import com.example.sedonortdd.data.models.OpenAiRequest
import com.example.sedonortdd.data.models.OpenAiResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

class ChatbotRepository {

    private val openAiApi: OpenAiApi

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openai.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .build()
            )
            .build()

        openAiApi = retrofit.create(OpenAiApi::class.java)
    }

    fun getGptResponse(authorizationHeader: String, request: OpenAiRequest, callback: Callback<OpenAiResponse>) {
        openAiApi.getGptResponse(authorizationHeader, request).enqueue(callback)
    }
}
