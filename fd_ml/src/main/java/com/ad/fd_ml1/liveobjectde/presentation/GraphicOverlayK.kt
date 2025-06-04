package com.ad.fd_ml1.liveobjectde.presentation

import android.content.Context
import android.graphics.Canvas
import android.graphics.Matrix
import android.util.AttributeSet
import android.view.View

@Suppress("unused")
class GraphicOverlayK @JvmOverloads constructor(
   context: Context,
   attrs: AttributeSet? = null
) : View(context, attrs) {

   private val lock = Any()

   private val graphics: MutableList<Graphic> = ArrayList()

   private val transformationMatrix = Matrix()

   private var imageWidth: Int = 0

   private var imageHeight: Int = 0

   private var scaleFactor = 1.0f

   private var postScaleWidthOffset: Float = 0f

   private var postScaleHeightOffset: Float = 0f

   private var isImageFlipped: Boolean = false

   private var needUpdateTransformation = true

   init {
      addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
         needUpdateTransformation = true
      }
   }

   fun clear() {
      synchronized(lock) {
         graphics.clear()
      }
      postInvalidate()
   }

   fun add(graphic: Graphic) {
      synchronized(lock) {
         graphics.add(graphic)
      }
   }

   fun remove(graphic: Graphic) {
      synchronized(lock) {
         graphics.remove(graphic)
      }
      postInvalidate()
   }

   fun setImageSourceInfo(imageWidth: Int, imageHeight: Int, isFlipped: Boolean) {
      synchronized(lock) {
         this.imageWidth = imageWidth
         this.imageHeight = imageHeight
         this.isImageFlipped = isFlipped
         needUpdateTransformation = true
      }
      postInvalidate()
   }

   private fun updateTransformationIfNeeded() {
      if (!needUpdateTransformation || imageWidth <= 0 || imageHeight <= 0) {
         return
      }
      val viewAspectRatio = width.toFloat() / height
      val imageAspectRatio = imageWidth.toFloat() / imageHeight
      postScaleWidthOffset = 0f
      postScaleHeightOffset = 0f

      if (viewAspectRatio > imageAspectRatio) {
         scaleFactor = width.toFloat() / imageWidth
         postScaleHeightOffset = (width.toFloat() / imageAspectRatio - height) / 2
      } else {
         scaleFactor = height.toFloat() / imageHeight
         postScaleWidthOffset = (height.toFloat() * imageAspectRatio - width) / 2
      }

      transformationMatrix.reset()
      transformationMatrix.setScale(scaleFactor, scaleFactor)
      transformationMatrix.postTranslate(-postScaleWidthOffset, -postScaleHeightOffset)

      if (isImageFlipped) {
         transformationMatrix.postScale(-1f, 1f, width / 2f, height / 2f)
      }

      needUpdateTransformation = false
   }

   override fun onDraw(canvas: Canvas) {
      super.onDraw(canvas)

      synchronized(lock) {
         updateTransformationIfNeeded()

         for (graphic in graphics) {
            graphic.draw(canvas)
         }
      }
   }

   abstract class Graphic(
      private val overlayK: GraphicOverlayK
   ) {

      fun scale(imagePixel: Float) = imagePixel * overlayK.scaleFactor

      abstract fun draw(canvas: Canvas)

      fun translateX(x: Float) =
         if (overlayK.isImageFlipped) overlayK.width - (scale(x) - overlayK.postScaleWidthOffset)
         else scale(x) - overlayK.postScaleWidthOffset

      fun translateY(y: Float) = scale(y) - overlayK.postScaleHeightOffset
   }
}