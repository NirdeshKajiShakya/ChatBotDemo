package com.example.testingproject.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    
    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()
    
    private val geminiRetrofit = Retrofit.Builder()
        .baseUrl("https://generativelanguage.googleapis.com/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    
    private val placesRetrofit = Retrofit.Builder()
        .baseUrl("https://maps.googleapis.com/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    
    val geminiApi: GeminiApiService by lazy {
        geminiRetrofit.create(GeminiApiService::class.java)
    }
    
    val placesApi: GooglePlacesService by lazy {
        placesRetrofit.create(GooglePlacesService::class.java)
    }
}
