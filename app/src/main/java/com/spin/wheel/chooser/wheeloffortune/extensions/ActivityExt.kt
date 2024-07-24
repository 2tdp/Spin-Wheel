package com.spin.wheel.chooser.wheeloffortune.extensions

import android.annotation.SuppressLint
import android.app.Activity
import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.spin.wheel.chooser.wheeloffortune.R
import com.spin.wheel.chooser.wheeloffortune.entity.LanguageModel
import java.util.Locale
import kotlin.random.Random

fun AppCompatActivity.changeLanguage(context: Context, language: LanguageModel?): Context {
    language?.let {
        try {
            Locale.setDefault(language.locale)
            return context.createConfigurationContext(Configuration(context.resources.configuration).apply {
                this.setLocale(language.locale)
            })
        } catch (e: Exception) {
            e.printStackTrace()
            return context.createConfigurationContext(Configuration(context.resources.configuration).apply {
                this.setLocale(Locale.ENGLISH)
            })
        }
    }
    return context.createConfigurationContext(Configuration(context.resources.configuration).apply {
        this.setLocale(Locale.ENGLISH)
    })
}

fun Activity.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Activity.showActivity(activity: Class<*>, bundle: Bundle? = null) {
    val intent = Intent(this, activity)
    intent.putExtras(bundle ?: Bundle())
    startActivity(intent)
    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
}

fun randomResultChooser(size: Int, countWinner: Int): List<Int> {
    try {
        if (size <= 2) {
            return arrayListOf(1)
        } else {
            val arr = when (countWinner) {
                1 -> {
                    when (size) {
                        in 0..1 -> arrayListOf(1)
                        in 2..5 -> arrayListOf(3)
                        in 6..8 -> arrayListOf(6)
                        in 9..10 -> arrayListOf(9)
                        else -> arrayListOf(1)
                    }
                }
                2 -> {
                    when (size) {
                        in 0..2 -> arrayListOf(1, 2)
                        in 3..6 -> arrayListOf(2, 3)
                        in 7..8 -> arrayListOf(5, 6)
                        in 9..10 -> arrayListOf(5, 9)
                        else -> arrayListOf(1, 2)
                    }
                }
                3 -> {
                    when (size) {
                        in 0..3 -> arrayListOf(1, 2, 3)
                        in 4..7 -> arrayListOf(1, 3, 4)
                        in 8..10 -> arrayListOf(2, 4, 7)
                        else -> arrayListOf(1, 2, 3)
                    }
                }
                4 -> {
                    when (size) {
                        in 0..4 -> arrayListOf(1, 2, 4, 3)
                        in 5..10 -> arrayListOf(2, 3, 5, 4)
                        else -> arrayListOf(1, 2, 4, 3)
                    }
                }
                else -> arrayListOf(1)
            }
            return arr
        }
    } catch (_: Exception) {
        return arrayListOf(1)
    }
}

fun randomColor(): String {
    val list = arrayListOf<String>(
        "#ffffff",
        "#ffe6e6",
        "#ff8080",
        "#e60000",
        "#ccffee",
        "#4dffc3",
        "#00b377",
        "#b3ecff",
        "#66d9ff",
        "#00bfff",
        "#ffbb99",
        "#ff7733",
        "#ffc34d",
        "#ffe6b3",
        "#adebad",
        "#5cd65c",
        "#2eb82e",
        "#ffcc99",
        "#ff8c1a",
        "#e67300",
        "#ff99ff",
        "#ff33ff",
        "#e600e6",
        "#ffff1a",
        "#ffff80",
        "#4dffdb"
    )
    val index = Random.nextInt(list.size)
    return list[index]
}

fun randomRank(list: MutableList<Int>) : List<Int> {
    if (list.size <= 1) return list

    val random = Random.Default
    val index1 = random.nextInt(list.size)
    var index2 = random.nextInt(list.size)

    while (index2 == index1) {
        index2 = random.nextInt(list.size)
    }

    val temp = list[index1]
    list[index1] = list[index2]
    list[index2] = temp

    return list
}

fun randomColorWheel(): String {
    val list = arrayListOf<String>(
        "#80dfff",
        "#00bfff",
        "#b3b3ff",
        "#b3b3cc",
        "#66ffb3",
        "#00ff80",
        "#1affff",
        "#ff99ff",
        "#ff4dff",
        "#ffcc80",
        "#ff9999",
        "#ff4d4d",
        "#9fff80",
        "#53ff1a",
    )
    val index = Random.nextInt(list.size)
    return list[index]
}

@Suppress("DEPRECATION")
fun makeVibrate(context: Context) {
    val vibrator: Vibrator
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        vibrator = (context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager).defaultVibrator
        vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
    } else {
        //deprecated in API 26
        vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(200)
    }
}

fun AppCompatActivity.setStatusBarTransparent(context: Context, isVisible: Boolean, colorStatus: Int, colorNavi: Int) {
    val decorView = window.decorView
    window.statusBarColor = colorStatus
    window.navigationBarColor = colorNavi

    var flags = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    if (isVisible) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        decorView.systemUiVisibility = flags
    } else {
//        window.statusBarColor = Color.TRANSPARENT
//        window.navigationBarColor = Color.TRANSPARENT
//        if (Build.VERSION.SDK_INT >= 30)
//            WindowCompat.setDecorFitsSystemWindows(window, false)
//        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            flags = flags or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
//            decorView.systemUiVisibility = flags
//        } else if (Build.VERSION.SDK_INT in 21..26) {
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//            window.decorView.systemUiVisibility = SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or SYSTEM_UI_FLAG_LAYOUT_STABLE or SYSTEM_UI_FLAG_HIDE_NAVIGATION
//            actionBar?.hide()
//        }

        /**
         * display above lock screen
         */
        var flag = WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        val manager =
            applicationContext.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
            manager.requestDismissKeyguard(this, null)
        } else {
            flag = flag or WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        }
        /**
         * set navigation bar transparent
         */
        decorView.systemUiVisibility = flags
        window.addFlags(flag)
        window.navigationBarColor = Color.parseColor("#01ffffff")
        window.statusBarColor = Color.TRANSPARENT

        (context.getSystemService(Context.POWER_SERVICE) as PowerManager).let {
            @SuppressLint("InvalidWakeLockTag")
            val wOn = it.newWakeLock(PowerManager.FULL_WAKE_LOCK, "tag")
            wOn.acquire(5 * 1000L)
        }
    }
}
