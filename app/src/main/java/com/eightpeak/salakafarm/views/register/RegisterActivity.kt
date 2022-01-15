package com.eightpeak.salakafarm.views.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.databinding.ActivityRegisterBinding
import com.eightpeak.salakafarm.repository.AppRepository
import com.eightpeak.salakafarm.serverconfig.RequestBodies
import com.eightpeak.salakafarm.subscription.confirmSubscription.ConfirmSubscription
import com.eightpeak.salakafarm.utils.subutils.Resource
import com.eightpeak.salakafarm.viewmodel.RegisterViewModel
import com.eightpeak.salakafarm.viewmodel.ViewModelProviderFactory
import com.eightpeak.salakafarm.views.home.HomeActivity
import com.google.android.material.snackbar.Snackbar
import com.eightpeak.salakafarm.utils.subutils.errorSnack
import com.eightpeak.salakafarm.views.otpverification.OTPActivity
import kotlinx.android.synthetic.main.activity_login.*

class RegisterActivity : AppCompatActivity() {

    lateinit var registerViewModel: RegisterViewModel
    private lateinit var binding: ActivityRegisterBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }


    private fun init() {
        val repository = AppRepository()
        val factory = ViewModelProviderFactory(application, repository)
        registerViewModel = ViewModelProvider(this, factory).get(RegisterViewModel::class.java)
        changeStatusBarColor()
    }

    private fun changeStatusBarColor() {
        val window: Window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = resources.getColor(R.color.main_color)
    }

    fun onRegisterClick(view: View) {
        val first_name = binding.etFirstName.text.toString()
        val last_name = binding.etLastName.text.toString()
        val email = binding.etEmailAddress.text.toString()
        val password = binding.etPassword.text.toString()
        val contact = binding.contactDetails.text.toString()
        val intent = Intent(this@RegisterActivity, OTPActivity::class.java)
            intent.putExtra("first_name", first_name)
            intent.putExtra("last_name", last_name)
            intent.putExtra("email", email)
            intent.putExtra("password", password)
            intent.putExtra("contact", contact)
            startActivity(intent)
        }

}