package in.ckd.calenderkhanado.adapters;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import in.ckd.calenderkhanado.R;
import in.ckd.calenderkhanado.fragments.OrderDetailsActivity;
import in.ckd.calenderkhanado.models.DashBoardModel;
import in.ckd.calenderkhanado.utils.CapsName;
import in.ckd.calenderkhanado.widgets.CkdTextview;

import java.text.DecimalFormat;
import java.util.List;

public class HomeSpecialCardAdapter extends RecyclerView.Adapter<HomeSpecialCardAdapter.ViewHolder> implements View.OnClickListener {
    Context context;
    List<DashBoardModel.Data> data;

    public HomeSpecialCardAdapter(Context context, List<DashBoardModel.Data> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public void onClick(View v) {
        int position = (Integer) v.getTag();
        switch (v.getId()) {
            case R.id.food_image_bg:

                try {

                    Intent i = new Intent(context, OrderDetailsActivity.class);
                    i.putExtra("order_id", data.get(position).id);
                    i.putExtra("data", data.get(position));
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

        public ViewHolder(View itemView) {
            super(itemView);

            orderTitle = itemView.findViewById(R.id.order_titile);
            orderPrice = itemView.findViewById(R.id.order_price);
            ChefName = itemView.findViewById(R.id.chef_name);
            foodImage = itemView.findViewById(R.id.food_image_bg);
            order_distance = itemView.findViewById(R.id.order_distance);
        }
    }

    @Override
    public HomeSpecialCardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_today_special, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HomeSpecialCardAdapter.ViewHolder viewHolder, int i) {

        DashBoardModel.Data model = data.get(i);

        try {
            viewHolder.orderTitle.setText(model.food_detail.get(0).name);
            viewHolder.orderPrice.setText(context.getResources().getString(R.string.rs_symbol) + model.food_detail.get(0).price);
            String name = CapsName.CapitalizeFullName(model.name.trim());
            viewHolder.ChefName.setText(name);

        } catch (Exception e) {
            e.printStackTrace();
        }

        float[] results = new float[1];
        Location.distanceBetween(12.9732098, 79.1590077, 22.7602485, 75.8880693, results);
        float distance = results[0] / 100000;
        DecimalFormat value = new DecimalFormat("#.#");

        viewHolder.order_distance.setText(value.format(distance) + "km");

        try {

            if (model.food_detail != null) {
                if (model.food_detail.get(0).food_media != null) {

                    if (model.food_detail.get(0).food_media.size() != 0) {
                        String imageUrl = "http://cdn.mummysfood.in/" + model.food_detail.get(0).food_media.get(0).media.name;

                        Glide.with(context).load(imageUrl).placeholder(R.mipmap.foodimage).into(viewHolder.foodImage);
                    }

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        viewHolder.foodImage.setTag(i);
        viewHolder.foodImage.setOnClickListener(this);


    }

    @Override
    public int getItemCount() {
        return data.size() == 0 ? 0 : data.size();
    }
}
