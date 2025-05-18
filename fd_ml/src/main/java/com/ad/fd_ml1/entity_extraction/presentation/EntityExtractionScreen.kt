package com.ad.fd_ml1.entity_extraction.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntityExtractionScreen(
   modifier: Modifier = Modifier,
   vm: EntityExtractionViewModel
) {

   Column(modifier) {
      TextField(
         value = vm.input.value,
         onValueChange = vm::onInputChange,
         modifier = Modifier.height(200.dp)
      )
      OutlinedButton(onClick = vm::extraction) { Text("Extract entities") }
      Text(vm.result.value)
   }
}