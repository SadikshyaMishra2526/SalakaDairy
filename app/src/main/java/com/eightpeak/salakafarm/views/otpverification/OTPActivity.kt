package com.eightpeak.salakafarm.views.otpverification

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.eightpeak.salakafarm.databinding.ActivityOtpActivityBinding
import com.eightpeak.salakafarm.databinding.OtpVerificationViewBinding
import com.eightpeak.salakafarm.repository.AppRepository
import com.eightpeak.salakafarm.serverconfig.RequestBodies
import com.eightpeak.salakafarm.utils.subutils.Resource
import com.eightpeak.salakafarm.utils.subutils.errorSnack
import com.eightpeak.salakafarm.viewmodel.OTPViewModel
import com.eightpeak.salakafarm.viewmodel.ViewModelProviderFactory
import com.eightpeak.salakafarm.views.login.LoginActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_add_to_cart.*

class OTPActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOtpActivityBinding
    private lateinit var subBinding: OtpVerificationViewBinding
    private var _binding: ActivityOtpActivityBinding? = null
    private lateinit var firstName: String
    private lateinit var lastName: String
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var contact: String


    lateinit var otpViewModel: OTPViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpActivityBinding.inflate(layoutInflater)
        subBinding = OtpVerificationViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()


        getRegistrationDetail()

    }

    private fun getRegistrationDetail() {

        firstName = intent.getStringExtra("first_name").toString()
        lastName = intent.getStringExtra("last_name").toString()
        email = intent.getStringExtra("email").toString()
        password = intent.getStringExtra("password").toString()
        contact = intent.getStringExtra("contact").toString()

        binding.customerPhoneNumber.setText(contact)


        binding.generateOTP.setOnClickListener {
            if (contact!!.substring(0, 1) == "9" && contact!!.length == 10) {
                otpViewModel.checkOTP(contact!!)
                getOTPResponse()

            } else {
                binding.otpLayout.errorSnack(
                    "Please add valid phone number...",
                    Snackbar.LENGTH_LONG
                )

            }
        }


    }

    private fun getOTPResponse() {
        otpViewModel.generateOtp.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let {
                        setContentView(subBinding.root)
                        getOTPVerification()
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        binding.otpLayout.errorSnack(message, Snackbar.LENGTH_LONG)
                    }

                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun getOTPVerification() {
        val opt1: String = subBinding.otp1.text.toString()
        val opt2 = subBinding.otp2.text.toString()
        val opt3 = subBinding.otp3.text.toString()
        val opt4 = subBinding.otp4.text.toString()
        val opt5 = subBinding.otp5.text.toString()
        val opt6 = subBinding.otp6.text.toString()
        val otpReceived = opt1 + opt2 + opt3 + opt4 + opt5 + opt6
        contact?.let { otpViewModel.checkVerifyOTP(it, otpReceived) }
        verifyOTPResponse()


    }

    private fun verifyOTPResponse() {
        otpViewModel.verifyOtp.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let {
                        Log.i("TAG", "verifyOTPResponse: " + response.message)
                        startRegistration()
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        binding.otpLayout.errorSnack(message, Snackbar.LENGTH_LONG)
                    }

                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun startRegistration() {

        if (email.isNotEmpty()&& password.isNotEmpty()) {
            val body = RequestBodies.RegisterBody(
                firstName,
                lastName,
                email,
                password,
                "NP",
                contact
            )

            otpViewModel.registerUser(body)
            otpViewModel.registerResponse.observe(this, Observer { event ->
                event.getContentIfNotHandled()?.let { response ->
                    when (response) {
                        is Resource.Success -> {
                            hideProgressBar()
                            response.data?.let { _ ->
                                Intent(this@OTPActivity, LoginActivity::class.java).also {
                                 startActivity(it)
                                }
                            }
                        }

                        is Resource.Error -> {
                            hideProgressBar()
                            response.message?.let { message ->
                                progress.errorSnack(message, Snackbar.LENGTH_LONG)
                            }
                        }

                        is Resource.Loading -> {
                            showProgressBar()
                        }
                    }
                }
            })
        }
    }

    private fun setupViewModel() {
        val repository = AppRepository()
        val factory = ViewModelProviderFactory(application, repository)
        otpViewModel = ViewModelProvider(this, factory).get(OTPViewModel::class.java)
    }

    private fun hideProgressBar() {
        binding.progress.visibility = View.GONE
    }

    private fun showProgressBar() {
        binding.progress.visibility = View.VISIBLE
    }


    fun onProgressClick(view: View) {
        //Preventing Click during loading
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding =null
    }
}