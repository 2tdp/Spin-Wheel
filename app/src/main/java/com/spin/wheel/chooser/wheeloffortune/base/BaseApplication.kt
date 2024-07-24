package com.spin.wheel.chooser.wheeloffortune.base

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.google.firebase.FirebaseApp
import com.nlbn.ads.callback.AdCallback
import com.nlbn.ads.util.AdsApplication
import com.nlbn.ads.util.AppFlyer
import com.nlbn.ads.util.AppOpenManager
import com.spin.wheel.chooser.wheeloffortune.R
import com.spin.wheel.chooser.wheeloffortune.SplashActivity
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApplication: AdsApplication() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context

        @SuppressLint("StaticFieldLeak")
        var w = 0f
        var screenWidth = 0
        var screenHeight = 0
    }
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        w = resources.displayMetrics.widthPixels / 100f
        screenWidth = resources.displayMetrics.widthPixels
        screenHeight = resources.displayMetrics.heightPixels

        FirebaseApp.initializeApp(this)

        AppFlyer.getInstance().initAppFlyer(this, getString(R.string.app_flyer), true)

        //ads
        AppOpenManager.getInstance().disableAppResumeWithActivity(SplashActivity::class.java)
    }

    override fun enableAdsResume(): Boolean = true

    override fun getKeyRemoteIntervalShowInterstitial(): String = ""

    override fun getListTestDeviceId(): MutableList<String>? = null

    override fun getResumeAdId(): String = getString(R.string.appopen_resume)

    override fun buildDebug(): Boolean = false
}