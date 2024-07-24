package com.spin.wheel.chooser.wheeloffortune.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.get
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.nlbn.ads.banner.BannerPlugin
import com.nlbn.ads.callback.NativeCallback
import com.nlbn.ads.util.Admob
import com.nlbn.ads.util.ConsentHelper
import com.spin.wheel.chooser.wheeloffortune.R
import com.spin.wheel.chooser.wheeloffortune.base.BaseActivity
import com.spin.wheel.chooser.wheeloffortune.databinding.ActivityMainBinding
import com.spin.wheel.chooser.wheeloffortune.databinding.AdsNativeBotHorizontalMediaLeftBinding
import com.spin.wheel.chooser.wheeloffortune.extensions.gone
import com.spin.wheel.chooser.wheeloffortune.extensions.onClickEffect
import com.spin.wheel.chooser.wheeloffortune.extensions.showActivity
import com.spin.wheel.chooser.wheeloffortune.extensions.visible
import com.spin.wheel.chooser.wheeloffortune.utils.AdsConfig.Companion.adsNativeAll
import com.spin.wheel.chooser.wheeloffortune.utils.AdsConfig.Companion.adsNativeHome
import com.spin.wheel.chooser.wheeloffortune.utils.AdsConfig.Companion.loadNativeAll
import com.spin.wheel.chooser.wheeloffortune.utils.IntentKey

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    override fun isHideNavigation(): Boolean = true

    override fun nameActivity(): String = this::class.java.simpleName

    private val backForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        showNativeHome()
        if(adsNativeAll == null) loadNativeAll(this)
    }

    override fun initView() {
        if (sharePref.openGuide)
            showActivity(InstructionActivity::class.java, Bundle().apply {
                putInt(IntentKey.KEY_TYPE_INSTRUCTION, InstructionActivity.TYPE_FIRST)
            })
    }

    override fun initData() {
        val consentHelper = ConsentHelper.getInstance(this)
        if (!consentHelper.canLoadAndShowAds()) consentHelper.reset()

        consentHelper.obtainConsentAndShow(this) {
            loadBannerCollapse()
            showNativeHome()
//            loadInterHome(this)
            if (adsNativeAll == null) loadNativeAll(this)
        }
    }

    override fun initListener() {
        binding.imvSetting.onClickEffect { startIntentForResult(backForResult, SettingActivity::class.java.name, false) }
        binding.btnChooser.onClick = { startIntentForResult(backForResult, ChooseActivity::class.java.name, false) }
        binding.btnHomoGraft.onClick = { startIntentForResult(backForResult, HomoGraftActivity::class.java.name, false) }
        binding.btnRanking.onClick = { startIntentForResult(backForResult, RankActivity::class.java.name, false) }
        binding.btnRoulette.onClick = { startIntentForResult(backForResult, RouletteConfigActivity::class.java.name, false) }
        binding.imvInstruction.onClickEffect {
            startIntentForResult(backForResult, Intent(this@MainActivity, InstructionActivity::class.java).apply {
                putExtra(IntentKey.KEY_TYPE_INSTRUCTION, InstructionActivity.TYPE_GUIDE)
            }, false)
        }
    }

    private fun loadBannerCollapse() {
        if (haveNetworkConnection() && ConsentHelper.getInstance(this@MainActivity).canRequestAds()) {

            val cbFetchInterval = sharePref.cbFetchInterval.toInt()
            val config = BannerPlugin.Config()
            config.defaultRefreshRateSec = cbFetchInterval
            config.defaultCBFetchIntervalSec = cbFetchInterval

            binding.rlBanner.visibility = View.VISIBLE
            if (sharePref.isSwitchBannerCollapse) {
                if (!sharePref.isLoadBannerCollapseHome) {
                    binding.rlBanner.visibility = View.GONE
                    return
                }
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

    private fun showNativeHome() {
        if (haveNetworkConnection() && ConsentHelper.getInstance(this).canRequestAds() && sharePref.isLoadNativeHome) {
            binding.rlNative.visible()

            adsNativeHome?.let {
                pushViewNative(it)
            } ?: run {
                Admob.getInstance().loadNativeAd(this, getString(R.string.native_home), object : NativeCallback() {
                        override fun onNativeAdLoaded(nativeAd: NativeAd) {
                            pushViewNative(nativeAd)
                        }

                        override fun onAdFailedToLoad() {
                            binding.frNativeAds.removeAllViews()
                        }
                    }
                )
            }
        } else binding.rlNative.gone()
    }

    private fun pushViewNative(nativeAd: NativeAd) {
        val adView =
            AdsNativeBotHorizontalMediaLeftBinding.inflate(LayoutInflater.from(this))

        binding.frNativeAds.removeAllViews()
        binding.frNativeAds.addView(adView.root)
        Admob.getInstance().pushAdsToViewCustom(nativeAd, adView.root)
    }
}