package com.spin.wheel.chooser.wheeloffortune.custom

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import com.spin.wheel.chooser.wheeloffortune.R
import com.spin.wheel.chooser.wheeloffortune.base.BaseCustomView
import com.spin.wheel.chooser.wheeloffortune.databinding.ViewHeaderBinding
import com.spin.wheel.chooser.wheeloffortune.extensions.loadImage
import com.spin.wheel.chooser.wheeloffortune.extensions.onClickEffect
import com.spin.wheel.chooser.wheeloffortune.extensions.setInvisible
import com.spin.wheel.chooser.wheeloffortune.share_pref.MySharePref
import com.spin.wheel.chooser.wheeloffortune.share_pref.MySharePrefImpl
import com.spin.wheel.chooser.wheeloffortune.utils.AppUtil

class HeaderView(context: Context, attrs: AttributeSet) : BaseCustomView(context, attrs) {

    private lateinit var binding: ViewHeaderBinding
    private var sharePref: MySharePref?= null
    var onBack: () -> Unit = {}
    var onDone: () -> Unit = {}

    override fun getStyleableRes(): IntArray = R.styleable.HeaderView

    override fun initView() {
        binding = ViewHeaderBinding.inflate(LayoutInflater.from(context), this, true)
        sharePref = MySharePrefImpl(context)
    }

    override fun initListener() {
        binding.imvBack.onClickEffect { onBack.invoke() }
        binding.imvDone.onClickEffect { onDone.invoke() }
    }

    override fun initData() {

    }

    override fun initStyleable(arr: TypedArray?) {
        if (arr != null) {
            try {
                if (arr.hasValue(R.styleable.HeaderView_hv_show_back)) {
                    binding.imvBack.setInvisible(
                        arr.getBoolean(
                            R.styleable.HeaderView_hv_show_back,
                            true
                        )
                    )
                }
                if (arr.hasValue(R.styleable.HeaderView_hv_show_done)) {
                    binding.imvDone.setInvisible(
                        arr.getBoolean(
                            R.styleable.HeaderView_hv_show_done,
                            true
                        )
                    )
                }
                if (arr.hasValue(R.styleable.HeaderView_hv_screen)) {
                    val locale = sharePref?.currentLanguage?.locale?.language ?: "en"
                    when (arr.getInt(R.styleable.HeaderView_hv_screen, 1)) {
                        LANGUAGE -> binding.imvTitle.loadImage(AppUtil.imageTitle("language", locale))
                        SETTING -> binding.imvTitle.loadImage(AppUtil.imageTitle("setting", locale))
                        CHOOSER -> binding.imvTitle.loadImage(AppUtil.imageTitle("chooser", locale))
                        HOMO_GRAFT -> binding.imvTitle.loadImage(AppUtil.imageTitle("homo_graft", locale))
                        RANKING -> binding.imvTitle.loadImage(AppUtil.imageTitle("rank", locale))
                        ROULETTE -> binding.imvTitle.loadImage(AppUtil.imageTitle("roulette", locale))
                        INSTRUCTION -> binding.imvTitle.loadImage(AppUtil.imageTitle("instruction", locale))
                    }
                }
            } finally {
                arr.recycle()
            }
        }
    }

    fun setLocale(screenName: String, locale: String = "en") {
        binding.imvTitle.loadImage(AppUtil.imageTitle(screenName, locale))
    }

    fun setVisibleBack(isVisible: Boolean) = binding.imvBack.setInvisible(isVisible)

    fun setVisibleDone(isVisible: Boolean) = binding.imvDone.setInvisible(isVisible)

    companion object {
        const val LANGUAGE = 1
        const val SETTING = 2
        const val CHOOSER = 3
        const val HOMO_GRAFT = 4
        const val RANKING = 5
        const val ROULETTE = 6
        const val INSTRUCTION = 7
    }
}