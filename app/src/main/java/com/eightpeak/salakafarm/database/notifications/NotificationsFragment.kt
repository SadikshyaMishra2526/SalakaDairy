package com.eightpeak.salakafarm.database.notifications

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.eightpeak.salakafarm.App
import com.eightpeak.salakafarm.database.NotificationDetails
import com.eightpeak.salakafarm.databinding.FragmentNotificationsBinding
import com.eightpeak.salakafarm.views.home.HomeActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class NotificationsFragment : Fragment() {

    private lateinit var notificationsViewModel: NotificationViewModel
    private var _binding: FragmentNotificationsBinding? = null
    private var TAG: String?="NotificationsFragment"
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel = ViewModelProvider(this).get(NotificationViewModel::class.java)
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


//    open class PushNotificationService : FirebaseMessagingService() {
//
//        private lateinit var notificationViewModel: NotificationViewModel
//
//        override fun onMessageReceived(remoteMessage: RemoteMessage) {
////        logViewModel = ViewModelProvider(App.getContext()).get(NotificationsViewModel::class.java)
//
//            var data = remoteMessage.data
//            val title = remoteMessage.notification?.title
//            val message = remoteMessage.notification!!.body
//            val imageUrl = data.get("image")
//
////        val action = data.get("action") as String
//            Log.i("TAG", "onMessageReceived: title : $title")
//            Log.i("TAG", "onMessageReceived: message : $message")
//            Log.i("TAG", "onMessageReceived: imageUrl : $imageUrl")
//
//            val loggerRegistration=  NotificationDetails(0,"title","message","imageUrl","ddd")
//
//
//
//            notificationViewModel.addDailyLogger(loggerRegistration)
//
//            val intent = Intent(App.getContext(), HomeActivity::class.java)
//            intent.putExtra("EXTRA_DATA", title)
//        }
//
//        override fun onNewToken(token: String) {
//            super.onNewToken(token)
//            // whatever you want
//        }
//        protected  fun onHandleIntent(@Nullable intent: Intent) {
//            sendDataToActivity()
//        }
//
//        private fun sendDataToActivity()
//        {
//            var  sendLevel = Intent();
//            sendLevel.action = "GET_SIGNAL_STRENGTH";
//            sendLevel.putExtra( "LEVEL_DATA","Strength_Value");
//            sendBroadcast(sendLevel);
//
//        }
//    }

}