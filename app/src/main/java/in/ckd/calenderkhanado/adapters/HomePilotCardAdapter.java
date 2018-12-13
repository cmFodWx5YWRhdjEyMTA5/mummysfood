package in.ckd.calenderkhanado.adapters;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import in.ckd.calenderkhanado.R;
import in.ckd.calenderkhanado.data.pref.PreferenceManager;
import in.ckd.calenderkhanado.fragments.HomeFragment;
import in.ckd.calenderkhanado.fragments.OrderDetailsActivity;
import in.ckd.calenderkhanado.fragments.ProfileFragmentChef;
import in.ckd.calenderkhanado.models.DashBoardModel;
import in.ckd.calenderkhanado.models.HomeFeed;
import in.ckd.calenderkhanado.utils.CalculateDistance;
import in.ckd.calenderkhanado.utils.CapsName;
import in.ckd.calenderkhanado.widgets.CkdTextview;
import in.ckd.calenderkhanado.widgets.ImageLoadProgressBar;

import java.text.DecimalFormat;
import java.util.List;

import static android.location.Location.distanceBetween;

public class HomePilotCardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private Context context;
    private List<DashBoardModel.Data> data;
    private List<HomeFeed.Data> dataList;
    private OrderListner listner;


    public HomePilotCardAdapter(Context activity, List<HomeFeed.Data> fetchDataHome, HomeFragment listner) {
        this.context = activity;
        this.dataList = fetchDataHome;
        this.listner = listner;
    }


    public interface OrderListner {
        void AddToCart(int position);

        void AddFoodQuantity(int position);

        void SubFoodQuantity(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView food_image;
        CkdTextview chef_name;
        CkdTextview food_title;
        CkdTextview food_price;
        CkdTextview order_distance;
        SimpleDraweeView myImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            food_image = (ImageView) itemView.findViewById(R.id.food_image);
            food_title = (CkdTextview)itemView.findViewById(R.id.food_title);
            chef_name = (CkdTextview)itemView.findViewById(R.id.chef_name);
            food_price = (CkdTextview)itemView.findViewById(R.id.food_price);
            order_distance = (CkdTextview)itemView.findViewById(R.id.order_distance);
            myImageView = itemView.findViewById(R.id.my_image_view);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_food_pilots, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int i) {

        final ViewHolder holder = (ViewHolder) viewHolder;

        HomeFeed.Data data = dataList.get(i);


        try {
//            pf = new PreferenceManager(context,PreferenceManager.ORDER_PREFERENCES_FILE);

            String name = CapsName.CapitalizeFullName(data.name.trim());
            holder.chef_name.setText(name);
            if(data.food_media.get(0) != null){
                try {
                    String imageUrl = "http://cdn.mummysfood.in/"+data.food_media.get(0).media.name;
                    Log.d("ImageUrl",imageUrl);
                  //  Glide.with(context).load(imageUrl).into(holder.food_image);


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
                                .setOldController( holder.myImageView.getController())
                                .build();

                        holder.myImageView.getHierarchy().setProgressBarImage(new ImageLoadProgressBar());
                        //Assigning the controller to DraweeView
                        holder.myImageView.setController(controller);

                    } catch (Exception e) {

                        e.printStackTrace();

                    }

                }catch (IllegalArgumentException e){
                    e.printStackTrace();
                }
            }else{
                holder.food_image.setImageResource(R.mipmap.foodimage);
            }

            if (data.price != null){
                holder.food_price.setText(context.getResources().getString(R.string.Rs)+data.price);
            }

            if (data.name != null){
                holder.food_title.setText(data.name);
            }

            float[] results = new float[1];
            Location.distanceBetween(12.9732098, 79.1590077, 22.7602485, 75.8880693,results);
            float distance = results[0]/100000;
            DecimalFormat value = new DecimalFormat("#.#");

            holder.order_distance.setText(value.format(distance)+"km");
            holder.myImageView.setTag(i);
            holder.myImageView.setOnClickListener(this);
            holder.chef_name.setTag(i);
            holder.chef_name.setOnClickListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }


        /*holder.add_to_cart.setTag(i);
        holder.add_to_cart.setOnClickListener(this);
        holder.sub_item.setTag(i);
        holder.sub_item.setOnClickListener(this);
        holder.add_item.setTag(i);
        holder.add_item.setOnClickListener(this);*/

    }

    @Override
    public void onClick(View v) {
        int position = (Integer) v.getTag();
        switch (v.getId()){
            case R.id.my_image_view:
                try {
                    Intent i = new Intent(context,OrderDetailsActivity.class);
                    i.putExtra("order_id",dataList.get(position).id);
                    i.putExtra("data",dataList.get(position));
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


                Intent profileIntent = new Intent(context,ProfileFragmentChef.class);
                profileIntent.putExtra("user_id",data.get(position).chef_detail.user_id);
                context.startActivity(profileIntent);
                break;

            /*case R.id.add_to_cart:
                listner.AddToCart(position);
                break;
            case R.id.add_item:
                listner.AddFoodQuantity(position);
                break;
            case R.id.sub_item:
                listner.SubFoodQuantity(position);
                break;*/

        }

    }

    @Override
    public int getItemCount() {
        return dataList.size()!=0 ? dataList.size():0;
    }

}
