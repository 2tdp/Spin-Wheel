package com.spin.wheel.chooser.wheeloffortune

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.nlbn.ads.callback.AdCallback
import com.nlbn.ads.util.Admob
import com.nlbn.ads.util.ConsentHelper
import com.spin.wheel.chooser.wheeloffortune.base.BaseActivity
import com.spin.wheel.chooser.wheeloffortune.custom.SeekbarSplashView
import com.spin.wheel.chooser.wheeloffortune.databinding.ActivitySplashBinding
import com.spin.wheel.chooser.wheeloffortune.extensions.showActivity
import com.spin.wheel.chooser.wheeloffortune.share_pref.MySharePrefImpl
import com.spin.wheel.chooser.wheeloffortune.ui.LanguageActivity
import com.spin.wheel.chooser.wheeloffortune.utils.IntentKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity<ActivitySplashBinding>(R.layout.activity_splash) {

    override fun isHideNavigation(): Boolean = true

    override fun nameActivity(): String = this::class.java.simpleName

    private var interCallback: AdCallback? = null

    override fun initView() {
        binding.sb.apply {
            setMax(100)
            setProgress(0)
        }

        if (haveNetworkConnection()) {
            CoroutineScope(Dispatchers.IO).launch {
                val remote = async { loadConfigRemote() }.await()

                if (remote)
                    withContext(Dispatchers.Main) {
                        interCallback = object : AdCallback() {
                            override fun onNextAction() {
                                super.onNextAction()
                                startActivity()
                            }
                        }

                        val consentHelper = ConsentHelper.getInstance(this@SplashActivity)
                        if (!consentHelper.canLoadAndShowAds()) consentHelper.reset()

                        consentHelper.obtainConsentAndShow(this@SplashActivity) {
                            Admob.getInstance().loadSplashInterAds2(this@SplashActivity, getString(R.string.inter_splash), 0, interCallback)
                        }
                    }
            }
        } else Handler(Looper.getMainLooper()).postDelayed({ startActivity() }, 1500)
    }

    override fun initData() {

    }

    override fun initListener() {

    }

    private fun startActivity() {
        showActivity(LanguageActivity::class.java, Bundle().apply {
            putBoolean(IntentKey.KEY_OPEN_LANGUAGE_FROM_SETTING, false)
        })
    }

    private suspend fun loadConfigRemote(): Boolean {
        return suspendCoroutine { continuation ->
            val configSetting = FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(BuildConfig.Minimum_Fetch)
                .build()

            val remoteConfig = FirebaseRemoteConfig.getInstance().apply {
                setConfigSettingsAsync(configSetting)
            }

            val remoteConfigDefault: Map<String, Any> = mapOf(
                MySharePrefImpl.CB_FETCH_INTERVAL to BuildConfig.cb_fetch_interval,
                MySharePrefImpl.IS_SWITCH_BANNER_COLLAPSE to BuildConfig.switch_bannerCollapse_bannerDefault,
                MySharePrefImpl.IS_LOAD_BANNER_COLLAPSE_HOME to BuildConfig.is_load_banner_collapse_home,
                MySharePrefImpl.IS_LOAD_BANNER to BuildConfig.is_load_banner,
                MySharePrefImpl.IS_LOAD_NATIVE_LANGUAGE to BuildConfig.is_load_native_language,
                MySharePrefImpl.IS_LOAD_NATIVE_LANGUAGE_SETTING to BuildConfig.is_load_native_language_setting,
                MySharePrefImpl.IS_LOAD_NATIVE_INTRO_1 to BuildConfig.is_load_native_intro1,
                MySharePrefImpl.IS_LOAD_NATIVE_INTRO_2 to BuildConfig.is_load_native_intro2,
                MySharePrefImpl.IS_LOAD_NATIVE_INTRO_3 to BuildConfig.is_load_native_intro3,
                MySharePrefImpl.IS_LOAD_INTER_GUIDE to BuildConfig.is_load_inter_guide,
                MySharePrefImpl.IS_LOAD_INTER_BACK to BuildConfig.is_load_inter_back,
                MySharePrefImpl.IS_LOAD_INTER_ROULETTE to BuildConfig.is_load_inter_roulette,
            )

            remoteConfig.setDefaultsAsync(remoteConfigDefault)

            remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val valueCBFetchInterval = remoteConfig.getLong(MySharePrefImpl.CB_FETCH_INTERVAL)
                    val valueSwitchBannerCollapse = remoteConfig.getBoolean(MySharePrefImpl.IS_SWITCH_BANNER_COLLAPSE)
                    val valueLoadBannerCollapseHome = remoteConfig.getBoolean(MySharePrefImpl.IS_LOAD_BANNER_COLLAPSE_HOME)
                    val valueLoadBanner = remoteConfig.getBoolean(MySharePrefImpl.IS_LOAD_BANNER)
                    val valueLoadNativeLanguage = remoteConfig.getBoolean(MySharePrefImpl.IS_LOAD_NATIVE_LANGUAGE)
                    val valueLoadNativeLanguageSetting = remoteConfig.getBoolean(MySharePrefImpl.IS_LOAD_NATIVE_LANGUAGE_SETTING)
                    val valueLoadNativeIntro1 = remoteConfig.getBoolean(MySharePrefImpl.IS_LOAD_NATIVE_INTRO_1)
                    val valueLoadNativeIntro2 = remoteConfig.getBoolean(MySharePrefImpl.IS_LOAD_NATIVE_INTRO_2)
                    val valueLoadNativeIntro3 = remoteConfig.getBoolean(MySharePrefImpl.IS_LOAD_NATIVE_INTRO_3)
                    val valueLoadNativeHome = remoteConfig.getBoolean(MySharePrefImpl.IS_LOAD_NATIVE_HOME)
                    val valueLoadInterGuide = remoteConfig.getBoolean(MySharePrefImpl.IS_LOAD_INTER_GUIDE)
                    val valueLoadInterBack = remoteConfig.getBoolean(MySharePrefImpl.IS_LOAD_INTER_BACK)
                    val valueLoadInterRoulette = remoteConfig.getBoolean(MySharePrefImpl.IS_LOAD_INTER_ROULETTE)

                    sharePref.cbFetchInterval = valueCBFetchInterval
                    sharePref.isSwitchBannerCollapse = valueSwitchBannerCollapse
                    sharePref.isLoadBannerCollapseHome = valueLoadBannerCollapseHome
                    sharePref.isLoadBanner = valueLoadBanner
                    sharePref.isLoadNativeLanguage = valueLoadNativeLanguage
                    sharePref.isLoadNativeLanguageSetting = valueLoadNativeLanguageSetting
                    sharePref.isLoadNativeIntro1 = valueLoadNativeIntro1
                    sharePref.isLoadNativeIntro2 = valueLoadNativeIntro2
                    sharePref.isLoadNativeIntro3 = valueLoadNativeIntro3
                    sharePref.isLoadNativeHome = valueLoadNativeHome
                    sharePref.isLoadInterGuide = valueLoadInterGuide
                    sharePref.isLoadInterBack = valueLoadInterBack
                    sharePref.isLoadInterRoulette = valueLoadInterRoulette
                }

                continuation.resume(true)
            }
        }
    }
}