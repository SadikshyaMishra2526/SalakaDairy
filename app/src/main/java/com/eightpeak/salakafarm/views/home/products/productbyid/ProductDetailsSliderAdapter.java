package com.eightpeak.salakafarm.views.home.products.productbyid;

import static com.eightpeak.salakafarm.utils.EndPoints.BASE_URL;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eightpeak.salakafarm.R;
import com.eightpeak.salakafarm.utils.AppUtils;
import com.eightpeak.salakafarm.views.home.slider.Data;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailsSliderAdapter extends SliderViewAdapter<ProductDetailsSliderAdapter.SliderAdapterVH> {

    private Context context;
    private List<String> mSliderItems = new ArrayList<>();

    public ProductDetailsSliderAdapter(Context context,List<String>mSliderItems) {
        this.context = context;
        this.mSliderItems=mSliderItems;
    }

    public void renewItems(List<String> sliderItems) {
        this.mSliderItems = sliderItems;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        this.mSliderItems.remove(position);
        notifyDataSetChanged();
    }

    public void addItem(List<String> sliderItem) {
//        Log.i("TAG", "addItem:........... "+sliderItem.get(0).getImage());
        this.mSliderItems=sliderItem;
        notifyDataSetChanged();
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {

        String sliderItem = mSliderItems.get(position);
        Log.i("TAG", "onBindViewHolder: "+sliderItem);
        Glide.with(viewHolder.itemView)
                .load(BASE_URL+sliderItem)
                .fitCenter()
                .placeholder(R.drawable.logo)
                .into(viewHolder.imageViewBackground);

    }

    @Override
    public int getCount() {
        return mSliderItems.size();
    }


    static class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        ImageView imageViewBackground;
        ImageView imageGifContainer;
        TextView textViewDescription;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
            imageGifContainer = itemView.findViewById(R.id.iv_gif_container);
            textViewDescription = itemView.findViewById(R.id.tv_auto_image_slider);
            this.itemView = itemView;
        }
    }
}
