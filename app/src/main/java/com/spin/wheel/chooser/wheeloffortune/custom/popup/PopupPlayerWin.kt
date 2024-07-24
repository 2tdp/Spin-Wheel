package com.spin.wheel.chooser.wheeloffortune.custom.popup

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.PopupWindow
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.spin.wheel.chooser.wheeloffortune.R
import com.spin.wheel.chooser.wheeloffortune.extensions.onAvoidDoubleClick
import com.spin.wheel.chooser.wheeloffortune.extensions.toDp

class PopupPlayerWin(private val activity: Activity, val view: View, val onClick: (Int) -> Unit = {}, val onDismiss: () -> Unit = {}) : PopupWindow(activity) {

    private var popupWindow: PopupWindow? = null
    private var currentCount = 1

    private fun init() {
        val inflater =
            activity.getSystemService(AppCompatActivity.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val popup = inflater.inflate(R.layout.popup_player_win, null)
        popupWindow = PopupWindow(popup, 120.toDp, 195.toDp, true)
//        popupWindow?.apply {
//            isOutsideTouchable = false
//            isFocusable = false
//        }
        val tv1 = popup.findViewById<TextView>(R.id.tv_1)
        val tv2 = popup.findViewById<TextView>(R.id.tv_2)
        val tv3 = popup.findViewById<TextView>(R.id.tv_3)
        val tv4 = popup.findViewById<TextView>(R.id.tv_4)

        tv1.setBackgroundColor(0)
        tv1.setTextColor(Color.parseColor("#3A3E46"))

        tv2.setBackgroundColor(0)
        tv2.setTextColor(Color.parseColor("#3A3E46"))

        tv3.setBackgroundColor(0)
        tv3.setTextColor(Color.parseColor("#3A3E46"))

        tv4.setBackgroundColor(0)
        tv4.setTextColor(Color.parseColor("#3A3E46"))

        when (currentCount) {
            1 -> {
                tv1.setBackgroundColor(Color.parseColor("#F49A1C"))
                tv1.setTextColor(Color.parseColor("#FFFFFF"))
            }
            2 -> {
                tv2.setBackgroundColor(Color.parseColor("#F49A1C"))
                tv2.setTextColor(Color.parseColor("#FFFFFF"))
            }
            3 -> {
                tv3.setBackgroundColor(Color.parseColor("#F49A1C"))
                tv3.setTextColor(Color.parseColor("#FFFFFF"))
            }
            4 -> {
                tv4.setBackgroundColor(Color.parseColor("#F49A1C"))
                tv4.setTextColor(Color.parseColor("#FFFFFF"))
            }
        }

        tv1.onAvoidDoubleClick {
            onClick.invoke(1)
            currentCount = 1
            popupWindow?.dismiss()
        }

        tv2.onAvoidDoubleClick {
            onClick.invoke(2)
            currentCount = 2
            popupWindow?.dismiss()
        }

        tv3.onAvoidDoubleClick {
            onClick.invoke(3)
            currentCount = 3
            popupWindow?.dismiss()
        }

        tv4.onAvoidDoubleClick {
            onClick.invoke(4)
            currentCount = 4
            popupWindow?.dismiss()
        }

        popupWindow?.setOnDismissListener { onDismiss.invoke() }
    }

    fun show() {
        init()
        if (isShowing) return
        popupWindow?.showAsDropDown(view, (-80).toDp, 10.toDp)
    }
}