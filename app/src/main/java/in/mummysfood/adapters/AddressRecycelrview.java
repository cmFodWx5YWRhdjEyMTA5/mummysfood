package in.mummysfood.adapters;

/**
 * Created by acer on 10/31/2018.
 */
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import in.mummysfood.R;
import in.mummysfood.models.ProfileModel;
import in.mummysfood.utils.AppConstants;
import in.mummysfood.widgets.CkdTextview;


public class AddressRecycelrview extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<ProfileModel.Addresses> foodMediaList;

    public AddressRecycelrview(Context context, List<ProfileModel.Addresses> foodMediaList) {
        this.context = context;
        this.foodMediaList = foodMediaList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

       public ImageView typeImage;
        public CkdTextview addressTitle;
        public CkdTextview addressType;
        public CkdTextview editAddress;
        public CkdTextview deleteAddress;

        public ViewHolder(View itemView) {
            super(itemView);
            typeImage= itemView.findViewById(R.id.typeImage);
            addressType= itemView.findViewById(R.id.addressType);
            addressTitle= itemView.findViewById(R.id.addressTitle);
            editAddress= itemView.findViewById(R.id.editAddress);
            deleteAddress= itemView.findViewById(R.id.deleteAddress);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.saved_address_item, parent, false);
        return new AddressRecycelrview.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        ChefFoodAdapter.ViewHolder holder = (ChefFoodAdapter.ViewHolder) viewHolder;

        ProfileModel.Addresses model = foodMediaList.get(i);
        if (model.type != null && !model.type.isEmpty())
        {
            if (model.type.equalsIgnoreCase("office"))
            {

            }else if (model.type.equalsIgnoreCase("home"))
            {

            }
        }


    }

    @Override
    public int getItemCount() {
        return foodMediaList.size()!=0 ? foodMediaList.size():0;
    }
}

