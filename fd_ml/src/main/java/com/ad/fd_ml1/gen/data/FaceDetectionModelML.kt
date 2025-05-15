package com.ad.fd_ml1.gen.data

import android.graphics.Bitmap
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class FaceDetectionModelML @Inject constructor() {

   private val highAccuracyOpts = FaceDetectorOptions.Builder()
      .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
      .build()

   private val detector = FaceDetection.getClient(highAccuracyOpts)

   suspend fun detectFace(bitmap: Bitmap): List<Face> =
     suspendCancellableCoroutine { continuation ->

       detector.process(InputImage.fromBitmap(bitmap, 0))
         .addOnSuccessListener {
           continuation.resume(it)
         }
         .addOnFailureListener {
           continuation.resumeWithException(it)
         }

       continuation.invokeOnCancellation {
         detector.close()
       }
     }

   fun closeDetector() = detector.close()
}