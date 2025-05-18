package com.ad.fd_ml1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ad.fd_ml1.entity_extraction.presentation.EntityExtractionScreen
import com.ad.fd_ml1.entity_extraction.presentation.EntityExtractionViewModel
import com.ad.fd_ml1.facede.FaceDetectionScreen
import com.ad.fd_ml1.facede.FaceDetectionViewModel
import com.ad.fd_ml1.handwriting.presentation.InkRecognitionScreen
import com.ad.fd_ml1.handwriting.presentation.InkRecognitionViewModel
import com.ad.fd_ml1.liveobjectde.presentation.LiveObjectDetectionScreen
import com.ad.fd_ml1.liveobjectde.presentation.LiveObjectDetectionViewModel
import com.ad.fd_ml1.navigation.Screen
import com.ad.fd_ml1.objectde.presentation.MLScreen
import com.ad.fd_ml1.objectde.presentation.ObjectDetectionViewModel
import com.ad.fd_ml1.ui.theme.AvalonTheme
import dagger.hilt.android.AndroidEntryPoint

@OptIn(ExperimentalMaterial3Api::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      enableEdgeToEdge()
      setContent {
         AvalonTheme {

            val navController = rememberNavController()

            Scaffold(
               topBar = { TopAppBar(title = { Text("Machine learning for Android") }) }
            ) {
               Column(modifier = Modifier.padding(it)) {
                  Row {
                     listOf(
                        Screen.ObjectDetection,
                        Screen.FaceDetection,
                        Screen.InkRecognition,
                        Screen.EntityExtraction,
                        Screen.LiveObjectDetection
                     ).forEach { screen ->
                        OutlinedButton(
                           onClick = {
                              navController.navigate(screen.route) {
                                 popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                 }
                                 launchSingleTop = true
                                 restoreState = true
                              }
                           },
                        ) {
                           Icon(screen.icon, contentDescription = null)
                        }
                     }
                  }
                  NavHost(
                     navController = navController,
                     startDestination = Screen.ObjectDetection.route,
                  ) {
                     composable(Screen.ObjectDetection.route) {
                        val vm = hiltViewModel<ObjectDetectionViewModel>()
                        MLScreen(
                           vm = vm,
                        )
                     }
                     composable(Screen.EntityExtraction.route) {
                        val vm = hiltViewModel<EntityExtractionViewModel>()
                        EntityExtractionScreen(
                           vm = vm
                        )
                     }
                     composable(Screen.InkRecognition.route) {
                        val vm = hiltViewModel<InkRecognitionViewModel>()
                        InkRecognitionScreen(
                           vm = vm
                        )
                     }
                     composable(Screen.LiveObjectDetection.route) {
                        val vm = hiltViewModel<LiveObjectDetectionViewModel>()
                        LiveObjectDetectionScreen(
                           vm = vm
                        )
                     }
                     composable(Screen.FaceDetection.route) {
                        val vm = hiltViewModel<FaceDetectionViewModel>()
                        FaceDetectionScreen(
                           vm = vm
                        )
                     }
                  }
               }
            }
         }
      }
   }
}