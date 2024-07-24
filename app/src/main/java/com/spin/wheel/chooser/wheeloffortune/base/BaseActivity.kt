package com.spin.wheel.chooser.wheeloffortune.base

import android.app.ActivityOptions
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.spin.wheel.chooser.wheeloffortune.R
import com.spin.wheel.chooser.wheeloffortune.databinding.DialogLoadingBinding
import com.spin.wheel.chooser.wheeloffortune.databinding.DialogUpdateAppBinding
import com.spin.wheel.chooser.wheeloffortune.extensions.changeLanguage
import com.spin.wheel.chooser.wheeloffortune.extensions.createBackground
import com.spin.wheel.chooser.wheeloffortune.extensions.setStatusBarTransparent
import com.spin.wheel.chooser.wheeloffortune.extensions.setUpDialog
import com.spin.wheel.chooser.wheeloffortune.extensions.toast
import com.spin.wheel.chooser.wheeloffortune.share_pref.MySharePref
import com.spin.wheel.chooser.wheeloffortune.share_pref.MySharePrefImpl
import com.spin.wheel.chooser.wheeloffortune.utils.Constants
import com.spin.wheel.chooser.wheeloffortune.utils.Constants.TAG
import com.spin.wheel.chooser.wheeloffortune.utils.DeviceUtil

abstract class BaseActivity<B : ViewDataBinding>(@LayoutRes private val resId: Int) : AppCompatActivity() {

    protected lateinit var binding: B
    protected val sharePref: MySharePref by lazy { MySharePrefImpl(this) }

    private var loadingDialog: AlertDialog? = null
    private var networkDialog: AlertDialog? = null

    var w = 0F
    private lateinit var decorView: View
    private var finish = 0
    private var mIsShowLoading = false
    private var isShowNetwork = false

    private var count = 0
    private var timeClickOld = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        decorView = window.decorView
        if (!isHideNavigation())
            setStatusBarTransparent(this@BaseActivity, true, getColorState()[0], getColorState()[1])
        else {
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
            window.navigationBarColor = Color.parseColor("#01ffffff")
            window.statusBarColor = Color.TRANSPARENT
            decorView.systemUiVisibility = hideSystemBars()
        }

        binding = DataBindingUtil.setContentView(this, resId)
        binding.lifecycleOwner = this
//        AppUtil.setLanguage(this, sharePref.currentLanguage)

