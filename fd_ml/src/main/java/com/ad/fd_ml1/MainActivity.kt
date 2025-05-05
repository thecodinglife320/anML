package com.ad.fd_ml1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.ad.fd_ml1.navigation.presentation.AppNavigation
import com.ad.fd_ml1.ui.theme.AvalonTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      enableEdgeToEdge()
      setContent {
         AvalonTheme {
            AppNavigation()
         }
      }
   }
}