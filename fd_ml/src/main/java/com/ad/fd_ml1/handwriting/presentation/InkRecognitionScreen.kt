package com.ad.fd_ml1.handwriting.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InkRecognitionScreen(
   modifier: Modifier = Modifier,
   vm: InkRecognitionViewModel
) {

   Column(modifier) {
      AndroidView(
         factory = {
            CustomDrawingSurface(
               it,
               onTouchUp = {
                  vm.onTouchUp(it)
               }
            ).also {
               vm.clearLambda = {
                  it.clear()
               }
            }
         },
         modifier = Modifier.height(500.dp)
      )
      OutlinedButton(onClick = vm::recognize) { Text("Recognize") }
      OutlinedButton(onClick = vm::onClear) { Text("Clear") }
      Text(vm.results.value)
   }
}