package in.mummysfood.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import in.mummysfood.Location.EnterFullAdressActivity;
import in.mummysfood.Location.UserLocationActivtiy;
import in.mummysfood.R;
import in.mummysfood.base.BaseFragment;
import in.mummysfood.data.pref.PreferenceManager;
import in.mummysfood.models.DashBoardModel;
import in.mummysfood.models.OrderModel;
import in.mummysfood.utils.AppConstants;
import in.mummysfood.widgets.CkdTextview;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailsFragment extends BaseFragment{

    @BindView(R.id.order_image)
    ImageView order_image;

    @BindView(R.id.order_chef_profile_img)
    CircularImageView order_chef_profile_img;

    @BindView(R.id.order_chef_name)
    CkdTextview order_chef_name;

    @BindView(R.id.order_detail)
    CkdTextview order_detail;

    @BindView(R.id.order_titile)
    CkdTextview order_titile;

    @BindView(R.id.place_order)
    CkdTextview place_order;

    @BindView(R.id.sub_item)
    CkdTextview sub_item;

    @BindView(R.id.add_item)
    CkdTextview add_item;

    @BindView(R.id.item_count)
    CkdTextview item_count;

    @BindView(R.id.addAddress)
    CkdTextview addAddress;

    @BindView(R.id.quantity)
    CkdTextview quantity;

    @BindView(R.id.numberOfQuantity)
    CkdTextview numberOfQuantity;


    @BindView(R.id.viewAddedItem)
    CkdTextview viewAddedItem;

    @BindView(R.id.order_price)
    CkdTextview order_price;

    @BindView(R.id.priceValue)
    CkdTextview priceValue;

    @BindView(R.id.priceGst)
    CkdTextview priceGst;

    @BindView(R.id.userDelAddress)
    CkdTextview userDelAddress;

    private int orderId;
    DashBoardModel.Data data;
    PreferenceManager pf;
    PreferenceManager userPf;
    Context context;

    private String UserCUrrentAdd = "";

    public OrderDetailsFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_order_details, container, false);

        context = getContext();
        pf = new PreferenceManager(context,PreferenceManager.ORDER_PREFERENCES_FILE);
        userPf = new PreferenceManager(context,PreferenceManager.USER_ADDRESS);

       UserCUrrentAdd  = userPf.getStringForKey("CurrentAddress","");


        ButterKnife.bind(this,rootView);

        if (getArguments() != null)
        {

            try {
                orderId = getArguments().getInt("order_id",0);
                data = (DashBoardModel.Data) getArguments().getSerializable("data");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        prepareOrderDetails();

        return rootView;
    }

    private void prepareOrderDetails() {


        String userAddress = pf.getStringForKey("UserAddress","");

        userAddress = "Khajarana Ganesh Mandir";
        try {
            Glide.with(getContext()).load(data.profile_image).into(order_chef_profile_img);
            order_chef_name.setText(data.f_name);
            order_titile.setText(data.food_detail.name);
            order_detail.setText(data.food_detail.details);
            order_price.setText(data.food_detail.price);
            userDelAddress.setText(UserCUrrentAdd);

        } catch (Exception e) {
            e.printStackTrace();
        }

        //check share prefrence

       try {

            int itemcount = pf.getIntForKey(PreferenceManager.ORDER_quantity,0);

            if (pf.getIntForKey(PreferenceManager.USER_ID, 0) != 0 && pf.getIntForKey(PreferenceManager.USER_ID, 0) == data.id){
                if (itemcount != 0) {
                    item_count.setText("" + pf.getIntForKey(PreferenceManager.ORDER_quantity, 1));
                } else {
                    item_count.setText("" + pf.getIntForKey(PreferenceManager.ORDER_quantity, 1));
                }
            }else {
                item_count.setText("" + pf.getIntForKey(PreferenceManager.ORDER_quantity, 1));
            }

            int gst = 450;

            numberOfQuantity.setText("" + pf.getIntForKey(PreferenceManager.ORDER_quantity, 1));

            int totalQuantity = Integer.parseInt(item_count.getText().toString());

            double price = Double.parseDouble(data.food_detail.price);

            int priceOrgValue = (int) price;

            int totalPrice = (priceOrgValue * totalQuantity)+gst;

            priceValue.setText(String.valueOf(totalPrice));


            int addedItem = pf.getIntForKeyAddedItem(PreferenceManager.ORDER_quantity,1);
            if (addedItem == 0)
            {
                viewAddedItem.setVisibility(View.GONE);

            }else
            {
                viewAddedItem.setText("view added items");
                viewAddedItem.setVisibility(View.VISIBLE);
            }



      } catch (Exception e)
    {


            e.printStackTrace();

        }

    }

    @OnClick(R.id.viewAddedItem)
    public void viewAddedItem()
    {
        showItemContainPopup();
    }

    private void showItemContainPopup()
    {

        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.item_detail_view_popup, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        alertDialogBuilder.setView(promptsView);

        final AlertDialog alertDialog = alertDialogBuilder.create();

        Button UpdateCart = promptsView.findViewById(R.id.UpdateCart);
        Button Done = promptsView.findViewById(R.id.Done);



        UpdateCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
            }
        });

        Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();


    }

    @OnClick(R.id.place_order)
    public void PlaceOrder(){
        OrderModel.Data orderModel = new OrderModel.Data();
        orderModel.food_user_id = data.chef_detail.user_id;
        orderModel.order_by = data.id;
        orderModel.order_for = data.chef_detail.user_id;
        orderModel.food_detail = data.food_detail.details;
        orderModel.food_name = data.food_detail.name;
        orderModel.chef_name = data.f_name;
        orderModel.subscribe_id =  0;
        orderModel.house_no = "dk-329";
        orderModel.landmark = "vijay nagar";
        orderModel.street = "scheme no. 74";
        orderModel.city = "indore";
        orderModel.state = "MP";
        orderModel.pincode = "452010";
        orderModel.address_type = data.f_name;
        orderModel.price = data.food_detail.price;
        orderModel.quantity = pf.getIntForKey(PreferenceManager.ORDER_quantity, 1);
        orderModel.payment_status = "confirm";
        orderModel.is_order_confirmed = 1;

        Call<OrderModel.Data> loginRequestCall = AppConstants.restAPI.orderPlace(orderModel);

        loginRequestCall.enqueue(new Callback<OrderModel.Data>() {
            @Override
            public void onResponse(Call<OrderModel.Data> call, Response<OrderModel.Data> response) {

                if (response != null){

                    if (response.isSuccessful()){
                        OrderModel.Data res = response.body();
                        if (res.status != null && res.status.equalsIgnoreCase(AppConstants.SUCCESS)){

                        }else{
                        }

                    }else {

                        try {
                            Log.e("Response is not success",""+response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }else {
                    try {
                        Log.e("Response is null",""+response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<OrderModel.Data> call, Throwable t) {
                Log.e("Response is failure", ""+t);

            }
        });

    }

    @OnClick(R.id.addAddress)
    public void AddAddressValue()
    {

        Intent enterOtherAct = new Intent(getActivity(),EnterFullAdressActivity.class);
        enterOtherAct.putExtra("Address",UserCUrrentAdd);
        enterOtherAct.putExtra("From","OrderDetails");
        startActivity(enterOtherAct);

    }

    public void UpdateAddress(String address)
    {
        userDelAddress.setText(address);
    }


}
