package com.example.jetcaster.ui.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.jetcaster.ui.network.RetrofitInstance
import com.example.jetcaster.ui.network.YouTubeResponse
import retrofit2.Response

class YouTubeSearchWorker(appContext: Context, workerParams: WorkerParameters) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val query = inputData.getString("query") ?: return Result.failure()

        try {
            // Realizar la búsqueda en YouTube
            val response: Response<YouTubeResponse> = RetrofitInstance.youTubeApi.searchVideos(query = query, apiKey = "AIzaSyAFWUN_gkOjBXpupOnxc4PXsktdTAY6bD8")

            if (response.isSuccessful) {
                // Procesar la respuesta de la API de YouTube
                val videoList = response.body()?.items ?: return Result.failure()
                // Aquí puedes hacer lo que necesites con la lista de videos, como guardarlos en la base de datos o actualizar la UI
                return Result.success()
            } else {
                return Result.failure()
            }
        } catch (e: Exception) {
            return Result.failure()
        }
    }
}


