package com.eightpeak.salakafarm.subscription

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.eightpeak.salakafarm.databinding.ActivityCartBinding
import com.eightpeak.salakafarm.databinding.ActivitySubscriptionBinding
import com.eightpeak.salakafarm.serverconfig.network.TokenManager
import com.eightpeak.salakafarm.utils.Constants
import com.eightpeak.salakafarm.viewmodel.GetResponseViewModel
import com.eightpeak.salakafarm.views.addtocart.CartActivity

class SubscriptionActivity:AppCompatActivity() {

    private lateinit var binding: ActivitySubscriptionBinding
    private lateinit var viewModel: GetResponseViewModel
    private var _binding: ActivitySubscriptionBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySubscriptionBinding.inflate(layoutInflater)

        binding.headerTitle.text="Add Your Subscription Plan"
        binding.returnHome.setOnClickListener {
            val mainActivity = Intent(this, SubscriptionActivity::class.java)
            startActivity(mainActivity)
            finish()
        }
        binding.goToCart.setOnClickListener {
            val mainActivity = Intent(this, CartActivity::class.java)
            startActivity(mainActivity)
            finish()
        }
        setContentView(binding.root)

    }
}