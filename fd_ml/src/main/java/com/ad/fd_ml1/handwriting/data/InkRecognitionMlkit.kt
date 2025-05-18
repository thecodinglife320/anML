package com.ad.fd_ml1.handwriting.data

import android.util.Log
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.vision.digitalink.DigitalInkRecognition
import com.google.mlkit.vision.digitalink.DigitalInkRecognitionModel
import com.google.mlkit.vision.digitalink.DigitalInkRecognitionModelIdentifier
import com.google.mlkit.vision.digitalink.DigitalInkRecognizer
import com.google.mlkit.vision.digitalink.DigitalInkRecognizerOptions
import com.google.mlkit.vision.digitalink.Ink
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

class InkRecognitionMlkit @Inject constructor() {

   private lateinit var recognizer: DigitalInkRecognizer

   init {
      val modelIdentifier: DigitalInkRecognitionModelIdentifier? =
         DigitalInkRecognitionModelIdentifier.fromLanguageTag("en-US")

      val model = DigitalInkRecognitionModel.builder(modelIdentifier!!).build()

      RemoteModelManager.getInstance().download(model, DownloadConditions.Builder().build())
         .addOnSuccessListener {
            Log.i("InkSample", "Model Downloaded")
            recognizer =
               DigitalInkRecognition.getClient(DigitalInkRecognizerOptions.builder(model).build())
         }.addOnFailureListener { e: Exception ->
            Log.e("InkSample", "Model failed $e")
         }
   }

   suspend fun recognize(ink: Ink) =
      suspendCancellableCoroutine {
         recognizer.recognize(ink)
            .addOnSuccessListener { results ->
               it.resume(results.candidates)
            }.addOnFailureListener {
               Log.e("Digital Ink Test", "Error during recognition: $it")
            }
         it.invokeOnCancellation {
            recognizer.close()
         }
      }

   fun closeRecognizer() = recognizer.close()

}