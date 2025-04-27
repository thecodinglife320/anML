package com.ad.fd_ml1.image_label.data

import android.graphics.Bitmap
import com.ad.fd_ml1.image_label.domain.ImageLabelRepository
import com.google.mlkit.vision.label.ImageLabel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ImageLabelRepositoryImpl @Inject constructor(
   private val imageLabelModelML: ImageLabelModelML
): ImageLabelRepository {

   override suspend fun labelImage(bitmap: Bitmap)=
      withContext(Dispatchers.IO){
         imageLabelModelML.labelImage(bitmap)
      }

   override fun closeLabeler() =imageLabelModelML.close()
}