package com.eightpeak.salakafarm.views.home.ui.user_profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.database.UserPrefManager
import com.eightpeak.salakafarm.databinding.ActivityHomeBinding
import com.eightpeak.salakafarm.databinding.ActivityUserProfileBinding

class UserProfile : AppCompatActivity() {

private lateinit var userPrefManager:UserPrefManager

    private lateinit var binding: ActivityUserProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPrefManager= UserPrefManager(this)

        binding.userLogout.setOnClickListener {
            userPrefManager.clearData()
        }
          }
}