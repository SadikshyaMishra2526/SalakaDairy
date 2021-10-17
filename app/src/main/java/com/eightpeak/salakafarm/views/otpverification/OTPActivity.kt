package com.eightpeak.salakafarm.views.otpverification

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.databinding.ActivityOtpActivityBinding
import com.eightpeak.salakafarm.databinding.ActivityRegisterBinding

class OTPActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOtpActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signUp.setOnClickListener {
            setContentView(R.layout.otp_verification_view);

        }

    }
}