package com.ad.fd_ml1.face_detection.domain

import android.graphics.Bitmap
import com.google.mlkit.vision.face.Face

interface FaceDetectionRepository {
   suspend fun detectFace(bitmap: Bitmap):List<Face>
   fun closeDetector()
}