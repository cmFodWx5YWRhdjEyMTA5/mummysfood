package com.mf.mumizzfood.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mf.mumizzfood.fragments.HomeFragment;

import java.util.List;

import com.mf.mumizzfood.R;

import com.mf.mumizzfood.models.FilterModel;
import com.mf.mumizzfood.widgets.CkdTextview;

public class HomeFilterAdapter extends RecyclerView.Adapter<HomeFilterAdapter.ViewHolder> implements View.OnClickListener {
    private Context context;
    private List<FilterModel> data;
    private FilterListner listner;

    public interface FilterListner {

        void clickOnFilter(int position);
    }

    public HomeFilterAdapter(Context context, List<FilterModel> data, HomeFragment listner) {
        this.context = context;
        this.data = data;
        this.listner = listner;
    }

    @Override
    public void onClick(View v) {
        int position = (Integer) v.getTag();
        switch (v.getId()) {
            case R.id.filter_name:

                listner.clickOnFilter(position);
                break;
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView filterLayout;
        CkdTextview filterName;
        ImageView filterImage;

        public ViewHolder(View itemView) {
            super(itemView);

            filterLayout = itemView.findViewById(R.id.filter_layout);
            filterName = itemView.findViewById(R.id.filter_name);
            filterImage = itemView.findViewById(R.id.filterImage);
        }
    }

    @Override
    public HomeFilterAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.filter_layout, parent, false);
        return new HomeFilterAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HomeFilterAdapter.ViewHolder viewHolder, int i) {

        FilterModel model = data.get(i);

        viewHolder.filterName.setText(model.filter_name);


        if (data.get(i).filter_position == 0) {
            Glide.with(context).load(R.drawable.daily_meal).into(viewHolder.filterImage);
        } else if (data.get(i).filter_position == 1) {
            Glide.with(context).load(R.drawable.snakcs).into(viewHolder.filterImage);
        }
//        else if (data.get(i).filter_position == 2) {
//            Glide.with(context).load(R.drawable.myfev).into(viewHolder.filterImage);
//        } else if (data.get(i).filter_position == 3) {
//            Glide.with(context).load(R.drawable.zarahtke).into(viewHolder.filterImage);
//        } else if (data.get(i).filter_position == 4) {
//            Glide.with(context).load(R.drawable.topfood).into(viewHolder.filterImage);
//        }
        else {
            Glide.with(context).load(R.drawable.party_orders).into(viewHolder.filterImage);
        }

        viewHolder.filterLayout.setTag(i);
        viewHolder.filterLayout.setOnClickListener(this);

    }

    @Override
    public int getItemCount() {
        return data.size() == 0 ? 0 : data.size();
    }
}
