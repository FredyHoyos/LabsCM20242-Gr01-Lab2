/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.bluromatic.data

import android.content.Context
import android.net.Uri
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.bluromatic.KEY_BLUR_LEVEL
import com.example.bluromatic.KEY_IMAGE_URI
import com.example.bluromatic.getImageUri
import com.example.bluromatic.workers.BlurWorker
import com.example.bluromatic.workers.CleanupWorker
import com.example.bluromatic.workers.SaveImageToFileWorker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class WorkManagerBluromaticRepository(context: Context) : BluromaticRepository {

    private val workManager = WorkManager.getInstance(context)
    private var imageUri: Uri = context.getImageUri()

    override val outputWorkInfo: Flow<WorkInfo?> = MutableStateFlow(null)


    /**
     * Create the WorkRequests to apply the blur and save the resulting image
     * @param blurLevel The amount to blur the image
     */
    override fun applyBlur(blurLevel: Int) {
        // Add WorkRequest to Cleanup temporary images
        // equivale a llamar a OneTimeWorkRequestBuilder<CleanupWorker>().build()
        var continuation = workManager.beginWith(OneTimeWorkRequest.from(CleanupWorker::class.java))
        // Create WorkRequest to blur the image
        val blurBuilder = OneTimeWorkRequestBuilder<BlurWorker>()
        // New code for input data object
        blurBuilder.setInputData(createInputDataForWorkRequest(blurLevel, imageUri))
        // Start the work
        //workManager.enqueue(blurBuilder.build())

        // Add the blur work request to the chain
        continuation = continuation.then(blurBuilder.build())
        // Add WorkRequest to save the image to the filesystem
        val save = OneTimeWorkRequestBuilder<SaveImageToFileWorker>()
            .build()
        continuation = continuation.then(save)
        // Start the work
        continuation.enqueue()
    }


    /**
     * Cancel any ongoing WorkRequests
     * */
    override fun cancelWork() {

    }


    /**
     * Creates the input data bundle which includes the blur level to
     * update the amount of blur to be applied and the Uri to operate on
     * @return Data which contains the Image Uri as a String and blur level as an Integer
     */
    // For reference - already exists in the app
    private fun createInputDataForWorkRequest(blurLevel: Int, imageUri: Uri): Data {
        val builder = Data.Builder()
        builder.putString(KEY_IMAGE_URI, imageUri.toString()).putInt(KEY_BLUR_LEVEL, blurLevel)
        return builder.build()
    }
}