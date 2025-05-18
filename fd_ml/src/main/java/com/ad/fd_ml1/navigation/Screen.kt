package com.ad.fd_ml1.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.CenterFocusStrong
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Handyman
import androidx.compose.material.icons.filled.Rocket
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val icon: ImageVector) {
   object ObjectDetection :
      Screen("object_detection", Icons.Default.CenterFocusStrong)

   object FaceDetection : Screen("face_detection", Icons.Default.Face)
   object EntityExtraction : Screen("entity_extraction", Icons.Default.Rocket)
   object InkRecognition : Screen("ink_recognition", Icons.Default.Handyman)
   object LiveObjectDetection :
      Screen("live_object_detection", Icons.Default.Camera)
}