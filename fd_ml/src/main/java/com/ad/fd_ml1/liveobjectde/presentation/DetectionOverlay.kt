package com.ad.fd_ml1.liveobjectde.presentation

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ad.fd_ml1.liveobjectde.data.DetectionResult

@Composable
fun DetectionOverlay(
  detectionResults: List<DetectionResult>,
  imageWidth: Int,
  imageHeight: Int,
  previewViewWidth: Int,
  previewViewHeight: Int
) {
  val density = LocalDensity.current.density
  val textPaint = remember {
    Paint().apply {
      color = android.graphics.Color.YELLOW
      textSize = 18.sp.value.toPx(density)
      style = Paint.Style.FILL
    }
  }

  Canvas(modifier = Modifier.fillMaxSize()) {
    if (detectionResults.isEmpty() || imageWidth == 0 || imageHeight == 0 || previewViewWidth == 0 || previewViewHeight == 0) {
      return@Canvas
    }

    // Tính toán scale factor và offset để ánh xạ từ tọa độ ảnh sang tọa độ View
    val viewAspectRatio = previewViewWidth.toFloat() / previewViewHeight.toFloat()
    val imageAspectRatio = imageWidth.toFloat() / imageHeight.toFloat()

    val scaleX: Float
    val scaleY: Float
    val translateX: Float
    val translateY: Float

    // Giả định PreviewView sử dụng scale type tương tự CENTER_CROP hoặc FIT
    // Đây là logic ánh xạ đơn giản, có thể cần điều chỉnh tùy vào scale type thực tế của PreviewView
    if (viewAspectRatio > imageAspectRatio) { // PreviewView cao hơn ảnh
      scaleY = previewViewHeight.toFloat() / imageHeight.toFloat()
      scaleX = scaleY
      translateX = (previewViewWidth - imageWidth * scaleX) / 2f
      translateY = 0f
    } else { // PreviewView rộng hơn ảnh
      scaleX = previewViewWidth.toFloat() / imageWidth.toFloat()
      scaleY = scaleX
      translateY = (previewViewHeight - imageHeight * scaleY) / 2f
      translateX = 0f
    }


    detectionResults.forEach { result ->
      // Áp dụng scale và translate cho bounding box
      val mappedRect = android.graphics.RectF(
        result.boundingBox.left * scaleX + translateX,
        result.boundingBox.top * scaleY + translateY,
        result.boundingBox.right * scaleX + translateX,
        result.boundingBox.bottom * scaleY + translateY
      )
      drawRect(
        color = Color.Yellow,
        topLeft = Offset(mappedRect.left, mappedRect.top),
        size = Size(mappedRect.width(), mappedRect.height()),
        style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.dp.value.toPx(density))
      )

      val label = "${result.label}: %.2f".format(result.confidence)
      drawContext.canvas.nativeCanvas.drawText(
        label,
        mappedRect.left,
        mappedRect.top - 5.dp.value.toPx(density),
        textPaint
      )
    }
  }
}

fun Float.toPx(density: Float): Float {
  return this * density
}

@Composable
private fun remember(calculation: () -> Paint): Paint =
  androidx.compose.runtime.remember(calculation)