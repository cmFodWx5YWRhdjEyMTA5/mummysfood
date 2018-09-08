package in.mummysfood.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.mummysfood.R;
import in.mummysfood.models.DashBoardModel;
import in.mummysfood.models.ProfileModel;

import java.util.ArrayList;
import java.util.List;

public class HomeSpecialCardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<DashBoardModel.Data> data;

    public HomeSpecialCardAdapter(Context context, List<DashBoardModel.Data> data) {
        this.context = context;
        this.data = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {



        public ViewHolder(View itemView) {
            super(itemView);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_today_special, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return data.size()!=0 ? data.size():0;
    }
}
