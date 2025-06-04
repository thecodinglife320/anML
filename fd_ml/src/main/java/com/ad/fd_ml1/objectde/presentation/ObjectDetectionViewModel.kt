package com.ad.fd_ml1.objectde.presentation

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
import com.ad.fd_ml1.objectde.data.ObjectDetectionRepository
import com.google.mlkit.vision.objects.DetectedObject
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ObjectDetectionViewModel @Inject constructor(
  private val objectDetectionRepository: ObjectDetectionRepository,
  @ApplicationContext private val context: Context
) : ViewModel() {

  val bitmaps =
    listOf(
      "https://cdn.pixabay.com/photo/2025/04/20/08/44/purple-leaf-plum-9545165_1280.jpg",
      "https://cdn.pixabay.com/photo/2023/04/21/17/47/plum-blossoms-7942343_640.jpg",
      "https://cdn.pixabay.com/photo/2013/03/20/23/20/butterfly-95364_640.jpg",
      "https://cdn.pixabay.com/photo/2021/01/28/10/36/dragonfly-5957597_1280.jpg"
    )

  private val _index = mutableIntStateOf(0)

  private var _bitmap = mutableStateOf<Bitmap?>(null)

  init {
    viewModelScope.launch {
      withContext(Dispatchers.IO) {
        _bitmap.value = getBitmapFromUrl(context, bitmaps[_index.intValue])
      }
    }
     Log.i(TAG, "init")
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

  fun detectObject() =
    run {
      viewModelScope.launch {
        try {
          val objects = mutableMapOf<DetectedObject, String>()
          _bitmap.value = _bitmap.value?.let { bitmap ->
            objectDetectionRepository.detectObject(bitmap).forEach {
              val bounds = it.boundingBox
              val croppedBitmap = Bitmap.createBitmap(
                bitmap,
                bounds.left,
                bounds.top,
                bounds.width(),
                bounds.height()
              )
              val label = objectDetectionRepository.labelImage(croppedBitmap)[0].text
              objects[it] = label
            }
            bitmap.drawWithRectangle(objects)
          }
        } catch (e: Exception) {
          Log.e("vm", e.message, e)
        }
      }
    }

  private fun Bitmap.drawWithRectangle(objects: Map<DetectedObject, String>): Bitmap? {
    val bitmap = copy(config!!, true)
    val canvas = Canvas(bitmap)
    val paint = Paint().apply {
      color = Color.RED
      style = Paint.Style.STROKE
      strokeWidth = 4.0f
      isAntiAlias = true
    }

    val paint1 = Paint().apply {
      color = Color.BLUE
      textSize = 20f
    }

    objects.forEach { a, b ->
      val bound = a.boundingBox
      canvas.drawRect(
        bound,
        paint
      )
      canvas.drawText(
        b,
        bound.left.toFloat(),
        bound.top.toFloat(), paint1
      )
    }
    return bitmap
  }

  override fun onCleared() {
    super.onCleared()
    objectDetectionRepository.closeDetector()
  }

  companion object {
    const val TAG = "MLViewModel"
  }
}