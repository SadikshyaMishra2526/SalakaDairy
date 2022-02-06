package com.eightpeak.salakafarm.views.home.ui.user_profile

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.api.load
import com.eightpeak.salakafarm.App
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.database.UserPrefManager
import com.eightpeak.salakafarm.databinding.ActivityUserProfileBinding
import com.eightpeak.salakafarm.repository.AppRepository
import com.eightpeak.salakafarm.serverconfig.RequestBodies
import com.eightpeak.salakafarm.serverconfig.network.TokenManager
import com.eightpeak.salakafarm.subscription.displaysubscription.TrackSubscriptionView
import com.eightpeak.salakafarm.utils.Constants
import com.eightpeak.salakafarm.utils.EndPoints.Companion.BASE_URL
import com.eightpeak.salakafarm.utils.GeneralUtils
import com.eightpeak.salakafarm.utils.subutils.Resource
import com.eightpeak.salakafarm.utils.subutils.addAddressSnack
import com.eightpeak.salakafarm.utils.subutils.errorSnack
import com.eightpeak.salakafarm.utils.subutils.showSnack
import com.eightpeak.salakafarm.viewmodel.UserProfileViewModel
import com.eightpeak.salakafarm.viewmodel.ViewModelProviderFactory
import com.eightpeak.salakafarm.views.addresslist.AddressListModel
import com.eightpeak.salakafarm.views.addtocart.CartActivity
import com.eightpeak.salakafarm.views.comparelist.CompareListActivity
import com.eightpeak.salakafarm.views.home.HomeActivity
import com.eightpeak.salakafarm.views.home.address.AddressModification
import com.eightpeak.salakafarm.views.home.ui.EditProfile
import com.eightpeak.salakafarm.views.order.orderview.orderhistory.OrderHistory
import com.eightpeak.salakafarm.views.wishlist.WishlistActivity
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.material.snackbar.Snackbar
import okhttp3.MultipartBody
import java.util.*


class UserProfile : AppCompatActivity(),GoogleApiClient.OnConnectionFailedListener  {

    private lateinit var userPrefManager: UserPrefManager
    private var _binding: ActivityUserProfileBinding? = null

    private val RESULT_LOAD_IMG = 101
    private lateinit var binding: ActivityUserProfileBinding
    private lateinit var viewModel: UserProfileViewModel
    private lateinit var profilePicture: MultipartBody.Part
    var dateSelected: Calendar = Calendar.getInstance()

    var value1: ByteArray = GeneralUtils.decoderfun(Constants.SECRET_KEY)
    private var dialog: Dialog? = null
    private var datePickerDialog: DatePickerDialog? = null

    private var googleApiClient: GoogleApiClient? = null

    private var gso: GoogleSignInOptions? = null

    private fun init() {
        val repository = AppRepository()
        val factory = ViewModelProviderFactory(application, repository)
        viewModel = ViewModelProvider(this, factory).get(UserProfileViewModel::class.java)

    }

    private var tokenManager: TokenManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tokenManager = TokenManager.getInstance(
            getSharedPreferences(
                Constants.TOKEN_PREF,
                MODE_PRIVATE
            )
        )
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        binding.headerName.text = getString(R.string.user_profile)
        binding.btBackpressed.setOnClickListener { finish() }

        setContentView(binding.root)
        getStatusColor()
        userPrefManager = UserPrefManager(this)
        binding.userName.text = userPrefManager.firstName + " " + userPrefManager.lastName
        binding.userEmail.text = userPrefManager.email
        if(!userPrefManager.contactNo.equals("not_found")){
            binding.userPhone.text = userPrefManager.contactNo
        }else{
            binding.userPhone.visibility=View.GONE
        }
        Log.i("TAG", "onCreate: "+userPrefManager.avatar)
        if(!userPrefManager.avatar.equals("not_found")&&!userPrefManager.avatar.equals("null")){
            if(userPrefManager.avatar.contains("https://")){
                binding.customerAvatar.load(userPrefManager.avatar)
            }else{
                binding.customerAvatar.load(BASE_URL+userPrefManager.avatar)
            }

        }else{
            binding.customerAvatar.load(R.drawable.logo)

        }
        dialog = Dialog(this)

        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        googleApiClient = GoogleApiClient.Builder(this)
            .enableAutoManage(this, this)
            .addApi(Auth.GOOGLE_SIGN_IN_API, gso!!)
            .build()


