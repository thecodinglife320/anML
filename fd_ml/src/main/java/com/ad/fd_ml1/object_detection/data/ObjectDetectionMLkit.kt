package com.ad.fd_ml1.object_detection.data

import android.graphics.Bitmap
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.DetectedObject
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class ObjectDetectionMLKit @Inject constructor() {

   private val options =
      ObjectDetectorOptions.Builder()
         .setDetectorMode(ObjectDetectorOptions.SINGLE_IMAGE_MODE)
         .enableMultipleObjects()
         .build()

   private val objectDetector = ObjectDetection.getClient(options)

   suspend fun detectObject(bitmap: Bitmap): List<DetectedObject> =
      suspendCancellableCoroutine {
         objectDetector.process(InputImage.fromBitmap(bitmap, 0))
            .addOnSuccessListener { detectedObjects ->
               it.resume(detectedObjects)
            }
            .addOnFailureListener { e ->
               it.resumeWithException(e)
            }
      }

   fun close() = objectDetector.close()
}