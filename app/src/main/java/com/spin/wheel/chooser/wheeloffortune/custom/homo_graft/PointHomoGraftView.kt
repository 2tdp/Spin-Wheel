package com.spin.wheel.chooser.wheeloffortune.custom.homo_graft

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.ColorFilter
import android.view.MotionEvent
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.SimpleColorFilter
import com.airbnb.lottie.model.KeyPath
import com.airbnb.lottie.value.LottieValueCallback
import com.spin.wheel.chooser.wheeloffortune.R
import com.spin.wheel.chooser.wheeloffortune.extensions.randomColor

@SuppressLint("ClickableViewAccessibility")
class PointHomoGraftView(context: Context) : LottieAnimationView(context) {

    private var mId = -1
    private var dX: Float = 0f
    private var dY: Float = 0f
    private var canMoving = true
    var Ox = 0f
    var Oy = 0f

    init {
        this.setAnimation(R.raw.spread)

        this.loop(true)

        val filter = SimpleColorFilter(Color.parseColor(randomColor()))
        val keyPath = KeyPath("**")
        val callback = LottieValueCallback<ColorFilter>(filter)
        this.addValueCallback(keyPath, LottieProperty.COLOR_FILTER, callback)

        this.playAnimation()

        this.setOnTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    dX = view.x - event.rawX
                    dY = view.y - event.rawY
                    true
                }

                MotionEvent.ACTION_MOVE -> {
                    if (canMoving) {
//                        view.animate()
//                            .x(event.rawX + dX)
//                            .y(event.rawY + dY)
//                            .setDuration(0)
//                            .start()
                    }
                    true
                }
                else -> true
            }
        }
    }

    fun stopEffect() {
        this.pauseAnimation()
    }

    fun setIdPoint(id: Int) {
        mId = id
    }

    fun getIdPoint() = mId

    fun setColor(color: String) {
        val filter = SimpleColorFilter(Color.parseColor(color))
        val keyPath = KeyPath("**")
        val callback = LottieValueCallback<ColorFilter>(filter)
        this.addValueCallback(keyPath, LottieProperty.COLOR_FILTER, callback)
        canMoving = false
    }

}