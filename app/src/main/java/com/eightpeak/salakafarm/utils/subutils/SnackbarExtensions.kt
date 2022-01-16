package com.eightpeak.salakafarm.utils.subutils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.IntegerRes
import androidx.core.content.ContextCompat
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.databinding.AddNewAddressBinding
import com.eightpeak.salakafarm.serverconfig.RequestBodies
import com.eightpeak.salakafarm.views.addtocart.CartActivity
import com.eightpeak.salakafarm.views.comparelist.CompareListActivity
import com.eightpeak.salakafarm.views.home.address.AddressModification
import com.eightpeak.salakafarm.views.login.LoginActivity
import com.eightpeak.salakafarm.views.wishlist.WishlistActivity
import com.google.android.material.snackbar.Snackbar

@SuppressLint("ResourceType")
inline fun View.snack(@IntegerRes messageRes: Int, length: Int = Snackbar.LENGTH_LONG, f: Snackbar.() -> Unit) {
    snack(resources.getString(messageRes), length, f)
}


inline fun View.snack(message: String, length: Int = Snackbar.LENGTH_LONG, f: Snackbar.() -> Unit) {
    val snack = Snackbar.make(this, message, length)
    snack.f()
    snack.show()
}

fun View.showSnack(message: String, length: Int = Snackbar.LENGTH_LONG) {
    val snack = Snackbar.make(this, message, length)

    snack.setActionTextColor(Color.parseColor("#FFFFFF"))
    snack.view.setBackgroundColor(ContextCompat.getColor(context, R.color.yellow))
    snack.show()
}

inline fun View.errorSnack(message: String, length: Int = Snackbar.LENGTH_LONG, f: Snackbar.() -> Unit) {
    val snack = Snackbar.make(this, message, length)
    snack.f()
    snack.setActionTextColor(Color.parseColor("#FFFFFF"))
    snack.view.setBackgroundColor(Color.parseColor("#C62828"))
    snack.show()
}

fun View.errorSnack(message: String, length: Int = Snackbar.LENGTH_LONG) {
    val snack = Snackbar.make(this, message, length)
    snack.setActionTextColor(Color.parseColor("#FFFFFF"))
    snack.view.setBackgroundColor(Color.parseColor("#C62828"))
    snack.show()
}

fun View.notLoginWarningSnack(
    context: Context,
    length: Int = Snackbar.LENGTH_LONG
) {
    val snackbar = Snackbar
        .make(this, "Login/Register to ADD !!! ", length)
        .setAction("Lets Login") {
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        }

    val snackbarLayout = snackbar.view
    val lp = LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
    )

    lp.setMargins(10, 150, 10, 10)

    snackbarLayout.layoutParams = lp


    snackbar.setActionTextColor(Color.parseColor("#FFFFFF"))
    snackbar.view.setBackgroundColor(ContextCompat.getColor(context, R.color.yellow))
    snackbar.show()
}

fun View.successWishListSnack(

    context: Context,
    message: String,
    length: Int = Snackbar.LENGTH_LONG
) {
    val snackbar = Snackbar
        .make(this, message, length)
        .setAction(context.getString(R.string.view_wishlist)) {
            val intent = Intent(context, WishlistActivity::class.java)
            context.startActivity(intent)

        }
    snackbar.setActionTextColor(Color.parseColor("#FFFFFF"))
    snackbar.view.setBackgroundColor(ContextCompat.getColor(context, R.color.main_color))
    snackbar.show()
}

fun View.successAddToCartSnack(
    context: Context,
    message: String,
    length: Int = Snackbar.LENGTH_LONG
) {
    val snackbar = Snackbar
        .make(this, message, length)
        .setAction(context.getString(R.string.view_cart)) {
            val intent = Intent(context, CartActivity::class.java)
            context.startActivity(intent)

        }
    snackbar.setActionTextColor(Color.parseColor("#FFFFFF"))
    snackbar.view.setBackgroundColor(ContextCompat.getColor(context, R.color.main_color))
    snackbar.show()


}fun View.addAddressSnack(
    context: Context,
    message: String,
    length: Int = Snackbar.LENGTH_LONG
) {
    val snackbar = Snackbar
        .make(this, message, length)
        .setAction(context.getString(R.string.add_address)) {
            val intent = Intent(context, AddressModification::class.java)
            context.startActivity(intent)

        }
    snackbar.setActionTextColor(Color.parseColor("#FFFFFF"))
    snackbar.view.setBackgroundColor(Color.parseColor("#C62828"))
    snackbar.show()
}
fun View.successCompareSnack(
    context: Context,
    message: String,
    length: Int = Snackbar.LENGTH_LONG
) {
    val snackbar = Snackbar
        .make(this, message, length)
        .setAction("View Compare List") {
            val intent = Intent(context, CompareListActivity::class.java)
            context.startActivity(intent)

        }
    snackbar.setActionTextColor(Color.parseColor("#FFFFFF"))
    snackbar.view.setBackgroundColor(ContextCompat.getColor(context, R.color.main_color))
    snackbar.show()
}

@SuppressLint("ResourceType")
fun Snackbar.action(@IntegerRes actionRes: Int, color: Int? = null, listener: (View) -> Unit) {
    action(view.resources.getString(actionRes), color, listener)
}

fun Snackbar.action(action: String, color: Int? = null, listener: (View) -> Unit) {
    setAction(action, listener)
    color?.let { setActionTextColor(color) }
}

fun Context.toast(message: String){
    Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
}

fun ProgressBar.show(){
    this.visibility = View.VISIBLE
}

fun ProgressBar.hide(){
    this.visibility = View.GONE
}
