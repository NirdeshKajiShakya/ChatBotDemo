package com.example.testingproject

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.testingproject.service.SchoolSearchService
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var schoolSearchService: SchoolSearchService
    private lateinit var chatBox: TextView
    private var chatHistory = StringBuilder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize API keys from BuildConfig
        val geminiApiKey = BuildConfig.GEMINI_API_KEY
        val placesApiKey = BuildConfig.PLACES_API_KEY

        if (!isValidApiKey(geminiApiKey) || !isValidApiKey(placesApiKey)) {
            showApiKeyError()
            return
        }

        schoolSearchService = SchoolSearchService(geminiApiKey, placesApiKey)

        val input = findViewById<EditText>(R.id.userInput)
        val sendBtn = findViewById<Button>(R.id.sendBtn)
        chatBox = findViewById(R.id.chatBox)

        sendBtn.setOnClickListener {
            val message = input.text.toString().trim()
            
            if (message.isEmpty()) {
                Toast.makeText(this, "Please enter a message", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            appendToChat("You: $message")
            input.text.clear()

            // Process the message
            processUserMessage(message)
        }
    }

    private fun isValidApiKey(key: String): Boolean {
        // Google API keys typically start with "AIza" and are 39 characters long
        // But allow placeholder keys for build testing
        if (key.isEmpty() || key == "placeholder_key") {
            return false
        }
        // Basic validation - check if it looks like a valid API key format
        return key.length >= 30 && key.matches(Regex("[A-Za-z0-9_-]+"))
    }

    private fun showApiKeyError() {
        chatBox = findViewById(R.id.chatBox)
        chatBox.text = """
            ⚠️ API Keys Not Configured
            
            Please add your API keys to local.properties:
            
            GEMINI_API_KEY=your_gemini_key_here
            PLACES_API_KEY=your_google_places_key_here
            
            To get free API keys:
            1. Gemini: https://makersuite.google.com/app/apikey
            2. Google Places: https://console.cloud.google.com/
            
            Both services have free tiers available.
        """.trimIndent()
    }

    private fun processUserMessage(message: String) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                appendToChat("Bot: Thinking...")
                
                val response = if (schoolSearchService.isSchoolSearchQuery(message)) {
                    // This is a school search query
                    val location = schoolSearchService.extractLocationFromQuery(message)
                    schoolSearchService.searchSchools(location)
                } else {
                    // Use Gemini AI for general conversation
                    val context = """
                        You are a helpful assistant that specializes in helping users find schools.
                        When users ask about schools in a location, provide friendly guidance.
                        Keep responses concise and helpful.
                    """.trimIndent()
                    schoolSearchService.chatWithGemini(message, context)
                }
                
                // Remove the "Thinking..." message and show the actual response
                updateLastBotMessage(response)
                
            } catch (e: Exception) {
                updateLastBotMessage("Error: ${e.message}")
            }
        }
    }

    private fun appendToChat(message: String) {
        if (chatHistory.isNotEmpty()) {
            chatHistory.append("\n\n")
        }
        chatHistory.append(message)
        chatBox.text = chatHistory.toString()
        
        // Auto-scroll to bottom
        chatBox.post {
            (chatBox.parent as? android.widget.ScrollView)?.fullScroll(android.view.View.FOCUS_DOWN)
        }
    }

    private fun updateLastBotMessage(newMessage: String) {
        val currentText = chatHistory.toString()
        val lastBotIndex = currentText.lastIndexOf("Bot: ")
        
        if (lastBotIndex != -1) {
            chatHistory.clear()
            chatHistory.append(currentText.substring(0, lastBotIndex))
            chatHistory.append("Bot: $newMessage")
            chatBox.text = chatHistory.toString()
            
            // Auto-scroll to bottom
            chatBox.post {
                (chatBox.parent as? android.widget.ScrollView)?.fullScroll(android.view.View.FOCUS_DOWN)
            }
        } else {
            appendToChat("Bot: $newMessage")
        }
    }
}