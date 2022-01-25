package com.eightpeak.salakafarm.database.notifications

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import coil.api.load
import com.eightpeak.salakafarm.App
import com.eightpeak.salakafarm.R
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

        notificationsViewModel.fetchRegisterLogger.observe(viewLifecycleOwner, Observer { user ->
            if(user.isNotEmpty()){
                binding.notificationList.removeAllViews()
                Log.i(TAG, "onCreateView: "+user.size)
                for (i in user.indices) {
                    val itemView: View =
                        LayoutInflater.from(requireContext())
                            .inflate(R.layout.notification_items, binding.notificationList, false)
                    val notiTitle = itemView.findViewById<TextView>(R.id.notification_title)
                    val  notiImage= itemView.findViewById<ImageView>(R.id.notification_image)
                    val notiMessage = itemView.findViewById<TextView>(R.id.notification_description)
                    val notiDate = itemView.findViewById<TextView>(R.id.notification_date)
                    val notiLayout = itemView.findViewById<CardView>(R.id.notification_layout)

                    notiTitle.setText(user[i].notification_title)
                    notiMessage.setText(user[i].notification_description)
                    notiImage.load(user[i].notification_image)
                    notiDate.setText(user[i].notification_date)

                    notiLayout.setOnClickListener {
                        Log.i(TAG, "onCreateView: "+user[i].id)
                        notificationsViewModel.deleteNotificationDetails(user[i].id.toString())

                    }

                    binding.notificationList.addView(itemView)
                }
                }else{

            }
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}