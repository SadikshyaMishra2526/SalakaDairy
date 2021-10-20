package com.eightpeak.salakafarm.views.home.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.databinding.FragmentHomeBinding
import com.eightpeak.salakafarm.views.home.categories.CategoriesFragment
import com.eightpeak.salakafarm.views.home.products.ProductFragment
import com.eightpeak.salakafarm.views.home.slider.SliderFragment
import com.eightpeak.salakafarm.views.login.LoginActivity

class HomeFragment : Fragment() {


//    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null


    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        homeViewModel =
//            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.customerProfile.setOnClickListener {
            val mainActivity = Intent(context, LoginActivity::class.java)
            startActivity(mainActivity)
        }

        setUpSliderFragment()
        setUpCategoriesFragment()
        setUpProductFragment()
        return root
    }

    private fun setUpSliderFragment() {
        val managerSlider = childFragmentManager
        val fragmentSlider: Fragment = SliderFragment()
        val transactionSlider = managerSlider.beginTransaction()
        transactionSlider.replace(R.id.containerSlider, fragmentSlider).addToBackStack(null)
        transactionSlider.commit()

    }

    private fun setUpCategoriesFragment() {
        val managerCategories = childFragmentManager
        val fragmentCategories: Fragment = CategoriesFragment()
        val transactionCategories = managerCategories.beginTransaction()
        transactionCategories.replace(R.id.containerCategories, fragmentCategories).addToBackStack(null)
        transactionCategories.commit()

    }
    private fun setUpProductFragment() {
        val managerProductList = childFragmentManager
        val fragmentProductList: Fragment = ProductFragment()
        val transactionProductList = managerProductList.beginTransaction()
        transactionProductList.replace(R.id.containerProducts, fragmentProductList).addToBackStack(null)
        transactionProductList.commit()

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}