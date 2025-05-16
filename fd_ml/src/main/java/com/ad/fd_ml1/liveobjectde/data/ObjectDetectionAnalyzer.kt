// camera/ObjectDetectionAnalyzer.kt

package com.ad.fd_ml1.liveobjectde.data // Thay thế bằng package của bạn

import android.annotation.SuppressLint
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions


class ObjectAnalyzer(
  private val onDetectionResult: (List<DetectionResult>) -> Unit
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

    lensFacing == CameraSelector.LENS_FACING_FRONT
    imageProxy.imageInfo.rotationDegrees

    val frame = InputImage.fromMediaImage(
      imageProxy.image!!,
      imageProxy.imageInfo.rotationDegrees
    )
    objectDetector.process(frame)
      .addOnSuccessListener { detectedObjects ->
        // Task completed successfully
        val results = detectedObjects.map {
          DetectionResult(
            boundingBox = it.boundingBox,
            label = if (it.labels.isNotEmpty()) it.labels[0].text else "Object",
            confidence = if (it.labels.isNotEmpty()) it.labels[0].confidence else 0f
          )
        }
        onDetectionResult(results)
      }

      .addOnFailureListener { e ->
      }
      .addOnCompleteListener {
        imageProxy.close()
      }
  }
}