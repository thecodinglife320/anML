package com.ad.fd_ml1.face_detection.di

import com.ad.fd_ml1.face_detection.data.FaceDetectionRepositoryImpl
import com.ad.fd_ml1.face_detection.domain.FaceDetectionRepository
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
}