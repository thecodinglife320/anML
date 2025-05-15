package com.ad.fd_ml1.gen.presentation

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
import androidx.compose.material.icons.sharp.CenterFocusStrong
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
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MLScreen(
  modifier: Modifier = Modifier,
  vm: MLViewModel,
  onDrawerClick: () -> Unit
) {
  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text("🐵") },
        navigationIcon = {
          IconButton(onClick = onDrawerClick) {
            Icon(Icons.Default.Menu, contentDescription = null)
          }
        }
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
        index = vm.index.value,
        lastIndex = vm.lastIndex,
      )
      RowButton1(
        onDetectFaceButtonClick = vm::detectFace,
        onDetectObjectButtonClick = vm::detectObject
      )
    }
  }

}

@Composable
fun ImageToDetect(
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

@Preview
@Composable
fun RowButton(
  modifier: Modifier = Modifier,
  onNextButtonClick: () -> Unit = {},
  onPreviousButtonClick: () -> Unit = {},

  index: Int = 0,
  lastIndex: Int = 0,

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
  }
}

@Preview
@Composable
fun RowButton1(
  modifier: Modifier = Modifier,
  onDetectFaceButtonClick: () -> Unit = {},
  onDetectObjectButtonClick: () -> Unit = {}
) {
  Row(modifier) {
    OutlinedButton(onClick = onDetectFaceButtonClick) {
      Icon(imageVector = Icons.Sharp.Search, contentDescription = null)
      Text("Detect face")
    }
    OutlinedButton(onClick = onDetectObjectButtonClick) {
      Icon(imageVector = Icons.Sharp.CenterFocusStrong, contentDescription = null)
      Text("Detect object")
    }
  }
}