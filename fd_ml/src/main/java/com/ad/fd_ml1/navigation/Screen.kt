package com.ad.fd_ml1.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CenterFocusStrong
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Image
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
   object FaceDetection : Screen("face_detection", "Face detection", Icons.Default.Face)
   object ImageLabel : Screen("image_label", "Image Label", Icons.Default.Image)
   object ObjectDetection :
      Screen("object_detection", "Object Detection", Icons.Default.CenterFocusStrong)
}