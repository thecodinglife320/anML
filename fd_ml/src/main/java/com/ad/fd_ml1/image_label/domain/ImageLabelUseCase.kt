package com.ad.fd_ml1.image_label.domain

import android.graphics.Bitmap
import javax.inject.Inject

class ImageLabelUseCase @Inject constructor(
   private val imageLabelRepository: ImageLabelRepository
) {
   suspend operator fun invoke(bitmap: Bitmap)=imageLabelRepository.labelImage(bitmap)
   fun closeLabeler() = imageLabelRepository.closeLabeler()
}