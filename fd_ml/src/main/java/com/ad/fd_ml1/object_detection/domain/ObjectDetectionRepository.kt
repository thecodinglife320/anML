package com.ad.fd_ml1.object_detection.domain

import android.graphics.Bitmap
import com.google.mlkit.vision.objects.DetectedObject

interface ObjectDetectionRepository {
   suspend fun detectObject(bitmap: Bitmap): List<DetectedObject>
   fun closeDetector()
}