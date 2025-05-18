package com.ad.fd_ml1.handwriting.data

import com.google.mlkit.vision.digitalink.Ink
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class InkRecognitionRepository @Inject constructor(
   private val mlkit: InkRecognitionMlkit
) {

   suspend fun recognize(ink: Ink) =
      withContext(Dispatchers.Default) {
         mlkit.recognize(ink)
      }

   fun closeModel() = mlkit.closeRecognizer()
}