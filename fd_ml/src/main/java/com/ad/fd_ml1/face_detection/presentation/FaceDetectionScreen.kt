package com.ad.fd_ml1.face_detection.presentation

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.sharp.Search
import androidx.compose.material.icons.twotone.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ad.fd_ml1.R

@Preview
@Composable
fun FaceDetectionScreen(
    modifier: Modifier = Modifier,
    vm: FaceDetectionViewModel = viewModel()
) {
    Column(
        modifier
    ) {

        ImageToDetect(
            bitmap =  vm.bitmap.value,
        )
        RowButton(
            onNextButtonClick = vm::goNextBitMap,
            onPreviousButtonClick = vm::goPreviousBitMap,
            onDetectFaceButtonClick = vm::detectFace,
            bitmaps = vm.bitmaps,
            bitmap = vm.bitmap.value
        )
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

@Composable
fun RowButton(
    modifier: Modifier = Modifier,
    onNextButtonClick:()->Unit={},
    onPreviousButtonClick:()->Unit={},
    onDetectFaceButtonClick:()->Unit={},
    bitmaps: List<Bitmap?>,
    bitmap: Bitmap?
) {
    Row(modifier = modifier) {
        OutlinedButton(onClick = onPreviousButtonClick, enabled = (bitmap!= bitmaps.first())) {
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
            Text("Previous image")
        }
        OutlinedButton(onClick = onNextButtonClick, enabled = (bitmap != bitmaps.last())) {
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
            Text("Next Image")
        }
        OutlinedButton(onClick = onDetectFaceButtonClick) {
            Icon(imageVector = Icons.Sharp.Search, contentDescription = null)
            Text("Detect face")
        }
    }
}