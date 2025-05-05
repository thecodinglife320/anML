package com.ad.fd_ml1.object_detection.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.ad.fd_ml1.face_detection.presentation.ImageToDetect
import com.ad.fd_ml1.face_detection.presentation.RowButton
import com.ad.fd_ml1.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ObjectDetectionScreen(
   modifier: Modifier = Modifier,
   vm: ObjectDetectionViewModel = hiltViewModel(),
   onDrawerClick: () -> Unit = {}
) {
   Scaffold(
      topBar = {
         TopAppBar(
            title = { Text(Screen.ObjectDetection.title) },
            navigationIcon = {
               IconButton(onClick = onDrawerClick) {
                  Icon(Icons.Default.Menu, contentDescription = null)
               }
            },
         )
      }
   ) {
      Column(
         modifier.padding(it)
      ) {

         ImageToDetect(
            bitmap = vm.bitmap.value,
         )
         RowButton(
            onNextButtonClick = vm::goNextBitMap,
            onPreviousButtonClick = vm::goPreviousBitMap,
            onDetectFaceButtonClick = vm::detectObject,
            index = vm.index.value,
            lastIndex = vm.lastIndex
         )
      }
   }

}