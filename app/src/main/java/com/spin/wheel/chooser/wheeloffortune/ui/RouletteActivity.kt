package com.spin.wheel.chooser.wheeloffortune.ui

import android.graphics.Color
import android.os.CountDownTimer
import android.view.LayoutInflater
import com.google.android.gms.ads.nativead.NativeAd
import com.nlbn.ads.callback.NativeCallback
import com.nlbn.ads.util.Admob
import com.nlbn.ads.util.ConsentHelper
import com.spin.wheel.chooser.wheeloffortune.R
import com.spin.wheel.chooser.wheeloffortune.base.BaseActivity
import com.spin.wheel.chooser.wheeloffortune.custom.dialog.ResultWheelDialog
import com.spin.wheel.chooser.wheeloffortune.custom.wheel.WheelItem
import com.spin.wheel.chooser.wheeloffortune.databinding.ActivityWheelBinding
import com.spin.wheel.chooser.wheeloffortune.databinding.AdsNativeBotHorizontalMediaLeftBinding
import com.spin.wheel.chooser.wheeloffortune.databinding.NativeBotHorizontalMediaLeftLoadingBinding
import com.spin.wheel.chooser.wheeloffortune.extensions.gone
import com.spin.wheel.chooser.wheeloffortune.extensions.onClickEffect
import com.spin.wheel.chooser.wheeloffortune.extensions.randomColorWheel
import com.spin.wheel.chooser.wheeloffortune.extensions.toast
import com.spin.wheel.chooser.wheeloffortune.extensions.visible
import com.spin.wheel.chooser.wheeloffortune.utils.AdsConfig
import com.spin.wheel.chooser.wheeloffortune.utils.DeviceUtil
import com.spin.wheel.chooser.wheeloffortune.utils.IntentKey
import kotlin.random.Random

class RouletteActivity : BaseActivity<ActivityWheelBinding>(R.layout.activity_wheel) {

    override fun isHideNavigation(): Boolean = true

    override fun nameActivity(): String = this::class.java.simpleName

    private var winner = ""
    private var listData = arrayListOf<String>()
    private var listPlayer = arrayListOf<WheelItem>()
    private var timer: CountDownTimer? = null
    var count = 3
    private val resultDialog: ResultWheelDialog by lazy { ResultWheelDialog(this) }
    private var isProcess = true

    override fun initView() {
        showNative()
        binding.wheel.setLuckyWheelReachTheTarget {
            DeviceUtil.playVoiceSuccess(this)
            timer?.cancel()
            resultDialog.showDialog(winner)
            binding.btnReset.isEnabled = true
            binding.btnReset.setBackgroundResource(R.drawable.bg_btn_start)
            isProcess = false
        }

        timer = object : CountDownTimer(3000, 1000) {

            override fun onTick(p0: Long) {
                binding.tvCount.text = (count--).toString()
            }

            override fun onFinish() {
                startWheel()
            }
        }
    }

    override fun initData() {
        try {
            listData =
                intent?.getStringArrayListExtra(IntentKey.KEY_LIST_PLAYER) as ArrayList<String>
            listPlayer = listData.map {
                WheelItem(
                    Color.parseColor(
                        randomColorWheel()
                    ), it
                )
            } as ArrayList<WheelItem>
            binding.wheel.addWheelItems(listPlayer)

            binding.btnReset.isEnabled = false
            binding.btnReset.setBackgroundResource(R.drawable.bg_btn_disable)
            timer?.start()
        } catch (_: Exception) {
        }
    }

    override fun initListener() {
        binding.header.onBack = {
            onBackPressed()
        }

        binding.btnReset.onClickEffect(scale = 0.95f) {
            binding.btnReset.isEnabled = false
            isProcess = true
            binding.btnReset.setBackgroundResource(R.drawable.bg_btn_disable)
            binding.tvCount.visible()
            timer?.start()
        }
    }

    private fun startWheel() {
        DeviceUtil.playVibrator(this)
        binding.tvCount.gone()
        count = 3
        try {
            val position = Random.nextInt(listPlayer.size)
            winner = listPlayer[position].text
            binding.wheel.rotateWheelTo(position + 1)
        } catch (_: Exception) {
            winner = listPlayer[0].text
            binding.wheel.rotateWheelTo(0)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
        count = 3
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (!isProcess) {
            super.onBackPressed()
            timer?.cancel()
        } else toast(getString(R.string.message_can_change_winner))
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
                Admob.getInstance()
                    .loadNativeAd(this, getString(R.string.native_all), object : NativeCallback() {
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