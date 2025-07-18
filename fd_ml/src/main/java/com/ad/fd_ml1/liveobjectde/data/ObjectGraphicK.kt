package com.ad.fd_ml1.liveobjectde.data

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import com.ad.fd_ml1.liveobjectde.presentation.GraphicOverlayK
import com.google.mlkit.vision.objects.DetectedObject
import java.util.Locale
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class ObjectGraphicK(
   overlay: GraphicOverlayK,
   private val detectedObject: DetectedObject
) : GraphicOverlayK.Graphic(overlay) {

   companion object {
      private const val TEXT_SIZE = 54.0f
      private const val STROKE_WIDTH = 4.0f
      private const val NUM_COLORS = 10
      private val COLORS = arrayOf(
         intArrayOf(Color.BLACK, Color.WHITE),
         intArrayOf(Color.WHITE, Color.MAGENTA),
         intArrayOf(Color.BLACK, Color.LTGRAY),
         intArrayOf(Color.WHITE, Color.RED),
         intArrayOf(Color.WHITE, Color.BLUE),
         intArrayOf(Color.WHITE, Color.DKGRAY),
         intArrayOf(Color.BLACK, Color.CYAN),
         intArrayOf(Color.BLACK, Color.YELLOW),
         intArrayOf(Color.WHITE, Color.BLACK),
         intArrayOf(Color.BLACK, Color.GREEN)
      )
      private const val LABEL_FORMAT = "%.2f%% confidence (index: %d)"
   }

   private val boxPaints: Array<Paint> = Array(NUM_COLORS) { Paint() }
   private val textPaints: Array<Paint> = Array(NUM_COLORS) { Paint() }
   private val labelPaints: Array<Paint> = Array(NUM_COLORS) { Paint() }

   init {

      for (i in 0 until NUM_COLORS) {
         textPaints[i].color = COLORS[i][0]
         textPaints[i].textSize = TEXT_SIZE

         boxPaints[i].color = COLORS[i][1]
         boxPaints[i].style = Paint.Style.STROKE
         boxPaints[i].strokeWidth = STROKE_WIDTH

         labelPaints[i].color = COLORS[i][1]
         labelPaints[i].style = Paint.Style.FILL
      }
   }

   override fun draw(canvas: Canvas) {

      val colorID = detectedObject.trackingId?.let { abs(it % NUM_COLORS) } ?: 0
      var textWidth = textPaints[colorID].measureText("Tracking ID: ${detectedObject.trackingId}")
      val lineHeight = TEXT_SIZE + STROKE_WIDTH
      var yLabelOffset = -lineHeight

      // Calculate width and height of label box
      for (label in detectedObject.labels) {
         textWidth = max(textWidth, textPaints[colorID].measureText(label.text))
         textWidth = max(
            textWidth,
            textPaints[colorID].measureText(
               String.format(
                  Locale.US, LABEL_FORMAT, label.confidence * 100, label.index
               )
            )
         )
         yLabelOffset -= 2 * lineHeight
      }

      val rect = RectF(detectedObject.boundingBox)
      val x0 = translateX(rect.left)
      val x1 = translateX(rect.right)
      rect.left = min(x0, x1)
      rect.right = max(x0, x1)
      rect.top = translateY(rect.top)
      rect.bottom = translateY(rect.bottom)
      canvas.drawRect(rect, boxPaints[colorID])

      canvas.drawRect(
         rect.left - STROKE_WIDTH,
         rect.top + yLabelOffset,
         rect.left + textWidth + (2 * STROKE_WIDTH),
         rect.top,
         labelPaints[colorID]
      )

      yLabelOffset += TEXT_SIZE
      canvas.drawText(
         "Tracking ID: ${detectedObject.trackingId}",
         rect.left,
         rect.top + yLabelOffset,
         textPaints[colorID]
      )
      yLabelOffset += lineHeight

      for (label in detectedObject.labels) {
         canvas.drawText(label.text, rect.left, rect.top + yLabelOffset, textPaints[colorID])
         yLabelOffset += lineHeight
         canvas.drawText(
            String.format(Locale.US, LABEL_FORMAT, label.confidence * 100, label.index),
            rect.left,
            rect.top + yLabelOffset,
            textPaints[colorID]
         )
         yLabelOffset += lineHeight
      }
   }
}