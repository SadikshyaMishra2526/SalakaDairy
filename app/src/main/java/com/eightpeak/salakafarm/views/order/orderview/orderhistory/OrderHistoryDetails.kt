package com.eightpeak.salakafarm.views.order.orderview.orderhistory

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.database.UserPrefManager
import com.eightpeak.salakafarm.databinding.FragmentOrderHistoryDetailsBinding
import com.eightpeak.salakafarm.repository.AppRepository
import com.eightpeak.salakafarm.serverconfig.network.TokenManager
import com.eightpeak.salakafarm.utils.Constants
import com.eightpeak.salakafarm.utils.subutils.Resource
import com.eightpeak.salakafarm.viewmodel.OrderViewModel
import com.eightpeak.salakafarm.viewmodel.ViewModelProviderFactory
class OrderHistoryDetails : Fragment() {

    private var tokenManager: TokenManager? = null
    private lateinit var viewModel: OrderViewModel

    private var _binding: FragmentOrderHistoryDetailsBinding? = null
    lateinit var userPrefManager: UserPrefManager

    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        userPrefManager = UserPrefManager(requireContext())
        _binding = FragmentOrderHistoryDetailsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        tokenManager = TokenManager.getInstance(
            requireActivity().getSharedPreferences(
                Constants.TOKEN_PREF,
                AppCompatActivity.MODE_PRIVATE
            )
        )

        setupViewModel()
        return root
    }

    private fun setupViewModel() {
        val repository = AppRepository()
        val factory = ViewModelProviderFactory(requireActivity().application, repository)
        viewModel = ViewModelProvider(this, factory).get(OrderViewModel::class.java)
        getOrderHistoryDetails()
    }


    private fun getOrderHistoryDetails() {
        tokenManager?.let { it1 -> viewModel.getOrderHistoryDetails(it1,"2") }

        viewModel.getOrderList.observe(requireActivity(), Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { picsResponse ->
                        Log.i("TAG", "getOrderHistoryDetails: i m here")
//                        populateHistoryView(picsResponse)
//                        binding.shimmerLayout.stopShimmer()
//                        binding.shimmerLayout.visibility = View.GONE
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
//                        binding.orderHistory.errorSnack(message, Snackbar.LENGTH_LONG)
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