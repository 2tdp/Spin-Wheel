package com.spin.wheel.chooser.wheeloffortune.custom

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import com.spin.wheel.chooser.wheeloffortune.R
import com.spin.wheel.chooser.wheeloffortune.base.BaseCustomView
import com.spin.wheel.chooser.wheeloffortune.databinding.ViewHomeButtonBinding
import com.spin.wheel.chooser.wheeloffortune.extensions.gone
import com.spin.wheel.chooser.wheeloffortune.extensions.onClickEffect
import com.spin.wheel.chooser.wheeloffortune.extensions.visible

class HomeButton(context: Context?, attrs: AttributeSet?) : BaseCustomView(context, attrs) {

    private lateinit var binding: ViewHomeButtonBinding
    var onClick: () -> Unit = {}
    override fun getStyleableRes(): IntArray? = R.styleable.HomeButton

    override fun initView() {
        binding = ViewHomeButtonBinding.inflate(LayoutInflater.from(context), this, true)
    }

    override fun initListener() {
        binding.clItem.onClickEffect(scale = 0.95f) { onClick.invoke() }
    }

    override fun initData() {

    }

    override fun initStyleable(arr: TypedArray?) {
        if (arr != null) {
            try {
                if (arr.hasValue(R.styleable.HomeButton_hb_icon)) {
                    val icon = arr.getDrawable(R.styleable.HomeButton_hb_icon)
                    binding.imv.setImageDrawable(icon)
                    binding.imv2.setImageDrawable(icon)
                }

                if (arr.hasValue(R.styleable.HomeButton_hb_name)) {
                    val name = arr.getString(R.styleable.HomeButton_hb_name)
                    binding.tvName.text = name
                    binding.tvName2.text = name
                }

                if (arr.hasValue(R.styleable.HomeButton_hb_icon_left)) {
                    val left = arr.getBoolean(R.styleable.HomeButton_hb_icon_left, true)
                    if (left) {
                        binding.cl2.gone()
                        binding.cl1.visible()
                    } else {
                        binding.cl1.gone()
                        binding.cl2.visible()
                    }
                }
            } finally {
                arr.recycle()
            }
        }
    }
}