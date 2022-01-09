package com.eightpeak.salakafarm.views.home.products.productbyid

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.eightpeak.salakafarm.views.home.products.AddToCartView
import kotlinx.android.synthetic.main.product_item.view.*
import kotlin.math.roundToInt

class RelatedProductAdapter : RecyclerView.Adapter<RelatedProductAdapter.ProductListViewHolder>() {

    inner class ProductListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<ProductRelation>() {

        override fun areItemsTheSame(oldItem: ProductRelation, newItem: ProductRelation): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProductRelation, newItem: ProductRelation): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProductListViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.product_item,
            parent,
            false
        )
    )

    override fun getItemCount() = differ.currentList.size


    override fun onBindViewHolder(holder: ProductListViewHolder, position: Int) {
        val categoriesItem = differ.currentList[position]
        holder.itemView.apply {
            product_thumbnail.load(EndPoints.BASE_URL + categoriesItem.image)
            if(categoriesItem.stock>0){
                out_of_stock.visibility=View.GONE
            }else{
                out_of_stock.visibility=View.VISIBLE
            }
            var userPrefManager = UserPrefManager(App.getContext())

            val rating:Int= categoriesItem.average_rating.roundToInt()
            rated_by.text = "("+categoriesItem.no_of_rating+") "
            if(rating==1){
                rating_1.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_star_rate_24))
            }else if(rating==2){
                rating_1.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_star_rate_24))
                rating_2.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_star_rate_24))
            }else if(rating==3){
                rating_1.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_star_rate_24))
                rating_2.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_star_rate_24))
                rating_3.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_star_rate_24))
            }else if(rating==4){
                rating_1.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_star_rate_24))
                rating_2.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_star_rate_24))
                rating_3.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_star_rate_24))
                rating_4.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_star_rate_24))
            }else if(rating==5){
                rating_1.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_star_rate_24))
                rating_2.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_star_rate_24))
                rating_3.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_star_rate_24))
                rating_4.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_star_rate_24))
                rating_5.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_star_rate_24))
            }

            if (categoriesItem.descriptions.isNotEmpty()) {
                if (userPrefManager.language.equals("ne")) {
                    product_name.text = categoriesItem.descriptions[1].name

//                    if(!categoriesItem.promotion_price.price_promotion.equals("0")){
//                        product_price_discount.text=GeneralUtils.getUnicodeNumber(categoriesItem.price.toString())
//                        product_price_discount.paintFlags = product_price_discount.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
//                        product_price.text =
//                            context.getString(R.string.rs) + " " + GeneralUtils.getUnicodeNumber(categoriesItem.promotion_price.price_promotion.toString())
//                    }else{
//                        product_price.text =
//                            context.getString(R.string.rs) + " " + GeneralUtils.getUnicodeNumber(categoriesItem.price.toString())
//                    }

                } else {
                    product_name.text = categoriesItem.descriptions[0].name

                    if(!categoriesItem.cost.equals("0")){
                        product_price_discount.text=categoriesItem.price.toString()
                        product_price_discount.paintFlags = product_price_discount.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
//                        product_price.text =
//                            context.getString(R.string.rs) + categoriesItem.promotion_price.price_promotion.toString()
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