package com.spin.wheel.chooser.wheeloffortune.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import com.applovin.impl.sdk.utils.BundleUtils.putStringArrayList
import com.google.android.gms.ads.nativead.NativeAd
import com.nlbn.ads.callback.AdCallback
import com.nlbn.ads.callback.NativeCallback
import com.nlbn.ads.util.Admob
import com.nlbn.ads.util.ConsentHelper
import com.spin.wheel.chooser.wheeloffortune.R
import com.spin.wheel.chooser.wheeloffortune.adapter.recycler_view.WheelAdapter
import com.spin.wheel.chooser.wheeloffortune.base.BaseActivity
import com.spin.wheel.chooser.wheeloffortune.databinding.ActivityWheelConfigBinding
import com.spin.wheel.chooser.wheeloffortune.databinding.AdsNativeBotHorizontalMediaLeftBinding
import com.spin.wheel.chooser.wheeloffortune.databinding.NativeBotHorizontalMediaLeftLoadingBinding
import com.spin.wheel.chooser.wheeloffortune.entity.WheelModel
import com.spin.wheel.chooser.wheeloffortune.extensions.onClickEffect
import com.spin.wheel.chooser.wheeloffortune.extensions.showActivity
import com.spin.wheel.chooser.wheeloffortune.extensions.toast
import com.spin.wheel.chooser.wheeloffortune.extensions.visible
import com.spin.wheel.chooser.wheeloffortune.utils.AdsConfig
import com.spin.wheel.chooser.wheeloffortune.utils.IntentKey

class RouletteConfigActivity :
    BaseActivity<ActivityWheelConfigBinding>(R.layout.activity_wheel_config) {

    override fun isHideNavigation(): Boolean = true

    override fun nameActivity(): String = this::class.java.simpleName

    private val wheelAdapter: WheelAdapter by lazy { WheelAdapter() }

    override fun initView() {
        if (AdsConfig.interRoulette == null) AdsConfig.loadInterRoulette(this@RouletteConfigActivity)
        showNative()
        binding.rcvPlayer.apply {
            setHasFixedSize(true)
            adapter = wheelAdapter
        }
    }

    override fun initData() {
        wheelAdapter.setData(WheelModel.listDefault())
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun initListener() {
        binding.btnAdd.setOnClickListener {
            if (wheelAdapter.itemCount >= 10) {
                toast(getString(R.string.message_max_10))
                return@setOnClickListener
            }
            wheelAdapter.add(WheelModel(wheelAdapter.itemCount + 1, "",true))
        }

        binding.btnStart.onClickEffect {
            var isItemNull = false
            wheelAdapter.getListName().forEach { if (it.isEmpty()) isItemNull = true }
            if (isItemNull) toast(getString(R.string.message_null))
            else showInterAds()
        }

        binding.header.onBack = { onBackPressedDispatcher.onBackPressed() }
    }

    private fun showInterAds() {
        if (haveNetworkConnection() && ConsentHelper.getInstance(this).canRequestAds()
            && AdsConfig.checkTimeShowInter() && sharePref.isLoadInterRoulette && AdsConfig.interRoulette != null) {
            Admob.getInstance().showInterAds(this@RouletteConfigActivity, AdsConfig.interRoulette, object : AdCallback() {
                override fun onNextAction() {
                    super.onNextAction()
                    showActivity(RouletteActivity::class.java, Bundle().apply {
                        putStringArrayList(IntentKey.KEY_LIST_PLAYER, wheelAdapter.getListName())
                    })
                }

                override fun onAdClosedByUser() {
                    super.onAdClosedByUser()
                    AdsConfig.lastTimeShowInter = System.currentTimeMillis()
                    AdsConfig.interRoulette = null
                    AdsConfig.loadInterRoulette(this@RouletteConfigActivity)
                }
            })
        } else showActivity(RouletteActivity::class.java, Bundle().apply {
            putStringArrayList(IntentKey.KEY_LIST_PLAYER, wheelAdapter.getListName())
        })
    }

    private fun showNative() {
        if (haveNetworkConnection() && ConsentHelper.getInstance(this).canRequestAds()) {
            val shimmer =
                NativeBotHorizontalMediaLeftLoadingBinding.inflate(LayoutInflater.from(this))
            binding.frNativeAds.removeAllViews()
            binding.frNativeAds.addView(shimmer.root)
            binding.rlNative.visible()

            AdsConfig.adsNativeAll?.let {
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