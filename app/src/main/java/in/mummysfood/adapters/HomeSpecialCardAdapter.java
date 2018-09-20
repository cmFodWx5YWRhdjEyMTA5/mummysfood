package in.mummysfood.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
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
            case R.id.chef_image:

                try {

                    Intent i = new Intent(context,OrderDetailsActivity.class);
                    i.putExtra("order_id",data.get(position).id);
                    i.putExtra("data",data.get(position));
                    context.startActivity(i);
                } catch (Exception e) {
                    e.printStackTrace();
                }

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
            foodImage = itemView.findViewById(R.id.chef_image);
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

        viewHolder.orderTitle.setText(model.food_detail.name);
        viewHolder.orderPrice.setText(context.getResources().getString(R.string.rupee_txt)+" "+model.food_detail.price);
        viewHolder.ChefName.setText(model.name);

        RoundRectShape roundRectShape = new RoundRectShape(new float[]{
                100, 100, 100, 100,
                100, 100, 100, 100}, null, null);
        ShapeDrawable shapeDrawable = new ShapeDrawable(roundRectShape);
        shapeDrawable.getPaint().setColor(Color.parseColor("#FFFFFF"));
        viewHolder.foodImage.setBackground(shapeDrawable);

        try {
            Glide.with(context).load(data.get(i).profile_image).placeholder(R.mipmap.foodimage).into(viewHolder.foodImage);
        }catch (IllegalArgumentException e){
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