        w = resources.displayMetrics.widthPixels / 100F
        // init
        initView()
        initData()
        initListener()
    }

    override fun onResume() {
        super.onResume()

        // get remote config
        val remoteConfig = Firebase.remoteConfig
        remoteConfig.setConfigSettingsAsync(remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
        })
        var remoteUpdate = false
        remoteConfig.fetchAndActivate().addOnCompleteListener(this) { task ->
            if (task.isSuccessful) remoteUpdate = remoteConfig.getBoolean("force_update")
        }

        if (remoteUpdate) checkUpdate()
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(changeLanguage(newBase, MySharePrefImpl(newBase).currentLanguage))
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)

        if (isHideNavigation()) {
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
            window.navigationBarColor = Color.parseColor("#01ffffff")
            window.statusBarColor = Color.TRANSPARENT
            decorView.systemUiVisibility = hideSystemBars()
        }
    }

    private fun hideSystemBars(): Int {
        return (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }

    fun startIntent(nameActivity: String, isFinish: Boolean) {
        val intent = Intent().apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            component = ComponentName(this@BaseActivity, nameActivity)
        }
        startActivity(
            intent,
            ActivityOptions.makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_out_left)
                .toBundle()
        )
        if (isFinish) this.finish()
    }

    fun startIntent(intent: Intent, isFinish: Boolean) {
        startActivity(
            intent,
            ActivityOptions.makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_out_left)
                .toBundle()
        )
        if (isFinish) this.finish()
    }

    fun startIntentForResult(startForResult: ActivityResultLauncher<Intent>, nameActivity: String, isFinish: Boolean) {
        startForResult.launch(
            Intent().apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                component = ComponentName(this@BaseActivity, nameActivity)
            },
            ActivityOptionsCompat.makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_out_left)
        )
        if (isFinish) this.finish()
    }

    fun startIntentForResult(startForResult: ActivityResultLauncher<Intent>, intent: Intent, isFinish: Boolean) {
        startForResult.launch(
            intent,
            ActivityOptionsCompat.makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_out_left)
        )
        if (isFinish) this.finish()
    }

    fun checkUpdate() {
        //check update is UPDATE_AVAILABLE
        AppUpdateManagerFactory.create(this).appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)
            ) {
                showDialogUpdate()
            } else if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                //CHECK THIS if AppUpdateType.FLEXIBLE, otherwise you can skip
            } else Log.d(TAG, "checkUpdate: ehe")
        }
    }

    private fun showDialogUpdate() {
        val bindingDialog =
            DialogUpdateAppBinding.inflate(LayoutInflater.from(this@BaseActivity), null, false)
        bindingDialog.tvUpdate.createBackground(
            intArrayOf(ContextCompat.getColor(this@BaseActivity, R.color.color_FCD535)),
            2.5f * w, -1, -1
        )

        val dialog = AlertDialog.Builder(this@BaseActivity, R.style.SheetDialog).create()
        dialog.setUpDialog(bindingDialog.root, false)

        bindingDialog.root.layoutParams.width = (69.96f * w).toInt()
        bindingDialog.root.layoutParams.height = (84.889f * w).toInt()

        bindingDialog.tvUpdate.setOnClickListener {
            try {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=$packageName")
                    )
                )
            } catch (anfe: android.content.ActivityNotFoundException) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                    )
                )
            }
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val v = currentFocus
        if (v != null && (ev.action == MotionEvent.ACTION_UP || ev.action == MotionEvent.ACTION_MOVE) &&
            v is EditText &&
            !v.javaClass.name.startsWith("android.webkit.")
        ) {
            val sourceCoordinates = IntArray(2)
            v.getLocationOnScreen(sourceCoordinates)
            val x = ev.rawX + v.getLeft() - sourceCoordinates[0]
            val y = ev.rawY + v.getTop() - sourceCoordinates[1]
            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom()) {
                DeviceUtil.hideSoftKeyboard(this)
            }
        }

        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                count++
                val timeClick = System.currentTimeMillis()
                val interval = timeClick - timeClickOld
                Log.d(TAG, "tt: $count, name: ${nameActivity()}, time: ${timeClick}, scrW: ${BaseApplication.screenWidth}, scrH: ${BaseApplication.screenHeight}, dx: ${ev.x}, dy: ${ev.y}, interval: $interval")
                timeClickOld = timeClick
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    fun haveNetworkConnection(): Boolean {
        var haveConnectedWifi = false
        var haveConnectedMobile = false
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.allNetworkInfo
        for (ni in netInfo) {
            if (ni.typeName.equals("WIFI", ignoreCase = true))
                if (ni.isConnected) haveConnectedWifi = true
            if (ni.typeName.equals("MOBILE", ignoreCase = true))
                if (ni.isConnected) haveConnectedMobile = true
        }
        return haveConnectedWifi || haveConnectedMobile
    }

    protected open fun isHideNavigation(): Boolean = false

    protected open fun nameActivity(): String = BaseActivity::class.java.name

    protected open fun getColorState(): IntArray =
        intArrayOf(Color.TRANSPARENT, Color.parseColor("#272727"))
    protected abstract fun initView()
    protected abstract fun initData()
    protected abstract fun initListener()

    fun showLoading(cancelable: Boolean) {
        Handler(Looper.getMainLooper()).post {
            if (loadingDialog != null && mIsShowLoading) {
                loadingDialog?.cancel()
                loadingDialog = null
            }
            initDialog(cancelable)
        }
    }

    fun hideLoading() {
        Handler(Looper.getMainLooper()).post {
            if (loadingDialog != null && mIsShowLoading && !isFinishing) {
                loadingDialog?.cancel()
                loadingDialog = null
            }
            mIsShowLoading = false
        }
    }

    private fun initDialog(isCancel: Boolean) {
        val bindingDialog = DialogLoadingBinding.inflate(LayoutInflater.from(this@BaseActivity))
        bindingDialog.root.createBackground(intArrayOf(Color.WHITE), 3.5f * w, -1, -1)

        loadingDialog = AlertDialog.Builder(this@BaseActivity, R.style.SheetDialog).create()
        loadingDialog?.setUpDialog(bindingDialog.root, isCancel)

        bindingDialog.root.layoutParams.width = (73.889f * w).toInt()
        bindingDialog.root.layoutParams.height = (34.556f * w).toInt()
        mIsShowLoading = true
    }

    protected open fun handleListObject(response: BaseListResponse<*>) {
        when (response.status) {
            Constants.Status.LOADING -> {
                showLoading(true)
                handleLoading()
            }

            Constants.Status.SUCCESS -> {
                hideLoading()
                getListObject(response.data)
            }

            Constants.Status.ERROR -> {
                hideLoading()
                handleError(response.message)
            }

            else -> hideLoading()
        }
    }

    open fun getListObject(data: List<*>?) {

    }

    protected open fun handleObject(response: BaseResponse<Any>) {
        when (response.status) {
            Constants.Status.LOADING -> {
                showLoading(true)
                handleLoading()
            }

            Constants.Status.SUCCESS -> {
                hideLoading()
                getObject(response.data)
            }

            Constants.Status.ERROR -> {
                hideLoading()
                handleError(response.message)
            }

            else -> hideLoading()
        }
    }

    open fun getObject(data: Any?) {

    }

    open fun handleLoading() {

    }
    open fun handleError(message: String?) {
        toast(message ?: "")
    }
}