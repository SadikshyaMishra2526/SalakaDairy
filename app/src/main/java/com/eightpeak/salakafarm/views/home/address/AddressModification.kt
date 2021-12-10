package com.eightpeak.salakafarm.views.home.address

import android.app.AlertDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.database.UserPrefManager
import com.eightpeak.salakafarm.serverconfig.network.TokenManager
import com.eightpeak.salakafarm.utils.Constants
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.eightpeak.salakafarm.databinding.AddAddressDetailsBinding
import com.eightpeak.salakafarm.repository.AppRepository
import com.eightpeak.salakafarm.serverconfig.RequestBodies
import com.eightpeak.salakafarm.viewmodel.UserProfileViewModel
import com.eightpeak.salakafarm.viewmodel.ViewModelProviderFactory

import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener
import okhttp3.RequestBody


class AddressModification:AppCompatActivity() , OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener {
    private lateinit var binding: AddAddressDetailsBinding
    private var tokenManager: TokenManager? = null
    lateinit var userPrefManager: UserPrefManager
    private lateinit var mMap: GoogleMap
    private  var latestLat: Double=0.0
    private  var latestLng: Double=0.0

    private lateinit var viewModel: UserProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AddAddressDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        tokenManager = TokenManager.getInstance(
            getSharedPreferences(
                Constants.TOKEN_PREF,
                MODE_PRIVATE
            )
        )
        userPrefManager = UserPrefManager(this)

init()

    }
 fun onAddAddressClicked(){
     if(binding.address1.text!=null){
         var address1 :String=binding.address1.text.toString()
         var address2 :String=binding.address2.text.toString()
         var address3 :String=binding.address3.text.toString()
         var contact :String=binding.phone.text.toString()
        var addressBody= RequestBodies.AddAddress(address1,address2,address3,contact,latestLat.toString(),latestLng.toString())
         tokenManager?.let { viewModel.addNewAddressDetails(it,addressBody) }

     }

}

    private fun init() {
        val repository = AppRepository()
        val factory = ViewModelProviderFactory(application, repository)
        viewModel = ViewModelProvider(this, factory).get(UserProfileViewModel::class.java)

    }
    override fun onMapReady(googleMap: GoogleMap) {
        val sydney = LatLng(userPrefManager.currentLat.toDouble(), userPrefManager.currentLng.toDouble())

        latestLat= userPrefManager.currentLat.toDouble()
        latestLng= userPrefManager.currentLng.toDouble()
        mMap=googleMap
        var mark: Marker? = null
        mark = mMap.addMarker(
            MarkerOptions()
                .position(sydney)
//                .snippet(personDetails.toString())
                .icon(
                    BitmapDescriptorFactory.defaultMarker(
                        BitmapDescriptorFactory.HUE_ORANGE
                    )
                )
                .title("Your Current Position")
                .draggable(true)
        )
//        mark.setTag(i)
//                                markerList.add(mark)
        val center = CameraUpdateFactory.newLatLng(sydney)
        val zoom = CameraUpdateFactory.zoomTo(16f)
        mark.showInfoWindow()
        mMap.moveCamera(center)
        mMap.animateCamera(zoom)
       changelocation(mMap)
    }

    private fun changelocation(map: GoogleMap) {
        map.setOnMarkerDragListener(object : OnMarkerDragListener {
            override fun onMarkerDragStart(arg0: Marker) {
                // TODO Auto-generated method stub
                Log.d(
                    "System out",
                    "onMarkerDragStart..." + arg0.position.latitude + "..." + arg0.position.longitude
                )
            }

            override fun onMarkerDragEnd(arg0: Marker) {
                // TODO Auto-generated method stub
                Log.d(
                    "System out",
                    "onMarkerDragEnd..." + arg0.position.latitude + "..." + arg0.position.longitude
                )
                latestLat= arg0.position.latitude
                latestLng= arg0.position.longitude
                map.animateCamera(CameraUpdateFactory.newLatLng(arg0.position))
            }

            override fun onMarkerDrag(arg0: Marker) {
                // TODO Auto-generated method stub
                Log.i("System out", "onMarkerDrag...")
            }
        })
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        var clickCount = marker.tag as Int
        clickCount += 1
        marker.tag = clickCount
        val builder = AlertDialog.Builder(this)
        builder.setTitle(marker.title)
        builder.setMessage(marker.snippet)
        builder.show()
        return false
    }

}