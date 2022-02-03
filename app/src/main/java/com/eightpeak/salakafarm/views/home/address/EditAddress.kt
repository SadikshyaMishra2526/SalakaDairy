package com.eightpeak.salakafarm.views.home.address

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.eightpeak.salakafarm.database.UserPrefManager
import com.eightpeak.salakafarm.databinding.EditAddressBinding
import com.eightpeak.salakafarm.repository.AppRepository
import com.eightpeak.salakafarm.serverconfig.RequestBodies
import com.eightpeak.salakafarm.serverconfig.network.TokenManager
import com.eightpeak.salakafarm.utils.Constants
import com.eightpeak.salakafarm.utils.subutils.Resource
import com.eightpeak.salakafarm.utils.subutils.errorSnack
import com.eightpeak.salakafarm.utils.subutils.showSnack
import com.eightpeak.salakafarm.viewmodel.UserProfileViewModel
import com.eightpeak.salakafarm.viewmodel.ViewModelProviderFactory
import com.eightpeak.salakafarm.views.home.ui.user_profile.Encrypt
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.google.android.material.snackbar.Snackbar

class EditAddress : AppCompatActivity()  {
    private lateinit var binding: EditAddressBinding
    private var tokenManager: TokenManager? = null
    lateinit var userPrefManager: UserPrefManager
    private lateinit var mMap: GoogleMap
    private var latestLat: Double = 0.0
    private var latestLng: Double = 0.0

    private lateinit var viewModel: UserProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = EditAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tokenManager = TokenManager.getInstance(
            getSharedPreferences(
                Constants.TOKEN_PREF,
                MODE_PRIVATE
            )
        )
        userPrefManager = UserPrefManager(this)
        init()
       binding.address1.setText(intent.getStringExtra("address1").toString())
       binding.address2.setText(intent.getStringExtra("address2").toString())
       binding.address3.setText(intent.getStringExtra("address3").toString())
       binding.phone.setText(intent.getStringExtra("contact").toString())
       val addressId=intent.getStringExtra("addressId").toString()
        Log.i("TAG", "viewAddressList: Edit"+addressId)

        binding.addAddress.setOnClickListener {
            if (binding.address1.text != null) {
                val address1: String = binding.address1.text.toString()
                val address2: String = binding.address2.text.toString()
                val address3: String = binding.address3.text.toString()
                val contact: String = binding.phone.text.toString()
                val addressBody = Encrypt.getEncrptedValue(address1)?.let { it1 ->
                    Encrypt.getEncrptedValue(address2)?.let { it2 ->
                        Encrypt.getEncrptedValue(address3)?.let { it3 ->
                            Encrypt.getEncrptedValue(contact)?.let { it4 ->
                                Encrypt.getEncrptedValue(addressId)?.let { it5->
                                    RequestBodies.EditAddress(
                                        it1,
                                        it2,
                                        it3,
                                        it4,
                                        it5
                                       )
                                     }
                                    }
                                }
                            }
                        }
                tokenManager?.let {
                    if (addressBody != null) {
                        viewModel.editAddressDetails(it, addressBody)
                    }
                }
                viewModel.editAddress.observe(this, Observer { response ->
                    when (response) {
                        is Resource.Success -> {
                            hideProgressBar()
                            response.data?.let { picsResponse ->
                                if(picsResponse.error!=0){
                                    binding.editAddressDetails.errorSnack(picsResponse.message, Snackbar.LENGTH_LONG)
                                  }else{
                                 binding.editAddressDetails.showSnack(picsResponse.message,Snackbar.LENGTH_SHORT)
                                }

                            }
                        }

                        is Resource.Error -> {
                            hideProgressBar()
                            response.message?.let { message ->
                                binding.editAddressDetails.errorSnack(message, Snackbar.LENGTH_LONG)
                            }
                        }

                        is Resource.Loading -> {
                            showProgressBar()
                        }
                    }
                })
            } else {
                Toast.makeText(
                    this@EditAddress,
                    "Please add Addresses first",
                    Toast.LENGTH_SHORT
                ).show()

            }
        }
    }

    fun onAddAddressClicked() {


    }

    private fun init() {
        val repository = AppRepository()
        val factory = ViewModelProviderFactory(application, repository)
        viewModel = ViewModelProvider(this, factory).get(UserProfileViewModel::class.java)

    }


    private fun changelocation(map: GoogleMap) {
        map.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
            override fun onMarkerDragStart(arg0: Marker) {
                // TODO Auto-generated method stub
                Log.d(
                    "System out",
                    "onMarkerDragStart..." + arg0.position.latitude + "..." + arg0.position.longitude
                )
            }

            override fun onMarkerDragEnd(arg0: Marker) {
                // TODO Auto-generated method stub
                Log.d(
                    "System out",
                    "onMarkerDragEnd..." + arg0.position.latitude + "..." + arg0.position.longitude
                )
                latestLat = arg0.position.latitude
                latestLng = arg0.position.longitude
                map.animateCamera(CameraUpdateFactory.newLatLng(arg0.position))
            }

            override fun onMarkerDrag(arg0: Marker) {
                // TODO Auto-generated method stub
                Log.i("System out", "onMarkerDrag...")
            }
        })
    }

    private fun hideProgressBar() {
        binding.progress.visibility = View.GONE
    }

    private fun showProgressBar() {
        binding.progress.visibility = View.VISIBLE
    }
}