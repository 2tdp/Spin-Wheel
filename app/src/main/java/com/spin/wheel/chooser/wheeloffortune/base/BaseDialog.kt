package com.spin.wheel.chooser.wheeloffortune.base

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import androidx.viewbinding.ViewBinding

abstract class BaseDialog<B : ViewBinding>(
    val bindingFactory: (LayoutInflater) -> B,
    context: Context,
    private val isCancel: Boolean = false
) : Dialog(context) {

    protected val binding: B by lazy { bindingFactory(layoutInflater) }
    private val screenHeight = context.resources.displayMetrics.heightPixels

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
//        layoutContainer.setPadding(16.toDp, 0, 16.toDp, 0)
//        window?.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.bg_transparent))
        setCancelable(isCancel)
        initView()
        initData()
        initListener()
        showSmooth()
    }

    override fun onBackPressed() {
        if (!isCancel) dismiss()
    }

    protected fun showSmooth() {
//        layoutContainer.translationY = screenHeight.toFloat()
//        layoutContainer.animate()
//            .translationY(0f)
//            .setDuration(300)
//            .start()
    }

    protected fun hideDialog() {
//        layoutContainer.animate()
//            .translationY(screenHeight.toFloat())
//            .setDuration(300)
//            .start()
//        Handler().postDelayed({
//            dismiss()
//        }, 300)
    }

    protected abstract fun initView()
    protected abstract fun initData()
    protected abstract fun initListener()
}