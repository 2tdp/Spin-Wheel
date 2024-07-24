package com.spin.wheel.chooser.wheeloffortune.ui

import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import com.nlbn.ads.callback.AdCallback
import com.nlbn.ads.util.Admob
import com.nlbn.ads.util.ConsentHelper
import com.spin.wheel.chooser.wheeloffortune.R
import com.spin.wheel.chooser.wheeloffortune.base.BaseActivity
import com.spin.wheel.chooser.wheeloffortune.databinding.ActivityInstructionBinding
import com.spin.wheel.chooser.wheeloffortune.extensions.gone
import com.spin.wheel.chooser.wheeloffortune.extensions.onClickEffect
import com.spin.wheel.chooser.wheeloffortune.extensions.setGradient
import com.spin.wheel.chooser.wheeloffortune.extensions.visible
import com.spin.wheel.chooser.wheeloffortune.utils.AdsConfig
import com.spin.wheel.chooser.wheeloffortune.utils.AdsConfig.Companion.interGuide
import com.spin.wheel.chooser.wheeloffortune.utils.AdsConfig.Companion.loadInterGuide
import com.spin.wheel.chooser.wheeloffortune.utils.AdsConfig.Companion.loadNativeHome
import com.spin.wheel.chooser.wheeloffortune.utils.IntentKey

class InstructionActivity : BaseActivity<ActivityInstructionBinding>(R.layout.activity_instruction) {

    override fun isHideNavigation(): Boolean = true

    override fun nameActivity(): String = this::class.java.simpleName

    private var isShowInter = true

    override fun initView() {
        binding.tvSkip.setGradient(intArrayOf(
            ContextCompat.getColor(this@InstructionActivity, R.color.color_F0B90B),
            ContextCompat.getColor(this@InstructionActivity, R.color.color_FCD535)
        ))
    }

    override fun initData() {
        if (AdsConfig.adsNativeHome == null) loadNativeHome(this@InstructionActivity)
        if (interGuide == null) loadInterGuide(this@InstructionActivity)

        onBackPressedDispatcher.addCallback(this@InstructionActivity, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (isShowInter) showInterAds() else finish()
            }
        })

        when (intent.getIntExtra(IntentKey.KEY_TYPE_INSTRUCTION, TYPE_GUIDE)) {
            TYPE_FIRST -> {
                binding.header.setVisibleBack(false)
                binding.btnGotIt.gone()
                binding.btnGotItBot.visible()

                if (sharePref.isInstructed) {
                    binding.tvSkip.visible()
                    binding.btnGotIt.visible()
                    binding.btnGotItBot.gone()
                } else sharePref.isInstructed = true
            }
            TYPE_GUIDE -> {
                binding.header.setVisibleBack(true)
                binding.btnGotIt.visible()
                binding.btnGotItBot.gone()
            }
        }
    }

    override fun initListener() {
        binding.btnGotIt.onClickEffect {
            isShowInter = true
            onBackPressedDispatcher.onBackPressed()
        }
        binding.btnGotItBot.onClickEffect {
            isShowInter = true
            onBackPressedDispatcher.onBackPressed()
        }
        binding.tvSkip.onClickEffect {
            isShowInter = false
            onBackPressedDispatcher.onBackPressed()
        }

        binding.header.onBack = {
            isShowInter = true
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun showInterAds() {
        if (haveNetworkConnection() && ConsentHelper.getInstance(this).canRequestAds()
            && AdsConfig.checkTimeShowInter() && sharePref.isLoadInterGuide && interGuide != null) {
            Admob.getInstance().showInterAds(this@InstructionActivity, interGuide, object : AdCallback() {
                override fun onNextAction() {
                    super.onNextAction()
                    finish()
                }

                override fun onAdClosedByUser() {
                    super.onAdClosedByUser()
                    AdsConfig.lastTimeShowInter = System.currentTimeMillis()
                    interGuide = null
                    loadInterGuide(this@InstructionActivity)
                }
            })
        } else finish()
    }

    companion object {
        const val TYPE_FIRST = 0
        const val TYPE_SECOND = 1
        const val TYPE_GUIDE = 2
    }
}