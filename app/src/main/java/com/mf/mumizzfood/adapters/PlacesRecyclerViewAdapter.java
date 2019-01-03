package com.mf.mumizzfood.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.mf.mumizzfood.R;
import com.mf.mumizzfood.widgets.CkdTextview;
import com.google.android.gms.location.places.Place;

import java.util.List;

public class PlacesRecyclerViewAdapter extends
        RecyclerView.Adapter<PlacesRecyclerViewAdapter.ViewHolder> implements View.OnClickListener {

    private List<Place> placesList;
    private Context context;
    private locationListner listner;

    public PlacesRecyclerViewAdapter(List<Place> list, Context ctx,locationListner listnerLoc) {
        placesList = list;
        context = ctx;
        listner = listnerLoc;
    }

    @Override
    public void onClick(View v)
    {
        int pos = (Integer) v.getTag();

        switch (v.getId())
        {
            case R.id.mainLocationLayout:
                listner.locationService(pos,placesList.get(pos));
                break;
        }

    }

    public interface locationListner
    {
         void locationService(int position, Place place);
    }


    @Override
    public int getItemCount() {
        return placesList.size();
    }

    @Override
    public ViewHolder
    onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.place_item, parent, false);

        ViewHolder viewHolder =
                new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final int itemPos = position;
        final Place place = placesList.get(position);
        holder.name.setText(place.getName());
        holder.address.setText(place.getAddress());


       holder.mainLocationLayout.setTag(position);
       holder.mainLocationLayout.setOnClickListener(this);



      /*  holder.viewOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOnMap(place);
            }        });
*/

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public CkdTextview name;
        public CkdTextview address;
       // public CkdTextview phone;
        //public CkdTextview website;
        //public RatingBar ratingBar;
        public RelativeLayout mainLocationLayout;

        public Button viewOnMap;

        public ViewHolder(View view) {

            super(view);

            name = view.findViewById(R.id.name);
            address = view.findViewById(R.id.address);
       /*     phone = ()view.findViewById(R.id.phone);
            website = view.findViewById(R.id.website);
            ratingBar = view.findViewById(R.id.rating);*/

            mainLocationLayout = view.findViewById(R.id.mainLocationLayout);

            viewOnMap = view.findViewById(R.id.view_map_b);
        }
    }
}
