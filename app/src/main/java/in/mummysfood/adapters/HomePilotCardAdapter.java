package in.mummysfood.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.bumptech.glide.Glide;
import in.mummysfood.R;
import in.mummysfood.data.pref.PreferenceManager;
import in.mummysfood.fragments.OrderDetailsFragment;
import in.mummysfood.fragments.ProfileFragment;
import in.mummysfood.models.DashBoardModel;
import in.mummysfood.models.ProfileModel;
import in.mummysfood.models.QuestionsModel;
import in.mummysfood.widgets.CkdTextview;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class HomePilotCardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private Context context;
    private List<DashBoardModel.Data> data;
    private OrderListner listner;
    PreferenceManager pf;

    public static OrderDetailsFragment fragment;

    public HomePilotCardAdapter(Context context, List<DashBoardModel.Data> data, OrderListner listner) {
        this.context = context;
        this.data = data;
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

        public ViewHolder(View itemView) {
            super(itemView);
            food_image = (ImageView) itemView.findViewById(R.id.food_image);
            food_title = (CkdTextview)itemView.findViewById(R.id.food_title);
            chef_name = (CkdTextview)itemView.findViewById(R.id.chef_name);
            food_price = (CkdTextview)itemView.findViewById(R.id.food_price);
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

        pf = new PreferenceManager(context,PreferenceManager.ORDER_PREFERENCES_FILE);

        holder.chef_name.setText(data.get(i).f_name);
        if(data.get(i).profile_image != null){
            try {
                Glide.with(context).load(data.get(i).profile_image).into(holder.food_image);

            }catch (IllegalArgumentException e){
                e.printStackTrace();
            }
        }else{
            holder.food_image.setImageResource(R.mipmap.pilot2);
        }
        /*Bitmap mbitmap = ((BitmapDrawable) context.getResources().getDrawable(R.drawable.rounded_white_smoke_bg)).getBitmap();
        Bitmap imageRounded = Bitmap.createBitmap(mbitmap.getWidth(), mbitmap.getHeight(), mbitmap.getConfig());
        Canvas canvas = new Canvas(imageRounded);
        Paint mpaint = new Paint();
        mpaint.setAntiAlias(true);
        mpaint.setShader(new BitmapShader(mbitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
        canvas.drawRoundRect((new RectF(0, 0, mbitmap.getWidth(), mbitmap.getHeight())), 100, 100, mpaint);// Round Image Corner 100 100 100 100
        holder.food_image.setImageBitmap(imageRounded);*/

        //check share prefrence
        /*if (pf.getIntForKey(PreferenceManager.USER_ID, 0) != 0 && pf.getIntForKey(PreferenceManager.USER_ID, 0) == data.get(i).id){
            holder.add_to_cart.setVisibility(View.GONE);
            holder.add_to_cart_item_layout.setVisibility(View.VISIBLE);
            if (pf.getIntForKey(PreferenceManager.ORDER_quantity, 0) != 0) {
                holder.item_count.setText("" + pf.getIntForKey(PreferenceManager.ORDER_quantity, 0));
            } else {
                holder.add_to_cart.setVisibility(View.VISIBLE);
                holder.add_to_cart_item_layout.setVisibility(View.GONE);
            }
        }else {
            holder.add_to_cart.setVisibility(View.VISIBLE);
            holder.add_to_cart_item_layout.setVisibility(View.GONE);
        }*/

        holder.food_image.setTag(i);
        holder.food_image.setOnClickListener(this);
        holder.chef_name.setTag(i);
        holder.chef_name.setOnClickListener(this);
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
            case R.id.chef_image:

                try {
                    fragment = new OrderDetailsFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("order_id", data.get(position).chef_detail.user_id);
                    bundle.putSerializable("data",data.get(position));
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = (((AppCompatActivity) context).getSupportFragmentManager());
                    FragmentTransaction fragmentTransaction = fragmentManager
                            .beginTransaction();
                    fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
                    fragmentTransaction.replace(R.id.content_frame, fragment);
                    fragmentTransaction.commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;

            case R.id.chef_name:
                ProfileFragment fragment1 = new ProfileFragment();
                Bundle bundle1 = new Bundle();
                bundle1.putInt("user_id", data.get(position).chef_detail.user_id);
                fragment1.setArguments(bundle1);
                FragmentManager fragmentManager1 = (((AppCompatActivity) context).getSupportFragmentManager());
                FragmentTransaction fragmentTransaction1 = fragmentManager1
                        .beginTransaction();
                fragmentTransaction1.addToBackStack(fragment1.getClass().getSimpleName());
                fragmentTransaction1.replace(R.id.content_frame, fragment1);
                fragmentTransaction1.commit();
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
        return data.size()!=0 ? data.size():0;
    }

}
