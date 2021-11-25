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


class PushNotificationService : FirebaseMessagingService() {

    private lateinit var logViewModel: NotificationsViewModel

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
//        logViewModel = ViewModelProvider(App.getContext()).get(NotificationsViewModel::class.java)

        var    data = remoteMessage.data
        val title = remoteMessage.notification!!.title
        val message = remoteMessage.notification!!.body
        val imageUrl = data.get("image")
//        val action = data.get("action") as String
        Log.i("TAG", "onMessageReceived: title : $title")
        Log.i("TAG", "onMessageReceived: message : $message")
        Log.i("TAG", "onMessageReceived: imageUrl : $imageUrl")
        val logRecorded =
            title?.let {
                if (message != null) {
                    if (imageUrl != null) {
                        NotificationDetails(0,
                            it, message, imageUrl,"fffff")
                    }
                }
            }

//        val intent = Intent("notification-message")
//        intent.putExtra("title", title)
//        intent.putExtra("message", message)
//        intent.putExtra("imageUrl", imageUrl)
//        LocalBroadcastManager.getInstance(App.getContext()).sendBroadcast(intent)

//        Log.i("TAG", "onMessageReceived: action : $action")
//        if (imageUrl == null) {
//            sendNotification(title, message, action)
//        } else {
//            BigPictureNotification(this, title, message, imageUrl, action)
//        }

    }


    // either use below function to get the token or directly get from the shared preferences
//    fun getToken(context: Context?): String? {
////        return PrefUtils.getInstance(context).getStringValue(PrefKeys.FCM_TOKEN, "")
//    }



    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // whatever you want
    }
}
