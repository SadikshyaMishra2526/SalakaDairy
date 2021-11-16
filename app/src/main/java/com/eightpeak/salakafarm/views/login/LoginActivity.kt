package com.eightpeak.salakafarm.views.login

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
import com.eightpeak.salakafarm.database.UserPrefManager
import com.eightpeak.salakafarm.databinding.ActivityLoginBinding
import com.eightpeak.salakafarm.repository.AppRepository
import com.eightpeak.salakafarm.serverconfig.RequestBodies
import com.eightpeak.salakafarm.serverconfig.network.TokenManager
import com.eightpeak.salakafarm.utils.Constants.Companion.TOKEN_PREF
import com.eightpeak.salakafarm.utils.subutils.Resource
import com.eightpeak.salakafarm.viewmodel.LoginViewModel
import com.eightpeak.salakafarm.viewmodel.SliderViewModel
import com.eightpeak.salakafarm.viewmodel.ViewModelProviderFactory
import com.eightpeak.salakafarm.views.home.HomeActivity
import com.eightpeak.salakafarm.views.register.RegisterActivity
import com.google.android.material.snackbar.Snackbar
import com.hadi.retrofitmvvm.util.errorSnack
import kotlinx.android.synthetic.main.activity_login.*
class LoginActivity : AppCompatActivity() {
    lateinit var loginViewModel: LoginViewModel
    lateinit var userProfileViewModel: SliderViewModel
    private lateinit var binding: ActivityLoginBinding
    lateinit var userPrefManager: UserPrefManager
    private var tokenManager: TokenManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userPrefManager= UserPrefManager(this)

        tokenManager = TokenManager.getInstance(getSharedPreferences(TOKEN_PREF, MODE_PRIVATE))

        binding.newCustomer.setOnClickListener {
            val mainActivity = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(mainActivity)
        }
        binding.forgetPassword.setOnClickListener {
//            val mainActivity = Intent(this@LoginActivity, ForgotPassword::class.java)
//            startActivity(mainActivity)
        }
        init()
    }

    private fun init() {
        val repository = AppRepository()
        val factory = ViewModelProviderFactory(application, repository)
        loginViewModel = ViewModelProvider(this, factory).get(LoginViewModel::class.java)
        userProfileViewModel= ViewModelProvider(this, factory).get(SliderViewModel::class.java)
        changeStatusBarColor()
    }

    fun onLoginClick(view: View) {
        var email = et_email.text.toString()
        val password = et_password.text.toString()
        if (email.isNotEmpty() && password.isNotEmpty()) {
            val body = RequestBodies.LoginBody(
                email,
                password,
                "1"
            )

            loginViewModel.loginUser(body)
            loginViewModel.loginResponse.observe(this, Observer { event ->
                event.getContentIfNotHandled()?.let { response ->
                    when (response) {
                        is Resource.Success -> {
                            hideProgressBar()
                            response.data?.let { loginResponse ->
                                 userPrefManager.userToken=loginResponse.access_token
                                tokenManager?.saveToken(loginResponse.access_token)
                                getUserDetails()

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

    private fun getUserDetails() {

        tokenManager?.let { loginViewModel.userDetailsUser(it) }
          loginViewModel.userDetailsResponse.observe(this, Observer { event ->
                event.getContentIfNotHandled()?.let { response ->
                    when (response) {
                        is Resource.Success -> {
                            hideProgressBar()
                            response.data?.let { loginResponse ->
                                Log.i("TAG", "loginResponse i m here: $loginResponse")
                                 userPrefManager.firstName=loginResponse.first_name
                                 userPrefManager.lastName=loginResponse.last_name
                                 userPrefManager.contactNo= loginResponse.phone.toString()
                                 userPrefManager.email=loginResponse.email
                                 userPrefManager.userAddress1=loginResponse.address1
                                 userPrefManager.userAddress2=loginResponse.address2
                                 userPrefManager.userCountry=loginResponse.country
                                val mainActivity = Intent(this@LoginActivity, HomeActivity::class.java)
                                startActivity(mainActivity)
                                finish()
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


    private fun changeStatusBarColor() {
        val window: Window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = resources.getColor(R.color.main_color)
    }
    private fun hideProgressBar() {
        progress.visibility = View.GONE
    }

    private fun showProgressBar() {
        progress.visibility = View.VISIBLE
    }
}