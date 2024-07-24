package com.spin.wheel.chooser.wheeloffortune.utils

import android.content.Context
import android.net.ConnectivityManager
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.nativead.NativeAd
import com.nlbn.ads.callback.AdCallback
import com.nlbn.ads.callback.NativeCallback
import com.nlbn.ads.util.Admob
import com.nlbn.ads.util.ConsentHelper
import com.spin.wheel.chooser.wheeloffortune.R
import com.spin.wheel.chooser.wheeloffortune.share_pref.MySharePrefImpl

class AdsConfig {

    companion object {
        var lastTimeShowInter = 0L

        var adsNativeAll: NativeAd? = null
        var adsNativeHome: NativeAd? = null

        var interGuide: InterstitialAd? = null
        var interBack: InterstitialAd? = null
        var interRoulette: InterstitialAd? = null

        fun loadInterRoulette(context: Context) {
            if (ConsentHelper.getInstance(context).canRequestAds() && interRoulette == null
                && haveNetworkConnection(context) && MySharePrefImpl(context).isLoadInterRoulette
            ) {
                Admob.getInstance().loadInterAds(
                    context,
                    context.getString(R.string.inter_roulette),
                    object : AdCallback() {
                        override fun onInterstitialLoad(interstitialAd: InterstitialAd?) {
                            super.onInterstitialLoad(interstitialAd)
                            interRoulette = interstitialAd
                        }
                    })
            }
        }

        fun loadInterBack(context: Context) {
            if (ConsentHelper.getInstance(context).canRequestAds() && interBack == null
                && haveNetworkConnection(context) && MySharePrefImpl(context).isLoadInterBack
            ) {
                Admob.getInstance().loadInterAds(
                    context,
                    context.getString(R.string.inter_back),
                    object : AdCallback() {
                        override fun onInterstitialLoad(interstitialAd: InterstitialAd?) {
                            super.onInterstitialLoad(interstitialAd)
                            interBack = interstitialAd
                        }
                    })
            }
        }

        fun loadInterGuide(context: Context) {
            if (ConsentHelper.getInstance(context).canRequestAds() && interGuide == null
                && haveNetworkConnection(context) && MySharePrefImpl(context).isLoadInterGuide
            ) {
                Admob.getInstance().loadInterAds(
                    context,
                    context.getString(R.string.inter_guide),
                    object : AdCallback() {
                        override fun onInterstitialLoad(interstitialAd: InterstitialAd?) {
                            super.onInterstitialLoad(interstitialAd)
                            interGuide = interstitialAd
                        }
                    })
            }
        }

        fun loadNativeHome(context: Context){
            if (haveNetworkConnection(context)
                && ConsentHelper.getInstance(context).canRequestAds()
                && MySharePrefImpl(context).isLoadNativeHome) {
                Admob.getInstance().loadNativeAd(context, context.getString(R.string.native_home), object : NativeCallback() {
                    override fun onNativeAdLoaded(nativeAd: NativeAd?) {
                        adsNativeHome = nativeAd
                    }

                    override fun onAdImpression() {
                        super.onAdImpression()
                        adsNativeHome = null
                        loadNativeHome(context)
                    }
                })
            }
        }

        fun loadNativeAll(context: Context){
            if (haveNetworkConnection(context)
                && ConsentHelper.getInstance(context).canRequestAds()
                && (MySharePrefImpl(context).isLoadNativeLanguageSetting)){
                Admob.getInstance()
                    .loadNativeAd(context, context.getString(R.string.native_all), object : NativeCallback() {
                            override fun onNativeAdLoaded(nativeAd: NativeAd) {
                                adsNativeAll = nativeAd
                            }

                            override fun onAdImpression() {
                                super.onAdImpression()
                                adsNativeAll = null
                            }
                        })
            }
        }

        fun checkTimeShowInter(): Boolean = System.currentTimeMillis() - lastTimeShowInter > 15000

        fun haveNetworkConnection(context: Context): Boolean {
            var haveConnectedWifi = false
            var haveConnectedMobile = false
            val cm =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val netInfo = cm.allNetworkInfo
            for (ni in netInfo) {
                if (ni.typeName.equals("WIFI", ignoreCase = true))
                    if (ni.isConnected) haveConnectedWifi = true
                if (ni.typeName.equals("MOBILE", ignoreCase = true))
                    if (ni.isConnected) haveConnectedMobile = true
            }
            return haveConnectedWifi || haveConnectedMobile
        }

        fun isLoadAdsNormal(): Boolean = !Admob.getInstance().isLoadFullAds
    }
}