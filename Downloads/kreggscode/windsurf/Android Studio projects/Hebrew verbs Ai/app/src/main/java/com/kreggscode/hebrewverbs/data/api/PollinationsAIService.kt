package com.kreggscode.hebrewverbs.data.api

import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

/**
 * Pollinations AI API Service
 * Temperature is ALWAYS set to 1.0 for balanced creativity
 */
interface PollinationsAIService {
    @POST("openai")
    suspend fun chat(@Body request: ChatRequest): ChatResponse

    companion object {
        private const val BASE_URL = "https://text.pollinations.ai/"
        
        fun create(): PollinationsAIService {
            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build()
            
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            
            return retrofit.create(PollinationsAIService::class.java)
        }
    }
}

data class ChatRequest(
    val model: String = "openai",
    val messages: List<Message>,
    val temperature: Double = 1.0, // CRITICAL: Always 1.0
    @SerializedName("max_tokens")
    val maxTokens: Int = 1000,
    val stream: Boolean = false
)

data class Message(
    val role: String, // "system", "user", or "assistant"
    val content: String
)

data class ChatResponse(
    val choices: List<Choice>
)

data class Choice(
    val message: Message,
    @SerializedName("finish_reason")
    val finishReason: String
)
