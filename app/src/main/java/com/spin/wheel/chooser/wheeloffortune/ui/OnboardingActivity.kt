package com.spin.wheel.chooser.wheeloffortune.ui

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.nlbn.ads.callback.NativeCallback
import com.nlbn.ads.util.Admob
import com.nlbn.ads.util.ConsentHelper
import com.spin.wheel.chooser.wheeloffortune.R
import com.spin.wheel.chooser.wheeloffortune.adapter.DepthPageTransformer
import com.spin.wheel.chooser.wheeloffortune.adapter.OnboardingAdapter
import com.spin.wheel.chooser.wheeloffortune.base.BaseActivity
import com.spin.wheel.chooser.wheeloffortune.databinding.ActivityOnboardingBinding
import com.spin.wheel.chooser.wheeloffortune.databinding.AdsNativeBotBinding
import com.spin.wheel.chooser.wheeloffortune.databinding.AdsNativeTopNewBinding
import com.spin.wheel.chooser.wheeloffortune.databinding.NativeButtonBotLoadingBinding
import com.spin.wheel.chooser.wheeloffortune.databinding.NativeTopLoadingBinding
import com.spin.wheel.chooser.wheeloffortune.extensions.createBackground
import com.spin.wheel.chooser.wheeloffortune.extensions.invisible
import com.spin.wheel.chooser.wheeloffortune.extensions.onClickEffect
import com.spin.wheel.chooser.wheeloffortune.extensions.showActivity
import com.spin.wheel.chooser.wheeloffortune.extensions.visible
import com.spin.wheel.chooser.wheeloffortune.utils.AdsConfig

class OnboardingActivity : BaseActivity<ActivityOnboardingBinding>(R.layout.activity_onboarding) {

    override fun isHideNavigation(): Boolean = true

    override fun nameActivity(): String = this::class.java.simpleName

    private var nativeAds: NativeAd? = null

    private val listLayoutRes = arrayListOf(
        R.layout.layout_onboarding_1,
        R.layout.layout_onboarding_2,
        R.layout.layout_onboarding_3
    )
    private var pageCount = 0

    override fun initView() {
        if (AdsConfig.isLoadAdsNormal()) {
            binding.tvNext.setTextColor(ContextCompat.getColor(this@OnboardingActivity, R.color.color_FCD535))
            val vLoad = NativeButtonBotLoadingBinding.inflate(LayoutInflater.from(this))
            binding.frNativeAds.addView(vLoad.root)
        } else {
            binding.tvNext.setTextColor(Color.WHITE)
            val vLoad = NativeTopLoadingBinding.inflate(LayoutInflater.from(this))
            binding.frNativeAds.addView(vLoad.root)
        }

        binding.vpTab.apply {
            adapter = OnboardingAdapter(this@OnboardingActivity, listLayoutRes)
            offscreenPageLimit = 3
            setPageTransformer(DepthPageTransformer())
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    initUi()
                    pageCount = position
                }
            })
        }
    }

    override fun initData() {
    }

    override fun initListener() {
        binding.tvNext.onClickEffect(delay = 300) {
            pageCount++
            binding.vpTab.setCurrentItem(pageCount, true)
            initUi()
            if (pageCount >= listLayoutRes.size) {
                binding.tvNext.isEnabled = false
                sharePref.openGuide = true
                showActivity(MainActivity::class.java)
                finishAffinity()
            }
        }
    }

    private fun initUi() {
        with(binding) {
            val currentPage = binding.vpTab.currentItem
            dotPage.currentPage(currentPage)
            when (currentPage) {
                TAB_ONE -> {
                    tvTitle.text = getString(R.string.str_title_onboarding_1)
                    tvQuote.text = getString(R.string.str_quote_onboarding_1)
                    tvNext.text = getString(R.string.str_next)

                    if (sharePref.isLoadNativeIntro1 || !AdsConfig.isLoadAdsNormal()) {
                        binding.rlNative.visible()
                        loadNativeIntro(0)
                    } else binding.rlNative.invisible()
                }

                TAB_TWO -> {
                    tvTitle.text = getString(R.string.str_title_onboarding_2)
                    tvQuote.text = getString(R.string.str_quote_onboarding_2)
                    tvNext.text = getString(R.string.str_next)

                    if (sharePref.isLoadNativeIntro2 || !AdsConfig.isLoadAdsNormal()) {
                        binding.rlNative.visible()
                        loadNativeIntro(1)
                    } else binding.rlNative.invisible()
                }

                TAB_THREE -> {
                    tvTitle.text = getString(R.string.str_title_onboarding_3)
                    tvQuote.text = getString(R.string.str_quote_onboarding_3)
                    tvNext.text = getString(R.string.str_start)

                    if (sharePref.isLoadNativeIntro3 || !AdsConfig.isLoadAdsNormal()) {
                        binding.rlNative.visible()
                        loadNativeIntro(2)
                    } else binding.rlNative.invisible()
                }
            }
        }
    }

    private fun loadNativeIntro(pos: Int) {
        var strId = ""
        when(pos) {
            0 -> strId = getString(R.string.native_intro1)
            1 -> strId = getString(R.string.native_intro2)
            2 -> strId = getString(R.string.native_intro3)
        }

        try {
            if (haveNetworkConnection() && ConsentHelper.getInstance(this).canRequestAds()) {
                nativeAds?.let {
                    pushViewAds(it)
                } ?: run {
                    Admob.getInstance().loadNativeAd(this, strId, object : NativeCallback() {
                        override fun onNativeAdLoaded(nativeAd: NativeAd) {
                            nativeAds = nativeAd
                            pushViewAds(nativeAd)
                        }

                        override fun onAdFailedToLoad() {
                            super.onAdFailedToLoad()
                            nativeAds = null
                            binding.rlNative.invisible()
                        }

                        override fun onAdImpression() {
                            super.onAdImpression()
                            nativeAds = null
                        }
                    })
                }
            } else binding.rlNative.invisible()
        } catch (e: Exception) {
            e.printStackTrace()
            binding.rlNative.invisible()
        }
    }

    private fun pushViewAds(nativeAd: NativeAd) {
        val adView: ViewBinding
        if (AdsConfig.isLoadAdsNormal()) {
            adView = AdsNativeBotBinding.inflate(LayoutInflater.from(this@OnboardingActivity))
        } else {
            adView = AdsNativeTopNewBinding.inflate(LayoutInflater.from(this@OnboardingActivity))
        }

        binding.frNativeAds.removeAllViews()
        binding.frNativeAds.addView(adView.root)
        Admob.getInstance().pushAdsToViewCustom(nativeAd, adView.root as NativeAdView)
    }

    companion object {
        const val TAB_ONE = 0
        const val TAB_TWO = 1
        const val TAB_THREE = 2
    }
}