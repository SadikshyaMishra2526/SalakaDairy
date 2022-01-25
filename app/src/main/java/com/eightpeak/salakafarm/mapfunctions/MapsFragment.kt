package com.eightpeak.salakafarm.mapfunctions

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.util.Linkify
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SnapHelper
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.databinding.ActivityMapsFragmentBinding
import com.eightpeak.salakafarm.repository.AppRepository
import com.eightpeak.salakafarm.serverconfig.network.TokenManager
import com.eightpeak.salakafarm.subscription.attributes.Branches
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
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper

class MapsFragment : Fragment(), OnMapReadyCallback, OnMarkerClickListener{

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsFragmentBinding
    private lateinit var viewModel: SubscriptionViewModel

    private var employeeAdapter: EmployeeAdapter? = null
    private var tokenManager: TokenManager? = null

    private var layoutManager: LinearLayoutManager? = null
    private fun setupViewModel() {
        val repository = AppRepository()
        val factory = ViewModelProviderFactory(requireActivity().application, repository)
        viewModel = ViewModelProvider(this, factory).get(SubscriptionViewModel::class.java)

    }
    private fun init() {
        val snapHelper: SnapHelper = GravitySnapHelper(Gravity.START)
        snapHelper.attachToRecyclerView(binding.branchesRecycler)
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        employeeAdapter = EmployeeAdapter()
        binding.branchesRecycler.layoutManager = layoutManager
        binding.branchesRecycler.setHasFixedSize(true)
        binding.branchesRecycler.isFocusable = false
        binding.branchesRecycler.adapter = employeeAdapter
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

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
        init()
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
                        displayBranchesDetails(response.data.branches)
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
                                        .title("Salaka Farm's Branches")
                                )
                                mark.tag = i
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

    private fun displayBranchesDetails(branches: List<Branches>) {
        employeeAdapter?.differ?.submitList(branches)
        binding.branchesRecycler.adapter = employeeAdapter
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        getBranchesList(googleMap)

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        mMap.isMyLocationEnabled = true;
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