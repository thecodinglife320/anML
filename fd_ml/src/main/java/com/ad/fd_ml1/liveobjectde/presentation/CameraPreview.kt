package com.ad.fd_ml1.liveobjectde.presentation

import android.content.Context
import androidx.camera.compose.CameraXViewfinder
import androidx.camera.core.SurfaceRequest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner

@Composable
fun CameraPreview(
  bindToCamera: (Context, LifecycleOwner) -> Unit,
  surfaceRequest: SurfaceRequest?,
) {

  val context = LocalContext.current
  val lifecycleOwner = LocalLifecycleOwner.current

  LaunchedEffect(lifecycleOwner) {
    bindToCamera(context, lifecycleOwner)
  }

  surfaceRequest?.let { request ->
    CameraXViewfinder(
      surfaceRequest = request,
    )
  }
}