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
import androidx.recyclerview.widget.LinearLayoutManager
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.repository.AppRepository
import com.eightpeak.salakafarm.viewmodel.SliderViewModel
import com.eightpeak.salakafarm.viewmodel.ViewModelProviderFactory
import com.google.android.material.snackbar.Snackbar
import com.hadi.retrofitmvvm.util.Resource
import com.hadi.retrofitmvvm.util.errorSnack
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.IndicatorView.draw.controller.DrawController.ClickListener
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import kotlinx.android.synthetic.main.fragment_home_slider.*
import java.util.ArrayList

class SliderFragment : Fragment() {
    private lateinit var viewModel: SliderViewModel
    lateinit var sliderAdapter: SliderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sliderAdapter = SliderAdapter(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_home_slider, parent, false)
        var sliderView: SliderView? = null

        sliderView = view.findViewById(R.id.home_slider)
        sliderList = ArrayList<SliderModel>()
        sliderAdapter!!.renewItems(sliderList!!)
        sliderView.setSliderAdapter(sliderAdapter!!)
        sliderView.setIndicatorAnimation(IndicatorAnimationType.SWAP) //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
        sliderView.autoCycleDirection = SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH
        sliderView.indicatorSelectedColor = Color.WHITE
        sliderView.indicatorUnselectedColor = Color.GRAY
        sliderView.scrollTimeInSec = 3
        sliderView.isAutoCycle = true
        sliderView.startAutoCycle()
        sliderView.setOnIndicatorClickListener(ClickListener {
            Log.i(
                "GGG",
                "onIndicatorClicked: " + sliderView.getCurrentPagePosition()
            )
        })
        init(view)
        return view
    }

    private fun init() {
        rvPics.setHasFixedSize(true)
        rvPics.layoutManager = LinearLayoutManager(this)
        sliderAdapter = SliderAdapter()
        setupViewModel()
    }

    private fun setupViewModel() {
        val repository = AppRepository()
        val factory = ViewModelProviderFactory(requireActivity().application, repository)
        viewModel = ViewModelProvider(this, factory).get(SliderViewModel::class.java)
        getPictures()
    }

    private fun getPictures() {
        viewModel.picsData.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { picsResponse ->
                        sliderAdapter.addItem(picsResponse)
                        sl.sliderAdapter = sliderAdapter
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        rootLayout.errorSnack(message, Snackbar.LENGTH_LONG)
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