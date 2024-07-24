package com.spin.wheel.chooser.wheeloffortune.share_pref

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.spin.wheel.chooser.wheeloffortune.entity.LanguageModel
import com.spin.wheel.chooser.wheeloffortune.extensions.setBoolean
import com.spin.wheel.chooser.wheeloffortune.extensions.setLong
import com.spin.wheel.chooser.wheeloffortune.extensions.setString

class MySharePrefImpl(context: Context): MySharePref {

    private var sharePref: SharedPreferences? = null

    init {
        sharePref = context.getSharedPreferences(SHARE_PREF, Context.MODE_PRIVATE)
    }

    companion object {
        const val SHARE_PREF = "SHARE_PREF"
        const val SHARE_PREF_OPEN_FIRST_APP = "SHARE_PREF_OPEN_FIRST_APP"
        const val SHARE_PREF_OPEN_GUIDE = "SHARE_PREF_OPEN_GUIDE"
        const val SHARE_PREF_CURRENT_LANGUAGE = "SHARE_PREF_CURRENT_LANGUAGE"
        const val SHARE_PREF_CHANGE_LANGUAGE = "SHARE_PREF_CHANGE_LANGUAGE"
        const val SHARE_PREF_GRANTED_PERMISSION = "SHARE_PREF_GRANTED_PERMISSION"
        const val SHARE_PREF_INSTRUCTION = "SHARE_PREF_INSTRUCTION"
        const val SHARE_PREF_RATED = "SHARE_PREF_RATED"

        const val CB_FETCH_INTERVAL = "cb_fetch_interval"
        const val IS_SWITCH_BANNER_COLLAPSE = "switch_bannerCollapse_bannerDefault"
        const val IS_LOAD_BANNER_COLLAPSE_HOME = "is_load_banner_collapse_home"
        const val IS_LOAD_BANNER = "is_load_banner"
        const val IS_LOAD_NATIVE_LANGUAGE = "is_load_native_language"
        const val IS_LOAD_NATIVE_LANGUAGE_SETTING = "is_load_native_language_setting"
        const val IS_LOAD_NATIVE_INTRO_1 = "is_load_native_intro1"
        const val IS_LOAD_NATIVE_INTRO_2 = "is_load_native_intro2"
        const val IS_LOAD_NATIVE_INTRO_3 = "is_load_native_intro3"
        const val IS_LOAD_NATIVE_HOME = "is_load_native_home"
        const val IS_LOAD_INTER_GUIDE = "is_load_inter_guide"
        const val IS_LOAD_INTER_BACK = "is_load_inter_back"
        const val IS_LOAD_INTER_ROULETTE = "is_load_inter_roulette"
    }

