package com.eightpeak.salakafarm.views.order.orderview.confirmOrder

import android.app.AlertDialog
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import coil.api.load
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.database.UserPrefManager
import com.eightpeak.salakafarm.databinding.ActivityOrderTrackingBinding
import com.eightpeak.salakafarm.repository.AppRepository
import com.eightpeak.salakafarm.serverconfig.RequestBodies
import com.eightpeak.salakafarm.serverconfig.network.TokenManager
import com.eightpeak.salakafarm.subscription.displaysubscription.EmployeeTrackDetails
import com.eightpeak.salakafarm.utils.Constants
import com.eightpeak.salakafarm.utils.Constants.Companion.ORDER_ID
import com.eightpeak.salakafarm.utils.Constants.Companion.ORDER_STATUS
import com.eightpeak.salakafarm.utils.Constants.Companion.PRODUCT_ID
import com.eightpeak.salakafarm.utils.EndPoints
import com.eightpeak.salakafarm.utils.subutils.Resource
import com.eightpeak.salakafarm.utils.subutils.errorSnack
import com.eightpeak.salakafarm.viewmodel.GetResponseViewModel
import com.eightpeak.salakafarm.viewmodel.OrderViewModel
import com.eightpeak.salakafarm.viewmodel.ViewModelProviderFactory
import com.eightpeak.salakafarm.views.home.products.ProductAdapter
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

class OrderTracking : BottomSheetDialogFragment()
    , OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var viewModel: OrderViewModel
    private lateinit var binding: ActivityOrderTrackingBinding
    private var userPrefManager: UserPrefManager? = null
    private var tokenManager: TokenManager? = null
    private var orderStatus: String? = null
    private var _binding: ActivityOrderTrackingBinding? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityOrderTrackingBinding.inflate(inflater, container, false)
        val root: View = binding.root
        userPrefManager = UserPrefManager(context)
        tokenManager = TokenManager.getInstance(
            requireActivity().getSharedPreferences(
                Constants.TOKEN_PREF,
                AppCompatActivity.MODE_PRIVATE
            )
        )
        binding.header.text = getString(R.string.track_your_order)
        setupViewModel()
        val mArgs = arguments

        orderStatus = mArgs!!.getString(ORDER_STATUS)
        val orderId = mArgs.getString(ORDER_ID)
        binding.orderId.text = "#$orderId"
        setTrackOrder(orderStatus)
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


    private fun setTrackOrder(orderStatus: String?) {
        if (orderStatus.equals("1")) {
            binding.newOrder.load(R.drawable.shape_status_current)
            binding.newOrderLine.setBackgroundColor(Color.parseColor("#3F3FE6"))

        } else if (orderStatus.equals("2")) {
            binding.newOrder.load(R.drawable.shape_staus_complete)
            binding.newOrderLine.setBackgroundColor(Color.parseColor("#5FA95F"))

            binding.processingOrder.load(R.drawable.shape_status_current)
            binding.processedOrderLine.setBackgroundColor(Color.parseColor("#3F3FE6"))

        } else if (orderStatus.equals("3")) {
            binding.newOrder.load(R.drawable.shape_status_remaining)
            binding.newOrderLine.setBackgroundColor(Color.parseColor("#c0c0c0"))

            binding.processingOrder.load(R.drawable.shape_status_remaining)
            binding.processingOrderLine.setBackgroundColor(Color.parseColor("#c0c0c0"))

            binding.processedOrder.load(R.drawable.shape_status_remaining)
            binding.processedOrderLine.setBackgroundColor(Color.parseColor("#c0c0c0"))

        } else if (orderStatus.equals("4") || orderStatus.equals("6")) {
            binding.orderCancel.visibility = View.VISIBLE
            binding.processedOrderLine.visibility = View.VISIBLE
            binding.newOrder.load(R.drawable.shape_staus_cancelled)
            binding.newOrderLine.setBackgroundColor(Color.parseColor("#8B0000"))

            binding.processingOrder.load(R.drawable.shape_staus_cancelled)
            binding.processingOrderLine.setBackgroundColor(Color.parseColor("#8B0000"))

            binding.processedOrder.load(R.drawable.shape_staus_cancelled)
            binding.processedOrderLine.setBackgroundColor(Color.parseColor("#8B0000"))

            binding.cancelledOrder.load(R.drawable.shape_staus_cancelled)
        } else if (orderStatus.equals("5")) {

            binding.newOrder.load(R.drawable.shape_staus_complete)
            binding.newOrderLine.setBackgroundColor(Color.parseColor("#5FA95F"))

            binding.processingOrder.load(R.drawable.shape_staus_complete)
            binding.processingOrderLine.setBackgroundColor(Color.parseColor("#5FA95F"))

            binding.processedOrder.load(R.drawable.shape_staus_complete)
            binding.processedOrderLine.setBackgroundColor(Color.parseColor("#5FA95F"))
        }

    }



    private fun hideProgressBar() {
        binding.progress.visibility = View.GONE
    }

    private fun showProgressBar() {
        binding.progress.visibility = View.VISIBLE
    }


    fun onProgressClick(view: View) {
    }

    override fun onDestroy() {
        super.onDestroy()
//        _binding = null
    }    @Override
    override fun onResume()
    {
        super.onResume();
//        startIntervalHandler()
    }
    override fun onPause() {
        super.onPause()
//        stopIntervalHandler()
    }

//    private fun startIntervalHandler() {
//        stopIntervalHandler()
//        handler.post(runnable)
//    }
//
//    private fun stopIntervalHandler() {
//        handler.removeCallbacks(runnable)
//    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        if(orderStatus=="2"||orderStatus=="5"){
            binding.customerDetails.visibility=View.VISIBLE
            binding.mapLayout.visibility=View.VISIBLE
            getOrderDetail()
        }

    }
    private fun getOrderDetail() {
        val mArgs = arguments
        val orderId = mArgs!!.getString(ORDER_ID)
        val type = mArgs!!.getString(Constants.TYPE)
        if (orderId != null) {
            val trackDetails= type?.let { RequestBodies.EmpLatlng(orderId, it) }
            trackDetails?.let { tokenManager?.let { it1 -> viewModel.empLatLng(it1, it) } }
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
                        binding.trackOrderLayout.errorSnack(message, Snackbar.LENGTH_LONG)
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
        var  employeePosition : LatLng = LatLng(employeeDetails.lat,employeeDetails.lng)
        var mark: Marker? = null
        mark = mMap.addMarker(
            MarkerOptions()
                .position(employeePosition)
                .snippet(getString(R.string.contact_employee)+data.latlng.phone)
                .icon(
                    BitmapDescriptorFactory.defaultMarker(
                        BitmapDescriptorFactory.HUE_ORANGE
                    )
                )
                .title(getString(R.string.delivery_on_its_way))
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
        binding.subscriberAvatar.load(EndPoints.BASE_URL +data.latlng.avatar)
        binding.employeeName.text=data.latlng.name
        binding.employeeContact.text=data.latlng.phone.toString()
        binding.employeeEmail.text=data.latlng.email
        if(data.latlng.gender==0){
            binding.employeeGender.text=getString(R.string.female)
        }else if(data.latlng.gender==1){
            binding.employeeGender.text=getString(R.string.male)
        }
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(marker.title)
        builder.setMessage(marker.snippet)
        builder.show()
        return false
    }
}