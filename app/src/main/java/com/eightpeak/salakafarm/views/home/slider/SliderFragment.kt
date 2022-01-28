package com.eightpeak.salakafarm.views.home.slider

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.eightpeak.salakafarm.databinding.FragmentHomeSliderBinding
import com.eightpeak.salakafarm.repository.AppRepository
import com.eightpeak.salakafarm.utils.subutils.Resource
import com.eightpeak.salakafarm.viewmodel.SliderViewModel
import com.eightpeak.salakafarm.viewmodel.ViewModelProviderFactory
import com.google.android.material.snackbar.Snackbar
import com.eightpeak.salakafarm.utils.subutils.errorSnack
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import java.util.ArrayList

class SliderFragment : Fragment() {
    private lateinit var viewModel: SliderViewModel
    lateinit var sliderAdapter: SliderAdapter
    private var sliderList: List<SliderModel>? = null


    private lateinit var binding: FragmentHomeSliderBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sliderAdapter = SliderAdapter(context,sliderList)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeSliderBinding.inflate(layoutInflater,parent,false)

        var sliderView: SliderView? = null

        sliderList = ArrayList()
        sliderAdapter.renewItems(sliderList)
        binding.homeSlider.setSliderAdapter(sliderAdapter)
        binding.homeSlider.setIndicatorAnimation(IndicatorAnimationType.SWAP) //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP
        binding.homeSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
        binding.homeSlider.autoCycleDirection = SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH
        binding.homeSlider.indicatorSelectedColor = Color.WHITE
        binding.homeSlider.indicatorUnselectedColor = Color.GRAY
        binding.homeSlider.scrollTimeInSec = 3
        binding.homeSlider.isAutoCycle = true
        binding.homeSlider.startAutoCycle()
        binding.homeSlider.setOnIndicatorClickListener {
            Log.i(
                "GGG",
                "onIndicatorClicked: " + sliderView!!.currentPagePosition
            )
        }

        init()
        return binding.rootLayout
    }

    private fun init() {
//         sliderAdapter = SliderAdapter(context,sliderList)
        setupViewModel()
    }

    private fun setupViewModel() {
        val repository = AppRepository()
        val factory = ViewModelProviderFactory(requireActivity().application, repository)
        viewModel = ViewModelProvider(this, factory).get(SliderViewModel::class.java)
        getPictures()
    }

    private fun getPictures() {
        viewModel.picsData.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()

                    response.data?.let { picsResponse ->

                        binding.shimmerLayout.stopShimmer()
                        binding.shimmerLayout.visibility = View.GONE

                        sliderAdapter.notifyDataSetChanged()
                        sliderAdapter.addItem(picsResponse)
                        sliderAdapter = SliderAdapter(context,picsResponse)
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        binding.rootLayout.errorSnack(message, Snackbar.LENGTH_LONG)
                    }

                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun hideProgressBar() {
//        progress.visibility = View.GONE
    }

    private fun showProgressBar() {
//        progress.visibility = View.VISIBLE
    }


    fun onProgressClick(view: View) {
        //Preventing Click during loading
    }

}