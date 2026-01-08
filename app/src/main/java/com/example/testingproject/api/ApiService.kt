package com.example.testingproject.api

import retrofit2.http.Body
import retrofit2.http.POST

// --- Groq Service (Free AI API) ---
interface GroqService {
    @POST("openai/v1/chat/completions")
    suspend fun createChatCompletion(
        @Body request: GroqRequest
    ): GroqResponse
}

// --- Groq Data Classes ---
data class GroqRequest(
    val model: String,
    val messages: List<GroqMessage>
)

data class GroqMessage(
    val role: String,
    val content: String
)

data class GroqResponse(
    val choices: List<GroqChoice>
)

data class GroqChoice(
    val message: GroqMessage
)

