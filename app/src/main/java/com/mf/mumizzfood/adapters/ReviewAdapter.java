package com.mf.mumizzfood.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import com.mf.mumizzfood.R;
import com.mf.mumizzfood.models.ReviewModel;

public class ReviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        Context context;
        ArrayList<ReviewModel> reviewList;

public ReviewAdapter(Context context, ArrayList<ReviewModel> reviewList) {
        this.context = context;
        this.reviewList = reviewList;
        }

public class ViewHolder extends RecyclerView.ViewHolder {


    public ViewHolder(View itemView) {
        super(itemView);

    }
}

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_review_layout, parent, false);
        return new ReviewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return reviewList.size()!=0 ? reviewList.size():0;
    }
}
