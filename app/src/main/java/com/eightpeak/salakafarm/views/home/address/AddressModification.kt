package com.eightpeak.salakafarm.views.home.address

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.eightpeak.salakafarm.databinding.ActivityRegisterBinding
import com.eightpeak.salakafarm.databinding.AddAddressDetailsBinding
import com.eightpeak.salakafarm.serverconfig.network.TokenManager
import com.eightpeak.salakafarm.utils.Constants

class AddressModification:AppCompatActivity() {
    private lateinit var binding: AddAddressDetailsBinding
    private var tokenManager: TokenManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AddAddressDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }

}