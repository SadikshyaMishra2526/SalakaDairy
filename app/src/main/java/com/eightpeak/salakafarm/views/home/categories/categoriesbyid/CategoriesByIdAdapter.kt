package com.eightpeak.salakafarm.views.home.categories.categoriesbyid

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.eightpeak.salakafarm.App
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.database.UserPrefManager
import com.eightpeak.salakafarm.utils.Constants
import com.eightpeak.salakafarm.utils.EndPoints
import com.eightpeak.salakafarm.utils.GeneralUtils
import com.eightpeak.salakafarm.views.home.products.AddToCartView
import com.eightpeak.salakafarm.views.home.products.productbyid.ProductByIdActivity
import kotlinx.android.synthetic.main.product_item.view.*
import kotlin.math.roundToInt

class CategoriesByIdAdapter : RecyclerView.Adapter<CategoriesByIdAdapter.CategoriesByIdViewHolder>() {

    inner class CategoriesByIdViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    lateinit var userPrefManager: UserPrefManager

    private val differCallback = object : DiffUtil.ItemCallback<Products_with_description>() {

        override fun areItemsTheSame(oldItem: Products_with_description, newItem: Products_with_description): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Products_with_description, newItem: Products_with_description): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CategoriesByIdViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.product_item,
            parent,
            false
        )
    )
    override fun getItemCount() =  differ.currentList.size


    override fun onBindViewHolder(holder: CategoriesByIdViewHolder, position: Int) {
        val categoriesItem = differ.currentList[position]
        userPrefManager= UserPrefManager(App.getContext())

        holder.itemView.apply {
            product_thumbnail.load(EndPoints.BASE_URL +categoriesItem.image)

            val rating: Int? = categoriesItem.average_rating?.roundToInt()
            rated_by.text = "("+categoriesItem.no_of_rating+") "
            if(rating==1){
                rating_1.setImageDrawable(AppCompatResources.getDrawable(context,R.drawable.ic_baseline_star_rate_24))
            }else if(rating==2){
                rating_1.setImageDrawable(AppCompatResources.getDrawable(context,R.drawable.ic_baseline_star_rate_24))
                rating_2.setImageDrawable(AppCompatResources.getDrawable(context,R.drawable.ic_baseline_star_rate_24))
            }else if(rating==3){
                rating_1.setImageDrawable(AppCompatResources.getDrawable(context,R.drawable.ic_baseline_star_rate_24))
                rating_2.setImageDrawable(AppCompatResources.getDrawable(context,R.drawable.ic_baseline_star_rate_24))
                rating_3.setImageDrawable(AppCompatResources.getDrawable(context,R.drawable.ic_baseline_star_rate_24))
            }else if(rating==4){
                rating_1.setImageDrawable(AppCompatResources.getDrawable(context,R.drawable.ic_baseline_star_rate_24))
                rating_2.setImageDrawable(AppCompatResources.getDrawable(context,R.drawable.ic_baseline_star_rate_24))
                rating_3.setImageDrawable(AppCompatResources.getDrawable(context,R.drawable.ic_baseline_star_rate_24))
                rating_4.setImageDrawable(AppCompatResources.getDrawable(context,R.drawable.ic_baseline_star_rate_24))
            }else if(rating==5){
                rating_1.setImageDrawable(AppCompatResources.getDrawable(context,R.drawable.ic_baseline_star_rate_24))
                rating_2.setImageDrawable(AppCompatResources.getDrawable(context,R.drawable.ic_baseline_star_rate_24))
                rating_3.setImageDrawable(AppCompatResources.getDrawable(context,R.drawable.ic_baseline_star_rate_24))
                rating_4.setImageDrawable(AppCompatResources.getDrawable(context,R.drawable.ic_baseline_star_rate_24))
                rating_5.setImageDrawable(AppCompatResources.getDrawable(context,R.drawable.ic_baseline_star_rate_24))
            }





            if (categoriesItem.descriptions?.isNotEmpty() == true) {
                if (userPrefManager.language.equals("ne")) {
                    product_name.text = categoriesItem.descriptions[1].name

                    if(categoriesItem.promotion_price.price_promotion != null){
                        product_price_discount.text= GeneralUtils.getUnicodeNumber(categoriesItem.price.toString())
                        product_price_discount.paintFlags = product_price_discount.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                        product_price.text =
                            context.getString(R.string.rs) + " " + GeneralUtils.getUnicodeNumber(categoriesItem.promotion_price.price_promotion.toString())
                    }else{
                        product_price.text =
                            context.getString(R.string.rs) + " " + GeneralUtils.getUnicodeNumber(categoriesItem.price.toString())
                    }

                } else {
                    product_name.text = categoriesItem.descriptions[0].name

                    if(categoriesItem.promotion_price!=null){
                        product_price_discount.text=categoriesItem.price.toString()
                        product_price_discount.paintFlags = product_price_discount.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                        product_price.text =
                            context.getString(R.string.rs) + categoriesItem.promotion_price.price_promotion.toString()
                    }else{
                        product_price.text =
                            context.getString(R.string.rs) + categoriesItem.price.toString()
                    }
                }
            }

            bt_add_to_cart.setOnClickListener {
                val args = Bundle()
                args.putString(Constants.PRODUCT_ID, categoriesItem.id.toString())
                val bottomSheet = AddToCartView()
                bottomSheet.arguments = args
                bottomSheet.show(
                    (context as FragmentActivity).supportFragmentManager,
                    bottomSheet.tag
                )
            }
            productViewItem.setOnClickListener {
                val intent = Intent(context, ProductByIdActivity::class.java)
                intent.putExtra(Constants.PRODUCT_ID, categoriesItem.id.toString())
                context.startActivity(intent)
            }
        }
    }
}