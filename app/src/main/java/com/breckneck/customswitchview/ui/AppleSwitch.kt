package com.breckneck.customswitchview.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.graphics.toRectF
import com.breckneck.customswitchview.R
import com.breckneck.customswitchview.extention.dpToPx
import kotlin.concurrent.thread

class AppleSwitch @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

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
    private var isSwitchEnabled = true

    private val switchBackgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val switchCursorPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val leftTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val rightTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val bodyRect = Rect()
    private val leftRect = Rect()
    private val rightRect = Rect()
    private val switchRect = Rect()
    private var x = 1

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

        }

        setOnClickListener {
            Log.e("TAG", "CLICKED")
            handleClick()
//            invalidate()
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
//        super.onDraw(canvas)
        drawBody(canvas = canvas!!)
        drawText(canvas = canvas)
        invalidate()
    }

    override fun onSaveInstanceState(): Parcelable {
        Log.e("TAG", "onSaveInstanceState $id")
        val savedState = SavedState(super.onSaveInstanceState())
        savedState.isSwitchEnabled = isSwitchEnabled
        return savedState
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        Log.e("TAG", "onRestoreInstanceState $id")
        if (state is SavedState) {
            super.onRestoreInstanceState(state)
            isSwitchEnabled = state.isSwitchEnabled
        } else {
            super.onRestoreInstanceState(state)
        }
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
        if (isSwitchEnabled) {
            canvas.drawRoundRect(leftRect.toRectF(), 50F, 50F, switchCursorPaint)
        } else {
            canvas.drawRoundRect(rightRect.toRectF(), 50F, 50F, switchCursorPaint)
        }
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
        isSwitchEnabled = !isSwitchEnabled
        Log.e("TAG", isSwitchEnabled.toString())
    }

    private class SavedState : Parcelable, BaseSavedState {
        var isSwitchEnabled: Boolean = true

        constructor(superState: Parcelable?) : super(superState)

        constructor(parcel: Parcel) : super(parcel) {
            //restore state form parcel
            isSwitchEnabled = parcel.readInt() == 1
        }

        override fun describeContents(): Int {
            TODO("Not yet implemented")
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            //write state to parcel
            super.writeToParcel(out, flags)
            out.writeInt(if (isSwitchEnabled) 1 else 0)
        }

        companion object CREATOR : Parcelable.Creator<SavedState> {
            override fun createFromParcel(parcel: Parcel): SavedState {
                return SavedState(parcel)
            }

            override fun newArray(size: Int): Array<SavedState?> {
                return arrayOfNulls(size)
            }
        }
    }
}