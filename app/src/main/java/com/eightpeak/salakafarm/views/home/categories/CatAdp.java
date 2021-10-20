package com.eightpeak.salakafarm.views.home.categories;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eightpeak.salakafarm.R;

import java.util.List;

public class CatAdp extends RecyclerView.Adapter<CatAdp.RecyclerViewCartHolder> {
    Context context;
    List<Data> products;


    public CatAdp(Context context, List<Data> products) {
        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public RecyclerViewCartHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.categories_item, viewGroup, false);
        return new RecyclerViewCartHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewCartHolder holder, int position) {

       holder.product_name.setText(products.get(position).getDescriptions().get(0).getTitle());
        Toast.makeText(context.getApplicationContext(), products.get(position).getImage(), Toast.LENGTH_SHORT).show();
    }


   @Override
    public int getItemCount() {
       Log.i("TAG", "getItemCount: "+products.size());
        return products.size();
    }


    class RecyclerViewCartHolder extends RecyclerView.ViewHolder {
        ImageView product_image;
        TextView product_name;
       public RecyclerViewCartHolder(@NonNull View itemView) {
            super(itemView);
           product_image = itemView.findViewById(R.id.categories_thumbnail);
           product_name = itemView.findViewById(R.id.categories_name);

        }
    }
}