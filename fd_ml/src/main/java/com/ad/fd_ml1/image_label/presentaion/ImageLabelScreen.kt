package com.ad.fd_ml1.image_label.presentaion

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.sharp.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.hilt.navigation.compose.hiltViewModel
import com.ad.fd_ml1.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageLabelScreen(
   modifier: Modifier = Modifier,
   vm: ImageLabelViewModel = hiltViewModel(),
   onDrawerClick: () -> Unit = {}
) {
   Scaffold(
      topBar = {
         TopAppBar(title = { Text(Screen.ImageLabel.title) }, navigationIcon = {
            IconButton(onClick = onDrawerClick) {
               Icon(Icons.Default.Menu, contentDescription = null)
            }
         })
      }
   ) {
      Column(
         modifier.padding(it)
      ) {

         ImageToLabel(
            bitmap = vm.bitmap.value,
         )
         RowButton(
            onNextButtonClick = vm::goNextBitMap,
            onPreviousButtonClick = vm::goPreviousBitMap,
            onLabelImageButtonClick = vm::labelImage,
            index = vm.index.value,
            lastIndex = vm.lastIndex
         )
         Text(vm.result.value)
      }
   }
}

@Composable
fun RowButton(
   modifier: Modifier = Modifier,
   onNextButtonClick: () -> Unit = {},
   onPreviousButtonClick: () -> Unit = {},
   onLabelImageButtonClick: () -> Unit = {},
   index: Int,
   lastIndex: Int
) {
   Row(modifier = modifier) {
      OutlinedButton(onClick = onPreviousButtonClick, enabled = (index != 0)) {
         Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
         Text("Previous image")
      }
      OutlinedButton(onClick = onNextButtonClick, enabled = (index != lastIndex)) {
         Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
         Text("Next Image")
      }
      OutlinedButton(onClick = onLabelImageButtonClick) {
         Icon(imageVector = Icons.Sharp.Search, contentDescription = null)
         Text("Label Image")
      }
   }
}

@Composable
fun ImageToLabel(
   modifier: Modifier = Modifier,
   bitmap: Bitmap?,
) {
   bitmap?.let {
      Image(
         modifier = modifier.fillMaxWidth(),
         bitmap = it.asImageBitmap(),
         contentDescription = null,
         contentScale = ContentScale.FillWidth
      )
   }
}