package in.mummysfood.adapters;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import in.mummysfood.R;
import in.mummysfood.models.UserProfileModel;
import in.mummysfood.widgets.CkdButton;
import in.mummysfood.widgets.CkdTextview;

import java.util.List;

/**
 * Created by acer on 8/8/2018.
 */

public class OrderStatusAdapter extends RecyclerView.Adapter<OrderStatusAdapter.MyHolder> implements View.OnClickListener {


   private Context ckdContext;
    List<UserProfileModel.Orders> ordersListglobal;
    List<UserProfileModel.Subscribes> SubscribesListglobal;
    private String  orderStatus = "";
    private CancelOrderListener listener;
    int remmainPlates = 0;

   public interface CancelOrderListener
    {
        void actionOnOrder(int position, String action,int remainingPlates);
    }


    public OrderStatusAdapter(Context activity, List<UserProfileModel.Orders>
            ordersList, List<UserProfileModel.Subscribes> subscribesList,CancelOrderListener orderListener)
    {

        ckdContext = activity;
        ordersListglobal = ordersList;
        SubscribesListglobal = subscribesList;
        this.listener = orderListener;

    }


    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ckdContext).inflate(R.layout.order_stattus_adapterview, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position)
    {
        UserProfileModel.Orders orders = ordersListglobal.get(position);



        if (SubscribesListglobal.size() != 0)
        {
            UserProfileModel.Subscribes subList = SubscribesListglobal.get(0);

            int totalPlates = subList.number_of_days;
            int orderPlates = subList.ordered_plates;

           holder.remainingPlates.setText("Remaining plates : "+String.valueOf(totalPlates - orderPlates));
            remmainPlates = totalPlates - orderPlates;
        }


        holder.foodDetail.setText(orders.food_detail);
        holder.foodDetail.setText(orders.food_name);

        holder.CancelOrder.setTag(position);
        holder.CancelOrder.setOnClickListener(this);

        holder.UpdateOrder.setTag(position);
        holder.UpdateOrder.setOnClickListener(this);



        if (remmainPlates != 0)
        {
            orderStatus = "Active";
            holder.CancelOrder.setText("Show Details");
            holder.CancelOrder.setTextColor(ckdContext.getResources().getColor(R.color.colorPrimary));
            holder.CancelOrder.setBackground(ckdContext.getResources().getDrawable(R.drawable.fill_rounded_full_primary));
            holder.lineaarBg.setBackground(ckdContext.getResources().getDrawable(R.drawable.fill_rounded_full_primary));
        }else
        {

            orderStatus = "Closed";
            holder.remainingPlates.setText("Order Closed");
            holder.CancelOrder.setText("Delete Order");
            holder.CancelOrder.setTextColor(ckdContext.getResources().getColor(R.color.red));
            holder.CancelOrder.setBackground(ckdContext.getResources().getDrawable(R.drawable.border_red_color));
            holder.lineaarBg.setBackground(ckdContext.getResources().getDrawable(R.drawable.border_red_color));

        }




    }

    @Override
    public int getItemCount() {
        return ordersListglobal.size();
    }

    @Override
    public void onClick(View v)
    {

        int postion = (Integer) v.getTag();

        switch (v.getId())
        {
            case R.id.CancelOrder:
                if (orderStatus.equalsIgnoreCase("Active"))
                {

                    listener.actionOnOrder(postion,"Show",remmainPlates);
                }else
                {
                    listener.actionOnOrder(postion,"Delete",remmainPlates);
                }
                break;
            case R.id.UpdateOrder:


                    listener.actionOnOrder(postion,"Update",remmainPlates);

                break;

        }

    }

    public class MyHolder extends RecyclerView.ViewHolder {

        CkdTextview foodname;
        CkdTextview foodDetail;
        CkdTextview chefName;
        CkdTextview orderStatus;
        CkdTextview remainingPlates;
        CkdTextview UpdateOrder;
        CkdTextview price;
        CkdTextview CancelOrder;
        LinearLayout lineaarBg;

        public MyHolder(View itemView) {
            super(itemView);

            foodname = (CkdTextview) itemView.findViewById(R.id.foodName);
            foodDetail = (CkdTextview) itemView.findViewById(R.id.foodDetails);
            remainingPlates = (CkdTextview) itemView.findViewById(R.id.remaningThali);
            UpdateOrder = (CkdTextview) itemView.findViewById(R.id.UpdateOrder);
            CancelOrder = (CkdTextview) itemView.findViewById(R.id.CancelOrder);
            lineaarBg = (LinearLayout) itemView.findViewById(R.id.lineaarBg);
        }
    }
}
