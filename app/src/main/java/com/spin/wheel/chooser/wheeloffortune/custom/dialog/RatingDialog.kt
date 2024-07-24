package com.spin.wheel.chooser.wheeloffortune.custom.dialog

import android.content.Context
import android.view.View
import com.spin.wheel.chooser.wheeloffortune.R
import com.spin.wheel.chooser.wheeloffortune.base.BaseFullScreenDialog
import com.spin.wheel.chooser.wheeloffortune.databinding.LayoutRatingDialogBinding
import com.spin.wheel.chooser.wheeloffortune.extensions.onAvoidDoubleClick

class RatingDialog(context: Context) : BaseFullScreenDialog<LayoutRatingDialogBinding>(LayoutRatingDialogBinding::inflate, context) {

    private var rating = 5
    var onRating: (Int) -> Unit = {}

    override fun initView() {

    }

    override fun initData() {

    }

    override fun initListener() {
        binding.llDialog.onAvoidDoubleClick { hideDialog() }
        binding.tvClose.onAvoidDoubleClick { hideDialog() }

        binding.imv1.setOnClickListener { rating(RATING_ONE) }
        binding.imv2.setOnClickListener { rating(RATING_TWO) }
        binding.imv3.setOnClickListener { rating(RATING_THREE) }
        binding.imv4.setOnClickListener { rating(RATING_FOUR) }
        binding.imv5.setOnClickListener { rating(RATING_FIVE) }

        binding.btnRate.onAvoidDoubleClick {
            onRating(rating)
            hideDialog()
        }
    }

    override val layoutContainer: View
        get() = binding.llDialog

    private fun rating(position: Int) {
        when (position) {
            RATING_ONE -> {
                binding.imv1.setImageResource(R.drawable.ic_star_yellow)
                binding.imv2.setImageResource(R.drawable.ic_star_white)
                binding.imv3.setImageResource(R.drawable.ic_star_white)
                binding.imv4.setImageResource(R.drawable.ic_star_white)
                binding.imv5.setImageResource(R.drawable.ic_star_recoment)

                binding.imvIcon.setImageResource(R.drawable.r1)
                binding.tvTitle.text = context.getString(R.string.str_title_rate_dialog_123)
                binding.tvMessage.text = context.getString(R.string.str_quote_rate_dialog_123)
            }

            RATING_TWO -> {
                binding.imv1.setImageResource(R.drawable.ic_star_yellow)
                binding.imv2.setImageResource(R.drawable.ic_star_yellow)
                binding.imv3.setImageResource(R.drawable.ic_star_white)
                binding.imv4.setImageResource(R.drawable.ic_star_white)
                binding.imv5.setImageResource(R.drawable.ic_star_recoment)

                binding.imvIcon.setImageResource(R.drawable.r2)
                binding.tvTitle.text = context.getString(R.string.str_title_rate_dialog_123)
                binding.tvMessage.text = context.getString(R.string.str_quote_rate_dialog_123)
            }

            RATING_THREE -> {
                binding.imv1.setImageResource(R.drawable.ic_star_yellow)
                binding.imv2.setImageResource(R.drawable.ic_star_yellow)
                binding.imv3.setImageResource(R.drawable.ic_star_yellow)
                binding.imv4.setImageResource(R.drawable.ic_star_white)
                binding.imv5.setImageResource(R.drawable.ic_star_recoment)

                binding.imvIcon.setImageResource(R.drawable.r3)
                binding.tvTitle.text = context.getString(R.string.str_title_rate_dialog_123)
                binding.tvMessage.text = context.getString(R.string.str_quote_rate_dialog_123)
            }

            RATING_FOUR -> {
                binding.imv1.setImageResource(R.drawable.ic_star_yellow)
                binding.imv2.setImageResource(R.drawable.ic_star_yellow)
                binding.imv3.setImageResource(R.drawable.ic_star_yellow)
                binding.imv4.setImageResource(R.drawable.ic_star_yellow)
                binding.imv5.setImageResource(R.drawable.ic_star_recoment)

                binding.imvIcon.setImageResource(R.drawable.r4)
                binding.tvTitle.text = context.getString(R.string.str_title_rate_dialog_45)
                binding.tvMessage.text = context.getString(R.string.str_quote_rate_dialog_45)
            }

            RATING_FIVE -> {
                binding.imv1.setImageResource(R.drawable.ic_star_yellow)
                binding.imv2.setImageResource(R.drawable.ic_star_yellow)
                binding.imv3.setImageResource(R.drawable.ic_star_yellow)
                binding.imv4.setImageResource(R.drawable.ic_star_yellow)
                binding.imv5.setImageResource(R.drawable.ic_star_yellow)

                binding.imvIcon.setImageResource(R.drawable.r5)
                binding.tvTitle.text = context.getString(R.string.str_title_rate_dialog_45)
                binding.tvMessage.text = context.getString(R.string.str_quote_rate_dialog_45)
            }
        }
        rating = position
    }

    companion object {
        const val RATING_ONE = 1
        const val RATING_TWO = 2
        const val RATING_THREE = 3
        const val RATING_FOUR = 4
        const val RATING_FIVE = 5
    }
}