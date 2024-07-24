package com.spin.wheel.chooser.wheeloffortune.extensions

import android.content.res.Resources
import kotlin.math.roundToInt

val Int.toDp: Int get() = (this * Resources.getSystem().displayMetrics.density).roundToInt()