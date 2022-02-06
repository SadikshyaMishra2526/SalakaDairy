package com.eightpeak.salakafarm.views.login

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.eightpeak.salakafarm.database.UserPrefManager
import com.eightpeak.salakafarm.databinding.ActivityLoginBinding
import com.eightpeak.salakafarm.repository.AppRepository
import com.eightpeak.salakafarm.serverconfig.RequestBodies
import com.eightpeak.salakafarm.serverconfig.network.TokenManager
import com.eightpeak.salakafarm.utils.Constants.Companion.TOKEN_PREF
import com.eightpeak.salakafarm.utils.GeneralUtils
import com.eightpeak.salakafarm.utils.subutils.Resource
import com.eightpeak.salakafarm.viewmodel.LoginViewModel
import com.eightpeak.salakafarm.viewmodel.ViewModelProviderFactory
import com.eightpeak.salakafarm.views.home.HomeActivity
import com.eightpeak.salakafarm.views.register.RegisterActivity
import com.google.android.material.snackbar.Snackbar
import com.eightpeak.salakafarm.utils.subutils.errorSnack
import com.eightpeak.salakafarm.views.home.ui.user_profile.Encrypt
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.FacebookException

import com.facebook.login.LoginResult

import com.facebook.FacebookCallback


import com.facebook.login.widget.LoginButton
import java.util.*
import com.facebook.login.LoginManager

import com.facebook.CallbackManager
import androidx.fragment.app.Fragment
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.views.forgotpassword.ForgotPassword


class LoginActivity : AppCompatActivity() {
    lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding
    lateinit var userPrefManager: UserPrefManager
    private var tokenManager: TokenManager? = null
  private var mGoogleSignInClient: GoogleSignInClient? = null
  private val  RC_SIGN_IN: Int = 100
    private var saveLogin: Boolean? = null
    private  var callbackManager:CallbackManager?=null
    private val EMAIL = "email"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userPrefManager = UserPrefManager(this)

        tokenManager = TokenManager.getInstance(getSharedPreferences(TOKEN_PREF, MODE_PRIVATE))

        binding.newCustomer.setOnClickListener {
            val mainActivity = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(mainActivity)
        }
        binding.forgetPassword.setOnClickListener {
            val mainActivity = Intent(this@LoginActivity, ForgotPassword::class.java)
            startActivity(mainActivity)

        }
       binding.signUp.visibility=View.VISIBLE
        val loginPreferences:SharedPreferences? = getSharedPreferences("loginPrefs", MODE_PRIVATE)
        saveLogin = loginPreferences?.getBoolean("saveLogin", false)
        if (saveLogin == true) {
            binding.etEmail.setText(loginPreferences?.getString("username", ""))
            binding.etPassword.setText(loginPreferences?.getString("password", ""))
            binding.rememberMe.isChecked = true
        }


        googleLogin()
        getFCMToken()
        init()
    }

    private fun addFacebookLogin() {

        LoginManager.getInstance().logInWithReadPermissions(this, listOf("public_profile","name","email"));
        callbackManager = CallbackManager.Factory.create()


        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult?> {
                override fun onSuccess(loginResult: LoginResult?) {
                    // App code
                }

                override fun onCancel() {
                    // App code
                }

                override fun onError(exception: FacebookException) {
                    // App code
                }
            })
      val  loginButton = findViewById<View>(R.id.login_button) as LoginButton
        loginButton.setReadPermissions(listOf(EMAIL))
        // If you are using in a fragment, call loginButton.setFragment(this);

        // Callback registration
        // If you are using in a fragment, call loginButton.setFragment(this);

        // Callback registration
        loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {
            override fun onSuccess(loginResult: LoginResult?) {
                // App code
            }

            override fun onCancel() {
                // App code
            }

            override fun onError(exception: FacebookException) {
                // App code
            }
        })
    }

    private fun googleLogin() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        val signInButton = findViewById<ImageView>(R.id.sign_in_button)
