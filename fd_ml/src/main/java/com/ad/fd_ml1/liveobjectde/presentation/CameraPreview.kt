package com.ad.fd_ml1.liveobjectde.presentation

import android.content.Context
import androidx.camera.compose.CameraXViewfinder
import androidx.camera.core.SurfaceRequest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner

@Composable
fun CameraPreview(
  modifier: Modifier = Modifier,
  bindToCamera: (Context, LifecycleOwner) -> Unit,
  surfaceRequest: SurfaceRequest?,
  onGloballyPositioned: (LayoutCoordinates) -> Unit
) {

  val context = LocalContext.current
  val lifecycleOwner = LocalLifecycleOwner.current

  LaunchedEffect(lifecycleOwner) {
    bindToCamera(context, lifecycleOwner)
  }

  surfaceRequest?.let { request ->
    CameraXViewfinder(
      surfaceRequest = request,
      modifier = modifier.onGloballyPositioned {
        onGloballyPositioned(it)
      }
    )
  }
}