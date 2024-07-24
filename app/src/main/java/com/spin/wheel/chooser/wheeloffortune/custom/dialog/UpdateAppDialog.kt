package com.spin.wheel.chooser.wheeloffortune.custom.dialog

import android.content.Context
import android.view.View
import com.spin.wheel.chooser.wheeloffortune.base.BaseFullScreenDialog
import com.spin.wheel.chooser.wheeloffortune.databinding.LayoutDialogUpdateAppBinding
import com.spin.wheel.chooser.wheeloffortune.extensions.onAvoidDoubleClick

class UpdateAppDialog(context: Context) : BaseFullScreenDialog<LayoutDialogUpdateAppBinding>(LayoutDialogUpdateAppBinding::inflate,
    context, isCancel = true
) {

    var onClick: () -> Unit = {}
    override fun initView() {

    }

    override fun initData() {

    }

    override fun initListener() {
        binding.btnUpdate.onAvoidDoubleClick {
            onClick()
            hideDialog()
        }
    }

    override val layoutContainer: View
        get() = binding.llDialog
}