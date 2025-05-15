package com.ad.fd_ml1.gen.presentation

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.core.graphics.createBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.imageLoader
import coil.request.ErrorResult
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.ad.fd_ml1.gen.data.MLRepository
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.objects.DetectedObject
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MLViewModel @Inject constructor(
  private val mlRepository: MLRepository,
  @ApplicationContext private val context: Context
) : ViewModel() {

  private val bitmaps =
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

  fun detectFace() =
    viewModelScope.launch {
      try {
        val faces = mlRepository.detectFace(_bitmap.value!!)
        _bitmap.value = _bitmap.value!!.drawWithRectangle(faces)
      } catch (e: Exception) {
        Log.e("vm", e.message.toString())
      }
    }

  fun detectObject() =
    run {
      viewModelScope.launch {
        try {
          val objects = mutableMapOf<DetectedObject, String>()
          _bitmap.value = _bitmap.value?.let { bitmap ->
            mlRepository.detectObject(bitmap).forEach {
              val bounds = it.boundingBox
              val croppedBitmap = Bitmap.createBitmap(
                bitmap,
                bounds.left,
                bounds.top,
                bounds.width(),
                bounds.height()
              )
              val label = mlRepository.labelImage(croppedBitmap)[0].text
              objects[it] = label
            }
            bitmap.drawWithRectangle(objects)
          }
        } catch (e: Exception) {
          Log.e("vm", e.message, e)
        }
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

  suspend fun getBitmapFromUrl(context: Context, imageUrl: String): Bitmap? {

    val imageLoader = context.imageLoader

    val request = ImageRequest.Builder(context)
      .data(imageUrl)
      .build()

    val result = imageLoader.execute(request)

    when (result) {
      is SuccessResult -> {
        val drawable = result.drawable
        return drawableToMutableBitmap(drawable)
      }

      is ErrorResult -> {
        println("Error loading image: ${result.throwable}")
        return null
      }
    }
  }

  fun drawableToMutableBitmap(drawable: Drawable): Bitmap {
    if (drawable is BitmapDrawable) {
      return drawable.bitmap.copy(Bitmap.Config.ARGB_8888, true)
    } else {
      val width = if (drawable.intrinsicWidth <= 0) 1 else drawable.intrinsicWidth
      val height = if (drawable.intrinsicHeight <= 0) 1 else drawable.intrinsicHeight
      val bitmap = createBitmap(width, height)

      val canvas = Canvas(bitmap)
      drawable.setBounds(0, 0, width, height)
      drawable.draw(canvas)
      return bitmap
    }
  }

  override fun onCleared() {
    super.onCleared()
    mlRepository.closeDetector()
  }

  companion object {
    const val TAG = "MLViewModel"
  }
}