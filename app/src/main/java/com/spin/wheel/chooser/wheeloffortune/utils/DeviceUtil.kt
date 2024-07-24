package com.spin.wheel.chooser.wheeloffortune.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.media.MediaPlayer
import android.net.ConnectivityManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.spin.wheel.chooser.wheeloffortune.R


object DeviceUtil {

    fun hasConnection(context: Context?): Boolean {
        if (context == null) {
            return false
        }

        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        if (wifiNetwork != null && wifiNetwork.isConnected) {
            return true
        }

        val mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
        if (mobileNetwork != null && mobileNetwork.isConnected) {
            return true
        }

        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnected
    }

    fun hideSoftKeyboard(activity: Activity?) {
        if (activity != null && activity.window != null) {
            val imm =
                activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(activity.window.decorView.windowToken, 0)
        }
    }

    fun View.showKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    fun clearFocus(activity: Activity?) {
        if (activity != null && activity.currentFocus != null) {
            activity.currentFocus!!.clearFocus()
        }
    }

    @SuppressLint("ServiceCast")
    fun playVibrator(context: Context, time: Int = 300) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibrator =
                (context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager).defaultVibrator
            vibrator.vibrate(
                VibrationEffect.createOneShot(
                    time.toLong(),
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            (context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator).vibrate(
                VibrationEffect.createOneShot(
                    time.toLong(), VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
        } else {
            // API < 26
            (context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator).vibrate(time.toLong())
        }
    }

    fun playVoiceSuccess(context: Context) {
        val mediaPlayer = MediaPlayer.create(context, R.raw.voice_success)
        mediaPlayer?.start()
        mediaPlayer?.setOnCompletionListener {
            mediaPlayer.release()
        }
    }
}
