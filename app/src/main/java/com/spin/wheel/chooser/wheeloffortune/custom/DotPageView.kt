package com.spin.wheel.chooser.wheeloffortune.custom

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import com.spin.wheel.chooser.wheeloffortune.R
import com.spin.wheel.chooser.wheeloffortune.base.BaseCustomView
import com.spin.wheel.chooser.wheeloffortune.databinding.ViewDotBinding

class DotPageView(context: Context, attrs: AttributeSet) : BaseCustomView(context, attrs) {

    private lateinit var binding: ViewDotBinding
    var onChangePage: (Int) -> Unit = {}
    override fun getStyleableRes(): IntArray? = null

    override fun initView() {
        binding = ViewDotBinding.inflate(LayoutInflater.from(context), this, true)
    }

    override fun initListener() {
//        binding.dotOne.onAvoidDoubleClick { onChangePage(PAGE_ONE) }
//        binding.dotTwo.onAvoidDoubleClick { onChangePage(PAGE_TWO) }
//        binding.dotThree.onAvoidDoubleClick { onChangePage(PAGE_THREE) }
    }

    override fun initData() {

    }

    fun currentPage(index: Int = 0) {
        binding.dotOne.setImageResource(R.drawable.ic_dot_page)
        binding.dotTwo.setImageResource(R.drawable.ic_dot_page)
        binding.dotThree.setImageResource(R.drawable.ic_dot_page)

        when (index) {
            PAGE_ONE -> binding.dotOne.setImageResource(R.drawable.ic_current_dot_page)
            PAGE_TWO -> binding.dotTwo.setImageResource(R.drawable.ic_current_dot_page)
            PAGE_THREE -> binding.dotThree.setImageResource(R.drawable.ic_current_dot_page)
        }
    }

    override fun initStyleable(mTypedArray: TypedArray?) {

    }

    companion object {
        const val PAGE_ONE = 0
        const val PAGE_TWO = 1
        const val PAGE_THREE = 2
    }
}