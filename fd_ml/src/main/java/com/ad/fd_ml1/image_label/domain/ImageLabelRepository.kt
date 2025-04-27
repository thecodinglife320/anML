package com.ad.fd_ml1.image_label.domain

import android.graphics.Bitmap
import com.google.mlkit.vision.label.ImageLabel

interface ImageLabelRepository {
   suspend fun labelImage(bitmap: Bitmap):List<ImageLabel>
   fun closeLabeler()
}