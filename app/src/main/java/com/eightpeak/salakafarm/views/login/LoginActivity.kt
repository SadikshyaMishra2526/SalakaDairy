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
import com.eightpeak.salakafarm.databinding.ActivityHomeBinding
import com.eightpeak.salakafarm.databinding.ActivityLoginBinding
import com.eightpeak.salakafarm.repository.AppRepository
import com.eightpeak.salakafarm.serverconfig.RequestBodies
import com.eightpeak.salakafarm.utils.subutils.Resource
import com.eightpeak.salakafarm.viewmodel.LoginViewModel
import com.eightpeak.salakafarm.viewmodel.SliderViewModel
import com.eightpeak.salakafarm.viewmodel.UserProfileViewModel
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userPrefManager= UserPrefManager(this)

        binding.newCustomer.setOnClickListener {
            val mainActivity = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(mainActivity)
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
        getUserDetails("Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiI0IiwianRpIjoiOTkwOWZiNmIzNDY1MzkzNzAxN2FmY2NkNDhiNmM1MDJjMDJmOWE2YjU3MDA2YmJjYjE3MTFlYTA5MjU2ZTQzMjdhZDg3NGI5MGUzYzU3NDYiLCJpYXQiOjE2MzUxODQyODYuNjQwMzQzLCJuYmYiOjE2MzUxODQyODYuNjQwMzQ3LCJleHAiOjE2NTA5MDkwODYuNDk1MDE4LCJzdWIiOiI4Iiwic2NvcGVzIjpbXX0.gvmXG-QIvGEWRBcenfP28tzDU792j07qKN7MZqZ7lulvkN9ARDU-LHWo_SwAl5_DRlHi4c87NRxLviSpka-4ppAfgiqkAt0wH_zzVlrLDUwBOAQycF8JoB2aXTOR5JkoCygBBGir98QZNMg4NOZRXPoOnn_d4Mqt5A9lE8wInT2SHtEqn_IMr0P9klFLPidtF64hkmcfZ0FzT6cPiaqWWR1Yi0JoDVW_gIZ4TqX3CjF4ghNAL7JMRXp1WeNJNEddmc-xBK13596XEUk8GhM6RZvcY_u_BroqyJ11iTZ6INOLSBBduhnCX_CXNtYvcB3FlP8Mf1fn0Iz6OQWl7UwXd_aVrtB5r5sX7SRfPJ2LSyxaMdru7Hg1ZqktrlSaAzHLU-VGu7RPGLq0Ci-Qp8DVRPxe2zUMK38pYG5YR8B1UUiXM5bvjAvla3Dkt-OUsmVg6apPwfSW7Z2q4SJYTkrUcWEeUZje7tYrvPkj4nlFTgR5N4aFAEnmxvf_1UHu450hlb4oBsvAC30-g-ERPwm9eGUC0B_UhnhaUeXFL6pyPMnJYmMIeFslhhLW_JVlm0Q0-Dp-_KpX2pHMgOrdmJt28iKc_1vCve1N74SYpzlyOJ5pnH7NCKjXZpXF5MIQtIKewMcxcAX0_px7W9z7DN5wPEkLkS15OwD67CaukONPXYc")
        if (email.isNotEmpty() && password.isNotEmpty()) {
//            val body = RequestBodies.LoginBody(
//                email,
//                password,
//                "1"
//            )
//
//            loginViewModel.loginUser(body)
//            loginViewModel.loginResponse.observe(this, Observer { event ->
//                event.getContentIfNotHandled()?.let { response ->
//                    when (response) {
//                        is Resource.Success -> {
//                            hideProgressBar()
//                            response.data?.let { loginResponse ->
//                                Log.i("TAG", "loginResponse: $loginResponse")
//                                 userPrefManager.userToken=loginResponse.access_token
//                                getUserDetails(loginResponse.access_token)
////                                Intent(this@LoginActivity, HomeActivity::class.java).also {
////                                    startActivity(it)
////                                }
//                            }
//                        }
//
//                        is Resource.Error -> {
//                            hideProgressBar()
//                            response.message?.let { message ->
//                                progress.errorSnack(message, Snackbar.LENGTH_LONG)
//                            }
//                        }
//
//                        is Resource.Loading -> {
//                            showProgressBar()
//                        }
//                    }
//                }
//            })
        }
    }

    private fun getUserDetails(accessToken: String) {
        loginViewModel.userDetailsUser("Bearer $accessToken")
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