    override var noOpenAppWhenInstall: Boolean
        get() = sharePref?.getBoolean(SHARE_PREF_OPEN_FIRST_APP, true) ?: true
        set(value) {
            sharePref?.setBoolean(SHARE_PREF_OPEN_FIRST_APP, value)
        }
    override var openGuide: Boolean
        get() = sharePref?.getBoolean(SHARE_PREF_OPEN_GUIDE, false) ?: false
        set(value) {
            sharePref?.setBoolean(SHARE_PREF_OPEN_GUIDE, value)
        }
    override var currentLanguage: LanguageModel?
        get() = try {
            Gson().fromJson(
                sharePref?.getString(SHARE_PREF_CURRENT_LANGUAGE, null),
                LanguageModel::class.java
            )
        } catch (e: Exception) {
            null
        }
        set(value) {
            sharePref?.setString(SHARE_PREF_CURRENT_LANGUAGE, Gson().toJson(value))
        }
    override var changeLanguage: Boolean
        get() = sharePref?.getBoolean(SHARE_PREF_CHANGE_LANGUAGE, false) ?: false
        set(value) {
            sharePref?.setBoolean(SHARE_PREF_CHANGE_LANGUAGE, value)
        }
    override var isGrantedPermission: Boolean
        get() = sharePref?.getBoolean(SHARE_PREF_GRANTED_PERMISSION, false) ?: false
        set(value) {
            sharePref?.setBoolean(SHARE_PREF_GRANTED_PERMISSION, value)
        }
    override var isRated: Boolean
        get() = sharePref?.getBoolean(SHARE_PREF_RATED, false) ?: false
        set(value) {
            sharePref?.setBoolean(SHARE_PREF_RATED, value)
        }
    override var isInstructed: Boolean
        get() = sharePref?.getBoolean(SHARE_PREF_INSTRUCTION, false) ?: false
        set(value) {
            sharePref?.setBoolean(SHARE_PREF_INSTRUCTION, value)
        }
    override var isSwitchBannerCollapse: Boolean
        get() = sharePref?.getBoolean(IS_SWITCH_BANNER_COLLAPSE, true) ?: true
        set(value) {
            sharePref?.setBoolean(IS_SWITCH_BANNER_COLLAPSE, value)
        }
    override var isLoadBannerCollapseHome: Boolean
        get() = sharePref?.getBoolean(IS_LOAD_BANNER_COLLAPSE_HOME, true) ?: true
        set(value) {
            sharePref?.setBoolean(IS_LOAD_BANNER_COLLAPSE_HOME, value)
        }
    override var isLoadBanner: Boolean
        get() = sharePref?.getBoolean(IS_LOAD_BANNER, false) ?: false
        set(value) {
            sharePref?.setBoolean(IS_LOAD_BANNER, value)
        }
    override var isLoadNativeLanguage: Boolean
        get() = sharePref?.getBoolean(IS_LOAD_NATIVE_LANGUAGE, true) ?: true
        set(value) {
            sharePref?.setBoolean(IS_LOAD_NATIVE_LANGUAGE, value)
        }
    override var isLoadNativeLanguageSetting: Boolean
        get() = sharePref?.getBoolean(IS_LOAD_NATIVE_LANGUAGE_SETTING, true) ?: false
        set(value) {
            sharePref?.setBoolean(IS_LOAD_NATIVE_LANGUAGE_SETTING, value)
        }
    override var isLoadNativeIntro1: Boolean
        get() = sharePref?.getBoolean(IS_LOAD_NATIVE_INTRO_1, true) ?: true
        set(value) {
            sharePref?.getBoolean(IS_LOAD_NATIVE_INTRO_1, value)
        }
    override var isLoadNativeIntro2: Boolean
        get() = sharePref?.getBoolean(IS_LOAD_NATIVE_INTRO_2, true) ?: true
        set(value) {
            sharePref?.setBoolean(IS_LOAD_NATIVE_INTRO_2, value)
        }
    override var isLoadNativeIntro3: Boolean
        get() = sharePref?.getBoolean(IS_LOAD_NATIVE_INTRO_3, true) ?: true
        set(value) {
            sharePref?.setBoolean(IS_LOAD_NATIVE_INTRO_3, value)
        }
    override var isLoadNativeHome: Boolean
        get() = sharePref?.getBoolean(IS_LOAD_NATIVE_HOME, true) ?: true
        set(value) {
            sharePref?.setBoolean(IS_LOAD_NATIVE_HOME, value)
        }
    override var isLoadInterGuide: Boolean
        get() = sharePref?.getBoolean(IS_LOAD_INTER_GUIDE, true) ?: true
        set(value) {
            sharePref?.setBoolean(IS_LOAD_INTER_GUIDE, value)
        }
    override var isLoadInterBack: Boolean
        get() = sharePref?.getBoolean(IS_LOAD_INTER_BACK, true) ?: true
        set(value) {
            sharePref?.setBoolean(IS_LOAD_INTER_BACK, value)
        }
    override var isLoadInterRoulette: Boolean
        get() = sharePref?.getBoolean(IS_LOAD_INTER_ROULETTE, true) ?: true
        set(value) {
            sharePref?.setBoolean(IS_LOAD_INTER_ROULETTE, value)
        }


    override fun isVisible(key: String): Boolean {
        return sharePref?.getBoolean(key, true) ?: true
    }

    override fun setVisible(key: String, value: Boolean) {
        sharePref?.setBoolean(key, value)
    }

    override var cbFetchInterval: Long
        get() = sharePref?.getLong(CB_FETCH_INTERVAL, 15L) ?: 15L
        set(value) {
            sharePref?.setLong(CB_FETCH_INTERVAL, value)
        }
}