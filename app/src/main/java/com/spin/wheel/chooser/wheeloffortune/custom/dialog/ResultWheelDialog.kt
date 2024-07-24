package com.spin.wheel.chooser.wheeloffortune.custom.dialog

import android.content.Context
import android.view.View
import com.spin.wheel.chooser.wheeloffortune.base.BaseFullScreenDialog
import com.spin.wheel.chooser.wheeloffortune.databinding.DialogResultWheelBinding
import com.spin.wheel.chooser.wheeloffortune.extensions.onAvoidDoubleClick

class ResultWheelDialog(context: Context) : BaseFullScreenDialog<DialogResultWheelBinding>(DialogResultWheelBinding::inflate,
    context, isCancel = true
) {
    override fun initView() {

    }

    override fun initData() {

    }

    override fun initListener() {
        binding.imvClose.onAvoidDoubleClick { dismiss() }
    }

    fun showDialog(name: String) {
        if (isShowing) return
        binding.tvName.text = name
        showDialog()
    }

    override val layoutContainer: View
        get() = binding.llDialog
}