//        signInButton.setSize(SignInButton.SIZE_STANDARD)
        val account = GoogleSignIn.getLastSignedInAccount(this)
        Log.i("TAG", "googleLogin: "+account)
        if(account!=null){
//            account==
        }else{
            signInButton.setOnClickListener {
                signIn()
            }
        }

    }
    private fun signIn() {
        val signInIntent = mGoogleSignInClient!!.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        callbackManager!!.onActivityResult(requestCode, resultCode, data);


        super.onActivityResult(requestCode, resultCode, data)


        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            val acct = GoogleSignIn.getLastSignedInAccount(this)
            if (acct != null) {
                val personName = acct.displayName
                val personGivenName = acct.givenName
                val personFamilyName = acct.familyName
                val personEmail = Encrypt.getEncrptedValue(acct.email)
                val personId =Encrypt.getEncrptedValue( acct.id)
                val personPhoto: String? =  acct.photoUrl.toString()
                Log.i("TAG", "handleSignInResult: $personName"+acct.email+" "+ acct.id )

                val name: Array<String>? = personName?.split(" ".toRegex())?.toTypedArray()
                    if (personId != null) {

                            if (personEmail != null) {
                                Encrypt.getEncrptedValue(name?.get(0))?.let { it1 ->
                                    Encrypt.getEncrptedValue(personFamilyName)?.let { it2 ->
                                            Encrypt.getEncrptedValue("google")?.let { it4 ->
                                                loginViewModel.socialGoogle(
                                                    it1,
                                                    it2,personEmail,userPrefManager.fcmToken,personId,personPhoto.toString(),
                                                    it4
                                                )

                                        }
                                    }
                                }
                            }
                        mGoogleSignInClient?.signOut()
                        getGoogleLoginResponse()
                }
            }
        } catch (e: ApiException) {
            Toast.makeText(this,"SignIn:failed message=" + e.message+" "+e.localizedMessage+" "+e.cause,Toast.LENGTH_SHORT).show()
            Log.w("TAG", "signInResult:failed message=" + e.message+" "+e.localizedMessage+" "+e.cause)
        }
    }

    private fun getGoogleLoginResponse() {
        loginViewModel.socialGoogle.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { picsResponse ->
                        Log.i("TAG", "getGoogleLoginResponse: "+picsResponse.access_token)
                        userPrefManager.subscriptionStatus=picsResponse.subscription
                        tokenManager?.saveToken(picsResponse.access_token)
                        getUserDetails()
                        getUserAddress()
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        binding.loginView.errorSnack(message, Snackbar.LENGTH_LONG)
                    }
                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun init() {
        val repository = AppRepository()
        val factory = ViewModelProviderFactory(application, repository)
        loginViewModel = ViewModelProvider(this, factory).get(LoginViewModel::class.java)
        changeStatusBarColor()
    }

    fun onLoginClick(view: View) {
        val email = binding.etEmail.text.toString()
        val password =  binding.etPassword.text.toString()
        GeneralUtils.hideKeyboard(this)
        if (email.isNotEmpty() && password.isNotEmpty()) {

            val loginPreferences:SharedPreferences? = getSharedPreferences("loginPrefs", MODE_PRIVATE)
            var loginPrefsEditor: SharedPreferences.Editor? = null
            loginPrefsEditor = loginPreferences?.edit()

            if ( binding.rememberMe.isChecked) {
                loginPrefsEditor?.putBoolean("saveLogin", true)
                loginPrefsEditor?.putString("username",   binding.etEmail.text.toString())
                loginPrefsEditor?.putString("password",   binding.etPassword.text.toString())
                loginPrefsEditor?.apply()
            } else {
                loginPrefsEditor?.clear()
                loginPrefsEditor?.apply()
            }


            val body = Encrypt.getEncrptedValue(email)?.let {
                Encrypt.getEncrptedValue (password)?.let { it1 ->
                    RequestBodies.LoginBody(
                        it,
                        it1,
                        "1",
                        userPrefManager.fcmToken
                    )
                }
            }

            if (body != null) {
                loginViewModel.loginUser(body)
            }
            loginViewModel.loginResponse.observe(this, Observer { event ->
                event.getContentIfNotHandled()?.let { response ->
                    when (response) {
                        is Resource.Success -> {
                            hideProgressBar()
                            response.data?.let { loginResponse ->

                                userPrefManager.subscriptionStatus=loginResponse.subscription
                                 tokenManager?.saveToken(loginResponse.access_token)
                                Log.i("TAG", "onLoginClick: "+loginResponse.access_token)
                                getUserDetails()
                                getUserAddress()

                            }
                        }

                        is Resource.Error -> {
                            hideProgressBar()
                            response.message?.let { message ->
                                binding.progress.errorSnack(message, Snackbar.LENGTH_LONG)
                            }
                        }

                        is Resource.Loading -> {
                            showProgressBar()
                        }
                    }
                }
            })
        }else{

        }
    }

    private fun getUserAddress() {
        tokenManager?.let { loginViewModel.getUserAddressList(it) }
        loginViewModel.userAddressList.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { picsResponse ->
                        if (picsResponse.address_list.isNotEmpty()) {
                            var addressListString: String = ""
                            for (i in picsResponse.address_list.indices) {
                                addressListString = addressListString +
                                        picsResponse.address_list[i].address1 + " " + picsResponse.address_list[i].address2 + " Nepal" + "\n"
                            }
                            userPrefManager.addressList = addressListString
                        }
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        binding.loginView.errorSnack(message, Snackbar.LENGTH_LONG)
                    }
                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun getUserDetails() {
        var addressListString = ""
        tokenManager?.let { loginViewModel.userDetailsUser(it) }
        loginViewModel.userDetailsResponse.observe(this, Observer { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        hideProgressBar()
                        response.data?.let { loginResponse ->
                            userPrefManager.firstName = loginResponse.first_name
                            userPrefManager.lastName = loginResponse.last_name
                            userPrefManager.email = loginResponse.email
                            userPrefManager.avatar = loginResponse.avatar
                            userPrefManager.contactNo = loginResponse.phone
//
                            val mainActivity = Intent(this@LoginActivity, HomeActivity::class.java)
                            startActivity(mainActivity)
                            finish()
                        }
                    }

                    is Resource.Error -> {
                        hideProgressBar()
                        response.message?.let { message ->
                            binding.progress.errorSnack(message, Snackbar.LENGTH_LONG)
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
        window.statusBarColor = resources.getColor(R.color.sub_color)
    }

    private fun hideProgressBar() {
        binding.progress.visibility = View.GONE
    }

    private fun showProgressBar() {
        binding.progress.visibility = View.VISIBLE
    }

    private fun getFCMToken() {
        val token = ""

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("TAG", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result.toString()
            userPrefManager.fcmToken = token
            Log.i("TAG", "getFCMToken: "+task.result.toString())

        })

    }

}