package com.spin.wheel.chooser.wheeloffortune.ui

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.nlbn.ads.callback.NativeCallback
import com.nlbn.ads.util.Admob
import com.nlbn.ads.util.ConsentHelper
import com.spin.wheel.chooser.wheeloffortune.R
import com.spin.wheel.chooser.wheeloffortune.adapter.recycler_view.LanguageAdapter
import com.spin.wheel.chooser.wheeloffortune.base.BaseActivity
import com.spin.wheel.chooser.wheeloffortune.databinding.ActivityLanguageBinding
import com.spin.wheel.chooser.wheeloffortune.databinding.AdsNativeBotBinding
import com.spin.wheel.chooser.wheeloffortune.databinding.AdsNativeBotHorizontalMediaLeftBinding
import com.spin.wheel.chooser.wheeloffortune.databinding.AdsNativeBotHorizontalMediaRightBinding
import com.spin.wheel.chooser.wheeloffortune.databinding.AdsNativeTopNew1Binding
import com.spin.wheel.chooser.wheeloffortune.databinding.AdsNativeTopNewBinding
import com.spin.wheel.chooser.wheeloffortune.databinding.NativeBotHorizontalMediaLeftLoadingBinding
import com.spin.wheel.chooser.wheeloffortune.databinding.NativeButtonBotLoadingBinding
import com.spin.wheel.chooser.wheeloffortune.databinding.NativeTopLoadingBinding
import com.spin.wheel.chooser.wheeloffortune.entity.LanguageModel
import com.spin.wheel.chooser.wheeloffortune.extensions.createBackground
import com.spin.wheel.chooser.wheeloffortune.extensions.gone
import com.spin.wheel.chooser.wheeloffortune.extensions.showActivity
import com.spin.wheel.chooser.wheeloffortune.extensions.visible
import com.spin.wheel.chooser.wheeloffortune.utils.AdsConfig
import com.spin.wheel.chooser.wheeloffortune.utils.AdsConfig.Companion.adsNativeAll
import com.spin.wheel.chooser.wheeloffortune.utils.AppUtil
import com.spin.wheel.chooser.wheeloffortune.utils.IntentKey

class LanguageActivity : BaseActivity<ActivityLanguageBinding>(R.layout.activity_language) {

    override fun isHideNavigation(): Boolean = true

    override fun nameActivity(): String = this::class.java.simpleName

    private val languageAdapter: LanguageAdapter by lazy { LanguageAdapter() }
    private var isOpenFromSetting = false
    private var languageSelected: LanguageModel? = null

    private var nativeAds: NativeAd? = null

    override fun initView() {
        isOpenFromSetting = intent.getBooleanExtra(IntentKey.KEY_OPEN_LANGUAGE_FROM_SETTING, false)

        if (AdsConfig.isLoadAdsNormal() || isOpenFromSetting) {
            binding.rlNative.visible()
            binding.rlNativeTop.gone()
        } else {
            binding.rlNative.gone()
            binding.rlNativeTop.visible()
        }

        binding.rcvLanguage.apply {
            setHasFixedSize(true)
            adapter = languageAdapter
        }

        if (sharePref.currentLanguage == null) binding.header.setVisibleDone(false)
        else {
            binding.header.setVisibleDone(true)
            languageSelected = sharePref.currentLanguage
        }
    }

    override fun initData() {
        binding.header.setVisibleBack(isOpenFromSetting)
        if (isOpenFromSetting) showNativeLanguageSetting() else showNativeLanguage()

        languageAdapter.setData(LanguageModel.listLanguage(sharePref.currentLanguage))
    }

    override fun initListener() {
        languageAdapter.onClick = { language ->
            binding.header.setVisibleDone(true)
            languageSelected = language
        }

        binding.header.onDone = {
            sharePref.currentLanguage = languageSelected

            if (isOpenFromSetting) {
                sharePref.openGuide = false
                startActivity(Intent(this, MainActivity::class.java))
            } else showActivity(OnboardingActivity::class.java)

            finishAffinity()
        }

        binding.header.onBack = { onBackPressed() }
    }

    private fun showNativeLanguage() {
        if (haveNetworkConnection() && ConsentHelper.getInstance(this).canRequestAds() && sharePref.isLoadNativeLanguage) {
            nativeAds?.let {
                pushViewAds(it)
            } ?: run {
                Admob.getInstance().loadNativeAd(this, getString(R.string.native_language), object : NativeCallback() {
                    override fun onNativeAdLoaded(nativeAd: NativeAd) {
                        nativeAds = nativeAd
                        if (AdsConfig.isLoadAdsNormal() || isOpenFromSetting)
                            pushViewAds(nativeAd)
                        else pushViewAdsNew(nativeAd)
                    }

                    override fun onAdFailedToLoad() {
                        super.onAdFailedToLoad()
                        nativeAds = null
                        binding.frNativeAds.removeAllViews()
                        binding.frNativeAdsTop.removeAllViews()
                    }

                    override fun onAdImpression() {
                        super.onAdImpression()
                        nativeAds = null
                    }
                })
            }

        } else {
            binding.rlNative.gone()
            binding.rlNativeTop.gone()
        }
    }

    private fun pushViewAds(nativeAd: NativeAd) {
        val adView = AdsNativeBotBinding.inflate(LayoutInflater.from(this@LanguageActivity))

        binding.rlNative.visible()
        binding.rlNative.removeAllViews()
        binding.rlNative.addView(adView.root)
        Admob.getInstance().pushAdsToViewCustom(nativeAd, adView.root)
    }

    private fun pushViewAdsNew(nativeAd: NativeAd) {
        val adView = AdsNativeTopNew1Binding.inflate(LayoutInflater.from(this@LanguageActivity))

        binding.rlNativeTop.visible()
        binding.rlNativeTop.removeAllViews()
        binding.rlNativeTop.addView(adView.root)
        Admob.getInstance().pushAdsToViewCustom(nativeAd, adView.root)
    }

    private fun showNativeLanguageSetting() {
        if (haveNetworkConnection() && sharePref.isLoadNativeLanguageSetting
            && ConsentHelper.getInstance(this).canRequestAds()) {
            val shimmer =
                NativeBotHorizontalMediaLeftLoadingBinding.inflate(LayoutInflater.from(this))
            binding.frNativeAds.removeAllViews()
            binding.frNativeAds.addView(shimmer.root)
            binding.rlNative.visible()

            adsNativeAll?.let {
                pushNativeLanguageSetting(it)
            } ?: run {
                Admob.getInstance().loadNativeAd(this, getString(R.string.native_all), object : NativeCallback() {
                        override fun onNativeAdLoaded(nativeAd: NativeAd) {
                            pushNativeLanguageSetting(nativeAd)
                        }

                        override fun onAdFailedToLoad() {
                            binding.frNativeAds.removeAllViews()
                        }
                    }
                )
            }
        } else binding.rlNative.removeAllViews()
    }

    private fun pushNativeLanguageSetting(nativeAd: NativeAd) {
        val adView =
            AdsNativeBotHorizontalMediaLeftBinding.inflate(LayoutInflater.from(this))

        binding.frNativeAds.removeAllViews()
        binding.frNativeAds.addView(adView.root)
        Admob.getInstance().pushAdsToViewCustom(nativeAd, adView.root)
    }
}