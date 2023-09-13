package com.example.imagemagnify

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatImageView

class LoupeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : AppCompatImageView(context, attrs) {

    private val magnificationFactorDefault = 2.0f
    private val loupeRadiusDefault = 100

    private var magnificationFactor = magnificationFactorDefault
    private var loupeRadius = loupeRadiusDefault

    private var centerX = 0
    private var centerY = 0
    private var offsetX = 0
    private var offsetY = 0
    private var isTouching = false
    private val drawMatrix = Matrix()
    private val drawableBounds = RectF()
    private val loupePath = Path()
    private val loupeBorderPaint = Paint()

    init {
        loupeBorderPaint.strokeWidth = 15f
        loupeBorderPaint.style = Paint.Style.STROKE
        loupeBorderPaint.color = Color.WHITE
        loupeBorderPaint.alpha = 127
    }

    fun setMagnificationFactor(factor: Float) {
        magnificationFactor = factor
        invalidate()
    }

    fun setLoupeRadius(radius: Int) {
        loupeRadius = radius
        invalidate()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val action = event.action
        isTouching = !(action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP)
        centerX = event.x.toInt() + offsetX
        centerY = event.y.toInt() + offsetY
        invalidate()
        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (isTouching) {
            drawMagnifier(canvas)
        }
    }

    private fun drawMagnifier(canvas: Canvas) {
        if (drawable == null) {
            return
        }

        canvas.save()
        clipCircle(canvas)
        drawMatrix.reset()
        drawMatrix.preScale(magnificationFactor, magnificationFactor)
        drawMatrix.postConcat(imageMatrix)
        val px = centerX - drawableBounds.left
        val py = centerY - drawableBounds.top
        drawMatrix.postTranslate(-px * (magnificationFactor - 1), -py * (magnificationFactor - 1))

        canvas.drawBitmap((drawable as BitmapDrawable).bitmap, drawMatrix, null)

        canvas.drawCircle(
            centerX.toFloat(),
            centerY.toFloat(),
            loupeRadius.toFloat(),
            loupeBorderPaint
        )
        canvas.restore()
    }

    private fun clipCircle(canvas: Canvas) {
        loupePath.reset()
        loupePath.addCircle(centerX.toFloat(), centerY.toFloat(), loupeRadius.toFloat(), Path.Direction.CW)
        canvas.clipPath(loupePath)
    }
}
