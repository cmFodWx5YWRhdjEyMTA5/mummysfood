package com.mf.mumizzfood.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.mf.mumizzfood.R;
import com.mf.mumizzfood.models.ProfileModel;
import com.mf.mumizzfood.utils.AppConstants;
import com.mf.mumizzfood.widgets.CkdTextview;
import com.mf.mumizzfood.widgets.ImageLoadProgressBar;

public class FoodDataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<ProfileModel.Food_detail> foodMediaList;

    public FoodDataAdapter(Context context, List<ProfileModel.Food_detail> foodMediaList) {
        this.context = context;
        this.foodMediaList = foodMediaList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView foodImage;
        SimpleDraweeView myImageView;
        CkdTextview food_title;
        CkdTextview food_price;

        public ViewHolder(View itemView) {
            super(itemView);
            foodImage = itemView.findViewById(R.id.food_image);
            myImageView = itemView.findViewById(R.id.my_image_view);
            food_title = (CkdTextview)itemView.findViewById(R.id.food_title);
            food_price = (CkdTextview)itemView.findViewById(R.id.food_price);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_food_image_layout, parent, false);
        return new FoodDataAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        FoodDataAdapter.ViewHolder holder = (FoodDataAdapter.ViewHolder) viewHolder;

        ProfileModel.Food_detail model = foodMediaList.get(i);

        //Log.d("image", "onBindViewHolder: "+AppConstants.IMAGEURL+model.food_media.get(model.food_media.size()-1).media.name);
        if (model.food_media.get(model.food_media.size()-1).media.name != null && !model.food_media.get(model.food_media.size()-1).media.name.isEmpty()){
            Glide.with(context).load(AppConstants.IMAGEURL+model.food_media.get(model.food_media.size()-1).media.name).into(holder.foodImage);
        }

        holder.food_title.setText(foodMediaList.get(i).name);
        holder.food_price.setText(foodMediaList.get(i).price);
//
//        try {
//            String imageUrl = AppConstants.IMAGEURL + model.food_media.get(0).media.name;
//            Log.d("ImageUrl", imageUrl);
//            //  Glide.with(context).load(imageUrl).into(holder.food_image);
//            try {
//
//                ImageRequest request = ImageRequestBuilder
//                        .newBuilderWithSource(Uri.parse(imageUrl))
//                        .setLocalThumbnailPreviewsEnabled(true)
//                        .setLowestPermittedRequestLevel(ImageRequest.RequestLevel.FULL_FETCH)
//                        .setProgressiveRenderingEnabled(true)
//                        .setCacheChoice(ImageRequest.CacheChoice.SMALL)
//                        .build();
//
//                DraweeController controller = Fresco.newDraweeControllerBuilder()
//                        .setImageRequest(request)
//                        .setOldController(holder.myImageView.getController())
//                        .build();
//
//                holder.myImageView.getHierarchy().setProgressBarImage(new ImageLoadProgressBar());
//                //Assigning the controller to DraweeView
//                holder.myImageView.setController(controller);
//
//            } catch (Exception e) {
//
//                e.printStackTrace();
//
//            }
//        } catch (Exception e) {
//
//            e.printStackTrace();
//
//        }
    }

    @Override
    public int getItemCount() {
        return foodMediaList.size()!=0 ? foodMediaList.size():0;
    }
}
