package com.ad.fd_ml1.object_detection.data

import android.graphics.Bitmap
import com.ad.fd_ml1.object_detection.domain.ObjectDetectionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ObjectDetectionRepositoryImpl @Inject constructor(
   private val mlKit: ObjectDetectionMLKit
) : ObjectDetectionRepository {

   override suspend fun detectObject(bitmap: Bitmap) =
      withContext(Dispatchers.Default) {
         mlKit.detectObject(bitmap)
      }

   override fun closeDetector() = mlKit.close()
}