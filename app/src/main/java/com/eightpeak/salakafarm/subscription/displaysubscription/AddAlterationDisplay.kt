package com.eightpeak.salakafarm.subscription.displaysubscription

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
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
    private var selectPeriod: String? = null
    private var remainingLitre: String? = null
    private var alterationStatus: String? = null

    private lateinit var deliveryPeriodRadio: RadioButton
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
        alterQuantity = mArgs.getString(Constants.QUANTITY)
        alterationDate = mArgs.getString(Constants.ALTER_DAY)
        remainingLitre = mArgs.getString(Constants.REMAINING_LITRE)
        alterationStatus = mArgs.getString(Constants.ALTERATION_STATUS)

        setupViewModel()
        getAlterationChange(root)
        if(alterationStatus.equals("1")||alterationStatus.equals("2")){
            getAlterationCancel(root)
        }
        return root.rootView
    }

    private fun setupViewModel() {

        val repository = AppRepository()
        val factory = ViewModelProviderFactory(requireActivity().application, repository)
        viewModel = ViewModelProvider(this, factory).get(SubscriptionViewModel::class.java)

    }

    private fun getAlterationChange(root: View) {
        binding.cancelSubscription.setOnClickListener {
            binding.deductSubscription.visibility = View.GONE
            binding.addSubscription.visibility = View.GONE
            binding.cancelSubscription.visibility = View.GONE

            binding.alterSubLayout.visibility = View.VISIBLE
            binding.cancelSubscriptionTv.text =
                "Do you want to cancel $alterationDate's subscription? "

            binding.submitSubscription.setOnClickListener {
                if (userPrefManager?.deliveryPeriod == 2) {
                    val deliveryPeriod: Int = binding.deliveryPeriod.checkedRadioButtonId
                    deliveryPeriodRadio = root.findViewById<View>(deliveryPeriod) as RadioButton
                    selectPeriod = deliveryPeriodRadio.text.toString()
                } else if (userPrefManager?.deliveryPeriod == 1) {
                    selectPeriod = "1"
                } else if (userPrefManager?.deliveryPeriod == 0) {
                    selectPeriod = "0"
                }
                val body = alterQuantity?.let { it1 ->
                    subscriptionId?.let { it2 ->
                        alterationDate?.let { it3 ->
                            selectPeriod?.let { it4 ->
                                RequestBodies.AddAlteration(
                                    it2, "1",
                                    it1, it3, it4
                                )
                            }
                        }

                    }
                }
                alterSubscription(body)
            }

        }


        binding.addSubscription.setOnClickListener {
            binding.cancelSubscription.visibility = View.GONE
            binding.addSubscription.visibility = View.GONE
            binding.deductSubscription.visibility = View.GONE
            binding.alterSubLayout.visibility = View.VISIBLE
            binding.alterationLayout.visibility = View.VISIBLE
            binding.alterationDate.setText(alterationDate)
            if (userPrefManager?.deliveryPeriod == 2) {
                val deliveryPeriod: Int = binding.deliveryPeriod.checkedRadioButtonId
                deliveryPeriodRadio = root.findViewById<View>(deliveryPeriod) as RadioButton
                selectPeriod = deliveryPeriodRadio.text.toString()
            } else if (userPrefManager?.deliveryPeriod == 1) {
                selectPeriod = "1"
            } else if (userPrefManager?.deliveryPeriod == 0) {
                selectPeriod = "0"
            }
            binding.submitSubscription.setOnClickListener {
                val alterQuantity: Int = binding.alterationQuantity.text.toString().toInt()
                if (alterQuantity != 0) {
                    if (alterQuantity < remainingLitre!!.toInt()) {
                        val body =
                            binding.alterationQuantity.text.toString().let { it1 ->
                                subscriptionId?.let { it2 ->
                                    binding.alterationDate.text.toString().let { it3 ->
                                        selectPeriod?.let { it4 ->
                                            RequestBodies.AddAlteration(
                                                it2, "2",
                                                it1, it3, it4
                                            )
                                        }
                                    }

                                }
                            }
                        alterSubscription(body)
                    } else {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.less_than_remaining),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.quantity_validation),
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }

        }
    }

    private fun getAlterationCancel(root: View) {
        binding.cancelAlteration.visibility=View.VISIBLE
        binding.cancelAlteration.setOnClickListener {
            val body = RequestBodies.AddAlteration(subscriptionId!!,"0","0",alterationDate!!,"0")
            alterSubscription(body)

        }
    }


    private fun alterSubscription(body: RequestBodies.AddAlteration?) {
        body?.let { tokenManager?.let { it1 -> viewModel.addAlterationSubscription(it1, it) } }
        viewModel.addAlterationSubscription.observe(requireActivity(), Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { _ ->
                        binding.alterSubscription.showSnack(
                            "Subscription Successfully edited",
                            Snackbar.LENGTH_LONG
                        )
                        binding.alterSubscription.visibility = View.GONE
//                        val handler = Handler()
//                        handler.postDelayed({
//                            dismiss()
                        startActivity(
                            Intent(
                                requireContext(),
                                DisplaySubscriptionDetails::class.java
                            )
                        )
//                                 }, 1500)
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        dismiss()
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