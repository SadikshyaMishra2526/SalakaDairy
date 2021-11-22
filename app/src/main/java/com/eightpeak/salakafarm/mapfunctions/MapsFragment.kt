package com.eightpeak.salakafarm.mapfunctions

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.util.Linkify
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.databinding.ActivityMapsFragmentBinding
import com.eightpeak.salakafarm.repository.AppRepository
import com.eightpeak.salakafarm.serverconfig.network.TokenManager
import com.eightpeak.salakafarm.utils.Constants
import com.eightpeak.salakafarm.utils.subutils.Resource
import com.eightpeak.salakafarm.viewmodel.SubscriptionViewModel
import com.eightpeak.salakafarm.viewmodel.ViewModelProviderFactory
import com.google.android.gms.maps.*
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener

import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.eightpeak.salakafarm.utils.subutils.errorSnack

class MapsFragment : Fragment(), OnMapReadyCallback, OnMarkerClickListener{

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsFragmentBinding
    private lateinit var viewModel: SubscriptionViewModel

    private var tokenManager: TokenManager? = null

    private fun setupViewModel() {
        val repository = AppRepository()
        val factory = ViewModelProviderFactory(requireActivity().application, repository)
        viewModel = ViewModelProvider(this, factory).get(SubscriptionViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = ActivityMapsFragmentBinding.inflate(layoutInflater, container, false)
        tokenManager = TokenManager.getInstance(
            requireActivity().getSharedPreferences(
                Constants.TOKEN_PREF,
                AppCompatActivity.MODE_PRIVATE
            )
        )
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        setupViewModel()
        return binding.mapfragment
    }


    private fun getBranchesList(googleMap: GoogleMap) {
        tokenManager?.let { it1 -> viewModel.getBranchesList(it1) }

        viewModel.branchesResponse.observe(requireActivity(), Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()

                    response.data?.let {
                        var branches=response.data.branches
                         mMap=googleMap
                        if (branches.size != 0) {
                            for (i in branches.indices) {
                                val personDetails = SpannableString(
                                    """
                  Outlet Name :- ${branches.get(i).name.toString()}
                  Contact Number :- ${branches.get(i).contact.toString()}
                  Outlet Location :- ${branches.get(i).address}
                  """.trimIndent()
                                )
                                val branchesPoints = LatLng(
                                    branches.get(i).latitude,
                                    branches.get(i).longitude
                                )
                                Linkify.addLinks(personDetails, Linkify.PHONE_NUMBERS)

                                var mark: Marker? = null
                                mark = mMap.addMarker(
                                    MarkerOptions()
                                        .position(branchesPoints)
                                        .snippet(personDetails.toString())
                                        .icon(
                                            BitmapDescriptorFactory.defaultMarker(
                                                BitmapDescriptorFactory.HUE_ORANGE
                                            )
                                        )
                                        .title("Salaka Shop Outlets")
                                )
                                mark.setTag(i)
//                                markerList.add(mark)
                                val center = CameraUpdateFactory.newLatLng(branchesPoints)
                                val zoom = CameraUpdateFactory.zoomTo(10f)
                                mark.showInfoWindow()
                                mMap.moveCamera(center)
                                mMap.animateCamera(zoom)
                            }
                            mMap.setOnMarkerClickListener(this)
                        }
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        binding.mapfragment.errorSnack(message, Snackbar.LENGTH_LONG)
                    }
                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        getBranchesList(googleMap)

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

    override fun onMarkerClick(marker: Marker): Boolean {
        var clickCount = marker.tag as Int
        clickCount += 1
        marker.tag = clickCount
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(marker.title)
        builder.setMessage(marker.snippet)
        builder.show()
        return false
    }


}