package com.example.jetcaster.ui.network


import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface YouTubeApiService {
    @GET("search")
    suspend fun searchVideos(
        @Query("part") part: String = "snippet",
        @Query("q") query: String,  // La consulta de búsqueda
        @Query("key") apiKey: String // Tu API Key
    ): Response<YouTubeResponse>
}

