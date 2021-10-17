package com.eightpeak.salakafarm.views.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.databinding.ActivityLoginBinding
import com.eightpeak.salakafarm.databinding.ActivityRegisterBinding
import com.eightpeak.salakafarm.repository.AppRepository
import com.eightpeak.salakafarm.viewmodel.LoginViewModel
import com.eightpeak.salakafarm.viewmodel.RegisterViewModel
import com.eightpeak.salakafarm.viewmodel.ViewModelProviderFactory
import com.eightpeak.salakafarm.views.otpverification.OTPActivity

class RegisterActivity : AppCompatActivity() {

    lateinit var registerViewModel: RegisterViewModel
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signUp.setOnClickListener {
            val mainActivity = Intent(this@RegisterActivity, OTPActivity::class.java)
            startActivity(mainActivity)
        }
        init()

    }
    private fun init() {
        val repository = AppRepository()
        val factory = ViewModelProviderFactory(application, repository)
        registerViewModel = ViewModelProvider(this, factory).get(RegisterViewModel::class.java)
    }

}