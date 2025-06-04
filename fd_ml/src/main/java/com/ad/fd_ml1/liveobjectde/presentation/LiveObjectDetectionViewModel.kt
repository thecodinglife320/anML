package com.ad.fd_ml1.liveobjectde.presentation

import android.app.Application
import android.content.Context
import androidx.camera.core.CameraSelector.DEFAULT_BACK_CAMERA
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.core.SurfaceRequest
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.lifecycle.awaitInstance
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ad.fd_ml1.liveobjectde.data.ImageSourceInfo
import com.ad.fd_ml1.liveobjectde.data.ObjectAnalyzer
import com.google.mlkit.vision.objects.DetectedObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LiveObjectDetectionViewModel @Inject constructor(
   application: Application,
) : ViewModel() {

   private val preview = Preview.Builder().build()

   private val imageAnalysis = ImageAnalysis.Builder()
      .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
      .build().also {
         val objectDetectorAnalyzer = ObjectAnalyzer(
            onDetectionResult = { results ->
               _detectionResults.value = results
            },
            onSetImageSourceInfo = {
               _imageSourceInfo.value = it
            }
         )
         it.setAnalyzer(
            ContextCompat.getMainExecutor(application),
            objectDetectorAnalyzer
         )
      }

   private val _surfaceRequest = MutableStateFlow<SurfaceRequest?>(null)
   val surfaceRequest: StateFlow<SurfaceRequest?> = _surfaceRequest

   private val _detectionResults = MutableStateFlow<List<DetectedObject>>(emptyList())
   val detectionResults: StateFlow<List<DetectedObject>> get() = _detectionResults

   private val _imageSourceInfo = MutableStateFlow<ImageSourceInfo?>(null)
   val imageSourceInfo: StateFlow<ImageSourceInfo?> get() = _imageSourceInfo

   fun bindToCamera(appContext: Context, lifecycleOwner: LifecycleOwner) {

      viewModelScope.launch {
         preview.setSurfaceProvider { newSurfaceRequest ->
            _surfaceRequest.update { newSurfaceRequest }
         }

         val processCameraProvider = ProcessCameraProvider.awaitInstance(appContext)
         processCameraProvider.bindToLifecycle(
            lifecycleOwner,
            DEFAULT_BACK_CAMERA,
            preview,
            imageAnalysis
         )

         try {
            awaitCancellation()
         } finally {
            processCameraProvider.unbindAll()
         }
      }
   }
}