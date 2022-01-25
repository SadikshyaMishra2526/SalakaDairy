package com.eightpeak.salakafarm.views.home.ui.user_profile

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
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

import android.widget.Toast

import android.widget.RadioButton
import android.widget.RadioGroup
import coil.api.load
import com.eightpeak.salakafarm.databinding.ActivityUserProfileBinding
import com.eightpeak.salakafarm.utils.EndPoints.Companion.BASE_URL
import com.eightpeak.salakafarm.views.home.address.AddressModification
import com.eightpeak.salakafarm.utils.GeneralUtils
import com.eightpeak.salakafarm.utils.subutils.Resource
import com.eightpeak.salakafarm.utils.subutils.addAddressSnack
import com.eightpeak.salakafarm.utils.subutils.errorSnack
import com.eightpeak.salakafarm.utils.subutils.showSnack
import com.eightpeak.salakafarm.views.home.address.EditAddress
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileNotFoundException

class UserProfile : AppCompatActivity() {

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
        binding.userPhone.text = userPrefManager.contactNo
        if(userPrefManager.avatar.length>5){
            if(userPrefManager.avatar.contains("https://")){
                binding.customerAvatar.load(userPrefManager.avatar)
            }else{
                binding.customerAvatar.load(BASE_URL+userPrefManager.avatar)
            }

        }else{
            binding.customerAvatar.load("https://salakafarm.com/public/data/logo/Untitled.png")

        }
        dialog = Dialog(this)

        binding.userLogout.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(R.string.logout)
            builder.setMessage(R.string.logout_msg)
            builder.setPositiveButton(R.string.logout,
                DialogInterface.OnClickListener { _, _ ->
                    viewModel.requestLogout(tokenManager!!)
                     getLogoutResponse()

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
//            mGoogleSignInClient.signOut()
//                .addOnCompleteListener(this, object : OnCompleteListener<Void?>() {
//                    fun onComplete(task: Task<Void?>) {
//                        // ...
//                    }
//                })


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
        getAddressList()

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
                val addressListItemDelete = itemView.findViewById<ImageView>(R.id.delete_address)
                addressListItem.text = i.address1 + ", " + i.address2 + ", Nepal"
                addressListItemEdit.setOnClickListener {
                    val intentEditAddress = Intent(this@UserProfile, EditAddress::class.java)
                    intentEditAddress.putExtra("address1", i.address1)
                    intentEditAddress.putExtra("address2", i.address2)
                    intentEditAddress.putExtra("address3", i.address3)
                    intentEditAddress.putExtra("contact", i.phone)
                    intentEditAddress.putExtra("addressId", i.id.toString())
                    Log.i("TAG", "viewAddressList: " + i.id.toString())
                    dialog?.dismiss()
                    startActivity(intentEditAddress)
                }
//                addressListItemDelete.setOnClickListener {
//                    tokenManager?.let { it1 ->
//                        viewModel.deleteAddressDetails(
//                            it1,
//                            i.id.toString()
//                        )
//                    }
//                    getDeleteResponse()
//                }
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

    private fun editProfile() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.layout_edit_profile)
        val firstName = dialog.findViewById<EditText>(R.id.customer_first_name)
        val lastName = dialog.findViewById<EditText>(R.id.customer_last_name)
        val userDoB = dialog.findViewById<TextView>(R.id.customer_DOB)
        val btnSummit = dialog.findViewById<Button>(R.id.continuebtn)
        val btnCancel = dialog.findViewById<Button>(R.id.cancel)
        val customerAvatar = dialog.findViewById<ImageView>(R.id.customer_avatar)
        customerAvatar.setOnClickListener {
            val photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"
            startActivityForResult(
                photoPickerIntent,
                RESULT_LOAD_IMG
            )
        }
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
        firstName.setText(userPrefManager.firstName)
        lastName.setText(userPrefManager.lastName)


        btnSummit.setOnClickListener {
            val userGender =
                dialog.findViewById<RadioGroup>(R.id.customer_gender).checkedRadioButtonId
            val gender = dialog.findViewById<View>(userGender) as RadioButton

            val name = firstName.text.toString()
            val email = lastName.text.toString()
            val dob = userDoB.text.toString()
            val genderString = gender.text.toString()
            if (dob.isNotEmpty()) {
                if (userGender != null) {
                    changeUserProfile(name, email, dob, genderString, profilePicture, dialog)
                } else {
                    changeUserProfile(name, email, dob, "", profilePicture, dialog)
                }
            } else {

                if (genderString.isNotEmpty()) {
                    changeUserProfile(name, email, "", genderString, profilePicture, dialog)
                } else {
                    changeUserProfile(name, email, "", "", profilePicture, dialog)
                }
            }
            GeneralUtils.hideKeyboard(this)
        }
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK) {
            try {
                val imageUri = data?.data
                val imageStream = contentResolver?.openInputStream(imageUri!!)
                val selectedImage = BitmapFactory.decodeStream(imageStream)
                val customerAvatar = dialog?.findViewById<ImageView>(R.id.customer_avatar)
                customerAvatar?.setImageBitmap(selectedImage)
                val file = File(imageUri?.let { getRealPathFromURI(it) })
//
                val requestFile: RequestBody = file.let {
                    RequestBody.create(
                        "multipart/form-data".toMediaTypeOrNull(),
                        it
                    )
                }!!

                profilePicture = MultipartBody.Part
                    .createFormData("avatar", file.name, requestFile)


            } catch (e: FileNotFoundException) {
                e.printStackTrace()
                Toast.makeText(this@UserProfile, "Something went wrong", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(this@UserProfile, "Please Choose your image", Toast.LENGTH_LONG).show()
        }
    }

    private fun getRealPathFromURI(contentURI: Uri): String? {
        val result: String
        val cursor: Cursor? = contentResolver.query(contentURI, null, null, null, null)
        if (cursor == null) {
            result = contentURI.path.toString()
        } else {
            cursor.moveToFirst()
            val idx: Int = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            result = cursor.getString(idx)
            cursor.close()
        }
        return result
    }

    private fun changeUserProfile(
        name: String,
        email: String,
        dob: String,
        gender: String,
        profilePicture: MultipartBody.Part,
        dialog: Dialog
    ) {
        val customerName: RequestBody = RequestBody.create(
            MultipartBody.FORM, name
        )
        val customerEmail: RequestBody = RequestBody.create(
            MultipartBody.FORM, email
        )
        val customerDOB: RequestBody = RequestBody.create(
            MultipartBody.FORM, dob
        )
        val customerGender: RequestBody = gender?.let {
            RequestBody.create(
                MultipartBody.FORM, it
            )
        }

        tokenManager?.let { it1 ->

            viewModel.getUserProfileDetails(it1, customerName,customerEmail,customerDOB,customerGender,profilePicture)

        }

        viewModel.userProfileDetails.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let {
                        dialog.dismiss()
                        getUserDetails()
                        Log.i("TAG", "changeUserProfile: " + response.data)
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
        tokenManager?.let { viewModel.userDetailsUser(it) }
        viewModel.userDetailsResponse.observe(this, Observer { event ->
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
                            binding.userName.text =
                                loginResponse.first_name + " " + loginResponse.last_name
                            binding.userEmail.text = loginResponse.email
                            binding.userPhone.text = loginResponse.phone

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
