package com.eightpeak.salakafarm.views.comparelist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.api.load
import com.eightpeak.salakafarm.App
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.database.UserPrefManager
import com.eightpeak.salakafarm.databinding.ActivityCompareListBinding
import com.eightpeak.salakafarm.databinding.ActivityWishlistBinding
import com.eightpeak.salakafarm.repository.AppRepository
import com.eightpeak.salakafarm.serverconfig.network.TokenManager
import com.eightpeak.salakafarm.utils.Constants
import com.eightpeak.salakafarm.utils.EndPoints.Companion.BASE_URL
import com.eightpeak.salakafarm.utils.subutils.Resource
import com.eightpeak.salakafarm.viewmodel.CompareViewModel
import com.eightpeak.salakafarm.viewmodel.GetResponseViewModel
import com.eightpeak.salakafarm.viewmodel.ViewModelProviderFactory
import com.eightpeak.salakafarm.views.home.slider.SliderAdapter
import com.google.android.material.snackbar.Snackbar
import com.hadi.retrofitmvvm.util.errorSnack
import kotlinx.android.synthetic.main.fragment_home_slider.*

class CompareListActivity :AppCompatActivity(){

    private lateinit var viewModel: CompareViewModel
    private var _binding: ActivityCompareListBinding? = null
    private var tokenManager: TokenManager? = null
    private lateinit var binding: ActivityCompareListBinding
    lateinit var userPrefManager: UserPrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCompareListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userPrefManager= UserPrefManager(this)

        tokenManager = TokenManager.getInstance(
            getSharedPreferences(
                Constants.TOKEN_PREF,
                MODE_PRIVATE
            )
        )
        setupViewModel()


    }
    private fun setupViewModel() {
        val repository = AppRepository()
        val factory = ViewModelProviderFactory(application, repository)
        viewModel = ViewModelProvider(this, factory).get(CompareViewModel::class.java)
        getCompareList()
    }
    private fun getCompareList() {
  var ids=""
            for(i in  App.getData()){
                ids += "$i,"
            }
        tokenManager?.let { it1 -> viewModel.getCompareResponse(it1,ids) }

        viewModel.compareResponse.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()

                    response.data?.let { picsResponse ->
                        Log.i("TAG", "getCompareList: gggggggggggggggggggggg"+picsResponse.products)
//                        binding.shimmerLayout.stopShimmer()
//                        binding.shimmerLayout.visibility = View.GONE
//
//                        sliderAdapter.notifyDataSetChanged()
//                        sliderAdapter.addItem(picsResponse)
//                        sliderAdapter = SliderAdapter(context,picsResponse)

                        displayCompareList(picsResponse)
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

    private fun displayCompareList(compareResponse: CompareResponse) {
        for (i in compareResponse.products.indices) {
            val itemView: View =
                LayoutInflater.from(this)
                    .inflate(R.layout.compare_list_item, binding.productsList, false)
            binding.header.text = "My Compare List"

            val categorySKU = itemView.findViewById<TextView>(R.id.product_sku)
            val productThumbnail = itemView.findViewById<ImageView>(R.id.product_thumbnail)
            val productName = itemView.findViewById<TextView>(R.id.product_name)
            val productPrice = itemView.findViewById<TextView>(R.id.product_price)
            val productAttribute = itemView.findViewById<TextView>(R.id.category_name)
            categorySKU.text=compareResponse.products[i].sku
            productName.text=compareResponse.products[i].descriptions[0].title
            productPrice.text=compareResponse.products[i].price.toString()
            productAttribute.text=compareResponse.products[i].categories_description[0].alias
            productThumbnail.load(BASE_URL+compareResponse.products[i].image)
            binding.productsList.addView(itemView)

          }
        }

    private fun hideProgressBar() {
       binding. progress.visibility = View.GONE
    }

    private fun showProgressBar() {
//        progress.visibility = View.VISIBLE
    }


    fun onProgressClick(view: View) {
        //Preventing Click during loading
    }
}