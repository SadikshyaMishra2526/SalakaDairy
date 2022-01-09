package com.eightpeak.salakafarm.subscription.displaysubscription

import android.app.AlertDialog
import android.os.Bundle
import android.text.SpannableString
import android.text.util.Linkify
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.api.load
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.database.UserPrefManager
import com.eightpeak.salakafarm.databinding.ActivityMapsFragmentBinding
import com.eightpeak.salakafarm.databinding.LayoutTrackEmpPositionBinding
import com.eightpeak.salakafarm.repository.AppRepository
import com.eightpeak.salakafarm.serverconfig.RequestBodies
import com.eightpeak.salakafarm.serverconfig.network.TokenManager
import com.eightpeak.salakafarm.utils.Constants
import com.eightpeak.salakafarm.utils.EndPoints.Companion.BASE_URL
import com.eightpeak.salakafarm.utils.subutils.Resource
import com.eightpeak.salakafarm.utils.subutils.errorSnack
import com.eightpeak.salakafarm.viewmodel.OrderViewModel
import com.eightpeak.salakafarm.viewmodel.ViewModelProviderFactory
import com.eightpeak.salakafarm.views.home.products.ServerResponse
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_add_to_cart_view.view.*

class TrackSubscriptionView : BottomSheetDialogFragment()
    ,OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener {

    private var _binding: LayoutTrackEmpPositionBinding? = null
    private val binding get() = _binding!!
    private var userPrefManager: UserPrefManager? = null
    private lateinit var viewModel: OrderViewModel
    private var tokenManager: TokenManager? = null
    private lateinit var mMap: GoogleMap
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = LayoutTrackEmpPositionBinding.inflate(inflater, container, false)
        val root: View = binding.root
        userPrefManager = UserPrefManager(context)
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
        return root.rootView
    }

    private fun setupViewModel() {

        val repository = AppRepository()
        val factory = ViewModelProviderFactory(requireActivity().application, repository)
        viewModel = ViewModelProvider(this, factory).get(OrderViewModel::class.java)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        Log.i("TAG", "onMapReady: i m ready")
        getOrderDetail()

    }
    private fun getOrderDetail() {
        val mArgs = arguments
        val productId = mArgs!!.getString(Constants.ORDER_ID)
        val type = mArgs!!.getString(Constants.TYPE)
        if (productId != null) {
            val trackDetails= type?.let { RequestBodies.EmpLatlng(productId, it) }
            trackDetails?.let { tokenManager?.let { it1 -> viewModel.empLatLng(it1, it) } }
            Log.i("TAG", "getOrderDetail: "+productId+" "+type)
        }
        viewModel.empLatLng.observe(requireActivity(), Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { _ ->
                     plotPositionInMap(response.data)
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        binding.trackEmp.errorSnack(message, Snackbar.LENGTH_LONG)
                    }

                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun plotPositionInMap(data: EmployeeTrackDetails) {
        var employeeDetails=data.latlng

           plotEmployeeDetails(data)
           var  employeePosition :LatLng = LatLng(employeeDetails.lat,employeeDetails.lng)
                var mark: Marker? = null
                mark = mMap.addMarker(
                    MarkerOptions()
                        .position(employeePosition)
                        .snippet("Contact Employee :-"+data.latlng.phone)
                        .icon(
                            BitmapDescriptorFactory.defaultMarker(
                                BitmapDescriptorFactory.HUE_ORANGE
                            )
                        )
                        .title("Your Delivery is own its way..")
                )
                mark.tag = mark
                val center = CameraUpdateFactory.newLatLng(employeePosition)
                val zoom = CameraUpdateFactory.zoomTo(12f)
                mark.showInfoWindow()
                mMap.moveCamera(center)
                mMap.animateCamera(zoom)

            mMap.setOnMarkerClickListener(this)


    }

    private fun plotEmployeeDetails(data: EmployeeTrackDetails) {
        binding.subscriberAvatar.load(BASE_URL+data.latlng.avatar)
        binding.employeeName.text=data.latlng.name
        binding.employeeContact.text=data.latlng.phone.toString()
        binding.employeeEmail.text=data.latlng.email
        if(data.latlng.gender==0){
            binding.employeeGender.text="Female"
        }else if(data.latlng.gender==1){
            binding.employeeGender.text="Male"
        }
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
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(marker.title)
        builder.setMessage(marker.snippet)
        builder.show()
        return false
    }

}