package com.mf.mumizzfood.adapters;

/**
 * Created by acer on 10/10/2018.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import com.mf.mumizzfood.R;
import com.mf.mumizzfood.models.ProfileModel;
import com.mf.mumizzfood.utils.AppConstants;


public class ChefFoodAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<String> foodMediaList ;

    public ChefFoodAdapter(Context context, ArrayList<String> foodMediaList) {
        this.context = context;
        this.foodMediaList = foodMediaList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView foodImage;

        public ViewHolder(View itemView) {
            super(itemView);
            foodImage = itemView.findViewById(R.id.food_image);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.food_image_layout, parent, false);
        return new ChefFoodAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        ChefFoodAdapter.ViewHolder holder = (ChefFoodAdapter.ViewHolder) viewHolder;

        String model = foodMediaList.get(i);
        if (model != null && !model.isEmpty()){
            Glide.with(context).load(AppConstants.IMAGEURL+model).into(holder.foodImage);
        }
    }

    @Override
    public int getItemCount() {
        return foodMediaList.size()!=0 ? foodMediaList.size():0;
    }
}

