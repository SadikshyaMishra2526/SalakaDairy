package com.eightpeak.salakafarm.views.home.ui.user_profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.database.UserPrefManager
import com.eightpeak.salakafarm.databinding.ActivityHomeBinding
import com.eightpeak.salakafarm.databinding.ActivityUserProfileBinding
import android.view.Window

import androidx.core.content.ContextCompat

import android.view.WindowManager


class UserProfile : AppCompatActivity() {

    private lateinit var userPrefManager: UserPrefManager

    private lateinit var binding: ActivityUserProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getStatusColor()
        userPrefManager = UserPrefManager(this)
         binding.userName.text=userPrefManager.firstName+" "+userPrefManager.lastName
        binding.userEmail.text=userPrefManager.email
        binding.userAddress1.text=userPrefManager.userAddress1
        binding.userAddress2.text=userPrefManager.userAddress2
        binding.userCountry.text=userPrefManager.userCountry
        binding.userPhone.text=userPrefManager.contactNo
        binding.userLogout.setOnClickListener {
            userPrefManager.clearData()
        }
    }

    private fun getStatusColor() {
        val window: Window = window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.sub_color)
    }
}