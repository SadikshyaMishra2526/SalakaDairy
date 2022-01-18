package com.eightpeak.salakafarm.database.notifications
import android.app.Notification
import android.app.NotificationChannel
import com.eightpeak.salakafarm.App
import androidx.annotation.Nullable
import com.eightpeak.salakafarm.views.home.HomeActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.app.NotificationManager


import android.media.RingtoneManager

import android.app.PendingIntent
import android.content.Context

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.eightpeak.salakafarm.subscription.SubscriptionActivity
import com.eightpeak.salakafarm.views.addtocart.CartActivity

import org.json.JSONException

import org.json.JSONObject
import android.os.Build.VERSION_CODES

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat.PRIORITY_MIN
import com.eightpeak.salakafarm.R


const val channel_id="notification_channel"
const val channel_name="com.eightpeak.salakafarm"
open class PushNotificationService : FirebaseMessagingService() {
    private val NOTIF_ID = 1234
    private var TAG = "PushNotificationService"
    private var mRemoteViews: RemoteViews? = null
    private lateinit var logViewModel: NotificationViewModel
        private  lateinit var mNotificationManager:NotificationManager
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: " + remoteMessage.data)
            try {
                val data = JSONObject(remoteMessage.data as Map<*, *>)
                val jsonMessage = data.getString("oid")
                Log.d(
                    TAG, """
     onMessageReceived: 
     Extra Information: $jsonMessage
     """.trimIndent()
                )
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }

        // Check if message contains a notification payload.
        if (remoteMessage.notification != null) {
            val title = remoteMessage.notification!!.title //get title
            val message = remoteMessage.notification!!.body //get message
            val click_action = remoteMessage.notification!!.clickAction //get click_action
            Log.d(TAG, "Message Notification Title: $title")
            Log.d(TAG, "Message Notification Body: $message")
//            Log.d(TAG, "Message Notification click_action: $click_action")
//            sendNotification(title, message, click_action)
            setUpNotification()
        }
    }

     open fun setUpNotification() {
         val channelId =
             if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                 createNotificationChannel("my_service", "My Background Service")
             } else {
                 // If earlier version channel ID is not used
                 // https://developer.android.com/reference/android/support/v4/app/NotificationCompat.Builder.html#NotificationCompat.Builder(android.content.Context)
                 ""
             }
        mNotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        // we need to build a basic notification first, then update it
        val intentNotif = Intent(this, HomeActivity::class.java)
        intentNotif.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendIntent =
            PendingIntent.getActivity(this, 0, intentNotif, PendingIntent.FLAG_UPDATE_CURRENT)

        // notification's layout
        mRemoteViews = RemoteViews(packageName, R.layout.notification_view)
        // notification's icon
        mRemoteViews!!.setImageViewResource(R.id.icon, R.mipmap.ic_launcher)
        // notification's title
        mRemoteViews!!.setTextViewText(R.id.message, resources.getString(R.string.app_name))
        // notification's content
//        mRemoteViews!!.setTextViewText(
//            R.id.notif_content,
//            resources.getString(R.string.content_text)
//        )
        val mBuilder = NotificationCompat.Builder(this)
//        val ticker: CharSequence = resources.getString(R.string.ticker_text)
         val ticker="Ticker"
        val apiVersion = Build.VERSION.SDK_INT
        if (apiVersion < VERSION_CODES.HONEYCOMB) {
           val mNotification = Notification(R.drawable.ic_user_icon, ticker, System.currentTimeMillis())
            mNotification.contentView = mRemoteViews
            mNotification.contentIntent = pendIntent
            mNotification.flags =
                mNotification.flags or Notification.FLAG_NO_CLEAR //Do not clear the notification
            mNotification.defaults = mNotification.defaults or Notification.DEFAULT_LIGHTS

            // starting service with notification in foreground mode
            startForeground(NOTIF_ID, mNotification)
        } else if (apiVersion >= VERSION_CODES.HONEYCOMB) {
            mBuilder.setSmallIcon(R.drawable.ic_user_icon)
                .setAutoCancel(false)
                .setOngoing(true)
                .setContentIntent(pendIntent)
                .setContent(mRemoteViews)
                .setTicker(ticker)

            // starting service with notification in foreground mode
            startForeground(NOTIF_ID, mBuilder.build())
        }

    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String): String{
        val chan = NotificationChannel(channelId,
            channelName, NotificationManager.IMPORTANCE_NONE)
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return channelId
    }

   private fun sendNotification(title: String?, messageBody: String?, click_action: String?) {
       var intent = Intent(this, SubscriptionActivity::class.java)
       intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        if (click_action == "SOMEACTIVITY") {
            intent = Intent(this, CartActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        } else if (click_action == "MAINACTIVITY") {
            intent = Intent(this, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        } else {
            intent = Intent(this, SubscriptionActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT
        )
        val defaultSoundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder: NotificationCompat.Builder =NotificationCompat.Builder(this, channel_id)
            .setSmallIcon(R.drawable.payment_icon)
            .setContentTitle(title)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000,1000,1000,1000))
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
//       notificationBuilder=notificationBuilder.setContent(getRemoteView(title,messageBody))
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }


}