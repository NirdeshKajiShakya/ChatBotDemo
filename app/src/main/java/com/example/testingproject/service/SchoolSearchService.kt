package com.example.testingproject.service

import com.example.testingproject.api.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SchoolSearchService(
    private val geminiApiKey: String,
    private val placesApiKey: String
) {
    
    private val geminiApi = RetrofitClient.geminiApi
    private val placesApi = RetrofitClient.placesApi
    
    suspend fun searchSchools(locationName: String): String = withContext(Dispatchers.IO) {
        try {
            // First, geocode the location name to get coordinates
            val geocodeResponse = placesApi.geocode(locationName, placesApiKey)
            
            if (geocodeResponse.status != "OK" || geocodeResponse.results.isEmpty()) {
                return@withContext "I couldn't find the location '$locationName'. Please try a different location."
            }
            
            val location = geocodeResponse.results[0].geometry.location
            val locationString = "${location.lat},${location.lng}"
            
            // Search for schools near the location
            val searchResponse = placesApi.searchNearby(
                location = locationString,
                radius = 5000, // 5km radius
                type = "school",
                keyword = "school",
                apiKey = placesApiKey
            )
            
            if (searchResponse.status != "OK") {
                return@withContext "Error searching for schools: ${searchResponse.errorMessage ?: searchResponse.status}"
            }
            
            if (searchResponse.results.isEmpty()) {
                return@withContext "I couldn't find any schools near $locationName."
            }
            
            // Format the results
            val schoolsList = searchResponse.results.take(5).mapIndexed { index, school ->
                val rating = school.rating?.let { "⭐ $it" } ?: "No rating"
                val reviews = school.userRatingsTotal?.let { "($it reviews)" } ?: ""
                "${index + 1}. ${school.name}\n   📍 ${school.vicinity}\n   $rating $reviews"
            }.joinToString("\n\n")
            
            return@withContext "Here are schools near $locationName:\n\n$schoolsList"
            
        } catch (e: Exception) {
            return@withContext "Error searching for schools: ${e.message}"
        }
    }
    
    suspend fun chatWithGemini(userMessage: String, context: String = ""): String = withContext(Dispatchers.IO) {
        try {
            val prompt = if (context.isNotEmpty()) {
                "$context\n\nUser: $userMessage"
            } else {
                userMessage
            }
            
            val request = GeminiRequest(
                contents = listOf(
                    Content(
                        parts = listOf(Part(text = prompt))
                    )
                ),
                generationConfig = GenerationConfig(
                    temperature = 0.7,
                    maxOutputTokens = 1024
                )
            )
            
            val response = geminiApi.generateContent(geminiApiKey, request)
            
            if (response.candidates.isEmpty()) {
                return@withContext "I couldn't generate a response. Please try again."
            }
            
            val responseText = response.candidates[0].content.parts.firstOrNull()?.text
            return@withContext responseText ?: "No response generated."
            
        } catch (e: Exception) {
            return@withContext "Error communicating with AI: ${e.message}"
        }
    }
    
    fun isSchoolSearchQuery(query: String): Boolean {
        val keywords = listOf(
            "\\bschool\\b", "\\bschools\\b", "\\beducation\\b", "\\bacademy\\b", 
            "\\bacademies\\b", "\\bcollege\\b", "\\bcolleges\\b",
            "\\buniversity\\b", "\\buniversities\\b", "\\binstitute\\b", "\\binstitutes\\b", 
            "\\blearning\\s+center\\b", "\\blearning\\s+centers\\b"
        )
        val locationKeywords = listOf("near", "in", "around", "at", "close to")
        
        val lowerQuery = query.lowercase()
        val hasSchoolKeyword = keywords.any { Regex(it).containsMatchIn(lowerQuery) }
        val hasLocationKeyword = locationKeywords.any { lowerQuery.contains(it) }
        
        return hasSchoolKeyword && hasLocationKeyword
    }
    
    fun extractLocationFromQuery(query: String): String {
        // Simple extraction - looks for location after keywords
        val keywords = listOf("near", "in", "around", "at", "close to")
        val lowerQuery = query.lowercase()
        
        for (keyword in keywords) {
            val index = lowerQuery.indexOf(keyword)
            if (index != -1) {
                val afterKeyword = query.substring(index + keyword.length).trim()
                // Remove common trailing words
                return afterKeyword
                    .replace(Regex("\\?.*$"), "")
                    .replace(Regex("please.*$", RegexOption.IGNORE_CASE), "")
                    .trim()
            }
        }
        
        return query.trim()
    }
}
