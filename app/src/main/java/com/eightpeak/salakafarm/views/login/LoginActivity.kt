package com.eightpeak.salakafarm.views.login

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.database.UserPrefManager
import com.eightpeak.salakafarm.databinding.ActivityLoginBinding
import com.eightpeak.salakafarm.repository.AppRepository
import com.eightpeak.salakafarm.serverconfig.RequestBodies
import com.eightpeak.salakafarm.serverconfig.network.TokenManager
import com.eightpeak.salakafarm.utils.Constants
import com.eightpeak.salakafarm.utils.Constants.Companion.TOKEN_PREF
import com.eightpeak.salakafarm.utils.GeneralUtils
import com.eightpeak.salakafarm.utils.subutils.Resource
import com.eightpeak.salakafarm.viewmodel.LoginViewModel
import com.eightpeak.salakafarm.viewmodel.SliderViewModel
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
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_login.*
import java.lang.Exception
import javax.crypto.Cipher.SECRET_KEY
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import androidx.core.app.ActivityCompat.startActivityForResult
import com.google.android.gms.common.api.ApiException

class LoginActivity : AppCompatActivity() {
    lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding
    lateinit var userPrefManager: UserPrefManager
    private var tokenManager: TokenManager? = null
  private var mGoogleSignInClient: GoogleSignInClient? = null
  private val  RC_SIGN_IN: Int = 100

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
        googleLogin()
        getFCMToken()
        init()
    }

    private fun googleLogin() {
        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        val signInButton = findViewById<SignInButton>(R.id.sign_in_button)
        signInButton.setSize(SignInButton.SIZE_STANDARD)
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
                val personEmail = acct.email
                val personId = acct.id
                val personPhoto: Uri? = acct.photoUrl
                Log.i("TAG", "handleSignInResult: "+personEmail+" "+personName)
                Toast.makeText(this,"handleSignInResult: "+personEmail+" "+personName,Toast.LENGTH_SHORT).show()
            }
//            updateUI(account)
        } catch (e: ApiException) {
            Toast.makeText(this,"signInResult:failed message=" + e.message+" "+e.localizedMessage+" "+e.cause,Toast.LENGTH_SHORT).show()

            Log.w("TAG", "signInResult:failed message=" + e.message+" "+e.localizedMessage+" "+e.cause)
//            updateUI(null)
        }
    }
    private fun init() {
        val repository = AppRepository()
        val factory = ViewModelProviderFactory(application, repository)
        loginViewModel = ViewModelProvider(this, factory).get(LoginViewModel::class.java)
        changeStatusBarColor()
    }

    fun onLoginClick(view: View) {
        val value1 = GeneralUtils.decoderfun(Constants.SECRET_KEY)
        val email = et_email.text.toString()
        val password = et_password.text.toString()


        //TODO
//        userPrefManager.subscriptionStatus = true
//        tokenManager?.saveToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiI0IiwianRpIjoiMGQ4NjFiOTlhMDk1Mzg5MzRjZjliYmUwY2NkM2RkMjVjMzQ2NzM0ZjljOTNjYWZjNDE2NDllM2NmYjZkNmU1NzcxYzIxMzc2NTg2YmEyNzQiLCJpYXQiOjE2NDIwMzU0NjQuODQ3Njk1LCJuYmYiOjE2NDIwMzU0NjQuODQ3NywiZXhwIjoxNjU3NjczODY0LjczMjAzNSwic3ViIjoiMjciLCJzY29wZXMiOltdfQ.QxHWK7-rbNNaYiqqpe9RxlmzyXx7vyEChajRVUB-MKrmwWgNgsLpYX4PuG7RwCpzjkaQ4y8lulEgPaevt_zZy9GZXscU_0EdzUGH_m35Er3WVK-B6e7Qhu4Yr-wBqg26tfDaZDcPjp2aqlWhMkEUwiyTM8W3da-S1hQz6efPuGst_vdDpXgJD7uFljjvFyBSmjeNqo427OS3pbywvObTTec93-yUs3uAk6clUAAIzCImc90rw53UWdb_lBjmljNdv_ow0a9x8zzwuYZAOmVybQ0znTKa0nFZzN117K26xJ8HUVFk7MrYhX4DSlMBEAj8qNskQ_1UJwmnYAeMjutMLDI7VOvxA_hFO2p_o0nr8Aqn-Ud1gREFHE8V-kgkpMJPdVsyHOKlti8CbAJEhDCzyq5XjJmB0g9N9GkBZGVCPEUYMlMrV333MC05A_UT6Hsd-pCDP2PH3GB4MvbA8SzlO4JXK7xVG8UOjdJy_CSyOyIFTC1S4jy-N9Zbukd-V9Qz8hABuo_ESKoAAg0IiqFyfCvhV6KLkWdan7SG06W3Mvla9AXFUqyh7JlSl5czrODqexkGh_3QZEcxqvWRzKBGJV0YPeuQGYJzFCY34vbhYVBaMFDWfrdOQ6rOJLJgomjZ61-jq-gxNbGMgyPuwIkdCr2MwhbTRglZ0zigncOoTxo")
//        getUserDetails()
//        getUserAddress()


        if (email.isNotEmpty() && password.isNotEmpty()) {
//            val body = Encrypt.encrypt(value1, email)?.let {
//                Encrypt.encrypt(value1, password)?.let { it1 ->
//                    RequestBodies.LoginBody(
//                        it,
//                        it1,
//                        "1",
//                        userPrefManager.fcmToken
//                    )
//                }
//            }

            val body =  RequestBodies.LoginBody(email,password,"1",userPrefManager.fcmToken)
            loginViewModel.loginUser(body)
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
                            Log.i("TAG", "loginResponse i m here: $loginResponse")
                            userPrefManager.firstName = loginResponse.first_name
                            userPrefManager.lastName = loginResponse.last_name
                            userPrefManager.contactNo = loginResponse.phone.toString()
                            userPrefManager.email = loginResponse.email

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
            Log.i("TAG", "getFCMToken: $token")

        })

    }

}