package com.breckneck.customswitchview.ui

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.animation.LinearInterpolator
import androidx.annotation.ColorInt
import androidx.appcompat.widget.SwitchCompat
import androidx.core.animation.doOnRepeat
import androidx.core.graphics.toRectF
import com.breckneck.customswitchview.R
import com.breckneck.customswitchview.extention.dpToPx
import kotlin.math.max

class AppleSwitch @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : SwitchCompat(context, attrs, defStyleAttr) {

    companion object {
        private const val DEFAULT_SWITCH_BACKGROUND_COLOR = Color.GRAY
        private const val DEFAULT_SWITCH_CURSOR_COLOR = Color.GREEN
        private const val DEFAULT_SIZE = 140
        private const val DEFAULT_CHECKED_TEXT_COLOR = Color.WHITE
        private const val DEFAULT_UNCHECKED_TEXT_COLOR = Color.WHITE
    }

    @ColorInt
    private var switchBackgroundColor: Int = DEFAULT_SWITCH_BACKGROUND_COLOR
    @ColorInt
    private var switchCursorColor: Int = DEFAULT_SWITCH_CURSOR_COLOR
    @ColorInt
    private var checkedTextColor: Int = DEFAULT_CHECKED_TEXT_COLOR
    @ColorInt
    private var uncheckedTextColor: Int = DEFAULT_UNCHECKED_TEXT_COLOR
    private var leftText: String = "left"
    private var rightText: String = "right"
    private var isEnable = true

    private val switchBackgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val switchCursorPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val leftTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val rightTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val bodyRect = Rect()
    private val leftRect = Rect()
    private val rightRect = Rect()
    private val switchRect = Rect()
    private var size = 0

    init {
        if (attrs != null) {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.AppleSwitch)
            switchBackgroundColor = ta.getColor(R.styleable.AppleSwitch_as_switchBackgroundColor, DEFAULT_SWITCH_BACKGROUND_COLOR)
            switchCursorColor = ta.getColor(R.styleable.AppleSwitch_as_switchCursorColor, DEFAULT_SWITCH_CURSOR_COLOR)
            checkedTextColor = ta.getColor(R.styleable.AppleSwitch_as_checkedTextColor, DEFAULT_CHECKED_TEXT_COLOR)
            uncheckedTextColor = ta.getColor(R.styleable.AppleSwitch_as_uncheckedTextColor, DEFAULT_UNCHECKED_TEXT_COLOR)
            leftText = ta.getString(R.styleable.AppleSwitch_as_leftText) ?: "left"
            rightText = ta.getString(R.styleable.AppleSwitch_as_rightText) ?: "right"
            ta.recycle()
            textOn = "on"
            textOff = "off"
        }

        setOnClickListener {
            Log.e("TAG", "CLICKED")
            handleClick()
            invalidate()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val initSize = resolveDefaultSize(spec = widthMeasureSpec)
        setMeasuredDimension(initSize, initSize / 5)
        Log.e("TAG", "onMeasure after set size: $measuredWidth $measuredHeight")
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w == 0) return
        with(bodyRect) {
            left = 0
            top = 0
            right = w
            bottom = h
        }
        with(leftRect) {
            left = 10
            top = 10
            right = (w / 2) - 10
            bottom = h - 10
        }
        with(rightRect) {
            left = (w / 2) + 10
            top = 10
            right = w - 10
            bottom = h - 10
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        Log.e("TAG", "onDraw")
        drawBody(canvas = canvas!!)
        drawText(canvas = canvas)
        invalidate()
//        setOnClickListener {
//            Log.e("TAG", "CLICKED")
//            handleClick(canvas = canvas)
//            invalidate()
//        }

    }

    private fun drawText(canvas: Canvas?) {
        with(leftTextPaint) {
            color = checkedTextColor
            textAlign = Paint.Align.CENTER
            textSize = height * 0.33F
        }
        with(rightTextPaint) {
            color = uncheckedTextColor
            textAlign = Paint.Align.CENTER
            textSize = height * 0.33F
        }
        val offsetY = (leftTextPaint.descent() + leftTextPaint.ascent()) / 2
        canvas!!.drawText(leftText, leftRect.exactCenterX(), bodyRect.exactCenterY() - offsetY, leftTextPaint)
        canvas!!.drawText(rightText, rightRect.exactCenterX(), bodyRect.exactCenterY() - offsetY, rightTextPaint)
    }

    private fun drawBody(canvas: Canvas?) {
        switchBackgroundPaint.color = switchBackgroundColor
        switchCursorPaint.color = switchCursorColor
        canvas!!.drawRoundRect(bodyRect.toRectF(), 50F, 50F, switchBackgroundPaint)
        if (isEnable) {
            switchCursorPaint.color = switchCursorColor
            canvas.drawRoundRect(leftRect.toRectF(), 50F, 50F, switchCursorPaint)
        } else {
            switchCursorPaint.color = Color.RED
            canvas.drawRoundRect(rightRect.toRectF(), 50F, 50F, switchCursorPaint)
        }
//        switchCursorPaint.color = Color.RED
//        canvas.drawRoundRect(rightRect.toRectF(), 50F, 50F, switchCursorPaint)
    }

    private fun resolveDefaultSize(spec: Int): Int {
        return when (MeasureSpec.getMode(spec)) {
            MeasureSpec.UNSPECIFIED -> context.dpToPx(DEFAULT_SIZE).toInt() //resolveDefaultSize()
            MeasureSpec.AT_MOST -> MeasureSpec.getSize(spec)
            MeasureSpec.EXACTLY -> MeasureSpec.getSize(spec)
            else -> MeasureSpec.getSize(spec)
        }
    }

    private fun handleClick() {
        isEnable = !isEnable
//        if (isEnable) {
//            switchCursorPaint.color = switchCursorColor
//            canvas!!.drawRoundRect(leftRect.toRectF(), 50F, 50F, switchCursorPaint)
//        } else {
//            switchCursorPaint.color = Color.RED
//            canvas!!.drawRoundRect(rightRect.toRectF(), 50F, 50F, switchCursorPaint)
//        }
        Log.e("TAG", isEnable.toString())
    }

    private fun toggleMode() {

    }

}