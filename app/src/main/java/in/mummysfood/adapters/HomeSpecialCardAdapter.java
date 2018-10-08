package in.mummysfood.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import in.mummysfood.R;
import in.mummysfood.fragments.OrderDetailsActivity;
import in.mummysfood.models.DashBoardModel;
import in.mummysfood.widgets.CkdTextview;

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

                    Intent i = new Intent(context,OrderDetailsActivity.class);
                    i.putExtra("order_id",data.get(position).id);
                    i.putExtra("data",data.get(position));
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

        public ViewHolder(View itemView) {
            super(itemView);

            orderTitle = itemView.findViewById(R.id.order_titile);
            orderPrice = itemView.findViewById(R.id.order_price);
            ChefName = itemView.findViewById(R.id.chef_name);
            foodImage = itemView.findViewById(R.id.food_image_bg);
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
            viewHolder.orderPrice.setText(context.getResources().getString(R.string.Rs)+" "+model.food_detail.get(0).price);
            viewHolder.ChefName.setText(model.name);

        } catch (Exception e) {
            e.printStackTrace();
        }

   /*     RoundRectShape roundRectShape = new RoundRectShape(new float[]{
                100, 100, 100, 100,
                100, 100, 100, 100}, null, null);
        ShapeDrawable shapeDrawable = new ShapeDrawable(roundRectShape);
    //    shapeDrawable.getPaint().setColor(Color.parseColor("#FFFFFF"));
        viewHolder.foodImage.setBackground(shapeDrawable);*/

        try {

            if (model.food_detail != null)
            {
                if(model.food_detail.get(0).food_media != null){

                    if (model.food_detail.get(0).food_media.size() != 0)
                    {
                        String imageUrl = "http://cdn.mummysfood.in/"+model.food_detail.get(0).food_media.get(0).media.name;

                        Log.d("ImageUrl",imageUrl);

                        Glide.with(context).load(imageUrl).placeholder(R.mipmap.foodimage).into(viewHolder.foodImage);
                    }

                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        viewHolder.foodImage.setTag(i);
        viewHolder.foodImage.setOnClickListener(this);


    }

    @Override
    public int getItemCount() {
        return data.size()==0 ? 0:data.size();
    }
}
