package com.example.testingproject.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface GeminiApiService {
    @POST("v1beta/models/gemini-pro:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GeminiRequest
    ): GeminiResponse
}

interface GooglePlacesService {
    @GET("maps/api/place/nearbysearch/json")
    suspend fun searchNearby(
        @Query("location") location: String,
        @Query("radius") radius: Int,
        @Query("type") type: String,
        @Query("keyword") keyword: String?,
        @Query("key") apiKey: String
    ): PlacesSearchResponse
    
    @GET("maps/api/geocode/json")
    suspend fun geocode(
        @Query("address") address: String,
        @Query("key") apiKey: String
    ): GeocodeResponse
}
