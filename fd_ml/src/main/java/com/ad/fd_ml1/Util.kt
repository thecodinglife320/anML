package com.ad.fd_ml1

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.core.graphics.createBitmap
import coil.imageLoader
import coil.request.ErrorResult
import coil.request.ImageRequest
import coil.request.SuccessResult
import java.io.IOException

fun Context.assetsToBitmap(fileName: String) =
   try {
      assets.open(fileName).let {
         BitmapFactory.decodeStream(it)
      }
   } catch (e: IOException) {
      e.printStackTrace()
      null
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