        binding.userLogout.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(R.string.logout)
            builder.setMessage(R.string.logout_msg)
            builder.setPositiveButton(R.string.logout,
                DialogInterface.OnClickListener { _, _ ->
                    viewModel.requestLogout(tokenManager!!)
                     getLogoutResponse()
                    logoutGoogle()
                    val handler = Handler()
                    handler.postDelayed({
                        userPrefManager.clearData()
                        tokenManager?.deleteToken()
                        startActivity(Intent(this@UserProfile, HomeActivity::class.java))
                        finish()
                    }, 2000)

                })
            builder.setNegativeButton(R.string.cancel, null)

            val dialog = builder.create()
            dialog.show()


////            for google signout


        }

        binding.editProfileLayout.setOnClickListener {
//            Toast.makeText(this@UserProfile,"Work on progress",Toast.LENGTH_LONG).show()
//            val args = Bundle()
//            val bottomSheet = EditProfile()
//            bottomSheet.arguments = args
//            bottomSheet.show(
//                (this@UserProfile as FragmentActivity).supportFragmentManager,
//                bottomSheet.tag
//            )

            EditProfile(
                onClickItem = { firstName, lastName, dob,photo->
                    initObservers(
                        firstName,
                        lastName,
                        dob,photo
                    )
                }).show(this.supportFragmentManager, "UserProfile")
        }

        binding.layoutUserPassword.setOnClickListener {
            editPassword()
        }

        binding.viewWishlist.setOnClickListener {
            startActivity(Intent(this@UserProfile, WishlistActivity::class.java))
        }

        binding.compareList.setOnClickListener {
            if (App.getData().size != 0) {

                val intent = Intent(this@UserProfile, CompareListActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(
                    this@UserProfile,
                    getString(R.string.compare_list_empty),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.cartList.setOnClickListener {
            val intent = Intent(this@UserProfile, CartActivity::class.java)
            startActivity(intent)
        }

        binding.orderHistory.setOnClickListener {
            val intent = Intent(this@UserProfile, OrderHistory::class.java)
            startActivity(intent)

        }
        binding.addAddresses.setOnClickListener {
            val args = Bundle()
            args.putString("address1", null)
            args.putString("address2",null)
            args.putString("address3",null)
            args.putString("contact", null)
            args.putString("addressId", null)
            val bottomSheet = AddressModification()
            bottomSheet.arguments=args
            bottomSheet.show(
                (this@UserProfile as FragmentActivity).supportFragmentManager,
                bottomSheet.tag
            )
        }
        binding.editAddress.setOnClickListener {
            dialog!!.show()
        }

        init()
        getAddressList()

    }

    private fun logoutGoogle() {
        googleApiClient?.let {
            Auth.GoogleSignInApi.signOut(it).setResultCallback { status ->
                if (status.isSuccess()) {
                    Toast.makeText(applicationContext, "Successfully", Toast.LENGTH_LONG)
                        .show()
                } else {
                    Toast.makeText(applicationContext, "Session not close", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }

    private fun initObservers(firstName: String, lastName: String, dob: String, photo: String) {
        binding.userName.text = "$firstName $lastName"
        binding.userEmail.text = userPrefManager.email
        binding.userPhone.text = userPrefManager.contactNo
        if(!photo.equals("not_found")){
            if(photo.contains("https://")){
                binding.customerAvatar.load(BASE_URL+photo)
            }else{
                binding.customerAvatar.load(BASE_URL+photo)
            }

        }else{
            binding.customerAvatar.load(R.drawable.logo)

        }
    }

    private fun getLogoutResponse() {

        viewModel.logout.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let {
                        Log.i("TAG", "getDeleteResponse: " + response)
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->

                        binding.userProfileView.errorSnack(message, Snackbar.LENGTH_LONG)
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun getAddressList() {
        tokenManager?.let { it1 -> viewModel.getUserAddressList(it1) }
        var addressListString = ""
        viewModel.userAddressList.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { addresses ->
                        if (addresses.address_list.isNotEmpty()) {
                            viewAddressList(addresses)
                            binding.customerDefaultAddress.text =
                                addressListString + addresses.address_list[0].address1 + " " + addresses.address_list[0].address2 + " Nepal" + "\n"
                            for (i in addresses.address_list.indices) {
                                addressListString = addressListString +
                                        addresses.address_list[i].address1 + " " + addresses.address_list[i].address2 + " Nepal" + "\n"
                            }
                            userPrefManager.addressList = addressListString
                        }

                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        binding.userProfileView.errorSnack(message, Snackbar.LENGTH_LONG)
                    }
                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun viewAddressList(addressListModel: AddressListModel) {
        if (addressListModel.address_list.isNotEmpty()) {

            dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog?.setCancelable(true)
            dialog?.setContentView(R.layout.layout_view_address)
            val fetchAddressList = dialog?.findViewById<LinearLayout>(R.id.fetch_address_list)

            for (i in addressListModel.address_list) {
                val itemView: View =
                    LayoutInflater.from(this)
                        .inflate(R.layout.address_list_item, fetchAddressList, false)

                val addressListItem = itemView.findViewById<TextView>(R.id.addressListItem)
                val addressListItemEdit = itemView.findViewById<ImageView>(R.id.edit_address)
                addressListItem.text = i.address1 + ", " + i.address2 + ", Nepal"
                addressListItemEdit.setOnClickListener {
                    val args = Bundle()
                   args.putString("address1", i.address1)
                   args.putString("address2", i.address2)
                   args.putString("address3", i.address3)
                   args.putString("contact", i.phone)
                   args.putString("addressId", i.id.toString())
                    val bottomSheet = AddressModification()
                    dialog?.dismiss()
                    bottomSheet.arguments = args
                    bottomSheet.show(
                        (this@UserProfile as FragmentActivity).supportFragmentManager,
                        bottomSheet.tag
                    )
                }

                fetchAddressList?.addView(itemView)
            }
            dialog?.setCanceledOnTouchOutside(true)

        } else {
            binding.userProfileView.addAddressSnack(
                this@UserProfile,
                "Address List Empty,Please add your address",
                Snackbar.LENGTH_LONG
            )
        }
    }

    private fun getDeleteResponse() {

        viewModel.deleteAddress.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let {
                        dialog?.show()
                        Log.i("TAG", "getDeleteResponse: " + response)
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->

                        binding.userProfileView.errorSnack(message, Snackbar.LENGTH_LONG)
                    }

                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun getStatusColor() {
        val window: Window = window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.sub_color)
    }


    private fun editPassword() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.user_password_change)
        val oldPassword = dialog.findViewById<TextView>(R.id.old_password)
        val newPassword = dialog.findViewById<EditText>(R.id.new_password)
        val confirmPassword = dialog.findViewById<EditText>(R.id.confirm_password)

        val btnSummit = dialog.findViewById<Button>(R.id.continuebtn)
        val btnCancel = dialog.findViewById<Button>(R.id.cancel)
        btnCancel.setOnClickListener { dialog.dismiss() }
        btnSummit.setOnClickListener {
            val oldPasswordString = oldPassword.text.toString()
            val newPasswordString = newPassword.text.toString()
            val confirmPasswordString = confirmPassword.text.toString()
            if (newPasswordString == confirmPasswordString) {
                passwordChange(oldPasswordString, newPasswordString, dialog)
            } else {
                confirmPassword.error = "New and Confirm Password is not equal."
            }

        }
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()
    }

    private fun passwordChange(
        oldPasswordString: String,
        newPasswordString: String,
        dialog: Dialog
    ) {
        val encrOldPassword: String? = Encrypt.encrypt(value1, oldPasswordString)
        val encrNewPassword: String? = Encrypt.encrypt(value1, newPasswordString)
        if (encrNewPassword?.length!! > 6 && encrOldPassword != null) {
            val body =
                RequestBodies.UpdatePassword(encrOldPassword, encrNewPassword)
            tokenManager?.let { it1 -> viewModel.updatePasswordDetails(it1, body) }

        }


        viewModel.updatePassword.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { picsResponse ->
                        dialog.dismiss()
                        var res = response.data.error
                        if (res > 0) {
                            binding.userProfileView.errorSnack(
                                response.data.message,
                                Snackbar.LENGTH_LONG
                            )
                        } else {
                            binding.userProfileView.showSnack(
                                response.data.message,
                                Snackbar.LENGTH_LONG
                            )

                        }
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        dialog.dismiss()
                        binding.userProfileView.errorSnack(message, Snackbar.LENGTH_LONG)
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

    override fun onConnectionFailed(p0: ConnectionResult) {

    }
}
