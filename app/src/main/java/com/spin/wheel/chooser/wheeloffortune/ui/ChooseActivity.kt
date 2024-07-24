package com.spin.wheel.chooser.wheeloffortune.ui

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.widget.FrameLayout
import androidx.activity.OnBackPressedCallback
import com.nlbn.ads.banner.BannerPlugin
import com.nlbn.ads.callback.AdCallback
import com.nlbn.ads.util.Admob
import com.nlbn.ads.util.ConsentHelper
import com.spin.wheel.chooser.wheeloffortune.R
import com.spin.wheel.chooser.wheeloffortune.base.BaseActivity
import com.spin.wheel.chooser.wheeloffortune.custom.chooser.PointChooserView
import com.spin.wheel.chooser.wheeloffortune.custom.popup.PopupPlayerWin
import com.spin.wheel.chooser.wheeloffortune.databinding.ActivityChooseBinding
import com.spin.wheel.chooser.wheeloffortune.extensions.gone
import com.spin.wheel.chooser.wheeloffortune.extensions.onClickEffect
import com.spin.wheel.chooser.wheeloffortune.extensions.randomResultChooser
import com.spin.wheel.chooser.wheeloffortune.extensions.toDp
import com.spin.wheel.chooser.wheeloffortune.extensions.toast
import com.spin.wheel.chooser.wheeloffortune.extensions.visible
import com.spin.wheel.chooser.wheeloffortune.utils.AdsConfig
import com.spin.wheel.chooser.wheeloffortune.utils.Constants
import com.spin.wheel.chooser.wheeloffortune.utils.DeviceUtil

class ChooseActivity : BaseActivity<ActivityChooseBinding>(R.layout.activity_choose) {

    override fun isHideNavigation(): Boolean = true

    override fun nameActivity(): String = this::class.java.simpleName

    private var popup: PopupPlayerWin? = null
    private val listView = mutableListOf<PointChooserView>()
    private var countWinner = 1
    private var isFindingWinner = false
    private var isResetting = false
    private var canChangeWinner = true
    private var timer: CountDownTimer? = null
    var count = 3

    override fun initView() {
        if (AdsConfig.interBack == null) AdsConfig.loadInterBack(this@ChooseActivity)
        loadBannerCollapse()

        onBackPressedDispatcher.addCallback(this@ChooseActivity, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showInterAds()
            }
        })

        popup = PopupPlayerWin(this, binding.ivCountWinner, {
            countWinner = it
            setCurCountWinner()
        }, { setCurCountWinner() })

        timer = object : CountDownTimer(3000, 1000) {

            override fun onTick(p0: Long) {
                binding.tvCountdown.text = (count--).toString()
            }

            override fun onFinish() {
                binding.tvCountdown.text = ""
                findWinner()
            }
        }
    }

    private fun setCurCountWinner() {
        when(countWinner) {
            1 -> binding.ivCountWinner.setImageResource(R.drawable.img_number_one)
            2 -> binding.ivCountWinner.setImageResource(R.drawable.img_number_two)
            3 -> binding.ivCountWinner.setImageResource(R.drawable.img_number_three)
            4 -> binding.ivCountWinner.setImageResource(R.drawable.img_number_four)
        }
    }

    override fun initData() {

    }

    @SuppressLint("ClickableViewAccessibility")
    override fun initListener() {
        binding.header.onBack = { onBackPressedDispatcher.onBackPressed() }

        binding.ivCountWinner.onClickEffect {
            if (!canChangeWinner) {
                toast(getString(R.string.message_can_change_winner))
                return@onClickEffect
            }
            binding.ivCountWinner.setImageResource(R.drawable.img_drop)
            popup?.show()
        }

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
                        if (listView.size > countWinner && !isFindingWinner) {
                            isFindingWinner = true
                            Handler(Looper.getMainLooper()).postDelayed({
                                binding.tvCountdown.visible()
                                timer?.start()
                            }, 3000)
                        }
                        if (listView.size <= countWinner) {
                            toast(getString(R.string.message_touch_point))
                        }
                    } catch (_: Exception) {}
                }
            }
            true
        }
    }

    private fun findWinner() {
        binding.fContainer.isEnabled = false
        canChangeWinner = false
        try {
            val list = randomResultChooser(listView.size, countWinner)
            val listWin = mutableListOf<PointChooserView>()

            binding.tvCountdown.gone()
            DeviceUtil.playVoiceSuccess(this)

            for (value in list) {
                listView[value - 1].setWin()
                listWin.add(listView[value - 1])
            }

            listView.removeAll(listWin)
            listView.forEach { it.setFail() }

            reset(7000)
            isResetting = true
        } catch (_: Exception) {
            reset(0)
        }
    }

    private fun addViewPoint(x: Float, y: Float) {
        DeviceUtil.playVibrator(this)
        val viewPoint = PointChooserView(this).apply {
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
            binding.fContainer.isEnabled = true

            listView.clear()

            isFindingWinner = false
            isResetting = false
            canChangeWinner = true
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
            Admob.getInstance().showInterAds(this@ChooseActivity, AdsConfig.interBack, object : AdCallback() {
                override fun onNextAction() {
                    super.onNextAction()
                    finish()
                }

                override fun onAdClosedByUser() {
                    super.onAdClosedByUser()
                    AdsConfig.lastTimeShowInter = System.currentTimeMillis()
                    AdsConfig.interBack = null
                    AdsConfig.loadInterBack(this@ChooseActivity)
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