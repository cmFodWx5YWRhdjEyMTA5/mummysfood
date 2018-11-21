package in.ckd.calenderkhanado.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import in.ckd.calenderkhanado.R;
import in.ckd.calenderkhanado.models.UserProfileModel;
import in.ckd.calenderkhanado.widgets.CkdTextview;

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
        void actionOnOrder(int position, String action,int remainingPlates,int subid);
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

        UserProfileModel.Subscribes modelSubscribe = SubscribesListglobal.get(position);

        UserProfileModel.Subscribes subList = SubscribesListglobal.get(position);

            int totalPlates = subList.number_of_days;
            int orderPlates = subList.ordered_plates;

            holder.remainingPlates.setText("Remaining plates : "+String.valueOf(totalPlates - orderPlates));
            remmainPlates = totalPlates - orderPlates;


        holder.lineaarBg.setVisibility(View.VISIBLE);


        try {
            holder.foodDetail.setText(modelSubscribe.orders.get(0).food_detail);
            holder.foodNameEntry.setText(modelSubscribe.orders.get(0).food_name);
            holder.foodNameEntry.setVisibility(View.GONE);
            holder.chefName_order.setText(modelSubscribe.orders.get(0).food_name);
            holder.foodLocation_order.setText(modelSubscribe.orders.get(0).city);
            Glide.with(ckdContext).load(modelSubscribe.orders.get(0).food_image).into(holder.food_imagehere);
        } catch (Exception e) {
            e.printStackTrace();
        }



        if (remmainPlates > 0)
        {
            orderStatus = "Active";
            holder.CancelOrder.setText("Show Details");
           // holder.CancelOrder.setTextColor(ckdContext.getResources().getColor(R.color.colorPrimary));
            //older.CancelOrder.setBackground(ckdContext.getResources().getDrawable(R.drawable.fill_rounded_full_primary));
           // holder.lineaarBg.setBackground(ckdContext.getResources().getDrawable(R.drawable.fill_rounded_full_primary));
        }else
        {

            orderStatus = "Closed";
            holder.remainingPlates.setText("Order Closed");
            holder.CancelOrder.setText("Completed");
         //   holder.CancelOrder.setTextColor(ckdContext.getResources().getColor(R.color.red));
           // holder.CancelOrder.setBackground(ckdContext.getResources().getDrawable(R.drawable.border_red_color));
         //   holder.lineaarBg.setBackground(ckdContext.getResources().getDrawable(R.drawable.border_red_color));

        }

        holder.CancelOrder.setTag(position);
        holder.CancelOrder.setOnClickListener(this);

      //  holder.UpdateOrder.setTag(position);
        //holder.UpdateOrder.setOnClickListener(this);





    }

    @Override
    public int getItemCount() {
        return SubscribesListglobal.size();
    }

    @Override
    public void onClick(View v)
    {

        int postion = (Integer) v.getTag();

        switch (v.getId())
        {
            case R.id.CancelOrder:

                if (SubscribesListglobal.get(postion).orders != null)
                {
                    if (SubscribesListglobal.get(postion).orders.size() != 0)
                    {

                        UserProfileModel.Subscribes subList = SubscribesListglobal.get(postion);

                        int totalPlates = subList.number_of_days;
                        int orderPlates = subList.ordered_plates;

                        remmainPlates = totalPlates - orderPlates;

                        if (remmainPlates>0)
                        {
                            listener.actionOnOrder(postion,"Show",remmainPlates,SubscribesListglobal.get(postion).id);
                        }else
                        {
                            listener.actionOnOrder(postion,"Delete",remmainPlates,SubscribesListglobal.get(postion).id);
                        }
                    }
                }


                break;
           /* case R.id.UpdateOrder:


                    listener.actionOnOrder(postion,"Update",remmainPlates);*/

              //  break;

        }

    }

    public class MyHolder extends RecyclerView.ViewHolder {

        CkdTextview foodname;
        CkdTextview foodDetail;
        CkdTextview chefName;
        CkdTextview orderStatus;
        CkdTextview remainingPlates;
        CkdTextview foodNameEntry;
    ///    CkdTextview UpdateOrder;
        CkdTextview price;
        CkdTextview CancelOrder;
        CardView lineaarBg;

        CkdTextview chefName_order;

        CkdTextview foodLocation_order;

        ImageView food_imagehere;


        public MyHolder(View itemView) {
            super(itemView);

            foodname = (CkdTextview) itemView.findViewById(R.id.foodNameEntry);
            foodDetail = (CkdTextview) itemView.findViewById(R.id.foodDetails);
            remainingPlates = (CkdTextview) itemView.findViewById(R.id.remaningThali);
            chefName_order = (CkdTextview) itemView.findViewById(R.id.chefName_order);
            foodNameEntry = (CkdTextview) itemView.findViewById(R.id.foodNameEntry);
            foodLocation_order = (CkdTextview) itemView.findViewById(R.id.foodLocation_order);
           // UpdateOrder = (CkdTextview) itemView.findViewById(R.id.UpdateOrder);
            CancelOrder = (CkdTextview) itemView.findViewById(R.id.CancelOrder);
            lineaarBg = (CardView) itemView.findViewById(R.id.lineaarBg);
            food_imagehere =  itemView.findViewById(R.id.food_imagehere);
        }
    }
}
