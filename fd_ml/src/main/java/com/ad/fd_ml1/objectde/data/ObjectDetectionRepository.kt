package com.ad.fd_ml1.objectde.data

import android.graphics.Bitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ObjectDetectionRepository @Inject constructor(
  private val objectDetectionMLKit: ObjectDetectionMLKit,
  private val imageLabelModelML: ImageLabelModelML
) {

  suspend fun labelImage(bitmap: Bitmap) =
    withContext(Dispatchers.IO) {
      imageLabelModelML.labelImage(bitmap)
    }

  suspend fun detectObject(bitmap: Bitmap) =
    withContext(Dispatchers.Default) {
      objectDetectionMLKit.detectObject(bitmap)
    }

  fun closeDetector() {
    objectDetectionMLKit.close()
    imageLabelModelML.close()
  }
}