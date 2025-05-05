package com.ad.fd_ml1.object_detection.presentation

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ad.fd_ml1.assetsToBitmap
import com.ad.fd_ml1.object_detection.domain.ObjectDetectionUseCase
import com.google.mlkit.vision.objects.DetectedObject
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ObjectDetectionViewModel @Inject constructor(
   private val useCase: ObjectDetectionUseCase,
   @ApplicationContext private val context: Context
) : ViewModel() {

   private val bitmaps =
      listOf(
         "bird.jpg"
      ).map {
         context.assetsToBitmap(it)
      }

   private val _index = mutableIntStateOf(0)

   private val _bitmap = mutableStateOf(
      bitmaps[_index.intValue]
   )

   val index: State<Int> get() = _index

   val bitmap: State<Bitmap?> get() = _bitmap

   val lastIndex = bitmaps.size - 1

   fun goNextBitMap() {
      _index.intValue += 1
      _bitmap.value = bitmaps[_index.intValue]
   }

   fun goPreviousBitMap() {
      _index.intValue -= 1
      _bitmap.value = bitmaps[_index.intValue]
   }

   fun detectObject() =
      run {
         viewModelScope.launch {
            try {
               _bitmap.value = _bitmap.value?.let {
                  it.drawWithRectangle(useCase(it))
               }
            } catch (e: Exception) {
               Log.e("vm", e.message.toString())
            }
         }
      }

   private fun Bitmap.drawWithRectangle(objects: List<DetectedObject>): Bitmap? {
      val bitmap = copy(config!!, true)
      val canvas = Canvas(bitmap)
      val paint = Paint().apply {
         color = Color.RED
         style = Paint.Style.STROKE
         strokeWidth = 4.0f
         isAntiAlias = true
         // draw rectangle on canvas
      }
      for (obj in objects) {
         val bound = obj.boundingBox
         canvas.drawRect(
            bound,
            paint
         )
      }
      return bitmap
   }

   override fun onCleared() {
      super.onCleared()
      useCase.closeDetector()
   }
}