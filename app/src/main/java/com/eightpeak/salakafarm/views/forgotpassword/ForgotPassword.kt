package com.eightpeak.salakafarm.views.forgotpassword

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.eightpeak.salakafarm.databinding.ActivityLoginBinding
import com.eightpeak.salakafarm.databinding.FragmentForgotPasswordBinding
import com.eightpeak.salakafarm.repository.AppRepository
import com.eightpeak.salakafarm.utils.GeneralUtils
import com.eightpeak.salakafarm.utils.subutils.Resource
import com.eightpeak.salakafarm.utils.subutils.errorSnack
import com.eightpeak.salakafarm.utils.subutils.showSnack
import com.eightpeak.salakafarm.viewmodel.GetResponseViewModel
import com.eightpeak.salakafarm.viewmodel.ViewModelProviderFactory
import com.eightpeak.salakafarm.views.home.ui.user_profile.Encrypt
import com.eightpeak.salakafarm.views.login.LoginActivity
import com.google.android.material.snackbar.Snackbar

class ForgotPassword : AppCompatActivity() {


    private lateinit var viewModel: GetResponseViewModel
    private var _binding: FragmentForgotPasswordBinding? = null

    var layoutManager: LinearLayoutManager? = null

    private lateinit var binding: FragmentForgotPasswordBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupViewModel()
    }

    private fun setupViewModel() {

        val repository = AppRepository()
        val factory = ViewModelProviderFactory(application, repository)
        viewModel = ViewModelProvider(this, factory).get(GetResponseViewModel::class.java)


        binding.forgotPassword.setOnClickListener {
            if(binding.etEmail.text.toString().isNotEmpty())
            {
                GeneralUtils.hideKeyboard(this@ForgotPassword
                )
                forgotPassword(binding.etEmail.text.toString())
            }
        }
    }
    private fun forgotPassword(email: String?) {
        if (email != null) {
            Encrypt.getEncrptedValue(email)?.let { viewModel.forgotPassword(it) }
        }
        viewModel.forgotPassword.observe(this, { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { _ ->
                        if(response.data.error==1){

                        binding.forgotLayout.errorSnack(response.data.message, Snackbar.LENGTH_LONG)
                        }else{
                            binding.forgotLayout.showSnack("Password changing link sent to your email. Please check...", Snackbar.LENGTH_LONG)

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
                        binding.forgotLayout.errorSnack(message, Snackbar.LENGTH_LONG)
                    }

                }

                is Resource.Loading -> {
                    showProgressBar()
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
        _binding = null
    }
}