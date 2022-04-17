package com.example.pacman

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

class PacmanView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    companion object {
        private const val MARGIN_COEFF = 0.8f
        private const val START_ANGLE = 45f
        private const val END_ANGLE = 270f
        private const val STROKE_WIDTH = 40f
    }

    private var radius = 0f

    private var centerX = 0f

    private var centerY = 0f

    private val backgroundPacmanPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val strokeBackground = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = context.getColor(R.color.black)
        strokeWidth = STROKE_WIDTH
        style = Paint.Style.STROKE
    }

    private val pacmanStroke by lazy {
        RectF(
            centerX - radius,
            centerY - radius,
            centerX + radius,
            centerY + radius
        )
    }

    private val pacmanEyeColor = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.PacmanView, 0, 0)
            .apply {
                backgroundPacmanPaint.apply {
                    color = getColor(
                        R.styleable.PacmanView_pacmanColor,
                        context.getColor(R.color.teal_200)
                    )
                }
                pacmanEyeColor.apply {
                    color = getColor(
                        R.styleable.PacmanView_pacmanEyeColor,
                        context.getColor(R.color.black)
                    )
                }
            }
    }

    private var startStrokeAngle = 45f

    private var endStrokeAngle = 270f

    private var pacmanMouthStartAngle = 0f

    private var pacmanMouthSweepAngle = 45f

    private val eye by lazy {
        RectF(
            centerX + radius * 0.3f,
            centerY - radius * 0.8f,
            centerX + radius * 0.1f,
            centerY - radius * 0.5f
        )
    }

    private val oval by lazy {
        RectF(
            centerX - radius,
            centerY - radius,
            centerX + radius,
            centerY + radius
        )
    }

    private val progressAnimator: ValueAnimator
        get() = ValueAnimator.ofFloat(
            pacmanMouthSweepAngle,
            pacmanMouthStartAngle
        ).apply {
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            duration = 2000L
            addUpdateListener {
                startStrokeAngle = (it.animatedValue as Float)
                pacmanMouthStartAngle = it.animatedValue as Float
                invalidate()
            }
        }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(widthMeasureSpec, widthMeasureSpec)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        centerX = width / 2f
        centerY = height / 2f
        radius = width / 2 * MARGIN_COEFF
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawArc(pacmanStroke, startStrokeAngle, endStrokeAngle, true, strokeBackground)
        canvas.drawArc(pacmanStroke, startStrokeAngle * -1, endStrokeAngle * -1, true, strokeBackground)
        canvas.drawArc(oval, START_ANGLE, END_ANGLE, true, backgroundPacmanPaint)
        canvas.drawArc(
            oval,
            pacmanMouthStartAngle,
            pacmanMouthSweepAngle,
            true,
            backgroundPacmanPaint
        )
        canvas.drawArc(
            oval,
            pacmanMouthStartAngle * -1f,
            pacmanMouthSweepAngle * -1f,
            true,
            backgroundPacmanPaint
        )
        canvas.drawOval(eye, pacmanEyeColor)
    }

    fun startAnimation() {
        progressAnimator.start()
    }
}