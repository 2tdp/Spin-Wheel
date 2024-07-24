package com.spin.wheel.chooser.wheeloffortune.entity

data class WheelModel(var id: Int, var name: String = "", var isDelete: Boolean = true) {

    companion object {
        fun listDefault() = arrayListOf(
            WheelModel(0, "Jisso", false),
            WheelModel(1, "Lisa", false),
        )
    }
}