package com.spin.wheel.chooser.wheeloffortune.extensions

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

fun ImageView.loadGif(url: Any) {
    Glide.with(context)
        .asGif()
        .load(url)
        .diskCacheStrategy(DiskCacheStrategy.NONE)
        .skipMemoryCache(true)
        .into(this)
}

fun ImageView.loadImage(url: Any) {
    Glide.with(context)
        .asBitmap()
        .load(url)
        .into(this)
}

fun ImageView.loadIcon(url: Any) {
    Glide.with(context)
        .asBitmap()
        .load(url)
        .into(this)
}

fun ImageView.loadIconFromVideo(url: Any) {
    Glide.with(context)
        .asBitmap()
        .load(url)
        .addListener(object : RequestListener<Bitmap> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Bitmap>,
                isFirstResource: Boolean
            ): Boolean {
                return false
            }

            override fun onResourceReady(
                resource: Bitmap,
                model: Any,
                target: Target<Bitmap>?,
                dataSource: DataSource,
                isFirstResource: Boolean
            ): Boolean {
                resource.let { bm ->
                    val resizeBm = Bitmap.createScaledBitmap(
                        bm, 720, 720 * bm.height / bm.width, false
                    )
                    setImageBitmap(resizeBm)
                }
                return true
            }

        })
        .into(this)
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.setVisible(isVisible: Boolean = true) {
    if (isVisible) this.visible()
    else this.gone()
}

fun View.setInvisible(isVisible: Boolean = true) {
    if (isVisible) this.visible()
    else this.invisible()
}

fun View.setMargin(left: Int = 0, top: Int = 0, right: Int = 0, bottom: Int = 0) {
    val param = this.layoutParams as ViewGroup.MarginLayoutParams
    param.leftMargin = left.toDp
    param.rightMargin = right.toDp
    param.topMargin = top.toDp
    param.bottomMargin = bottom.toDp
    this.layoutParams = param
}

fun View.onAvoidDoubleClick(
    throttleDelay: Long = 1000,
    onClick: (View) -> Unit
) {
    setOnClickListener {
        onClick(this)
        isClickable = false
        postDelayed({ isClickable = true }, throttleDelay)
    }
}

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    try {
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(editable: Editable?) {
                afterTextChanged.invoke(editable.toString())
            }
        })
    } catch (_: Exception) {
        afterTextChanged.invoke("")
    }

}

fun View.createBackground(colorArr: IntArray, border: Float, stroke: Int, colorStroke: Int) {
    background = GradientDrawable().apply {
        shape = GradientDrawable.RECTANGLE
        cornerRadius = border
        if (stroke != -1) setStroke(stroke, colorStroke)

        if (colorArr.size >= 2) {
            colors = colorArr
            gradientType = GradientDrawable.LINEAR_GRADIENT
        } else setColor(colorArr[0])
    }
}

fun View.createBackground(colorArr: IntArray, border: FloatArray, stroke: Int, colorStroke: Int) {
    background = GradientDrawable().apply {
        shape = GradientDrawable.RECTANGLE
        cornerRadii = border
        if (stroke != -1) setStroke(stroke, colorStroke)

        if (colorArr.size >= 2) {
            colors = colorArr
            gradientType = GradientDrawable.LINEAR_GRADIENT
        } else setColor(colorArr[0])
    }
}

fun View.loadAnimation(context: Context, anim: Int) {
    val animation = AnimationUtils.loadAnimation(context, anim)
    this.startAnimation(animation)
}

fun View.animationClick(scale: Float = 1.2f) {
    scaleViewSmoothly(scale)
    Handler().postDelayed( { scaleViewSmoothly(1f) }, 100)
}

fun View.scaleViewSmoothly(scale: Float = 1.1f) {
    val scaleXAnimator = ObjectAnimator.ofFloat(this, View.SCALE_X, scale)
    val scaleYAnimator = ObjectAnimator.ofFloat(this, View.SCALE_Y, scale)

    val scaleSet = AnimatorSet().apply {
        playTogether(scaleXAnimator, scaleYAnimator)
        duration = 200 // 200 milliseconds
    }
    scaleSet.start()
}

fun View.visibleSmooth() {
    this.visibility = View.VISIBLE
    this.translationY = this.height.toFloat()
    this.animate().translationY(0f).setDuration(200).start()
}

fun View.goneSmooth() {
    this.animate().translationY(this.height.toFloat()).setDuration(200)
        .withEndAction { this.visibility = View.GONE }.start()
}

@SuppressLint("ClickableViewAccessibility")
fun View.onClickEffect(
    delay: Long = 1000,
    scale: Float = 0.9f,
    onClick: (View) -> Unit
) {
    setOnClickListener {
        isClickable = false
        this.animate().scaleX(scale).scaleY(scale).setDuration(50)
            .withEndAction {
                this.animate().scaleX(1f).scaleY(1f).setDuration(80).withEndAction {
                    onClick(this)
                    postDelayed({ isClickable = true }, delay - 130)
                }.start()
            }.start()
    }
}
