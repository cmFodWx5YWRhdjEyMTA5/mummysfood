package com.mf.mumizzfood.adapters;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.mf.mumizzfood.data.pref.PreferenceManager;
import com.mf.mumizzfood.fragments.OrderDetailsActivity;

import com.mf.mumizzfood.R;

import com.mf.mumizzfood.models.HomeFeed;
import com.mf.mumizzfood.utils.CapsName;
import com.mf.mumizzfood.widgets.CkdTextview;
import com.mf.mumizzfood.widgets.DistanceCalculator;
import com.mf.mumizzfood.widgets.ImageLoadProgressBar;

import java.text.DecimalFormat;
import java.util.List;

public class HomeSpecialCardAdapter extends RecyclerView.Adapter<HomeSpecialCardAdapter.ViewHolder> implements View.OnClickListener {
    Context context;
    List<HomeFeed.Data> dataList;
    PreferenceManager pf;

  /*  public HomeSpecialCardAdapter(Context context, List<DashBoardModel.Data> data) {
        this.context = context;
        this.data = data;
    }*/

    public HomeSpecialCardAdapter(Context activity, List<HomeFeed.Data> fetchDataHome) {
        this.context = activity;
        this.dataList = fetchDataHome;
        pf = new PreferenceManager(activity);
    }

    @Override
    public void onClick(View v) {
        int position = (Integer) v.getTag();
        switch (v.getId()) {
            case R.id.my_image_view_special:

                try {

                    Intent i = new Intent(context, OrderDetailsActivity.class);
                    i.putExtra("order_id", dataList.get(position).id);
                    i.putExtra("data", dataList.get(position));
                    context.startActivity(i);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case R.id.chef_name:
              /*  ProfileFragmentChef fragment1 = new ProfileFragmentChef();
                Bundle bundle1 = new Bundle();
                bundle1.putInt("user_id", data.get(position).chef_detail.user_id);
                fragment1.setArguments(bundle1);
                FragmentManager fragmentManager1 = (((AppCompatActivity) context).getSupportFragmentManager());
                FragmentTransaction fragmentTransaction1 = fragmentManager1
                        .beginTransaction();
                fragmentTransaction1.addToBackStack(fragment1.getClass().getSimpleName());
                fragmentTransaction1.replace(R.id.content_frame, fragment1);
                fragmentTransaction1.commit();*/
                break;
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CkdTextview orderTitle;
        CkdTextview orderPrice;
        CkdTextview ChefName;
        ImageView foodImage;
        CkdTextview order_distance;
        SimpleDraweeView my_image_view;
        ImageView vegSysmbol;

        public ViewHolder(View itemView) {
            super(itemView);

            orderTitle = itemView.findViewById(R.id.order_titile);
            orderPrice = itemView.findViewById(R.id.order_price);
            ChefName = itemView.findViewById(R.id.chef_name);
            foodImage = itemView.findViewById(R.id.food_image_bg);
            order_distance = itemView.findViewById(R.id.order_distance);
            my_image_view = itemView.findViewById(R.id.my_image_view_special);
            vegSysmbol = itemView.findViewById(R.id.vegSysmbol);
        }
    }

    @Override
    public HomeSpecialCardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_today_special, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HomeSpecialCardAdapter.ViewHolder viewHolder, int i) {

        HomeFeed.Data model = dataList.get(i);

        try {
            viewHolder.orderTitle.setText(model.name);
            viewHolder.orderPrice.setText(context.getResources().getString(R.string.rs_symbol) + model.price);
            String name = CapsName.CapitalizeFullName(model.user.name.trim());
            viewHolder.ChefName.setText(name);

        } catch (Exception e) {
            e.printStackTrace();
        }

        double latji = pf.getDoubleForKey("latitude",12.9732098);
        double longji = pf.getDoubleForKey("lognitude",79.1590077);

         /*   Location.distanceBetween(latji, longji, data.addresses.get(0).latitude, data.addresses.get(0).longitude,results);
            float distance = results[0]/100000;
            DecimalFormat value = new DecimalFormat("#.#");
*/

        DistanceCalculator distance = new DistanceCalculator();

        double valuep =  distance.greatCircleInKilometers(latji, longji, model.addresses.get(0).latitude, model.addresses.get(0).longitude);
        viewHolder.order_distance.setText(String.valueOf(new DecimalFormat("##.##").format(valuep))+"km");

        if (model.food_type == null || model.food_type.equalsIgnoreCase("0"))
            viewHolder.vegSysmbol.setColorFilter(context.getResources().getColor(R.color.green));
        else
        {
            viewHolder.vegSysmbol.setColorFilter(context.getResources().getColor(R.color.red));
        }

        try {

            if (model != null) {
                if (model.food_media != null) {

                    if (model.food_media.size() != 0) {
                        String imageUrl = "http://cdn.mummysfood.in/" + model.food_media.get(0).media.name;

                       // Glide.with(context).load(imageUrl).placeholder(R.mipmap.foodimage).into(viewHolder.foodImage);

                        try {

                            ImageRequest request = ImageRequestBuilder
                                    .newBuilderWithSource(Uri.parse(imageUrl))
                                    .setLocalThumbnailPreviewsEnabled(true)
                                    .setLowestPermittedRequestLevel(ImageRequest.RequestLevel.FULL_FETCH)
                                    .setProgressiveRenderingEnabled(true)
                                    .setCacheChoice(ImageRequest.CacheChoice.SMALL)
                                    .build();

                            DraweeController controller = Fresco.newDraweeControllerBuilder()
                                    .setImageRequest(request)
                                    .setOldController(viewHolder.my_image_view.getController())
                                    .build();

                            viewHolder.my_image_view.getHierarchy().setProgressBarImage(new ImageLoadProgressBar());
                            //Assigning the controller to DraweeView
                            viewHolder.my_image_view.setController(controller);

                        } catch (IllegalArgumentException e) {

                            e.printStackTrace();

                        }
                    }

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        viewHolder.my_image_view.setTag(i);
        viewHolder.my_image_view.setOnClickListener(this);


    }

    @Override
    public int getItemCount() {
        return dataList.size() == 0 ? 0 : dataList.size();
    }
}
