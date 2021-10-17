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
import com.eightpeak.salakafarm.views.home.slider.SliderFragment
import com.eightpeak.salakafarm.views.login.LoginActivity

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.customerProfile.setOnClickListener {
            val mainActivity = Intent(context, LoginActivity::class.java)
            startActivity(mainActivity)
        }

        setUpSliderFragment()
        return root
    }

    private fun setUpSliderFragment() {
        val manager2 = childFragmentManager
        val fragment2: Fragment = SliderFragment()
        val transaction2 = manager2.beginTransaction()
        transaction2.replace(R.id.containerSlider, fragment2).addToBackStack(null)
        transaction2.commit()

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}