package com.spin.wheel.chooser.wheeloffortune.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.spin.wheel.chooser.wheeloffortune.R

class TextOutline constructor(context: Context, attrs: AttributeSet) : AppCompatTextView(context, attrs) {

    // data
    private var mOutlineSize = 0f
    private var mOutlineColor = 0
    private var mTextColor = 0
    private var mShadowRadius = 0f
    private var mShadowDx = 0f
    private var mShadowDy = 0f
    private var mShadowColor = 0

    init {
        // set defaults
        mOutlineSize = DEFAULT_OUTLINE_SIZE
        mOutlineColor = DEFAULT_OUTLINE_COLOR
        // text color
        mTextColor = currentTextColor
        val a = context.obtainStyledAttributes(attrs, R.styleable.TextViewOutline)
        // outline size
        if (a.hasValue(R.styleable.TextViewOutline_outlineSize))
            mOutlineSize = a.getDimension(R.styleable.TextViewOutline_outlineSize, DEFAULT_OUTLINE_SIZE)

        // outline color
        if (a.hasValue(R.styleable.TextViewOutline_outlineColor))
            mOutlineColor = a.getColor(R.styleable.TextViewOutline_outlineColor, DEFAULT_OUTLINE_COLOR)

        // shadow (the reason we take shadow from attributes is because we use API level 15 and only from 16 we have the get methods for the shadow attributes)
        if (a.hasValue(R.styleable.TextViewOutline_android_shadowRadius)
            || a.hasValue(R.styleable.TextViewOutline_android_shadowDx)
            || a.hasValue(R.styleable.TextViewOutline_android_shadowDy)
            || a.hasValue(R.styleable.TextViewOutline_android_shadowColor)
        ) {
            mShadowRadius = a.getFloat(R.styleable.TextViewOutline_android_shadowRadius, 0f)
            mShadowDx = a.getFloat(R.styleable.TextViewOutline_android_shadowDx, 0f)
            mShadowDy = a.getFloat(R.styleable.TextViewOutline_android_shadowDy, 0f)
            mShadowColor = a.getColor(R.styleable.TextViewOutline_android_shadowColor, Color.TRANSPARENT)
        }
        a.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setPaintToOutline()
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    private fun setPaintToOutline() {
        val paint: Paint = paint
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = mOutlineSize
        super.setTextColor(mOutlineColor)
        super.setShadowLayer(0f, 0f, 0f, Color.TRANSPARENT)
    }

    private fun setPaintToRegular() {
        val paint: Paint = paint
        paint.style = Paint.Style.FILL
        paint.strokeWidth = 0f
        super.setTextColor(mTextColor)
        super.setShadowLayer(mShadowRadius, mShadowDx, mShadowDy, mShadowColor)
    }

    override fun setTextColor(color: Int) {
        super.setTextColor(color)
        mTextColor = color
    }

    fun setOutlineSize(size: Float) {
        mOutlineSize = size
    }

    fun setOutlineColor(color: Int) {
        mOutlineColor = color
    }

    override fun onDraw(canvas: Canvas) {
        setPaintToOutline()
        super.onDraw(canvas)
        setPaintToRegular()
        super.onDraw(canvas)
    }

    fun setTextGradientColor() {
        val width = this.paint.measureText(this.text.toString())
        val textShader: Shader = LinearGradient(
            0f, 0f, width, this.textSize,
            intArrayOf(Color.parseColor("#FFC700"), Color.parseColor("#FFED73")), floatArrayOf(1f, 0f),
            Shader.TileMode.REPEAT
        )
        this.paint.shader = textShader
    }

    companion object {
        // constants
        private const val DEFAULT_OUTLINE_SIZE = 0f
        private const val DEFAULT_OUTLINE_COLOR = Color.TRANSPARENT
    }
}