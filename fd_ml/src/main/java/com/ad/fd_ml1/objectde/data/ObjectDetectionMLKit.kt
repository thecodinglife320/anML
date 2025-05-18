package com.ad.fd_ml1.objectde.data

import android.graphics.Bitmap
import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
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
         .enableClassification()
         .enableMultipleObjects()
         .build()

   private val objectDetector = ObjectDetection.getClient(options)

   @OptIn(ExperimentalGetImage::class)
   suspend fun detectObject(bitmap: Bitmap): List<DetectedObject> =
      suspendCancellableCoroutine {
         objectDetector.process(
            InputImage.fromBitmap(bitmap, 0)
         )
            .addOnSuccessListener { detectedObjects ->
               Log.d(TAG, detectedObjects.toString())
               it.resume(detectedObjects)
            }
            .addOnFailureListener { e ->
               it.resumeWithException(e)
            }
      }

   fun close() = objectDetector.close()

   companion object {
      const val TAG = "mlkit"
   }
}