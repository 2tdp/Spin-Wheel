package com.spin.wheel.chooser.wheeloffortune.extensions

import android.content.SharedPreferences

fun SharedPreferences.setInt(key: String, value: Int) = this.edit().putInt(key, value).apply()

fun SharedPreferences.getInt(key: String, defaultValue: Int = 0) =  getInt(key, defaultValue)

fun SharedPreferences.setBoolean(key: String, value: Boolean) =  edit().putBoolean(key, value).apply()

fun SharedPreferences.getBoolean(key: String, defaultValue: Boolean = false) =  getBoolean(key, defaultValue)

fun SharedPreferences.setString(key: String, value: String) =  edit().putString(key, value).apply()

fun SharedPreferences.getString(key: String, defaultValue: String = "") =  getString(key, defaultValue)!!

fun SharedPreferences.remove(key: String) =  edit().remove(key).apply()

fun SharedPreferences.clearAll() =  edit().clear().apply()
fun SharedPreferences.setLong(key: String, value: Long) =  edit().putLong(key, value).apply()
