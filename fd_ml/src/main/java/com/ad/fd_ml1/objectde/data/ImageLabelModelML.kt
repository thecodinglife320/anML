package com.ad.fd_ml1.objectde.data

import android.graphics.Bitmap
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabel
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class ImageLabelModelML @Inject constructor() {

   private val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)

   suspend fun labelImage(bitmap: Bitmap): List<ImageLabel> =
     suspendCancellableCoroutine {
       labeler.process(InputImage.fromBitmap(bitmap, 0))
         .addOnSuccessListener { labels ->
           it.resume(labels)
         }
         .addOnFailureListener { e ->
           it.resumeWithException(e)
         }
     }

   fun close() {
      labeler.close()
   }
}