package com.spin.wheel.chooser.wheeloffortune.entity

import java.util.Locale

data class LanguageModel(
    val id: Int,
    var name: String = "",
    var icon: String = "",
    var locale: Locale = Locale.ENGLISH,
    var isSelected: Boolean = false
) {
    companion object {
        fun listLanguage(current: LanguageModel? = null): MutableList<LanguageModel> {
            val list = arrayListOf(
                LanguageModel(
                    0,
                    "English",
                    "file:///android_asset/icon_language/ic_english.webp",
                    Locale.ENGLISH,
                    false
                ),
                LanguageModel(
                    1,
                    "French",
                    "file:///android_asset/icon_language/ic_french.webp",
                    Locale.FRANCE,
                    false
                ),
                LanguageModel(
                    2,
                    "Hindi",
                    "file:///android_asset/icon_language/ic_hindi.webp",
                    Locale("hi", "IN"),
                    false
                ),
                LanguageModel(
                    3,
                    "Portuguese",
                    "file:///android_asset/icon_language/ic_portuguese.webp",
                    Locale("pt", "PT"),
                    false
                ),
                LanguageModel(
                    4,
                    "Spanish",
                    "file:///android_asset/icon_language/ic_spanish.webp",
                    Locale("es", "ES"),
                    false
                ),
            )
            if (current != null) {
                list.forEach { it.isSelected = it.id == current.id }
                list.sortByDescending { !it.isSelected }
            }
            return list
        }
    }
}