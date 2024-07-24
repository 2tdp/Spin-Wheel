package com.spin.wheel.chooser.wheeloffortune.custom.homo_graft

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout
import com.spin.wheel.chooser.wheeloffortune.R
import com.spin.wheel.chooser.wheeloffortune.extensions.div
import com.spin.wheel.chooser.wheeloffortune.extensions.randomColor
import com.spin.wheel.chooser.wheeloffortune.extensions.toDp
import com.spin.wheel.chooser.wheeloffortune.extensions.toast
import com.spin.wheel.chooser.wheeloffortune.utils.Constants
import com.spin.wheel.chooser.wheeloffortune.utils.DeviceUtil
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking

class HomoGraftView(context: Context?, attrs: AttributeSet?) : FrameLayout(context!!, attrs) {

    private val listView = mutableListOf<PointHomoGraftView>()
    var countInGroup = 2
    private var isFindingWinner = false
    private var isResetting = false
    var canChangeWinner = true
    var onFind: () -> Unit = {}
    var onReset: () -> Unit = {}

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeWidth = 15f
        style = Paint.Style.FILL
        color = Color.WHITE
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {

            MotionEvent.ACTION_DOWN -> {
                if (listView.size <= Constants.MAX_PLAYER) {
                    val x = event.x
                    val y = event.y
                    addViewPoint(x, y)
                }
                return true
            }

            MotionEvent.ACTION_POINTER_DOWN -> {
                if (listView.size <= Constants.MAX_PLAYER) {
                    val pointerIndex = event.actionIndex
                    val x = event.getX(pointerIndex)
                    val y = event.getY(pointerIndex)
                    addViewPoint(x, y)
                }
                return true
            }

            MotionEvent.ACTION_UP,
            MotionEvent.ACTION_CANCEL,
            MotionEvent.ACTION_POINTER_UP -> {
                if (listView.size > countInGroup) {
                    if (!isFindingWinner) {
                        isFindingWinner = true
                        onFind.invoke()
                    }
                } else {
                    context.toast(context.getString(R.string.message_touch_point))
                }
            }
        }
        return true
    }

    fun findGroup() {
        canChangeWinner = false
        setWillNotDraw(false)
    }

    fun setEnableView(isEnable: Boolean = true) {
        this.isEnabled = isEnable
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (listView.isNotEmpty()) {
            try {
                runBlocking {
                    listView.asFlow().div(listView, countInGroup).flatMapMerge { list ->
                        flow { emit(Pair(list, randomColor())) }
                    }.collect { pair ->
                        drawLine(pair.first, pair.second, canvas)
                        reset(7000)
                        isResetting = true
                    }
                }
            } catch (_: Exception) {
                reset(0)
            }
        }
    }

    private fun drawLine(list: List<PointHomoGraftView>, color: String, canvas: Canvas) {
        paint.color = Color.parseColor(color)
        val tmp = list.sortedBy { it.Ox }
        if (tmp.size >= 2) {
            tmp.forEachIndexed { i, point ->
                point.setColor(color)
                if (i < tmp.size - 1) canvas.drawLine(
                    point.Ox,
                    point.Oy,
                    tmp[i + 1].Ox,
                    tmp[i + 1].Oy,
                    paint
                )
                else canvas.drawLine(
                    point.Ox,
                    point.Oy,
                    tmp[0].Ox,
                    tmp[0].Oy,
                    paint
                )
            }
        }
    }

    private fun addViewPoint(x: Float, y: Float) {
        DeviceUtil.playVibrator(context)
        val viewPoint = PointHomoGraftView(context).apply {
            isClickable = true
            isFocusable = true
            setIdPoint(listView.size + 1)
            Ox = x
            Oy = y
        }
        val params = LayoutParams(80.toDp, 80.toDp).apply {
            leftMargin = x.toInt() - Constants.SIZE_POINT_CHOOSER.toDp / 2
            topMargin = y.toInt() - Constants.SIZE_POINT_CHOOSER.toDp / 2
        }
        this.addView(viewPoint, params)
        listView.add(viewPoint)
    }

    fun reset(delay: Int = 3000) {
        if (isResetting) return
        postDelayed({
            this.removeAllViews()
            listView.clear()

            isFindingWinner = false
            this.isEnabled = true

            isResetting = false
            canChangeWinner = true

            setWillNotDraw(true)

            onReset.invoke()
        }, delay.toLong())
    }
}