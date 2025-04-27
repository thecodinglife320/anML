package com.ad.fd_ml1.face_detection.domain

import android.graphics.Bitmap
import javax.inject.Inject

class FaceDetectionUseCase @Inject constructor (
   private val faceDetectionRepository: FaceDetectionRepository
){
   suspend operator fun invoke(bitmap: Bitmap)=
      faceDetectionRepository.detectFace(bitmap)

   fun closeDetector() = faceDetectionRepository.closeDetector()
}

