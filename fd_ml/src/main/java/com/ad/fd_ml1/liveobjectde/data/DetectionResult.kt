package com.ad.fd_ml1.liveobjectde.data

import android.graphics.Rect

data class DetectionResult(
  val boundingBox: Rect,
  val label: String?, // Nhãn của vật thể (nếu bật phân loại)
  val confidence: Float // Độ tin cậy
)