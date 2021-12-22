package com.eightpeak.salakafarm.views.home.ui.user_profile

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.database.UserPrefManager
import android.view.Window

import androidx.core.content.ContextCompat

import android.view.WindowManager
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.eightpeak.salakafarm.App
import com.eightpeak.salakafarm.repository.AppRepository
import com.eightpeak.salakafarm.serverconfig.RequestBodies
import com.eightpeak.salakafarm.serverconfig.network.TokenManager
import com.eightpeak.salakafarm.utils.Constants
import com.eightpeak.salakafarm.utils.subutils.Resource
import com.eightpeak.salakafarm.viewmodel.UserProfileViewModel
import com.eightpeak.salakafarm.viewmodel.ViewModelProviderFactory
import com.eightpeak.salakafarm.views.addtocart.CartActivity
import com.eightpeak.salakafarm.views.comparelist.CompareListActivity
import com.eightpeak.salakafarm.views.home.HomeActivity
import com.eightpeak.salakafarm.views.order.orderview.orderhistory.OrderHistory
import com.eightpeak.salakafarm.views.wishlist.WishlistActivity
import com.google.android.material.snackbar.Snackbar
import java.util.*

import com.eightpeak.salakafarm.views.addresslist.AddressListModel
import com.eightpeak.salakafarm.utils.subutils.errorSnack

import java.lang.Exception
import android.widget.Toast

import android.widget.RadioButton
import android.widget.RadioGroup
import com.eightpeak.salakafarm.databinding.ActivityUserProfileBinding
import com.eightpeak.salakafarm.utils.subutils.addAddressSnack
import com.eightpeak.salakafarm.views.home.address.AddressModification


class UserProfile : AppCompatActivity() {

    private lateinit var userPrefManager: UserPrefManager
    private var _binding: ActivityUserProfileBinding? = null

    private lateinit var binding: ActivityUserProfileBinding
    private lateinit var viewModel: UserProfileViewModel

    var dateSelected: Calendar = Calendar.getInstance()

//    var value1: ByteArray = GeneralUtils.decoderfun(Constants.SECRET_KEY)
    private var dialog: Dialog? = null
    private var datePickerDialog: DatePickerDialog? = null
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
        dialog = Dialog(this)

        binding.userPhone.text = userPrefManager.contactNo
        binding.userLogout.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(R.string.logout)
            builder.setMessage(R.string.logout_msg)
            builder.setPositiveButton(R.string.logout,
                DialogInterface.OnClickListener { _, _ ->
                    userPrefManager.clearData()
                    tokenManager?.deleteToken()
                    startActivity(Intent(this@UserProfile, HomeActivity::class.java))
                    finish()
                })
            builder.setNegativeButton(R.string.cancel, null)

