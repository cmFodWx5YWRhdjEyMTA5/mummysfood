package in.ckd.calenderkhanado.adapters;

/**
 * Created by acer on 10/31/2018.
 */
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import in.ckd.calenderkhanado.R;
import in.ckd.calenderkhanado.activities.SubscriptionUpdate_Activity;
import in.ckd.calenderkhanado.models.ProfileModel;
import in.ckd.calenderkhanado.models.SubscribtionModel;
import in.ckd.calenderkhanado.widgets.CkdTextview;


public class SubscriptionUser extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    Context context;
    List<SubscribtionModel.Data> foodMediaList;
    takeActionOnAddress listnerAction;

    public SubscriptionUser(Context context, List<SubscribtionModel.Data> data, takeActionOnAddress subscriptionUpdate_activity) {
        this.context = context;
        this.foodMediaList = data;
        this.listnerAction = subscriptionUpdate_activity;
    }

    public interface takeActionOnAddress
    {
        void editAddress(int position);
        void deleteAddress(int position);
    }

    @Override
    public void onClick(View v)
    {

        int postion = (Integer) v.getTag();
        switch(v.getId())
        {
            case R.id.editAddress:

                listnerAction.editAddress(postion);
                break;
            case R.id.deleteAddress:
                listnerAction.deleteAddress(postion);
                break;
        }
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
        View view = LayoutInflater.from(context).inflate(R.layout.user_order_update_sub, parent, false);
        return new SubscriptionUser.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        ViewHolder holder = (ViewHolder) viewHolder;

        SubscribtionModel.Data model = foodMediaList.get(i);
        if (model != null)
        {
            holder.addressTitle.setText("Order By : "+String.valueOf(model.user_id));
            holder.addressType.setText("Ordered Plates : "+String.valueOf(model.ordered_plates));

            holder.editAddress.setTag(i);
            holder.deleteAddress.setTag(i);
            holder.editAddress.setOnClickListener(this);
            holder.deleteAddress.setOnClickListener(this);
        }


    }

    @Override
    public int getItemCount() {
        return foodMediaList.size()!=0 ? foodMediaList.size():0;
    }
}

