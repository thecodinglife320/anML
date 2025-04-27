package com.ad.fd_ml1.face_detection.presentation

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import com.ad.fd_ml1.face_detection.domain.FaceDetectionUseCase
import com.google.mlkit.vision.face.Face
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import java.io.IOException
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class FaceDetectionViewModel @Inject constructor(
   private val faceDetectionUseCase: FaceDetectionUseCase,
   @ApplicationContext private val context: Context,
) : ViewModel() {

   private val bitmaps =
      listOf(
         "face-test.jpg",
         "face-test-2.jpg",
         "face-test-3.jpg",
         "face-test-5.jpg",
         "osamu-tezuka.jpg",
         "osamu-tezuka2.jpg",
         "osamu-tezuka3.jpg",
         "deo-khau-trang.jpg"
      ).map {
         context.assetsToBitmap(it)
      }

   private val _index = mutableIntStateOf(0)

   private val _bitmap =mutableStateOf(
      bitmaps[_index.intValue]
   )

   val index:State<Int> get() = _index

   val bitmap: State<Bitmap?> get() = _bitmap

   val lastIndex = bitmaps.size-1

   private fun Bitmap.drawWithRectangle(faces: List<Face>): Bitmap? {
      val bitmap = copy(config!!, true)
      val canvas = Canvas(bitmap)
      val paint = Paint().apply {
         color = Color.RED
         style = Paint.Style.STROKE
         strokeWidth = 4.0f
         isAntiAlias = true
         // draw rectangle on canvas
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

   fun goNextBitMap(){
      _index.intValue +=1
      _bitmap.value=bitmaps[_index.intValue]
   }

   fun goPreviousBitMap(){
      _index.intValue-=1
      _bitmap.value=bitmaps[_index.intValue]
   }

   fun detectFace()=
      run {
         viewModelScope.launch {
            try {
               val faces = faceDetectionUseCase(_bitmap.value!!)
               _bitmap.value = _bitmap.value!!.drawWithRectangle(faces)
            }catch (e: Exception){
               Log.e("vm",e.message.toString())
            }

         }
      }

   override fun onCleared() {
      super.onCleared()
      faceDetectionUseCase.closeDetector()
   }
}