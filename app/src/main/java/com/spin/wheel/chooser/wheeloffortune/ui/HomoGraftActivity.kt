package com.spin.wheel.chooser.wheeloffortune.ui

import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import com.nlbn.ads.banner.BannerPlugin
import com.nlbn.ads.callback.AdCallback
import com.nlbn.ads.util.Admob
import com.nlbn.ads.util.ConsentHelper
import com.spin.wheel.chooser.wheeloffortune.R
import com.spin.wheel.chooser.wheeloffortune.base.BaseActivity
import com.spin.wheel.chooser.wheeloffortune.custom.popup.PopupHomoGraft
import com.spin.wheel.chooser.wheeloffortune.databinding.ActivityHomoGraftBinding
import com.spin.wheel.chooser.wheeloffortune.extensions.gone
import com.spin.wheel.chooser.wheeloffortune.extensions.onClickEffect
import com.spin.wheel.chooser.wheeloffortune.extensions.toast
import com.spin.wheel.chooser.wheeloffortune.extensions.visible
import com.spin.wheel.chooser.wheeloffortune.utils.AdsConfig
import com.spin.wheel.chooser.wheeloffortune.utils.Constants.TAG
import com.spin.wheel.chooser.wheeloffortune.utils.DeviceUtil

class HomoGraftActivity : BaseActivity<ActivityHomoGraftBinding>(R.layout.activity_homo_graft) {

    override fun isHideNavigation(): Boolean = true

    override fun nameActivity(): String = this::class.java.simpleName

    private var popup: PopupHomoGraft? = null
    private var timer: CountDownTimer? = null
    var count = 3

    override fun initView() {
        if (AdsConfig.interBack == null) AdsConfig.loadInterBack(this@HomoGraftActivity)
        loadBannerCollapse()

        onBackPressedDispatcher.addCallback(this@HomoGraftActivity, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showInterAds()
            }
        })

        popup = PopupHomoGraft(this, binding.ivCount, {
            binding.homoView.countInGroup = it
            setCurCount()
        }, { setCurCount() })
        popup?.setOnDismissListener { setCurCount() }

        timer = object : CountDownTimer(3000, 1000) {

            override fun onTick(p0: Long) {
                binding.tvCountdown.text = (count--).toString()
            }

            override fun onFinish() {
                binding.tvCountdown.text = ""
                DeviceUtil.playVoiceSuccess(this@HomoGraftActivity)
                binding.homoView.findGroup()
            }
        }
    }

    private fun setCurCount() {
        when(binding.homoView.countInGroup) {
            2 -> binding.ivCount.setImageResource(R.drawable.img_number_two)
            3 -> binding.ivCount.setImageResource(R.drawable.img_number_three)
            4 -> binding.ivCount.setImageResource(R.drawable.img_number_four)
            5 -> binding.ivCount.setImageResource(R.drawable.img_number_five)
        }
    }

    override fun initData() {

    }

    override fun initListener() {
        binding.header.onBack = { onBackPressedDispatcher.onBackPressed() }

        binding.ivCount.onClickEffect {
            if (!binding.homoView.canChangeWinner) {
                toast(getString(R.string.message_can_change_winner))
                return@onClickEffect
            }
            binding.ivCount.setImageResource(R.drawable.img_drop)
            popup?.show()
        }

        binding.homoView.onFind = {
            Handler(Looper.getMainLooper()).postDelayed({
                binding.homoView.setEnableView(false)
                binding.tvCountdown.visible()
                timer?.start()
            }, 3000)
        }

        binding.homoView.onReset = {
            count = 3
            binding.tvCountdown.gone()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
        count = 3
        binding.tvCountdown.gone()
    }

    private fun showInterAds() {
        if (haveNetworkConnection() && ConsentHelper.getInstance(this).canRequestAds()
            && AdsConfig.checkTimeShowInter() && sharePref.isLoadInterBack && AdsConfig.interBack != null) {
            Admob.getInstance().showInterAds(this@HomoGraftActivity, AdsConfig.interBack, object : AdCallback() {
                override fun onNextAction() {
                    super.onNextAction()
                    finish()
                }

                override fun onAdClosedByUser() {
                    super.onAdClosedByUser()
                    AdsConfig.lastTimeShowInter = System.currentTimeMillis()
                    AdsConfig.interBack = null
                    AdsConfig.loadInterBack(this@HomoGraftActivity)
                }
            })
        } else finish()
    }

    private fun loadBannerCollapse() {
        if (haveNetworkConnection() && ConsentHelper.getInstance(this).canRequestAds()) {

            val cbFetchInterval = sharePref.cbFetchInterval.toInt()
            val config = BannerPlugin.Config()
            config.defaultRefreshRateSec = cbFetchInterval
            config.defaultCBFetchIntervalSec = cbFetchInterval

            binding.rlBanner.visibility = View.VISIBLE
            if (sharePref.isSwitchBannerCollapse) {
                config.defaultAdUnitId = getString(R.string.banner_collapse_home)
                config.defaultBannerType = BannerPlugin.BannerType.CollapsibleBottom
            } else {
                if (!sharePref.isLoadBanner) {
                    binding.rlBanner.visibility = View.GONE
                    return
                }
                config.defaultAdUnitId = getString(R.string.banner)
                config.defaultBannerType = BannerPlugin.BannerType.Adaptive
            }
            Admob.getInstance().loadBannerPlugin(this, findViewById(R.id.rlBanner), findViewById(R.id.include), config)
        } else binding.rlBanner.visibility = View.GONE
    }
}