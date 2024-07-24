package com.spin.wheel.chooser.wheeloffortune.ui

import android.annotation.SuppressLint
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import androidx.activity.OnBackPressedCallback
import com.nlbn.ads.banner.BannerPlugin
import com.nlbn.ads.callback.AdCallback
import com.nlbn.ads.util.Admob
import com.nlbn.ads.util.ConsentHelper
import com.spin.wheel.chooser.wheeloffortune.R
import com.spin.wheel.chooser.wheeloffortune.base.BaseActivity
import com.spin.wheel.chooser.wheeloffortune.custom.rank.PointRankView
import com.spin.wheel.chooser.wheeloffortune.databinding.ActivityRankBinding
import com.spin.wheel.chooser.wheeloffortune.extensions.gone
import com.spin.wheel.chooser.wheeloffortune.extensions.randomRank
import com.spin.wheel.chooser.wheeloffortune.extensions.toDp
import com.spin.wheel.chooser.wheeloffortune.extensions.toast
import com.spin.wheel.chooser.wheeloffortune.extensions.visible
import com.spin.wheel.chooser.wheeloffortune.utils.AdsConfig
import com.spin.wheel.chooser.wheeloffortune.utils.Constants
import com.spin.wheel.chooser.wheeloffortune.utils.DeviceUtil

class RankActivity : BaseActivity<ActivityRankBinding>(R.layout.activity_rank) {

    override fun isHideNavigation(): Boolean = true

    override fun nameActivity(): String = this::class.java.simpleName

    private val listView = mutableListOf<PointRankView>()
    private var isFindingWinner = false
    private var isResetting = false
    private var timer: CountDownTimer? = null
    var count = 3

    override fun initView() {
        if (AdsConfig.interBack == null) AdsConfig.loadInterBack(this@RankActivity)
        loadBannerCollapse()

        onBackPressedDispatcher.addCallback(this@RankActivity, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showInterAds()
            }
        })

        timer = object : CountDownTimer(3000, 1000) {

            override fun onTick(p0: Long) {
                binding.tvCountdown.text = (count--).toString()
            }

            override fun onFinish() {
                binding.tvCountdown.text = ""
                if (listView.size == 1) toast(getString(R.string.message_touch_point))
                else findWinner()
            }
        }
    }

    override fun initData() {

    }

    @SuppressLint("ClickableViewAccessibility")
    override fun initListener() {
        binding.header.onBack = { onBackPressedDispatcher.onBackPressed() }

        binding.fContainer.setOnTouchListener { _, event ->
            when (event.actionMasked) {

                MotionEvent.ACTION_DOWN -> {
                    if (listView.size <= Constants.MAX_PLAYER) {
                        val x = event.x
                        val y = event.y
                        addViewPoint(x, y)
                    }
                }

                MotionEvent.ACTION_POINTER_DOWN -> {
                    if (listView.size <= Constants.MAX_PLAYER) {
                        val pointerIndex = event.actionIndex
                        val x = event.getX(pointerIndex)
                        val y = event.getY(pointerIndex)
                        addViewPoint(x, y)
                    }
                }

                MotionEvent.ACTION_UP,
                MotionEvent.ACTION_CANCEL,
                MotionEvent.ACTION_POINTER_UP -> {
                    try {
                        if (!isFindingWinner) {
                            isFindingWinner = true
                            Handler(Looper.getMainLooper()).postDelayed({
                                binding.tvCountdown.visible()
                                timer?.start()
                            }, 3000)
                        }
                    } catch (_: Exception) {}
                }
            }
            true
        }
    }

    private fun findWinner() {
        binding.fContainer.isEnabled = false
        try {
            val list = randomRank(listView.map { it.getIdPoint() }.toMutableList())

            binding.tvCountdown.gone()
            DeviceUtil.playVoiceSuccess(this)

            listView.forEachIndexed { index, point ->
                point.showResult(list[index])
            }
            reset(7000)
            isResetting = true

        } catch (_: Exception) {
            reset(0)
        }
    }

    private fun addViewPoint(x: Float, y: Float) {
        DeviceUtil.playVibrator(this)
        val viewPoint = PointRankView(this).apply {
            isClickable = true
            isFocusable = true
            setIdPoint(listView.size + 1)
        }
        val params = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            leftMargin = x.toInt() - Constants.SIZE_POINT_CHOOSER.toDp / 2
            topMargin = y.toInt() - Constants.SIZE_POINT_CHOOSER.toDp / 2
        }
        binding.fContainer.addView(viewPoint, params)
        listView.add(viewPoint)
    }

    private fun reset(delay: Int = 3000) {
        if (isResetting) return
        Handler(Looper.getMainLooper()).postDelayed({
            binding.fContainer.removeAllViews()
            listView.clear()

            isFindingWinner = false
            binding.fContainer.isEnabled = true
            isResetting = false
            count = 3
            binding.tvCountdown.gone()
        }, delay.toLong())
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
            Admob.getInstance().showInterAds(this@RankActivity, AdsConfig.interBack, object : AdCallback() {
                override fun onNextAction() {
                    super.onNextAction()
                    finish()
                }

                override fun onAdClosedByUser() {
                    super.onAdClosedByUser()
                    AdsConfig.lastTimeShowInter = System.currentTimeMillis()
                    AdsConfig.interBack = null
                    AdsConfig.loadInterBack(this@RankActivity)
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