package com.ad.fd_ml1.di

import com.ad.fd_ml1.face_detection.data.FaceDetectionRepositoryImpl
import com.ad.fd_ml1.face_detection.domain.FaceDetectionRepository
import com.ad.fd_ml1.image_label.data.ImageLabelRepositoryImpl
import com.ad.fd_ml1.image_label.domain.ImageLabelRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

   @Binds
   abstract fun bindFaceDetectionRepository(
      faceDetectionRepositoryImpl: FaceDetectionRepositoryImpl,
   ): FaceDetectionRepository

   @Binds
   abstract fun bindImageLabelRepository(
      imageLabelRepositoryImpl: ImageLabelRepositoryImpl
   ):ImageLabelRepository
}