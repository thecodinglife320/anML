package com.ad.fd_ml1.liveobjectde.data

import android.annotation.SuppressLint
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.DetectedObject
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions


class ObjectAnalyzer(
   private val onDetectionResult: (List<DetectedObject>) -> Unit,
   private val onSetImageSourceInfo: (ImageSourceInfo) -> Unit
) : ImageAnalysis.Analyzer {

   val options =
      ObjectDetectorOptions.Builder()
         .setDetectorMode(ObjectDetectorOptions.STREAM_MODE)
         .enableMultipleObjects()
         .enableClassification()
         .build()

   val objectDetector = ObjectDetection.getClient(options)
   private val lensFacing = CameraSelector.LENS_FACING_BACK

   @OptIn(ExperimentalGetImage::class)
   @SuppressLint("UnsafeExperimentalUsageError")
   override fun analyze(imageProxy: ImageProxy) {
      val isImageFlipped = lensFacing == CameraSelector.LENS_FACING_FRONT
      val rotationDegrees = imageProxy.imageInfo.rotationDegrees
      if (rotationDegrees == 0 || rotationDegrees == 180)
         onSetImageSourceInfo(
            ImageSourceInfo(
               imageProxy.width,
               imageProxy.height,
               isImageFlipped
            )
         ) else
         onSetImageSourceInfo(ImageSourceInfo(imageProxy.height, imageProxy.width, isImageFlipped))
      val frame = InputImage.fromMediaImage(
         imageProxy.image!!,
         imageProxy.imageInfo.rotationDegrees
      )
      objectDetector.process(frame)
         .addOnSuccessListener {
            onDetectionResult(it)
         }
         .addOnFailureListener { e ->
         }
         .addOnCompleteListener {
            imageProxy.close()
         }
   }
}