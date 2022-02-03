package com.eightpeak.salakafarm.views.home.address

import android.app.AlertDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.database.UserPrefManager
import com.eightpeak.salakafarm.serverconfig.network.TokenManager
import com.eightpeak.salakafarm.utils.Constants
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.eightpeak.salakafarm.databinding.AddAddressDetailsBinding
import com.eightpeak.salakafarm.repository.AppRepository
import com.eightpeak.salakafarm.serverconfig.RequestBodies
import com.eightpeak.salakafarm.utils.subutils.Resource
import com.eightpeak.salakafarm.utils.subutils.errorSnack
import com.eightpeak.salakafarm.viewmodel.UserProfileViewModel
import com.eightpeak.salakafarm.viewmodel.ViewModelProviderFactory
import com.eightpeak.salakafarm.views.home.ui.user_profile.Encrypt

import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener
import com.google.android.material.snackbar.Snackbar


class AddressModification:AppCompatActivity() , OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener {
    private lateinit var binding: AddAddressDetailsBinding
    private var tokenManager: TokenManager? = null
    lateinit var userPrefManager: UserPrefManager
    private lateinit var mMap: GoogleMap
    private var latestLat: Double = 0.0
    private var latestLng: Double = 0.0

    private lateinit var viewModel: UserProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AddAddressDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        tokenManager = TokenManager.getInstance(
            getSharedPreferences(
                Constants.TOKEN_PREF,
                MODE_PRIVATE
            )
        )
        userPrefManager = UserPrefManager(this)

        init()

        binding.addAddress.setOnClickListener {
            addAddress()
        }
    }


    private fun init() {
        val repository = AppRepository()
        val factory = ViewModelProviderFactory(application, repository)
        viewModel = ViewModelProvider(this, factory).get(UserProfileViewModel::class.java)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        val sydney =
            LatLng(userPrefManager.currentLat.toDouble(), userPrefManager.currentLng.toDouble())

        latestLat = userPrefManager.currentLat.toDouble()
        latestLng = userPrefManager.currentLng.toDouble()
        mMap = googleMap
        var mark: Marker? = null
        mark = mMap.addMarker(
            MarkerOptions()
                .position(sydney)
//                .snippet(personDetails.toString())
                .icon(
                    BitmapDescriptorFactory.defaultMarker(
                        BitmapDescriptorFactory.HUE_ORANGE
                    )
                )
                .title("Your Current Position")
                .draggable(true)
        )
//        mark.setTag(i)
//                                markerList.add(mark)
        val center = CameraUpdateFactory.newLatLng(sydney)
        val zoom = CameraUpdateFactory.zoomTo(16f)
        mark.showInfoWindow()
        mMap.moveCamera(center)
        mMap.animateCamera(zoom)
        changelocation(mMap)
    }

    private fun changelocation(map: GoogleMap) {
        map.setOnMarkerDragListener(object : OnMarkerDragListener {
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

    override fun onMarkerClick(marker: Marker): Boolean {
        var clickCount = marker.tag as Int
        clickCount += 1
        marker.tag = clickCount
        val builder = AlertDialog.Builder(this)
        builder.setTitle(marker.title)
        builder.setMessage(marker.snippet)
        builder.show()
        return false
    }

    private fun hideProgressBar() {
        binding.progress.visibility = View.GONE
    }

    private fun showProgressBar() {
        binding.progress.visibility = View.VISIBLE
    }


    private fun addAddress() {
        if (binding.address1.text.toString().isNotEmpty() && binding.address2.text.toString().isNotEmpty() && binding.address3.text.toString().isNotEmpty() && binding.phone.text.toString().isNotEmpty()) {
            val address1: String = binding.address1.text.toString()
            val address2: String = binding.address2.text.toString()
            val address3: String = binding.address3.text.toString()
            val contact: String = binding.phone.text.toString()
            val addressBody = Encrypt.getEncrptedValue(address1)?.let { it1 ->
                Encrypt.getEncrptedValue(address2)?.let { it2 ->
                    Encrypt.getEncrptedValue(address3)?.let { it3 ->
                        Encrypt.getEncrptedValue(contact)?.let { it4 ->
                            Encrypt.getEncrptedValue(latestLat.toString())?.let { it5 ->
                                Encrypt.getEncrptedValue(latestLng.toString())?.let { it6 ->
                                    RequestBodies.AddAddress(
                                        it1,
                                        it2,
                                        it3,
                                        it4,
                                        it5,
                                        it6
                                    )
                                }
                            }
                        }
                    }
                }
            }

            tokenManager?.let {
                if (addressBody != null) {
                    viewModel.addNewAddressDetails(it, addressBody)
                }
            }
            viewModel.addNewAddress.observe(this, Observer { response ->
                when (response) {
                    is Resource.Success -> {
                        hideProgressBar()
                        response.data?.let { picsResponse ->
                            finish()
                            Log.i("TAG", "onCreate: addNewAddress")
                        }
                    }

                    is Resource.Error -> {
                        hideProgressBar()
                        response.message?.let { message ->
                            binding.addAddressDetails.errorSnack(message, Snackbar.LENGTH_LONG)
                        }
                    }

                    is Resource.Loading -> {
                        showProgressBar()
                    }
                }
            })
        } else {
            binding.addAddressDetails.errorSnack(
                getString(R.string.address_error),
                Snackbar.LENGTH_LONG
            )
        }
    }
}