package com.eightpeak.salakafarm.views.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.databinding.ActivityRegisterBinding
import com.eightpeak.salakafarm.repository.AppRepository
import com.eightpeak.salakafarm.serverconfig.RequestBodies
import com.eightpeak.salakafarm.utils.GeneralUtils
import com.eightpeak.salakafarm.utils.subutils.Resource
import com.eightpeak.salakafarm.viewmodel.RegisterViewModel
import com.eightpeak.salakafarm.viewmodel.ViewModelProviderFactory
import com.google.android.material.snackbar.Snackbar
import com.eightpeak.salakafarm.utils.subutils.errorSnack
import com.eightpeak.salakafarm.utils.subutils.showSnack
import com.eightpeak.salakafarm.viewmodel.OTPViewModel
import com.eightpeak.salakafarm.views.home.HomeActivity
import com.eightpeak.salakafarm.views.home.ui.user_profile.Encrypt
import com.eightpeak.salakafarm.views.login.LoginActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.progress
import kotlinx.android.synthetic.main.fragment_add_to_cart.*

class RegisterActivity : AppCompatActivity() {

    lateinit var registerViewModel: RegisterViewModel
    private lateinit var binding: ActivityRegisterBinding
    lateinit var otpViewModel: OTPViewModel


    private var _binding: ActivityRegisterBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        setupViewModel()
    }

    private fun setupViewModel() {
        val repository = AppRepository()
        val factory = ViewModelProviderFactory(application, repository)
        otpViewModel = ViewModelProvider(this, factory).get(OTPViewModel::class.java)
    }
    private fun init() {
//        val repository = AppRepository()
//        val factory = ViewModelProviderFactory(application, repository)
//        registerViewModel = ViewModelProvider(this, factory).get(RegisterViewModel::class.java)
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
//        val intent = Intent(this@RegisterActivity, OTPActivity::class.java)
//            intent.putExtra("first_name", first_name)
//            intent.putExtra("last_name", last_name)
//            intent.putExtra("email", email)
//            intent.putExtra("password", password)
//            intent.putExtra("contact", contact)
//            startActivity(intent)
        GeneralUtils.hideKeyboard(this)
        getRegistrationDetail()
        }
    private fun getRegistrationDetail() {
        val first_name =  Encrypt.getEncrptedValue (binding.etFirstName.text.toString())
        val last_name =  Encrypt.getEncrptedValue (binding.etLastName.text.toString())
        val email =  Encrypt.getEncrptedValue (binding.etEmailAddress.text.toString())
        val password =  Encrypt.getEncrptedValue (binding.etPassword.text.toString())
        val contact =  Encrypt.getEncrptedValue (binding.contactDetails.text.toString())

        val body=RequestBodies.RegisterBody(first_name!!,last_name!!,email!!,password!!,"NP",contact!!)
        otpViewModel.registerUser(body)
            otpViewModel.registerResponse.observe(this, Observer { event ->
                event.getContentIfNotHandled()?.let { response ->
                    when (response) {
                        is Resource.Success -> {
                            hideProgressBar()
                            response.data?.let { _ ->

                                if(response.data.error==1){
                                    response.data.detail?.let { message ->
                                        progress.errorSnack(message, Snackbar.LENGTH_LONG)
                                    }
                                }else{
                                    response.data.message?.let { message ->
                                        progress.showSnack(message, Snackbar.LENGTH_LONG)
                                    }
                                    val handler = Handler()
                                    handler.postDelayed({
                                            finish()
                                    }, 1000)


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