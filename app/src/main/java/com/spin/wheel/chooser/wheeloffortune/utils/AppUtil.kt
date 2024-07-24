package com.spin.wheel.chooser.wheeloffortune.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.util.Log
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.spin.wheel.chooser.wheeloffortune.R
import com.spin.wheel.chooser.wheeloffortune.custom.dialog.UpdateAppDialog
import com.spin.wheel.chooser.wheeloffortune.entity.LanguageModel
import java.util.Locale

object AppUtil {

    private const val GMAIL = ""
    private const val POLICY = "https://doubleeightstudio.netlify.app/policy"
    fun checkUpdate(context: Context) {
        //check update is UPDATE_AVAILABLE
        AppUpdateManagerFactory.create(context).appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)
            ) {
                val updateDialog = UpdateAppDialog(context)
                UpdateAppDialog(context).onClick = {
                    try {
                        context.startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("market://details?id=${context.packageName}")
                            )
                        )
                    } catch (anfe: ActivityNotFoundException) {
                        context.startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://play.google.com/store/apps/details?id=${context.packageName}")
                            )
                        )
                    }
                }
                updateDialog.showDialog()
            } else if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                //CHECK THIS if AppUpdateType.FLEXIBLE, otherwise you can skip
            } else Log.d("vmh", "checkUpdate")
        }
    }

    fun rateApp(context: Context) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data =
                Uri.parse("https://play.google.com/store/apps/details?id=" + context.packageName)
        }
        context.startActivity(intent)
    }

    fun shareApp(context: Context) {
        try {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, context.resources.getString(R.string.app_name))
            }
            var shareMessage = "${context.getString(R.string.app_name)}: "
            shareMessage =
                shareMessage + "https://play.google.com/store/apps/details?id=" + context.packageName
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
            context.startActivity(
                Intent.createChooser(
                    shareIntent,
                    context.getString(R.string.app_name)
                )
            )
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    fun openPolicy(context: Context) {
        try {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(POLICY.replace("HTTPS", "https"))
            context.startActivity(i)
        } catch (e: ActivityNotFoundException) {
            Log.d("vmh", "openPolicy: error")
        }
    }

    fun setLanguage(context: Context, language: LanguageModel?) {
        if (language != null) {
            changeLanguage(context, language)
        } else {
            val configuration = Configuration()
            val locale = Locale.getDefault()
            Locale.setDefault(locale)
            configuration.locale = locale
            context.resources.updateConfiguration(configuration, context.resources.displayMetrics)
        }
    }

    private fun changeLanguage(context: Context, language: LanguageModel) {
        val myLocale = language.locale
        Locale.setDefault(myLocale)
        val configuration = Configuration()
        configuration.locale = myLocale
        context.resources.updateConfiguration(configuration, context.resources.displayMetrics)
    }

    fun imageTitle(screenName: String, locale: String): String {
        return "file:///android_asset/$screenName/$locale.webp"
    }

}