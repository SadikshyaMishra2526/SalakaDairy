package com.eightpeak.salakafarm.subscription.displaysubscription

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.database.UserPrefManager
import com.eightpeak.salakafarm.databinding.AlterSubscriptionLayoutBinding
import com.eightpeak.salakafarm.repository.AppRepository
import com.eightpeak.salakafarm.serverconfig.RequestBodies
import com.eightpeak.salakafarm.serverconfig.network.TokenManager
import com.eightpeak.salakafarm.utils.Constants
import com.eightpeak.salakafarm.utils.subutils.Resource
import com.eightpeak.salakafarm.utils.subutils.errorSnack
import com.eightpeak.salakafarm.utils.subutils.showSnack
import com.eightpeak.salakafarm.viewmodel.OrderViewModel
import com.eightpeak.salakafarm.viewmodel.SubscriptionViewModel
import com.eightpeak.salakafarm.viewmodel.ViewModelProviderFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar

class AddAlterationDisplay : BottomSheetDialogFragment() {

    private var _binding: AlterSubscriptionLayoutBinding? = null
    private val binding get() = _binding!!
    private var userPrefManager: UserPrefManager? = null
    private lateinit var viewModel: SubscriptionViewModel
    private var tokenManager: TokenManager? = null
    private var subscriptionId: String? = null
    private var alterQuantity: String? = null
    private var alterationDate: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AlterSubscriptionLayoutBinding.inflate(inflater, container, false)
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
        alterQuantity = mArgs!!.getString(Constants.QUANTITY)
        alterationDate = mArgs!!.getString(Constants.ALTER_DAY)

        setupViewModel()
        return root.rootView
    }

    private fun setupViewModel() {

        val repository = AppRepository()
        val factory = ViewModelProviderFactory(requireActivity().application, repository)
        viewModel = ViewModelProvider(this, factory).get(SubscriptionViewModel::class.java)
        getAlterationChange()
    }

    private fun getAlterationChange() {
        binding.cancelSubscription.setOnClickListener {
            binding.deductSubscription.visibility=View.GONE
            binding.addSubscription.visibility=View.GONE
            binding.cancelSubscription.visibility=View.GONE

            binding.alterSubLayout.visibility=View.VISIBLE
            binding.cancelSubscriptionTv.text = "Do you want to cancel this day's subscription? "

            binding.submitSubscription.setOnClickListener {
                val body= alterQuantity?.let { it1 ->
                    subscriptionId?.let { it2 ->
                        alterationDate?.let { it3 ->
                            RequestBodies.AddAlteration(
                                it2,"1",
                                it1, it3
                            )
                        }

                    }
                }
                alterSubscription(body)
            }

        }

        binding.deductSubscription.setOnClickListener {
            binding.cancelSubscription.visibility=View.GONE
            binding.deductSubscription.visibility=View.GONE
            binding.addSubscription.visibility=View.GONE
            binding.alterSubLayout.visibility=View.VISIBLE
            binding.alterationLayout.visibility=View.VISIBLE
            binding.alterationDate.text=alterationDate
            binding.alterationQuantity.setText(alterQuantity.toString())
            binding.submitSubscription.setOnClickListener {
                val body= binding.alterationQuantity.text.toString().let { it1 ->
                    subscriptionId?.let { it2 ->
                        alterationDate?.let { it3 ->
                            RequestBodies.AddAlteration(
                                it2,"0",
                                it1, it3
                            )
                        }

                    }
                }
                alterSubscription(body)
            }

        }
        binding.addSubscription.setOnClickListener {
            binding.cancelSubscription.visibility=View.GONE
            binding.addSubscription.visibility=View.GONE
            binding.deductSubscription.visibility=View.GONE
            binding.alterSubLayout.visibility=View.VISIBLE
            binding.alterationLayout.visibility=View.VISIBLE
            binding.alterationDate.text=alterationDate
            binding.alterationQuantity.setText(alterQuantity.toString())
            binding.submitSubscription.setOnClickListener {
                val body= binding.alterationQuantity.text.toString().let { it1 ->
                    subscriptionId?.let { it2 ->
                        alterationDate?.let { it3 ->
                            RequestBodies.AddAlteration(
                                it2,"2",
                                it1, it3
                            )
                        }

                    }
                }
                alterSubscription(body)
            }

        }
    }


    private fun alterSubscription(body: RequestBodies.AddAlteration?) {
        body?.let { tokenManager?.let { it1 -> viewModel.addAlterationSubscription(it1, it) } }
        viewModel.addAlterationSubscription.observe(requireActivity(), Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { _ ->
                        Log.i("TAG", "alterSubscription: "+response.message)
                      Toast.makeText(requireContext(),"Subscription Successfully edited", Toast.LENGTH_LONG).show()
                        dismiss()
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        binding.alterSubscription.errorSnack(message, Snackbar.LENGTH_LONG)
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


}