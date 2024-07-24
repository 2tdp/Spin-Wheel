package com.spin.wheel.chooser.wheeloffortune.share_pref

import com.spin.wheel.chooser.wheeloffortune.entity.LanguageModel

interface MySharePref {
    var noOpenAppWhenInstall: Boolean
    var currentLanguage: LanguageModel?
    var changeLanguage: Boolean
    var isGrantedPermission: Boolean
    var isRated: Boolean
    var openGuide: Boolean
    var isInstructed: Boolean

    var isSwitchBannerCollapse: Boolean
    var isLoadBannerCollapseHome: Boolean
    var isLoadBanner: Boolean
    var isLoadNativeLanguage: Boolean
    var isLoadNativeLanguageSetting: Boolean
    var isLoadNativeIntro1: Boolean
    var isLoadNativeIntro2: Boolean
    var isLoadNativeIntro3: Boolean
    var isLoadNativeHome: Boolean
    var isLoadInterGuide: Boolean
    var isLoadInterBack: Boolean
    var isLoadInterRoulette: Boolean

    fun isVisible(key: String): Boolean
    fun setVisible(key: String, value: Boolean)

    var cbFetchInterval: Long
}