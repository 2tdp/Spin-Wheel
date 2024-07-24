package com.spin.wheel.chooser.wheeloffortune.extensions

import android.graphics.LinearGradient
import android.graphics.Shader
import android.graphics.Typeface
import android.text.TextUtils
import android.util.TypedValue
import android.view.Gravity
import android.widget.TextView

fun TextView.textCustom(content: String, color: Int, textSize: Float, font: Typeface) {
    text = content
    setTextColor(color)
    setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
    setTypeface(font, Typeface.BOLD)
}

fun TextView.setGradient(colors: IntArray) {
    paint.shader = LinearGradient(
        0f,
        0f,
        paint.measureText(text.toString()),
        textSize,
        colors, null, Shader.TileMode.CLAMP)
    invalidate()
}

fun TextView.setScrollText() {
    try {
        maxLines = 1
        ellipsize = TextUtils.TruncateAt.MARQUEE
        isFocusable = true
        isFocusableInTouchMode = true
        isSingleLine = true
        marqueeRepeatLimit = -1
        isHorizontalScrollBarEnabled = true
        isSelected = true
        requestFocus()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun TextView.setThreeDot(gravity: Gravity) {
    try {
        maxLines = 1
        ellipsize = TextUtils.TruncateAt.END
        isSingleLine = true
    } catch (e: Exception) {
        e.printStackTrace()
    }
}