package com.ad.fd_ml1.object_detection.domain

import android.graphics.Bitmap
import javax.inject.Inject

class ObjectDetectionUseCase @Inject constructor(
   private val repository: ObjectDetectionRepository
) {
   suspend operator fun invoke(bitmap: Bitmap) = repository.detectObject(bitmap)
   fun closeDetector() = repository.closeDetector()
}