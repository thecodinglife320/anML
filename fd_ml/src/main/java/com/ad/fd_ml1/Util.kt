package com.ad.fd_ml1

import android.content.Context
import android.graphics.BitmapFactory
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