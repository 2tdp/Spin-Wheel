package com.spin.wheel.chooser.wheeloffortune.custom.rank

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.ColorFilter
import android.view.LayoutInflater
import android.view.MotionEvent
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.SimpleColorFilter
import com.airbnb.lottie.model.KeyPath
import com.airbnb.lottie.value.LottieValueCallback
import com.spin.wheel.chooser.wheeloffortune.base.BaseCustomView
import com.spin.wheel.chooser.wheeloffortune.databinding.ViewPointRankBinding
import com.spin.wheel.chooser.wheeloffortune.extensions.invisible
import com.spin.wheel.chooser.wheeloffortune.extensions.randomColor
import com.spin.wheel.chooser.wheeloffortune.extensions.visible

class PointRankView(context: Context) : BaseCustomView(context) {

    private lateinit var binding: ViewPointRankBinding
    private var mId = -1
    private var dX: Float = 0f
    private var dY: Float = 0f
    var onMove: (Float, Float) -> Unit = { _, _ -> }
    override fun getStyleableRes(): IntArray? {
        return null
    }


    override fun initView() {
        binding = ViewPointRankBinding.inflate(LayoutInflater.from(context), this, true)
        val filter = SimpleColorFilter(Color.parseColor(randomColor()))
        val keyPath = KeyPath("**")
        val callback = LottieValueCallback<ColorFilter>(filter)
        binding.effect.addValueCallback(keyPath, LottieProperty.COLOR_FILTER, callback)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun initListener() {
        binding.clItem.setOnTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    dX = view.x - event.rawX
                    dY = view.y - event.rawY
                    true
                }

                MotionEvent.ACTION_MOVE -> {
                    onMove.invoke(dX, dY)
//                    view.animate()
//                        .x(event.rawX + dX)
//                        .y(event.rawY + dY)
//                        .setDuration(0)
//                        .start()
                    true
                }

                else -> true
            }
        }
    }

    override fun initData() {

    }

    override fun initStyleable(mTypedArray: TypedArray?) {

    }

    fun showResult(rank: Int) {
        binding.effect.pauseAnimation()
        binding.effect.invisible()
        binding.tvRank.visible()
        binding.tvRank.text = rank.toString()
    }

    fun setIdPoint(id: Int) {
        mId = id
    }

    fun getIdPoint() = mId

}