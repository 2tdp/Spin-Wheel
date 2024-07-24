package com.spin.wheel.chooser.wheeloffortune.base

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.viewbinding.ViewBinding
import com.spin.wheel.chooser.wheeloffortune.extensions.onAvoidDoubleClick
import com.spin.wheel.chooser.wheeloffortune.extensions.toDp

abstract class BaseFullScreenDialog<B : ViewBinding>(
    val bindingFactory: (LayoutInflater) -> B,
    context: Context,
    private val isCancel: Boolean = false
) : Dialog(context) {

    protected val binding: B by lazy { bindingFactory(layoutInflater) }
    private val screenHeight = context.resources.displayMetrics.heightPixels
    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContentView(binding.root)
        window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
        layoutContainer.setPadding(16.toDp, 0, 16.toDp, 0)
        setCancelable(isCancel)
        initView()
        initData()
        initListener()
        showSmooth()
        layoutContainer.onAvoidDoubleClick {
            if (!isCancel) hideDialog()
        }
    }

    override fun onBackPressed() {
        if (!isCancel) hideDialog()
    }

    protected fun showSmooth() {
        layoutContainer.translationY = screenHeight.toFloat()
        layoutContainer.animate()
            .translationY(0f)
            .setDuration(300)
            .start()
    }

    fun showDialog() {
        show()
        window?.decorView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
        window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
    }

    fun hideDialog() {
        layoutContainer.animate()
            .translationY(screenHeight.toFloat())
            .setDuration(300)
            .start()
        Handler(Looper.getMainLooper()).postDelayed({ dismiss() }, 300)
    }

    protected abstract fun initView()
    protected abstract fun initData()
    protected abstract fun initListener()
    protected abstract val layoutContainer: View
}