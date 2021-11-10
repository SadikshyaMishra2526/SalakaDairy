package com.eightpeak.salakafarm.views.splash

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.pm.PackageInfoCompat
import com.eightpeak.salakafarm.App
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.database.UserPrefManager
import com.eightpeak.salakafarm.utils.subutils.Utils
import com.eightpeak.salakafarm.views.welcomeActivity.IntroActivity
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.tasks.Task
import java.util.*

class SplashActivity :AppCompatActivity() {
    private var appUpdateManager: AppUpdateManager? = null

    private val IMMEDIATE_APP_UPDATE_REQ_CODE = 124
    private val SPLASH_TIME_OUT = 1700

    lateinit var userPrefManager: UserPrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         userPrefManager= UserPrefManager(this)

        if (!Utils.hasInternetSplashConnection(this@SplashActivity)){
            setContentView(R.layout.no_internet_connection)
            var refresh=findViewById<Button>(R.id.refresh)
            refresh.setOnClickListener {
                startActivity(Intent(this@SplashActivity, SplashActivity::class.java))
                finish()
            }
        }else{
            setContentView(R.layout.activity_splash_screen)
        }

        setLocal(this)
        checkUpdate()
        goToHome()
    }
    fun setLocal(activity: Activity) {

        val locale = Locale( userPrefManager.language)
        Locale.setDefault(locale)
        val resources = activity.resources
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }


    private fun checkUpdate() {
        appUpdateManager = AppUpdateManagerFactory.create(applicationContext)
        val appUpdateInfoTask: Task<AppUpdateInfo>
        appUpdateInfoTask = appUpdateManager!!.appUpdateInfo
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() === UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)
            ) {
                startUpdateFlow(appUpdateInfo)
                Log.i("TAG", "checkUpdate: Update available 1")
            } else if (appUpdateInfo.updateAvailability() === UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                startUpdateFlow(appUpdateInfo)
                startActivity(Intent(this, IntroActivity::class.java))
                finish()
                Log.i("TAG", "checkUpdate: Update available 2")
            } else {
                goToHome()
                Log.i("TAG", "checkUpdate: No Update available")
            }
        }
    }

    private fun startUpdateFlow(appUpdateInfo: AppUpdateInfo) {
        try {
            appUpdateManager?.startUpdateFlowForResult(
                appUpdateInfo,
                AppUpdateType.FLEXIBLE,
                this, IMMEDIATE_APP_UPDATE_REQ_CODE
            )
        } catch (e: SendIntentException) {
            e.printStackTrace()
            Log.e("TAG", "startUpdateFlow: " + e.localizedMessage)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMMEDIATE_APP_UPDATE_REQ_CODE) {
            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(
                    applicationContext,
                    "Update canceled by user! Result Code: $resultCode", Toast.LENGTH_LONG
                ).show()
                startActivity(Intent(this, IntroActivity::class.java))
                finish()
            } else if (resultCode == RESULT_OK) {
                Toast.makeText(
                    applicationContext,
                    "Update success! Result Code: $resultCode", Toast.LENGTH_LONG
                ).show()
                startActivity(Intent(this, IntroActivity::class.java))
                finish()
            } else {
                Toast.makeText(
                    applicationContext,
                    "Update Failed! Result Code: $resultCode",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    fun goToHome() {
        val cm = applicationContext
            .getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting) {

            Handler().postDelayed(
                {
                    val intent = Intent(applicationContext, IntroActivity::class.java)
                    startActivity(intent)
                    finish()
//                    overridePendingTransition(R.anim.slide_in_right, R.anim.stay)
                },
               SPLASH_TIME_OUT.toLong()
            )
        } else {
        }

    }

    fun getAppVersionName(context: Context): String? {
        val manager = context.packageManager
        try {
            val info = manager.getPackageInfo(context.packageName, 0)
            val versionCode = PackageInfoCompat.getLongVersionCode(info).toInt()
            return "Version " + info.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return ""
    }
}