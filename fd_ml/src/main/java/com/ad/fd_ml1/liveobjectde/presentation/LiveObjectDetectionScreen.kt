package com.ad.fd_ml1.liveobjectde.presentation

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntSize
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus.Denied
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun LiveObjectDetectionScreen(
  modifier: Modifier = Modifier,
  onDrawerClick: () -> Unit,
  vm: LiveObjectDetectionViewModel
) {

  val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
  val detectionResults by vm.detectionResults.collectAsState()
  val surfaceRequest by vm.surfaceRequest.collectAsState()

  if (cameraPermissionState.status is Denied) {
    if ((cameraPermissionState.status as Denied).shouldShowRationale) {
      val context = LocalContext.current
      Button(onClick = {
        val intent = Intent(
          Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
          Uri.fromParts("package", context.packageName, null)
        )
        context.startActivity(intent)
      }) { Text("di den cai dat") }
    } else {
      Button(onClick = cameraPermissionState::launchPermissionRequest) { Text("cap quyen") }
    }
  } else {

    var previewViewSize by remember { mutableStateOf(IntSize.Zero) }

    val imageAnalysisTargetSize = remember { IntSize(640, 480) }

    Scaffold(topBar = {
      TopAppBar(
        title = { Text("🐵") },
        navigationIcon = {
          IconButton(onClick = onDrawerClick) {
            Icon(Icons.Default.Menu, contentDescription = null)
          }
        }
      )
    }) {
      Box(
        modifier
          .padding(it)
      ) {

        CameraPreview(
          bindToCamera = vm::bindToCamera,
          surfaceRequest = surfaceRequest,
          onGloballyPositioned = {
            previewViewSize = it.size
          },
        )

        DetectionOverlay(
          detectionResults = detectionResults,
          imageWidth = imageAnalysisTargetSize.width,
          imageHeight = imageAnalysisTargetSize.height,
          previewViewWidth = previewViewSize.width,
          previewViewHeight = previewViewSize.height,
        )
      }
    }
  }
}

