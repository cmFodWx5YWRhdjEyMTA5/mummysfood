package com.mf.mumizzfood.adapters;

/**
 * Created by acer on 10/31/2018.
 */
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mf.mumizzfood.widgets.CkdTextview;

import java.util.List;

import com.mf.mumizzfood.R;
import com.mf.mumizzfood.models.ProfileModel;


public class AddressRecycelrview extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    Context context;
    List<ProfileModel.Addresses> foodMediaList;
    takeActionOnAddress listnerAction;

    public interface takeActionOnAddress
    {
        void editAddress(int position);
        void deleteAddress(int position);
    }

    public AddressRecycelrview(Context context, List<ProfileModel.Addresses> foodMediaList,takeActionOnAddress listnerAction) {
        this.context = context;
        this.foodMediaList = foodMediaList;
        this.listnerAction = listnerAction;
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
        View view = LayoutInflater.from(context).inflate(R.layout.saved_address_item, parent, false);
        return new AddressRecycelrview.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        ViewHolder holder = (ViewHolder) viewHolder;

        ProfileModel.Addresses model = foodMediaList.get(i);
        if (model.type != null && !model.type.isEmpty())
        {
            if (model.type.equalsIgnoreCase("office"))
            {
                holder.typeImage.setImageResource(R.drawable.ic_dashboard_black_24dp);
                holder.addressType.setText(model.type);


            }else if (model.type.equalsIgnoreCase("home"))
            {
                holder.addressType.setText(model.type);
                holder.typeImage.setImageResource(R.drawable.ic_home_black_24dp);

            }

            try {
                holder.addressTitle.setText(model.complete_address);
            } catch (Exception e) {
                e.printStackTrace();
            }

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

