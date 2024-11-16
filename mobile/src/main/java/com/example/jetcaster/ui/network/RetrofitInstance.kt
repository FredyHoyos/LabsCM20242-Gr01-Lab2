package com.example.jetcaster.ui.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitInstance {
    private const val BASE_URL = "https://www.googleapis.com/youtube/v3/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val youTubeApi: YouTubeApiService by lazy {
        retrofit.create(YouTubeApiService::class.java)
    }
}