            val dialog = builder.create()
            dialog.show()

        }

        binding.editProfileLayout.setOnClickListener {
            editProfile()
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
             val intent = Intent(this@UserProfile, AddressModification::class.java)
//              intent.putExtra(Constants.ORDER_STATUS, orderHistory.orderlist[i].status.toString())
             startActivity(intent)

         }
        binding.editAddress.setOnClickListener {
            dialog!!.show()
        }
        init()
        viewAddress()
        getAddressList()

    }

    private fun getAddressList() {
        tokenManager?.let { it1 -> viewModel.getUserAddressList(it1) }
        var addressListString = ""
        viewModel.userAddressList.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { picsResponse ->
                        if(picsResponse.address_list.isNotEmpty()){
                            viewAddressList(picsResponse)
                            for (i in picsResponse.address_list.indices) {
                                addressListString =addressListString+
                                        picsResponse.address_list[i].address1 + " " + picsResponse.address_list[i].address2 + " Nepal" + "\n"
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

    private fun viewAddress() {
        if(userPrefManager.addressList!=null){
            binding.addAddressTv.visibility=View.GONE
            var addressListModel: List<String> =
                userPrefManager.addressList.split("\n") as List<String>
            Log.i("TAG", "viewAddress: "+addressListModel.size)

            for (i in addressListModel) {
                val itemView: View =
                    LayoutInflater.from(this)
                        .inflate(R.layout.address_list_item, binding.viewAddressList, false)

                val addressListItem = itemView.findViewById<TextView>(R.id.addressListItem)
                val addressListItemEdit = itemView.findViewById<ImageView>(R.id.edit_address)
                val addressListItemDelete = itemView.findViewById<ImageView>(R.id.delete_address)
                addressListItem.text = i

                addressListItemEdit.visibility = View.GONE
                addressListItemDelete.visibility = View.GONE
                binding.viewAddressList.addView(itemView)
            }
        }else{
            binding.addAddressTv.visibility=View.VISIBLE
        }
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
                val addressListItemDelete = itemView.findViewById<ImageView>(R.id.delete_address)
                addressListItem.text = i.address1 + ", " + i.address2 + ", Nepal"
                addressListItemEdit.setOnClickListener {

                }
                Log.i("TAG", "viewAddressList: "+i.id.toString())
                addressListItemDelete.setOnClickListener {
                    tokenManager?.let { it1 ->
                        viewModel.deleteAddressDetails(
                            it1,
                            i.id.toString()
                        )
                    }
                    getDeleteResponse()
                }

                fetchAddressList?.addView(itemView)
            }

            dialog?.setCanceledOnTouchOutside(true)

        }else{
            binding.userProfileView.addAddressSnack(this@UserProfile,"Address List Empty,Please add your address", Snackbar.LENGTH_LONG)
        }
    }

    private fun getDeleteResponse() {

        viewModel.deleteAddress.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let {
//                        finish();
//                        overridePendingTransition( 0, 0)
//                        startActivity(intent);
//                        overridePendingTransition( 0, 0)
                        dialog?.show()
                        Log.i("TAG", "getDeleteResponse: "+response)
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
        btnCancel.setOnClickListener { dialog.show() }
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
        try {
//            val encrOldPassword = Encrypt.encrypt(value1, oldPasswordString)
//            val encrNewPassword = Encrypt.encrypt(value1, newPasswordString)
//            val body = RequestBodies.UpdatePassword(
//                encrOldPassword, encrNewPassword
//            )
//            tokenManager?.let { it1 -> viewModel.updatePasswordDetails(it1, body) }


        } catch (e: Exception) {
            e.printStackTrace()
        }


        viewModel.updatePassword.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { picsResponse ->
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

    private fun editProfile() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.layout_edit_profile)
        val firstName = dialog.findViewById<EditText>(R.id.customer_first_name)
        val lastName = dialog.findViewById<EditText>(R.id.customer_last_name)
        val userGender = dialog.findViewById<RadioGroup>(R.id.customer_gender)
        val userDoB = dialog.findViewById<TextView>(R.id.customer_DOB)
        val btnSummit = dialog.findViewById<Button>(R.id.continuebtn)
        val btnCancel = dialog.findViewById<Button>(R.id.cancel)
        btnCancel.setOnClickListener { dialog.dismiss() }
        userDoB.setOnClickListener {
            val newCalendar = dateSelected
            val dateFormat: java.text.DateFormat? = DateFormat.getDateFormat(applicationContext)
            datePickerDialog = DatePickerDialog(
                this,
                { view, year, monthOfYear, dayOfMonth ->
                    dateSelected[year, monthOfYear, dayOfMonth, 0] = 0
                    userDoB.text = dateFormat?.format(dateSelected.time)
                },
                newCalendar[Calendar.YEAR],
                newCalendar[Calendar.MONTH],
                newCalendar[Calendar.DAY_OF_MONTH]
            )
            datePickerDialog!!.show()
            Log.i("TAG", "onCreate: " + dateSelected.time)

        }
        val selectedId: Int = userGender.checkedRadioButtonId

        Log.i("TAG", "editProfile: " + selectedId)


        btnSummit.setOnClickListener {

             val radioButtonID = userGender.checkedRadioButtonId
            val radioButton = dialog.findViewById<View>(radioButtonID) as RadioButton


            val name = firstName.text.toString()
            val email = lastName.text.toString()
            val dob = userDoB.text.toString()
            val gender =radioButton.text as String
            Log.i("TAG", "editProfile: "+gender)
            changeUserProfile(name, email, dob, "gender",dialog)
        }
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()
    }

    private fun changeUserProfile(
        name: String,
        email: String,
        dob: String,
        gender: String?,
        dialog: Dialog
    ) {
        val body = gender?.let {
            RequestBodies.UserProfile(
                name, email, dob, gender
            )


        }
        tokenManager?.let { it1 ->
            if (body != null) {
                viewModel.getUserProfileDetails(it1, body)
            }
        }

        viewModel.userProfileDetails.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { picsResponse ->
                        dialog.dismiss()
                        getUserDetails()
                        Log.i("TAG", "changeUserProfile: "+response.data)
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
    private fun getUserDetails() {
        var addressListString =""
        tokenManager?.let { viewModel.userDetailsUser(it) }
        viewModel.userDetailsResponse.observe(this, Observer { event ->
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
                            val mainActivity = Intent(this@UserProfile, UserProfile::class.java)
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
