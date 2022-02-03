package com.eightpeak.salakafarm.database.notifications

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.api.load
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.databinding.FragmentNotificationsBinding
import com.eightpeak.salakafarm.views.home.HomeActivity

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
                binding.noMessage.visibility=View.GONE
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

                    notiTitle.text = user[i].notification_title
                    notiMessage.text = user[i].notification_description
                    notiDate.setText(user[i].notification_date)

                    if(user[i].notification_image.contains("http")||user[i].notification_image.contains("https")){
                        notiImage.load(user[i].notification_image)
                    }else{
                        notiImage.load(R.drawable.logo)
                    }

                    notiLayout.setOnClickListener {
                        val builder = AlertDialog.Builder(requireContext())
                        builder.setTitle("Delete Notification")
                        builder.setMessage("Are you sure you want to delete?")
                        builder.setPositiveButton(R.string.delete,
                            DialogInterface.OnClickListener { _, _ ->
                                notificationsViewModel.deleteNotificationDetails(user[i].id.toString())
                            })
                        builder.setNegativeButton(R.string.cancel, null)

                        val dialog = builder.create()
                        dialog.show()

                    }

                    binding.notificationList.addView(itemView)
                }
                }else{
              binding.noMessage.visibility=View.VISIBLE
            }
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}