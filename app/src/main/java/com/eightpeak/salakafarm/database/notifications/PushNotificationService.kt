package com.eightpeak.salakafarm.database.notifications
import android.app.Notification
import android.app.NotificationChannel
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.app.NotificationManager


import android.app.PendingIntent
import android.content.Context

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat

import org.json.JSONException

import org.json.JSONObject

import android.os.Build
import androidx.lifecycle.ViewModelProvider
import com.eightpeak.salakafarm.App
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.database.NotificationDetails
import com.eightpeak.salakafarm.repository.NotificationRepository
import com.eightpeak.salakafarm.subscription.displaysubscription.DisplaySubscriptionDetails
import com.eightpeak.salakafarm.views.home.HomeActivity
import com.eightpeak.salakafarm.views.order.orderview.orderhistory.OrderHistory


const val channel_id="notification_channel"
const val channel_name="com.eightpeak.salakafarm"
open class PushNotificationService : FirebaseMessagingService() {
    private val NOTIF_ID = 1234
    private var TAG = "PushNotificationService"
    private lateinit var notificationViewModel: NotificationViewModel
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
        val title = remoteMessage.data["title"] //get title
        val message = remoteMessage.data["body"] //get message
        val click_action = remoteMessage.data["type"]//get click_action
        val image =remoteMessage!!.notification?.imageUrl
        Log.d(TAG, "Message Notification Title: $title")
        Log.d(TAG, "Message Notification Body: $message")
        Log.i(TAG, "onMessageReceived: "+click_action)


        val loginPreferences:SharedPreferences? = getSharedPreferences("notificationPrefs", MODE_PRIVATE)
        var loginPrefsEditor: SharedPreferences.Editor? = null
        loginPrefsEditor = loginPreferences?.edit()
        loginPrefsEditor?.putString("title",   title)
        loginPrefsEditor?.putString("message",  message)
        loginPrefsEditor?.putString("image",  image.toString())
        loginPrefsEditor?.apply()




        sendNotification(title,message,image,click_action)
    }

    private fun sendNotification(
        title: String?,
        message: String?,
        image: Uri?,
        click_action: String?
    ) {
        var notifyManager: NotificationManager? = null
        val NOTIFY_ID = 1002

        val name = "KotlinApplication"
        val id = "notification_channel"
        val description = "salaka_farm"

        val builder: NotificationCompat.Builder

        if (notifyManager == null) {
            notifyManager = this!!.getSystemService(Context.NOTIFICATION_SERVICE)
                    as NotificationManager
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            var mChannel = notifyManager.getNotificationChannel(id)
            if (mChannel == null) {
                mChannel = NotificationChannel(id, name, importance)
                mChannel.description = description
                mChannel.enableVibration(true)
                mChannel.lightColor = Color.GREEN
                mChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
                notifyManager.createNotificationChannel(mChannel)
            }
        }

        builder = NotificationCompat.Builder(this, id)
        var intent: Intent?
        Log.i(TAG, "sendNotification: $image")

        if(click_action=="order_status_done"){
             intent = Intent(this, OrderHistory::class.java)
             intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
         }else if(click_action=="sub_expiration_alert"&&click_action=="sub_delivered"){
             intent = Intent(this, DisplaySubscriptionDetails::class.java)
             intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
         }else if(click_action=="notification"){
             intent = Intent(this, HomeActivity::class.java)
             intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
         }else{

            intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP

        }
        intent.putExtra("title", title)
        intent.putExtra("message", message)
        intent.putExtra("image", image)

        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
        val defaultSoundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        builder.setContentTitle(title)  // required
            .setSmallIcon(R.drawable.small_logo) // required
            .setContentText(message)  // required
            .setDefaults(Notification.DEFAULT_ALL)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setTicker("Notification")
            .setSound(defaultSoundUri)
            .setVibrate(longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400))

        val dismissIntent = Intent(this, DisplaySubscriptionDetails::class.java)
        dismissIntent.action = "DISMISS"
        dismissIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingDismissIntent = PendingIntent.getActivity(this, 0, dismissIntent,
            PendingIntent.FLAG_UPDATE_CURRENT)
        val dismissAction = NotificationCompat.Action(R.drawable.small_logo,
            "DISMISS", pendingDismissIntent)
        builder.addAction(dismissAction)

        val notification = builder.build()
        notifyManager.notify(NOTIFY_ID, notification)
    }

//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun createNotificationChannel(channelId: String, channelName: String): String{
//        val chan = NotificationChannel(channelId,
//            channelName, NotificationManager.IMPORTANCE_NONE)
//        chan.lightColor = Color.BLUE
//        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
//        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        service.createNotificationChannel(chan)
//        return channelId
//    }
//
//   private fun sendNotification(title: String?, messageBody: String?, click_action: String?) {
//       var intent = Intent(this, SubscriptionActivity::class.java)
//       intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        if (click_action == "SOMEACTIVITY") {
//            intent = Intent(this, CartActivity::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        } else if (click_action == "MAINACTIVITY") {
//            intent = Intent(this, HomeActivity::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        } else {
//            intent = Intent(this, SubscriptionActivity::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        }
//        val pendingIntent = PendingIntent.getActivity(
//            this, 0 /* Request code */, intent,
//            PendingIntent.FLAG_ONE_SHOT
//        )
//        val defaultSoundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
//        val notificationBuilder: NotificationCompat.Builder =NotificationCompat.Builder(this, channel_id)
//            .setSmallIcon(R.drawable.payment_icon)
//            .setContentTitle(title)
//            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
//            .setContentText(messageBody)
//            .setAutoCancel(true)
//            .setVibrate(longArrayOf(1000,1000,1000,1000))
//            .setSound(defaultSoundUri)
//            .setContentIntent(pendingIntent)
////       notificationBuilder=notificationBuilder.setContent(getRemoteView(title,messageBody))
//        val notificationManager =
//            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
//    }


}