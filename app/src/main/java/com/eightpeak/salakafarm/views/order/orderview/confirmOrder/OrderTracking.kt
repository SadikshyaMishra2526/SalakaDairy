package com.eightpeak.salakafarm.views.order.orderview.confirmOrder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.GridLayoutManager
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.databinding.ActivityOrderTrackingBinding
import com.eightpeak.salakafarm.databinding.FragmentOrderHistoryBinding
import com.eightpeak.salakafarm.mapfunctions.MapsFragment
import com.eightpeak.salakafarm.serverconfig.network.TokenManager
import com.eightpeak.salakafarm.utils.Constants
import com.eightpeak.salakafarm.viewmodel.OrderViewModel
import com.eightpeak.salakafarm.views.home.products.ProductAdapter

class OrderTracking : AppCompatActivity() {

    private lateinit var viewModel: OrderViewModel
    private lateinit var binding: ActivityOrderTrackingBinding


    lateinit var productAdapter: ProductAdapter
    private var layoutManager: GridLayoutManager? = null
    private var tokenManager: TokenManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOrderTrackingBinding.inflate(layoutInflater)
        binding.returnHome.setOnClickListener { finish() }
        binding.header.text=getString(R.string.track_your_order)
        setContentView(binding.root)

        setUpBranchesView()
        tokenManager = TokenManager.getInstance(
            getSharedPreferences(
                Constants.TOKEN_PREF,
                MODE_PRIVATE
            )
        )
    }
    private fun setUpBranchesView() {
        val fm: FragmentManager = supportFragmentManager
        var fragment = fm.findFragmentByTag("myFragmentTag")
        if (fragment == null) {
            val ft: FragmentTransaction = fm.beginTransaction()
            fragment = MapsFragment()
            ft.add(R.id.branchesList, fragment, "myFragmentTag")
            ft.commit()
        }
    }

}