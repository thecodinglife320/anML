package com.ad.fd_ml1.gen.data

import android.graphics.Bitmap
import com.google.mlkit.vision.face.Face
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MLRepository @Inject constructor(
  private val faceDetectionModelML: FaceDetectionModelML,
  private val objectDetectionMLKit: ObjectDetectionMLKit,
  private val imageLabelModelML: ImageLabelModelML
) {

  suspend fun detectFace(bitmap: Bitmap): List<Face> =
    withContext(Dispatchers.IO) {
      faceDetectionModelML.detectFace(bitmap)
    }

  suspend fun labelImage(bitmap: Bitmap) =
    withContext(Dispatchers.IO) {
      imageLabelModelML.labelImage(bitmap)
    }

  suspend fun detectObject(bitmap: Bitmap) =
    withContext(Dispatchers.Default) {
      objectDetectionMLKit.detectObject(bitmap)
    }

  fun closeDetector() {
    faceDetectionModelML.closeDetector()
    objectDetectionMLKit.close()
    imageLabelModelML.close()
  }
}