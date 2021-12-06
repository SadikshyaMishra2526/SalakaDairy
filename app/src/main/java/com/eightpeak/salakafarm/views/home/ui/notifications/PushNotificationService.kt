package com.eightpeak.salakafarm.views.home.ui.notifications

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessagingService

import com.google.firebase.messaging.RemoteMessage

import com.google.firebase.installations.FirebaseInstallations
import java.lang.Exception
import android.R.attr.data
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.eightpeak.salakafarm.App
import com.eightpeak.salakafarm.database.NotificationDao
import com.eightpeak.salakafarm.database.NotificationDetails
import android.app.NotificationManager

import android.R

import android.graphics.BitmapFactory

import android.graphics.Bitmap

import android.app.PendingIntent
import android.app.appsearch.AppSearchSchema
import android.content.Context
import android.os.Build
import android.window.SplashScreen
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.eightpeak.salakafarm.views.home.HomeActivity
import com.eightpeak.salakafarm.views.settings.SettingsFragmentArgs


open class PushNotificationService : FirebaseMessagingService() {

    private lateinit var logViewModel: NotificationsViewModel

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
//        logViewModel = ViewModelProvider(App.getContext()).get(NotificationsViewModel::class.java)

        var data = remoteMessage.data
        val title = remoteMessage.notification!!.title
        val message = remoteMessage.notification!!.body
        val imageUrl = data.get("image")
//        val action = data.get("action") as String
        Log.i("TAG", "onMessageReceived: title : $title")
        Log.i("TAG", "onMessageReceived: message : $message")
        Log.i("TAG", "onMessageReceived: imageUrl : $imageUrl")

        val intent = Intent(App.getContext(), HomeActivity::class.java)
        intent.putExtra("EXTRA_DATA", title)
//        val intent = Intent("notification-message")
//        intent.putExtra("title", title)
//        intent.putExtra("message", message)
//        intent.putExtra("imageUrl", imageUrl)
//        LocalBroadcastManager.getInstance(App.getContext()).sendBroadcast(intent)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // whatever you want
    }
    protected  fun onHandleIntent(@Nullable  intent:Intent) {

        sendDataToActivity()
    }

    private fun sendDataToActivity()
    {
      var  sendLevel = Intent();
        sendLevel.action = "GET_SIGNAL_STRENGTH";
        sendLevel.putExtra( "LEVEL_DATA","Strength_Value");
        sendBroadcast(sendLevel);

    }
}
