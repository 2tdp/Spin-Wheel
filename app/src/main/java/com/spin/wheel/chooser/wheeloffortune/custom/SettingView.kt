package com.spin.wheel.chooser.wheeloffortune.custom

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import com.spin.wheel.chooser.wheeloffortune.R
import com.spin.wheel.chooser.wheeloffortune.base.BaseCustomView
import com.spin.wheel.chooser.wheeloffortune.databinding.LayoutSettingViewBinding
import com.spin.wheel.chooser.wheeloffortune.extensions.onAvoidDoubleClick

class SettingView(context: Context?, attrs: AttributeSet?) : BaseCustomView(context, attrs) {

    private lateinit var binding: LayoutSettingViewBinding
    var onClick: () -> Unit = {}
    override fun getStyleableRes(): IntArray? {
        return R.styleable.SettingView
    }

    override fun initView() {
        binding = LayoutSettingViewBinding.inflate(LayoutInflater.from(context), this, true)
    }

    override fun initListener() {
        binding.llItem.onAvoidDoubleClick { onClick() }
    }

    override fun initData() {
    }

    override fun initStyleable(mTypedArray: TypedArray?) {
        if (mTypedArray != null) {
            try {
                if (mTypedArray.hasValue(R.styleable.SettingView_stv_title)) {
                    binding.tvTitle.text = mTypedArray.getString(R.styleable.SettingView_stv_title)
                }
                if (mTypedArray.hasValue(R.styleable.SettingView_stv_icon)) {
                    binding.imvIcon.setImageDrawable(mTypedArray.getDrawable(R.styleable.SettingView_stv_icon))
                }
            } finally {
                mTypedArray.recycle()
            }
        }
    }
}