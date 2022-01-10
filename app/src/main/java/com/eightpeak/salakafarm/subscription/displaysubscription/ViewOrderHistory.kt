package com.eightpeak.salakafarm.subscription.displaysubscription

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.database.UserPrefManager
import com.eightpeak.salakafarm.databinding.ActivityDisplaySubscriptionDetailsBinding
import com.eightpeak.salakafarm.serverconfig.network.TokenManager
import com.eightpeak.salakafarm.utils.Constants
import com.eightpeak.salakafarm.viewmodel.SubscriptionViewModel

class ViewOrderHistory  : AppCompatActivity() {
    private lateinit var binding: ActivityDisplaySubscriptionDetailsBinding
    private lateinit var viewModel: SubscriptionViewModel
    lateinit var userPrefManager: UserPrefManager
    private var tokenManager: TokenManager? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tokenManager = TokenManager.getInstance(
            getSharedPreferences(
                Constants.TOKEN_PREF,
                MODE_PRIVATE
            )
        )
        binding = ActivityDisplaySubscriptionDetailsBinding.inflate(layoutInflater)
        binding.headerTitle.text = getString(R.string.track_your_subscription)
        binding.returnHome.setOnClickListener { finish() }
        userPrefManager = UserPrefManager(this)


        setContentView(binding.root)
    }
}