package com.mf.mumizzfood.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mf.mumizzfood.R;
import com.mf.mumizzfood.activities.PartyCorporateActivity;
import com.mf.mumizzfood.utils.AppConstants;

import java.util.ArrayList;

public class HomeWhatsNewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    Integer[] foodMediaList ;

    public HomeWhatsNewAdapter(Context context, Integer[] foodMediaList) {
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
        View view = LayoutInflater.from(context).inflate(R.layout.whats_new_layout, parent, false);
        return new HomeWhatsNewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        HomeWhatsNewAdapter.ViewHolder holder = (HomeWhatsNewAdapter.ViewHolder) viewHolder;

        holder.foodImage.setImageResource(foodMediaList[position]);

        holder.foodImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position == 0){
                    String contact = "+91 9981192339"; // use country code with your phone number
                    String url = "https://api.whatsapp.com/send?phone=" + contact;
                    try {
                        PackageManager pm = context.getPackageManager();
                        pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        context.startActivity(i);
                    } catch (PackageManager.NameNotFoundException e) {
                        Toast.makeText(context, "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }else if (position == 1){
                    final String appPackageName = context.getPackageName(); // getPackageName() from Context or Activity object
                    try {
                        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    }

                }else if (position == 1){


                }
            }
        });



    }

    @Override
    public int getItemCount() {
        return foodMediaList.length!=0 ? foodMediaList.length:0;
    }
}

