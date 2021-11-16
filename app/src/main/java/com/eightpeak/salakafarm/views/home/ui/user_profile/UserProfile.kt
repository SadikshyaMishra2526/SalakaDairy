package com.eightpeak.salakafarm.views.home.ui.user_profile

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.database.UserPrefManager
import android.view.Window

import androidx.core.content.ContextCompat

import android.view.WindowManager
import com.eightpeak.salakafarm.databinding.ActivityUserProfileBinding
import com.eightpeak.salakafarm.serverconfig.network.TokenManager
import com.eightpeak.salakafarm.utils.Constants
import com.eightpeak.salakafarm.views.addtocart.CartActivity
import com.eightpeak.salakafarm.views.comparelist.CompareListActivity
import com.eightpeak.salakafarm.views.home.HomeActivity
import com.eightpeak.salakafarm.views.order.orderview.orderhistory.OrderHistory
import com.eightpeak.salakafarm.views.wishlist.WishlistActivity


class UserProfile : AppCompatActivity() {

    private lateinit var userPrefManager: UserPrefManager

    private lateinit var binding: ActivityUserProfileBinding

    private var tokenManager: TokenManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tokenManager = TokenManager.getInstance(
            getSharedPreferences(
                Constants.TOKEN_PREF,
                MODE_PRIVATE
            )
        );
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getStatusColor()
        userPrefManager = UserPrefManager(this)
        binding.userName.text = userPrefManager.firstName + " " + userPrefManager.lastName
        binding.userEmail.text = userPrefManager.email
        binding.userAddress1.text = userPrefManager.userAddress1
        binding.userAddress2.text = userPrefManager.userAddress2
        binding.userCountry.text = userPrefManager.userCountry
        binding.userPhone.text = userPrefManager.contactNo

        binding.headerLayout.headerName.text = "User Profile"

        binding.userLogout.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(R.string.logout)
            builder.setMessage(R.string.logout_msg)
            builder.setPositiveButton(R.string.logout,
                DialogInterface.OnClickListener { dialog, which ->
                    userPrefManager.clearData()
                    tokenManager?.deleteToken()
                    startActivity(Intent(this@UserProfile, HomeActivity::class.java))
                    finish()
                })
            builder.setNegativeButton(R.string.cancel, null)

            val dialog = builder.create()
            dialog.show()

        }
        binding.viewWishlist.setOnClickListener {
            startActivity(Intent(this@UserProfile,WishlistActivity::class.java))
            finish()
        }
        binding.orderHistory.setOnClickListener {
            startActivity(Intent(this@UserProfile,WishlistActivity::class.java))
            finish()
        }
        binding.compareList.setOnClickListener {
//            startActivity(Intent(this@UserProfile,CompareListActivity::class.java))
//            finish()
            val intent = Intent(this@UserProfile, CartActivity::class.java)
            startActivity(intent)
        }
     binding.orderHistory.setOnClickListener {
            val intent = Intent(this@UserProfile, OrderHistory::class.java)
            startActivity(intent)

        }
    }

    private fun getStatusColor() {
        val window: Window = window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.sub_color)
    }
}