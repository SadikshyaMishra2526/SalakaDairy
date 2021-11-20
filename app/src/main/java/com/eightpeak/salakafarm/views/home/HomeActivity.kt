package com.eightpeak.salakafarm.views.home

import android.Manifest
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.databinding.ActivityHomeBinding

import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import kotlin.system.exitProcess
import com.google.android.gms.location.LocationServices


import com.google.android.gms.maps.SupportMapFragment
import android.widget.Toast

import android.content.pm.PackageManager

import androidx.core.app.ActivityCompat

import androidx.core.content.ContextCompat
import android.location.Address
import com.google.android.gms.maps.model.LatLng

import androidx.core.location.LocationManagerCompat.getCurrentLocation


import android.location.Geocoder
import android.location.Location
import android.os.Handler

import android.os.Looper
import android.util.Log
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.FragmentManager
import com.eightpeak.salakafarm.database.UserPrefManager
import com.eightpeak.salakafarm.views.home.ui.home.BottomNavigationBehavior
import com.eightpeak.salakafarm.views.home.ui.home.HomeFragment
import java.lang.Exception
import java.util.*


class HomeActivity : AppCompatActivity() , OnMapReadyCallback,
GoogleApiClient.ConnectionCallbacks,
GoogleApiClient.OnConnectionFailedListener
{

    private lateinit var binding: ActivityHomeBinding

    private val MULTIPLE_PERMISSION_REQUEST_CODE = 4
    private var longitude = 0.0
    private var latitude = 0.0
    private lateinit var googleApiClient: GoogleApiClient

    lateinit var userPrefManager: UserPrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navView: BottomNavigationView = binding.navView

        userPrefManager= UserPrefManager(this)

        //Initializing googleApiClient
        googleApiClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()

        val navController = findNavController(R.id.nav_host_fragment_activity_home)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_message, R.id.navigation_cart,R.id.navigation_setting
            )
        )
//        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val layoutParams = navView.layoutParams as CoordinatorLayout.LayoutParams
        layoutParams.behavior = BottomNavigationBehavior()

        checkPermissionsState()

    }

//    override fun onBackPressed() {
//        super.onBackPressed()
//        exitProcess(-1)
//    }

    override fun onMapReady(p0: GoogleMap) {

    }

    override fun onConnected(p0: Bundle?) {
        getCurrentLocation();
    }

    override fun onConnectionSuspended(p0: Int) {
        TODO("Not yet implemented")
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("Not yet implemented")
    }
    override fun onStart() {
        googleApiClient.connect()
        super.onStart()
    }

    override fun onStop() {
        googleApiClient.disconnect()
        super.onStop()
    }
    private fun getCurrentLocation() {
//        mMap.clear();
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        val location: Location? = LocationServices.FusedLocationApi.getLastLocation(googleApiClient)
        if (location != null) {
            //Getting longitude and latitude
            longitude = location.getLongitude()
            latitude = location.getLatitude()
            val latLng = LatLng(latitude, longitude)
            getMyPosition(latLng)
        }
    }


    private fun checkPermissionsState() {
        val internetPermissionCheck = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.INTERNET
        )
        val networkStatePermissionCheck = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_NETWORK_STATE
        )
        val writeExternalStoragePermissionCheck = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val coarseLocationPermissionCheck = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        val fineLocationPermissionCheck = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        val wifiStatePermissionCheck = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_WIFI_STATE
        )
        if (internetPermissionCheck == PackageManager.PERMISSION_GRANTED && networkStatePermissionCheck == PackageManager.PERMISSION_GRANTED && writeExternalStoragePermissionCheck == PackageManager.PERMISSION_GRANTED && coarseLocationPermissionCheck == PackageManager.PERMISSION_GRANTED && fineLocationPermissionCheck == PackageManager.PERMISSION_GRANTED && wifiStatePermissionCheck == PackageManager.PERMISSION_GRANTED) {
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_WIFI_STATE
                ),
                MULTIPLE_PERMISSION_REQUEST_CODE
            )
        }
    }

   override fun onRequestPermissionsResult(
       requestCode: Int,
       permissions: Array<out String>,
       grantResults: IntArray
   ) {
        super.onRequestPermissionsResult(requestCode, permissions!!, grantResults)
        when (requestCode) {
            MULTIPLE_PERMISSION_REQUEST_CODE -> {
                if (grantResults.size > 0) {
                    var somePermissionWasDenied = false
                    for (result in grantResults) {
                        if (result == PackageManager.PERMISSION_DENIED) {
                            somePermissionWasDenied = true
                        }
                    }
                    if (somePermissionWasDenied) {
                        Toast.makeText(
                            this,
                            "Cant load maps without all the permissions granted",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {

                    }
                } else {
                    Toast.makeText(
                        this,
                        "Cant load maps without all the permissions granted",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                return
            }
        }
    }

    private fun getMyPosition(myCoordinates: LatLng) {
        Handler(Looper.getMainLooper()).post(Runnable {
            var myCity: String? = ""
            val geocoder = Geocoder(applicationContext, Locale.getDefault())
            try {
                val addresses: List<Address> =
                    geocoder.getFromLocation(myCoordinates.latitude, myCoordinates.longitude, 1)
                val subLocality: String = addresses[0].subLocality
                val locality: String = addresses[0].locality
                val selectedCountry: String = addresses[0].countryName
                myCity += "$subLocality, "
                myCity += "$locality, "
                myCity += selectedCountry
                val addressLine: String =
                    addresses[0].getAddressLine(0).replace("Unnamed Road,", "")
//                currentlocation.setText("Your Current Location :-$addressLine")
                userPrefManager.currentPosition=addressLine
                Log.i("TAG", "getMyPosition: $addressLine")
             } catch (e: Exception) {
//                Toast.makeText(this@HomeActivity, "Could not get Address!!", Toast.LENGTH_SHORT)
//                    .show()
                e.printStackTrace()
            }
        })
    }

    override fun onBackPressed() {
        val fm: android.app.FragmentManager? = fragmentManager
        if (fm != null) {
            if (fm.getBackStackEntryCount() > 0) {
                Log.i("MainActivity", "popping backstack")
                fm.popBackStack()
            } else {
                Log.i("MainActivity", "nothing on backstack, calling super")
                super.onBackPressed()
            }
        }

    }
}