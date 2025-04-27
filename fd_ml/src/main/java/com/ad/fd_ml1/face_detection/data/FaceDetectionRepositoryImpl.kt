package com.ad.fd_ml1.face_detection.data

import android.graphics.Bitmap
import com.ad.fd_ml1.face_detection.domain.FaceDetectionRepository
import com.google.mlkit.vision.face.Face
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FaceDetectionRepositoryImpl @Inject constructor(
   private val faceDetectionModelML: FaceDetectionModelML
):FaceDetectionRepository{

   override suspend fun detectFace(bitmap: Bitmap): List<Face> =
      withContext(Dispatchers.IO) {
         faceDetectionModelML.detectFace(bitmap)
      }

   override fun closeDetector() {
      faceDetectionModelML.closeDetector()
   }
}