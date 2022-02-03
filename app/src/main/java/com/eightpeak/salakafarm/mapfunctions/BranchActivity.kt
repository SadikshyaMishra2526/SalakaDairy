package com.eightpeak.salakafarm.mapfunctions

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.SpannableString
import android.text.util.Linkify
import android.view.Gravity
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SnapHelper
import coil.api.load
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.databinding.ActivityMapsFragmentBinding
import com.eightpeak.salakafarm.repository.AppRepository
import com.eightpeak.salakafarm.serverconfig.network.TokenManager
import com.eightpeak.salakafarm.subscription.attributes.Branches
import com.eightpeak.salakafarm.utils.Constants
import com.eightpeak.salakafarm.utils.EndPoints.Companion.BASE_URL
import com.eightpeak.salakafarm.utils.subutils.Resource
import com.eightpeak.salakafarm.utils.subutils.errorSnack
import com.eightpeak.salakafarm.viewmodel.SubscriptionViewModel
import com.eightpeak.salakafarm.viewmodel.ViewModelProviderFactory
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar


class BranchActivity : AppCompatActivity(), OnMapReadyCallback, OnMarkerClickListener{

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsFragmentBinding
    private lateinit var viewModel: SubscriptionViewModel

    private var employeeAdapter: EmployeeAdapter? = null
    private var tokenManager: TokenManager? = null

    private var layoutManager: LinearLayoutManager? = null
    private fun setupViewModel() {
        val repository = AppRepository()
        val factory = ViewModelProviderFactory(application, repository)
        viewModel = ViewModelProvider(this, factory).get(SubscriptionViewModel::class.java)

    }
    private fun init() {
        val snapHelper: SnapHelper = GravitySnapHelper(Gravity.START)
        snapHelper.attachToRecyclerView(binding.branchesRecycler)
        layoutManager = LinearLayoutManager(this@BranchActivity, LinearLayoutManager.HORIZONTAL, false)
        employeeAdapter = EmployeeAdapter(onClickListener = { view, branch ->
            displayDetails(
                view,
                branch
            )
        }
        )
        binding.branchesRecycler.layoutManager = layoutManager
        binding.branchesRecycler.setHasFixedSize(true)
        binding.branchesRecycler.isFocusable = false
        binding.branchesRecycler.adapter = employeeAdapter
    }

    private fun displayDetails(view: View, branch: Branches) {

       val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.layout_branch_detail)

        val branchName = dialog.findViewById<TextView>(R.id.branch_name)
        val branchContact = dialog.findViewById<TextView>(R.id.branch_contact)
        val branchAddress = dialog.findViewById<TextView>(R.id.branch_address)
        val accountHolder = dialog.findViewById<TextView>(R.id.account_holder)
        val accNum = dialog.findViewById<TextView>(R.id.acc_num)
        val bankName = dialog.findViewById<TextView>(R.id.bank_name)
        val subscriptionAvailable = dialog.findViewById<TextView>(R.id.subscription_available)
        val bankQr = dialog.findViewById<ImageView>(R.id.bank_qr)
        branchName.text = branch.name
        branchContact.text = branch.contact
        Linkify.addLinks(branchContact,Linkify.PHONE_NUMBERS)
        branchAddress.text = branch.address
        accountHolder.text = branch.account_holder
        accNum.text = branch.account_number
        bankName.text = branch.bank
        subscriptionAvailable.text = branch.sub_packages_count.toString()+" subscriptions"
        bankQr.load(BASE_URL+branch.qrcode)
        dialog.show()


    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMapsFragmentBinding.inflate(layoutInflater)
        tokenManager = TokenManager.getInstance(
            getSharedPreferences(
                Constants.TOKEN_PREF,
                AppCompatActivity.MODE_PRIVATE
            )
        )

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
        setupViewModel()
        init()
        setContentView(binding.root)

    }


    private fun getBranchesList(googleMap: GoogleMap) {
        tokenManager?.let { it1 -> viewModel.getBranchesList(it1) }

        viewModel.branchesResponse.observe(this, Observer { response ->
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
               this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
               this,
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
        val builder = AlertDialog.Builder(this)
        builder.setTitle(marker.title)
        builder.setMessage(marker.snippet)
        builder.show()
        return false
    }


}