package com.spin.wheel.chooser.wheeloffortune.ui

import android.os.Bundle
import com.spin.wheel.chooser.wheeloffortune.R
import com.spin.wheel.chooser.wheeloffortune.base.BaseActivity
import com.spin.wheel.chooser.wheeloffortune.custom.dialog.RatingDialog
import com.spin.wheel.chooser.wheeloffortune.databinding.ActivitySettingBinding
import com.spin.wheel.chooser.wheeloffortune.extensions.gone
import com.spin.wheel.chooser.wheeloffortune.extensions.onClickEffect
import com.spin.wheel.chooser.wheeloffortune.extensions.showActivity
import com.spin.wheel.chooser.wheeloffortune.utils.AppUtil
import com.spin.wheel.chooser.wheeloffortune.utils.IntentKey

class SettingActivity : BaseActivity<ActivitySettingBinding>(R.layout.activity_setting) {

    override fun isHideNavigation(): Boolean = true

    override fun nameActivity(): String = this::class.java.simpleName

    override fun initView() {
        if (sharePref.isRated) binding.tvRate.gone()
    }

    override fun initData() {

    }

    override fun initListener() {
        binding.header.onBack = { onBackPressed() }

        binding.tvLanguage.onClickEffect(scale = 0.97f) {
            showActivity(LanguageActivity::class.java, Bundle().apply {
                putBoolean(IntentKey.KEY_OPEN_LANGUAGE_FROM_SETTING, true)
            })
        }

        binding.tvShare.onClickEffect(scale = 0.97f) {
            binding.tvShare.isEnabled = false
            AppUtil.shareApp(this)
        }

        binding.tvRate.onClickEffect(scale = 0.97f) {
            val ratingDialog = RatingDialog(this)
            ratingDialog.onRating = { rate ->
                try {
                    if (rate == 5) AppUtil.rateApp(this)

                    sharePref.isRated = true
                    binding.tvRate.gone()
                } catch (_: Exception) {
                }
            }
            ratingDialog.showDialog()
        }

        binding.tvPolicy.onClickEffect(scale = 0.97f) { AppUtil.openPolicy(this) }
    }

    override fun onResume() {
        super.onResume()
        binding.tvShare.isEnabled = true
    }
}