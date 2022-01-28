package com.eightpeak.salakafarm.subscription.displaysubscription

import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.api.load
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.database.UserPrefManager
import com.eightpeak.salakafarm.databinding.LayoutTrackEmpPositionBinding
import com.eightpeak.salakafarm.databinding.SubscriptionComplainBinding
import com.eightpeak.salakafarm.repository.AppRepository
import com.eightpeak.salakafarm.serverconfig.RequestBodies
import com.eightpeak.salakafarm.serverconfig.network.TokenManager
import com.eightpeak.salakafarm.utils.Constants
import com.eightpeak.salakafarm.utils.EndPoints
import com.eightpeak.salakafarm.utils.subutils.Resource
import com.eightpeak.salakafarm.utils.subutils.errorSnack
import com.eightpeak.salakafarm.utils.subutils.showSnack
import com.eightpeak.salakafarm.utils.subutils.successAddToCartSnack
import com.eightpeak.salakafarm.viewmodel.GetResponseViewModel
import com.eightpeak.salakafarm.viewmodel.OrderViewModel
import com.eightpeak.salakafarm.viewmodel.ViewModelProviderFactory
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

class SubscriptionComplain  : BottomSheetDialogFragment() {

    private var _binding:SubscriptionComplainBinding? = null
    private val binding get() = _binding!!
    private var userPrefManager: UserPrefManager? = null
    private lateinit var viewModel: GetResponseViewModel
    private var tokenManager: TokenManager? = null
    private lateinit var mMap: GoogleMap
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SubscriptionComplainBinding.inflate(inflater, container, false)
        val root: View = binding.root
        userPrefManager = UserPrefManager(context)
        tokenManager = TokenManager.getInstance(
            requireActivity().getSharedPreferences(
                Constants.TOKEN_PREF,
                AppCompatActivity.MODE_PRIVATE
            )
        )

        setupViewModel()
        return root.rootView
    }

    private fun setupViewModel() {

        val repository = AppRepository()
        val factory = ViewModelProviderFactory(requireActivity().application, repository)
        viewModel = ViewModelProvider(this, factory).get(GetResponseViewModel::class.java)
        getOrderDetail()
    }


    private fun getOrderDetail() {
       val customerName=userPrefManager?.firstName+userPrefManager?.lastName

        binding.customerName.setText(customerName)
        binding.btSummit.setOnClickListener {
            if(binding.complainSubject.length()>0){
                if(binding.complainDescription.length()>0){
                    val body=RequestBodies.AddComplain(binding.complainSubject.text.toString(),binding.complainDescription.text.toString())
                    tokenManager?.let { it1 -> viewModel.addComplain(it1,body) }
                }else{
                    Toast.makeText(requireContext(),"Please add subject of the complain",Toast.LENGTH_SHORT).show()
                }
        }else{
            Toast.makeText(requireContext(),"Please add subject of the complain",Toast.LENGTH_SHORT).show()
        } }
        viewModel.addComplain.observe(requireActivity(), Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { _ ->
                        binding.complainLayout.showSnack("We will investigate and contact you soon...", Snackbar.LENGTH_LONG)
                        binding.complainLayout.visibility=View.GONE
                        val handler = Handler()
                        handler.postDelayed({ dismiss() }, 1500)


                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        binding.complainLayout.errorSnack(message, Snackbar.LENGTH_LONG)
                        dismiss()
                    }

                }

                is Resource.Loading -> {
                    showProgressBar()
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

}