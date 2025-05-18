package com.ad.fd_ml1.facede

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CenterFocusStrong
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ad.fd_ml1.objectde.presentation.ImageToDetect
import com.ad.fd_ml1.objectde.presentation.RowButton

@Composable
fun FaceDetectionScreen(
   modifier: Modifier = Modifier,
   vm: FaceDetectionViewModel
) {
   Column(
      modifier
   ) {

      ImageToDetect(
         bitmap = vm.bitmap.value,
      )
      RowButton(
         onNextButtonClick = vm::goNextBitMap,
         onPreviousButtonClick = vm::goPreviousBitMap,
         index = vm.index.value,
         lastIndex = vm.lastIndex,
      )
      OutlinedButton(onClick = vm::detectFace) {
         Icon(Icons.Default.CenterFocusStrong, contentDescription = null)
      }
   }
}