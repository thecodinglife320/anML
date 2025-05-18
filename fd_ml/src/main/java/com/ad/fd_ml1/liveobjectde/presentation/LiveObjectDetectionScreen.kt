package com.ad.fd_ml1.liveobjectde.presentation

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.ad.fd_ml1.liveobjectde.data.ObjectGraphic
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus.Denied
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun LiveObjectDetectionScreen(
   modifier: Modifier = Modifier,
   vm: LiveObjectDetectionViewModel
) {

   val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
   val detectionResults by vm.detectionResults.collectAsState()
   val imageSourceInfo by vm.imageSourceInfo.collectAsState()
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

      Box(modifier) {

         CameraPreview(
            bindToCamera = vm::bindToCamera,
            surfaceRequest = surfaceRequest,
         )

         AndroidView(
            factory = {
               GraphicOverlay(it, null)
            },
            update = { go ->
               go.clear()
               detectionResults.forEach {
                  go.add(ObjectGraphic(go, it))
               }
               imageSourceInfo?.let {
                  go.setImageSourceInfo(it.imageWidth, it.imageHeight, it.isFlipped)
               }
               go.postInvalidate()
            }
         )
      }
   }
}


