package com.ad.fd_ml1.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.CenterFocusStrong
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
   object Detection : Screen("detection", "Detection", Icons.Default.CenterFocusStrong)
  object LiveObjectDetection :
    Screen("live_object_detection", "LiveObjectDetection", Icons.Default.Camera)
}