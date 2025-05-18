package com.ad.fd_ml1.facede

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
import com.ad.fd_ml1.getBitmapFromUrl
import com.google.mlkit.vision.face.Face
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FaceDetectionViewModel @Inject constructor(
   private val mlkit: FaceDetectionModelMlkit,
   @ApplicationContext private val context: Context
) : ViewModel() {
   val bitmaps =
      listOf(
         "https://cdn.pixabay.com/photo/2024/03/09/09/21/ai-generated-8622323_640.jpg",
         "https://cdn.pixabay.com/photo/2015/05/11/16/09/portrait-762666_640.jpg",
         "https://cdn.pixabay.com/photo/2020/09/25/16/50/portrait-5601950_640.jpg"
      )

   private val _index = mutableIntStateOf(0)

   private var _bitmap = mutableStateOf<Bitmap?>(null)

   init {
      viewModelScope.launch {
         withContext(Dispatchers.IO) {
            _bitmap.value = getBitmapFromUrl(context, bitmaps[_index.intValue])
         }
      }
   }

   val index: State<Int> get() = _index

   val bitmap: State<Bitmap?> get() = _bitmap

   val lastIndex = bitmaps.size - 1

   fun goNextBitMap() {
      _index.intValue += 1
      viewModelScope.launch {
         _bitmap.value = getBitmapFromUrl(context, bitmaps[_index.intValue])
      }
   }

   fun goPreviousBitMap() {
      _index.intValue -= 1
      viewModelScope.launch {
         _bitmap.value = getBitmapFromUrl(context, bitmaps[_index.intValue])
      }
   }

   private fun Bitmap.drawWithRectangle(faces: List<Face>): Bitmap? {
      val bitmap = copy(config!!, true)
      val canvas = Canvas(bitmap)
      val paint = Paint().apply {
         color = Color.RED
         style = Paint.Style.STROKE
         strokeWidth = 4.0f
         isAntiAlias = true
      }
      for (face in faces) {
         val bound = face.boundingBox
         canvas.drawRect(
            bound,
            paint
         )
      }
      return bitmap
   }

   fun detectFace() =
      viewModelScope.launch {
         try {
            val faces = mlkit.detectFace(_bitmap.value!!)
            _bitmap.value = _bitmap.value!!.drawWithRectangle(faces)
         } catch (e: Exception) {
            Log.e("vm", e.message.toString())
         }
      }

   override fun onCleared() {
      super.onCleared()
      mlkit.closeDetector()
   }
}