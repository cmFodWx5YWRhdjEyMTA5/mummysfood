package in.ckd.calenderkhanado.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import in.ckd.calenderkhanado.R;
import in.ckd.calenderkhanado.models.ProfileModel;
import in.ckd.calenderkhanado.utils.AppConstants;

public class FoodDataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<ProfileModel.Food_media> foodMediaList;

    public FoodDataAdapter(Context context, List<ProfileModel.Food_media> foodMediaList) {
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
        View view = LayoutInflater.from(context).inflate(R.layout.item_food_image_layout, parent, false);
        return new FoodDataAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        FoodDataAdapter.ViewHolder holder = (FoodDataAdapter.ViewHolder) viewHolder;

        ProfileModel.Food_media model = foodMediaList.get(i);
        if (model.media.name != null && !model.media.name.isEmpty()){
            Glide.with(context).load(AppConstants.IMAGEURL+model.media.name).into(holder.foodImage);
        }
    }

    @Override
    public int getItemCount() {
        return foodMediaList.size()!=0 ? foodMediaList.size():0;
    }
}
