package com.eightpeak.salakafarm.views.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.database.UserPrefManager
import com.eightpeak.salakafarm.databinding.ActivityRegisterBinding
import com.eightpeak.salakafarm.repository.AppRepository
import com.eightpeak.salakafarm.serverconfig.RequestBodies
import com.eightpeak.salakafarm.serverconfig.network.TokenManager
import com.eightpeak.salakafarm.utils.Constants
import com.eightpeak.salakafarm.utils.GeneralUtils
import com.eightpeak.salakafarm.utils.subutils.Resource
import com.eightpeak.salakafarm.utils.subutils.errorSnack
import com.eightpeak.salakafarm.utils.subutils.showSnack
import com.eightpeak.salakafarm.viewmodel.LoginViewModel
import com.eightpeak.salakafarm.viewmodel.RegisterViewModel
import com.eightpeak.salakafarm.viewmodel.ViewModelProviderFactory
import com.google.android.material.snackbar.Snackbar
import com.eightpeak.salakafarm.viewmodel.OTPViewModel
import com.eightpeak.salakafarm.views.home.HomeActivity
import com.eightpeak.salakafarm.views.home.ui.user_profile.Encrypt
import com.facebook.CallbackManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

class RegisterActivity : AppCompatActivity() {

    lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityRegisterBinding
    lateinit var otpViewModel: OTPViewModel
    lateinit var userPrefManager: UserPrefManager


    private var tokenManager: TokenManager? = null
    private var mGoogleSignInClient: GoogleSignInClient? = null
    private val  RC_SIGN_IN: Int = 100
    private var saveLogin: Boolean? = null
    private  var callbackManager: CallbackManager?=null
    private val EMAIL = "email"
    private var _binding: ActivityRegisterBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPrefManager = UserPrefManager(this)
        tokenManager = TokenManager.getInstance(getSharedPreferences(Constants.TOKEN_PREF, MODE_PRIVATE))
        googleLogin()
        init()
        setupViewModel()
    }

    private fun setupViewModel() {
        val repository = AppRepository()
        val factory = ViewModelProviderFactory(application, repository)
        otpViewModel = ViewModelProvider(this, factory).get(OTPViewModel::class.java)
        loginViewModel = ViewModelProvider(this, factory).get(LoginViewModel::class.java)
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

        val body=RequestBodies.RegisterBody(first_name!!,last_name!!,email!!,password!!,"",contact!!)
        otpViewModel.registerUser(body)
            otpViewModel.registerResponse.observe(this, Observer { event ->
                event.getContentIfNotHandled()?.let { response ->
                    when (response) {
                        is Resource.Success -> {
                            hideProgressBar()
                            response.data?.let { _ ->

                                if(response.data.error==1){
                                    response.data.detail?.let { message ->
                                        binding.registerLayout.errorSnack(message, Snackbar.LENGTH_LONG)
                                    }
                                }else{
                                    response.data.message?.let { message ->
                                        binding.registerLayout.showSnack(message, Snackbar.LENGTH_LONG)
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
                                binding.registerLayout.errorSnack(message, Snackbar.LENGTH_LONG)
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
    private fun googleLogin() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        val signInButton = findViewById<ImageView>(R.id.sign_in_button)
//        signInButton.setSize(SignInButton.SIZE_STANDARD)
        val account = GoogleSignIn.getLastSignedInAccount(this)

        signInButton.setOnClickListener {
            signIn()
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
                val personName = Encrypt.getEncrptedValue(acct.displayName)
                val personGivenName = acct.givenName
                val personFamilyName = acct.familyName
                val personEmail = Encrypt.getEncrptedValue(acct.email)
                val personId =Encrypt.getEncrptedValue( acct.id)
                val personPhoto: String? =  acct.photoUrl.toString()
                val name: Array<String>? = personName?.split(" ".toRegex())?.toTypedArray()

                if (personId != null) {
                    name?.get(0)?.let {
                        if (personEmail != null) {
                            Encrypt.getEncrptedValue( personName)?.let { it1 ->
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
                    }
                    getGoogleLoginResponse()
                }
            }
        } catch (e: ApiException) {
            Toast.makeText(this,"SignIn:failed message=" + e.message+" "+e.localizedMessage+" "+e.cause,
                Toast.LENGTH_SHORT).show()
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
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        binding.registerLayout.errorSnack(message, Snackbar.LENGTH_LONG)
                    }
                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun getUserDetails() {
        tokenManager?.let { loginViewModel.userDetailsUser(it) }
        loginViewModel.userDetailsResponse.observe(this, Observer { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        hideProgressBar()
                        response.data?.let { loginResponse ->
                            userPrefManager.firstName = loginResponse.first_name
                            userPrefManager.lastName = loginResponse.last_name
                            userPrefManager.contactNo = loginResponse.phone.toString()
                            userPrefManager.email = loginResponse.email
                            userPrefManager.avatar = loginResponse.avatar

                            val mainActivity = Intent(this@RegisterActivity, HomeActivity::class.java)
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
}