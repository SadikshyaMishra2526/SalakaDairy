package com.eightpeak.salakafarm.subscription.displaysubscription

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.eightpeak.salakafarm.database.UserPrefManager
import com.eightpeak.salakafarm.databinding.FragmentPaymentEvidenceBinding
import com.eightpeak.salakafarm.databinding.LayoutTrackEmpPositionBinding
import com.eightpeak.salakafarm.repository.AppRepository
import com.eightpeak.salakafarm.serverconfig.network.TokenManager
import com.eightpeak.salakafarm.utils.Constants
import com.eightpeak.salakafarm.viewmodel.SubscriptionViewModel
import com.eightpeak.salakafarm.viewmodel.ViewModelProviderFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileNotFoundException

class PaymentEvidenceFragment : BottomSheetDialogFragment() {

    private val RESULT_LOAD_IMG = 101
    private val CAMERA_REQUEST = 1888
    private val MY_CAMERA_PERMISSION_CODE = 100

    private var _binding: FragmentPaymentEvidenceBinding? = null
    private val binding get() = _binding!!
    private var userPrefManager: UserPrefManager? = null
    private lateinit var viewModel: SubscriptionViewModel
    private var tokenManager: TokenManager? = null
    private var subscriptionId: String? = null

    private lateinit var evidence: MultipartBody.Part
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPaymentEvidenceBinding.inflate(inflater, container, false)
        val root: View = binding.root
        userPrefManager = UserPrefManager(context)
        tokenManager = TokenManager.getInstance(
            requireActivity().getSharedPreferences(
                Constants.TOKEN_PREF,
                AppCompatActivity.MODE_PRIVATE
            )
        )
        val mArgs = arguments
        subscriptionId = mArgs!!.getString(Constants.SUBSCRIPTION_ID)


        setupViewModel()
        return root.rootView
    }
    private fun setupViewModel() {
        val repository = AppRepository()
        val factory = ViewModelProviderFactory(requireActivity().application, repository)
        viewModel = ViewModelProvider(this, factory).get(SubscriptionViewModel::class.java)

        getEvidence()
    }

    private fun getEvidence() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            // Do the file write
        } else {
            // Request permission from the user
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 0);
        }
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ), 2000
            )
        }

        binding.evidencePhoto.setOnClickListener(View.OnClickListener {
            val photoPickerIntent = Intent(Intent.ACTION_PICK)
//            photoPickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            photoPickerIntent.type = "image/*"
            startActivityForResult(
                photoPickerIntent,
                RESULT_LOAD_IMG
            )
        })
    }
    fun getFile(url: String): File? {
        val filename = url.hashCode().toString()
        // String filename = URLEncoder.encode(url);
        return File.createTempFile(filename, ".jpg", requireActivity().cacheDir)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK) {
            try {
                val imageUri = data?.data
                val imageStream = activity?.contentResolver?.openInputStream(imageUri!!)
                val selectedImage = BitmapFactory.decodeStream(imageStream)
                    binding.evidencePhoto.setImageBitmap(selectedImage)
                    val file= File(imageUri?.path)

                    val requestFile: RequestBody? = imageUri?.path?.let {
                        getFile(it)?.let {
                            RequestBody.create(
                                imageUri.let { it1 -> activity?.contentResolver?.getType(it1)?.toMediaTypeOrNull() },
                                it
                            )
                        }
                    }
                evidence =
                        requestFile?.let {
                            MultipartBody.Part.createFormData("screenshot", file.name,
                                it
                            )
                        }!!


            } catch (e: FileNotFoundException) {
                e.printStackTrace()
                Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(requireContext(), "Please Choose your image", Toast.LENGTH_LONG).show()
        }
    }

}