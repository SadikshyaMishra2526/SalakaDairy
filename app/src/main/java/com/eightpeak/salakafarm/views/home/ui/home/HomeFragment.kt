package com.eightpeak.salakafarm.views.home.ui.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.database.UserPrefManager
import com.eightpeak.salakafarm.databinding.FragmentHomeBinding
import com.eightpeak.salakafarm.serverconfig.network.TokenManager
import com.eightpeak.salakafarm.subscription.SubscriptionActivity
import com.eightpeak.salakafarm.subscription.displaysubscription.DisplaySubscription
import com.eightpeak.salakafarm.subscription.displaysubscription.SubscriptionDetails
import com.eightpeak.salakafarm.utils.Constants
import com.eightpeak.salakafarm.utils.Constants.Companion.DEFAULT
import com.eightpeak.salakafarm.utils.Constants.Companion.NO_LOGIN
import com.eightpeak.salakafarm.views.home.categories.CategoriesFragment
import com.eightpeak.salakafarm.views.home.products.ProductFragment
import com.eightpeak.salakafarm.views.home.slider.SliderFragment
import com.eightpeak.salakafarm.views.home.ui.user_profile.UserProfile
import com.eightpeak.salakafarm.views.login.LoginActivity
import com.eightpeak.salakafarm.views.search.SearchProductsActivity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import java.lang.Exception
import java.util.*

class HomeFragment : Fragment(), OnMapReadyCallback,
    GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener {

    private var _binding: FragmentHomeBinding? = null

    lateinit var userPrefManager: UserPrefManager

    private var googleApiClient: GoogleApiClient? = null

    private val binding get() = _binding!!
    private var tokenManager: TokenManager? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        homeViewModel =
//            ViewModelProvider(this).get(HomeViewModel::class.java)

        userPrefManager = UserPrefManager(requireContext())
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.customerCurrentLocation.text = userPrefManager.currentPosition

        tokenManager = TokenManager.getInstance(requireActivity().getSharedPreferences(
            Constants.TOKEN_PREF,
            AppCompatActivity.MODE_PRIVATE
        ))
        binding.customerProfile.setOnClickListener {
            tokenManager?.let {
                if (it.token!= NO_LOGIN) {
                    val mainActivity = Intent(context, UserProfile::class.java)
                    startActivity(mainActivity)
                } else {
                    val mainActivity = Intent(context, LoginActivity::class.java)
                    startActivity(mainActivity)
                }
            }


        }

        binding.tvSearchInput.setOnClickListener {
            val mainActivity = Intent(context, SearchProductsActivity::class.java)
            startActivity(mainActivity)
        }

        binding.subscriptionLayout.setOnClickListener {
            if(userPrefManager.subscriptionStatus){
                val mainActivity = Intent(context, SubscriptionDetails::class.java)
                startActivity(mainActivity)
            }else{
                val mainActivity = Intent(context, SubscriptionActivity::class.java)
                startActivity(mainActivity)
            }

        }

        //Initializing googleApiClient
        googleApiClient = GoogleApiClient.Builder(requireContext())
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()


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
        transactionCategories.replace(R.id.containerCategories, fragmentCategories)
            .addToBackStack(null)
        transactionCategories.commit()

    }

    private fun setUpProductFragment() {
        val managerProductList = childFragmentManager
        val fragmentProductList: Fragment = ProductFragment()
        val transactionProductList = managerProductList.beginTransaction()
        transactionProductList.replace(R.id.containerProducts, fragmentProductList)
            .addToBackStack(null)
        transactionProductList.commit()

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onMapReady(p0: GoogleMap) {
        TODO("Not yet implemented")
    }

    override fun onConnected(p0: Bundle?) {
        getCurrentLocation()
    }

    override fun onConnectionSuspended(p0: Int) {
        TODO("Not yet implemented")
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("Not yet implemented")
    }

    private fun getCurrentLocation() {
//        mMap.clear();
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        val location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient)
        var longitude = location.longitude
        var latitude = location.latitude
        val latLng = LatLng(latitude, longitude)
        getMyPosition(latLng)
    }

    private fun getMyPosition(myCoordinates: LatLng) {
        Handler(Looper.getMainLooper()).post {
            var myCity: String? = ""
            val geocoder = Geocoder(requireContext(), Locale.getDefault())
            try {
                val addresses =
                    geocoder.getFromLocation(myCoordinates.latitude, myCoordinates.longitude, 1)
                val subLocality = addresses[0].subLocality
                val locality = addresses[0].locality
                val selectedCountry = addresses[0].countryName
                if (subLocality != null) myCity += "$subLocality, "
                if (locality != null) myCity += "$locality, "
                if (selectedCountry != null) myCity += selectedCountry
                val addressLine = addresses[0].getAddressLine(0).replace("Unnamed Road,", "")
                binding.customerCurrentLocation.setText("Your Current Location :-$addressLine")
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Could not get Address!!", Toast.LENGTH_SHORT)
                    .show()
                e.printStackTrace()
            }
        }
    }
}