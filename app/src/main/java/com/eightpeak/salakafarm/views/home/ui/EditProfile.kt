package com.eightpeak.salakafarm.views.home.ui

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.database.Cursor
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.database.UserPrefManager
import com.eightpeak.salakafarm.databinding.AlterSubscriptionLayoutBinding
import com.eightpeak.salakafarm.databinding.LayoutEditProfileBinding
import com.eightpeak.salakafarm.repository.AppRepository
import com.eightpeak.salakafarm.serverconfig.RequestBodies
import com.eightpeak.salakafarm.serverconfig.network.TokenManager
import com.eightpeak.salakafarm.subscription.displaysubscription.DisplaySubscriptionDetails
import com.eightpeak.salakafarm.utils.Constants
import com.eightpeak.salakafarm.utils.GeneralUtils
import com.eightpeak.salakafarm.utils.subutils.Resource
import com.eightpeak.salakafarm.utils.subutils.errorSnack
import com.eightpeak.salakafarm.utils.subutils.showSnack
import com.eightpeak.salakafarm.viewmodel.SubscriptionViewModel
import com.eightpeak.salakafarm.viewmodel.UserProfileViewModel
import com.eightpeak.salakafarm.viewmodel.ViewModelProviderFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileNotFoundException
import java.util.*

class EditProfile(private val onClickItem: (firstName: String, lastName: String, Phone: String, Photo: String) -> Unit) :
    BottomSheetDialogFragment() {

    private var _binding: LayoutEditProfileBinding? = null
    private val binding get() = _binding!!
    private var userPrefManager: UserPrefManager? = null
    private lateinit var viewModel: UserProfileViewModel
    private var tokenManager: TokenManager? = null
    private val RESULT_LOAD_IMG = 101

    private lateinit var profilePicture: MultipartBody.Part
    var dateSelected: Calendar = Calendar.getInstance()

    var value1: ByteArray = GeneralUtils.decoderfun(Constants.SECRET_KEY)
    private var datePickerDialog: DatePickerDialog? = null

    private lateinit var gender: RadioButton
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = LayoutEditProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        userPrefManager = UserPrefManager(context)
        tokenManager = TokenManager.getInstance(
            requireActivity().getSharedPreferences(
                Constants.TOKEN_PREF,
                AppCompatActivity.MODE_PRIVATE
            )
        )
        setupViewModel()
        editProfile()
        return root.rootView
    }

    private fun setupViewModel() {
        val repository = AppRepository()
        val factory = ViewModelProviderFactory(requireActivity().application, repository)
        viewModel = ViewModelProvider(this, factory).get(UserProfileViewModel::class.java)

    }

    private fun editProfile() {
        binding.customerAvatar.setOnClickListener {
            val photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"
            startActivityForResult(
                photoPickerIntent,
                RESULT_LOAD_IMG
            )
        }
        binding.customerDOB.setOnClickListener {
            val newCalendar = dateSelected
            val dateFormat: java.text.DateFormat? = DateFormat.getDateFormat(requireContext())
            datePickerDialog = DatePickerDialog(
                requireContext(),
                { view, year, monthOfYear, dayOfMonth ->
                    dateSelected[year, monthOfYear, dayOfMonth, 0] = 0
                    binding.customerDOB.text = dateFormat?.format(dateSelected.time)
                },
                newCalendar[Calendar.YEAR],
                newCalendar[Calendar.MONTH],
                newCalendar[Calendar.DAY_OF_MONTH]
            )
            datePickerDialog!!.show()

        }
        binding.customerFirstName.setText(userPrefManager?.firstName)
        binding.customerLastName.setText(userPrefManager?.lastName)


        binding.continuebtn.setOnClickListener {
//            val userGender = findViewById<RadioGroup>(R.id.customer_gender).checkedRadioButtonId


            val name = binding.customerFirstName.text.toString()
            val email = binding.customerLastName.text.toString()
            val dob = binding.customerDOB.text.toString()
            val genderString = "male"
            if (dob.isNotEmpty()) {
                if (genderString != null) {
                    changeUserProfile(name, email, dob, genderString, profilePicture)
                } else {
                    changeUserProfile(name, email, dob, "", profilePicture)
                }
            } else {

                if (genderString.isNotEmpty()) {
                    changeUserProfile(name, email, "", genderString, profilePicture)
                } else {
                    changeUserProfile(name, email, "", "", profilePicture)
                }
            }
            GeneralUtils.hideKeyboard(requireActivity())
        }
        binding.cancel.setOnClickListener { dismissAllowingStateLoss()}
    }

    private fun changeUserProfile(
        name: String,
        email: String,
        dob: String,
        gender: String,
        profilePicture: MultipartBody.Part,
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

            viewModel.getUserProfileDetails(
                it1,
                customerName,
                customerEmail,
                customerDOB,
                customerGender,
                profilePicture
            )

        }

        viewModel.userProfileDetails.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let {
                        userPrefManager?.firstName = response.data.customer.first_name
                        userPrefManager?.lastName = response.data.customer.last_name
                        userPrefManager?.contactNo = response.data.customer.phone
                        userPrefManager?.email = response.data.customer.email
                        onClickItem(
                            response.data.customer.first_name,
                            response.data.customer.last_name,
                            response.data.customer.phone,
                            response.data.customer.email
                        )
                        dismissAllowingStateLoss()
                        Log.i("TAG", "changeUserProfile: " + response.data)
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        binding.editProfile.errorSnack(message, Snackbar.LENGTH_LONG)
                    }

                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }


    private fun getRealPathFromURI(contentURI: Uri): String? {
        val result: String
        val cursor: Cursor? =
            requireActivity().contentResolver.query(contentURI, null, null, null, null)
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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK) {
            try {
                val imageUri = data?.data
                val imageStream = requireActivity().contentResolver?.openInputStream(imageUri!!)
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
                Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(requireContext(), "Please Choose your image", Toast.LENGTH_LONG).show()
        }
    }


    private fun hideProgressBar() {
        binding.progress.visibility = View.GONE
    }

    private fun showProgressBar() {
        binding.progress.visibility = View.VISIBLE
    }


}