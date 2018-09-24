package in.mummysfood.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bumptech.glide.Glide;
import in.mummysfood.Location.EnterFullAdressActivity;
import in.mummysfood.Location.UserLocationActivtiy;
import in.mummysfood.R;
import in.mummysfood.activities.YourCartActivity;
import in.mummysfood.base.BaseActivity;
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

public class OrderDetailsActivity extends BaseActivity implements EnterFullAdressActivity.updateAdd {

    @BindView(R.id.order_image)
    ImageView order_image;

    @BindView(R.id.order_chef_profile_img)
    CircularImageView order_chef_profile_img;

    @BindView(R.id.order_chef_name)
    CkdTextview order_chef_name;

    @BindView(R.id.processedButton)
    CkdTextview processedButton;

    @BindView(R.id.order_detail)
    CkdTextview order_detail;
    @BindView(R.id.weekly)
    CkdTextview weekly;

    @BindView(R.id.monthly)
    CkdTextview monthly;
    @BindView(R.id.lunchPrice)
    CkdTextview lunchPrice;
    @BindView(R.id.dinnerPrice)
    CkdTextview dinnerPrice;
    @BindView(R.id.bothPrice)
    CkdTextview bothPrice;

    @BindView(R.id.LunchSwitch)
    SwitchCompat LunchSwitch;

    @BindView(R.id.DInnerSwitch)
    SwitchCompat DInnerSwitch;

    @BindView(R.id.bothSwitch)
    SwitchCompat bothSwitch;

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

    @BindView(R.id.backArrow)
    ImageView backArrow;

    @BindView(R.id.radioAction)
    RadioGroup radioAction;


    private int orderId;
    private DashBoardModel.Data data;
    private PreferenceManager pf;
    private PreferenceManager userPf;
    private int monthlyValue;
    private int weeklyValue;
    private  int priceOrgValue;

    private String UserCUrrentAdd = "";

    public OrderDetailsActivity() {
        // Required empty public constructor
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.order_detail_layout);
        pf = new PreferenceManager(this, PreferenceManager.ORDER_PREFERENCES_FILE);
        userPf = new PreferenceManager(this, PreferenceManager.USER_ADDRESS);

        UserCUrrentAdd = userPf.getStringForKey("CurrentAddress", "");


        ButterKnife.bind(this);

        if (getIntent() != null) {

            try {
                orderId = getIntent().getIntExtra("order_id", 0);
                data = (DashBoardModel.Data) getIntent().getSerializableExtra("data");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        radioAction.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

            }
        });
       /* LunchSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                } else {

                }

            }
        });

        DInnerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                } else {

                }

            }
        });

        bothSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                } else {

                }

            }
        });*/

        prepareOrderDetails();
    }


    private void prepareOrderDetails() {


        String userAddress = pf.getStringForKey("UserAddress", "");

        userAddress = "Khajarana Ganesh Mandir";
        try {
            //  Glide.with(this).load(data.profile_image).into(order_chef_profile_img);
            order_chef_name.setText(data.f_name);
            order_titile.setText(data.food_detail.name);
            order_detail.setText(data.food_detail.details);
            order_price.setText("Rs. " + data.food_detail.price + "/-");
            userDelAddress.setText(UserCUrrentAdd);


            double price = Double.parseDouble(data.food_detail.price);

            priceOrgValue= (int) price;


            monthlyValue = priceOrgValue * 31;
            weeklyValue = priceOrgValue * 7;

            dinnerPrice.setText(String.valueOf(weeklyValue));
            lunchPrice.setText(String.valueOf(weeklyValue));
            bothPrice.setText(String.valueOf(weeklyValue+weeklyValue));


        } catch (Exception e) {
            e.printStackTrace();
        }

        //check share prefrence


        try {

            int itemcount = pf.getIntForKey(PreferenceManager.ORDER_quantity, 0);

            if (pf.getIntForKey(PreferenceManager.USER_ID, 0) != 0 && pf.getIntForKey(PreferenceManager.USER_ID, 0) == data.id) {
                if (itemcount != 0) {
                    item_count.setText("" + pf.getIntForKey(PreferenceManager.ORDER_quantity, 1));
                } else {
                    item_count.setText("" + pf.getIntForKey(PreferenceManager.ORDER_quantity, 1));
                }
            } else {
                item_count.setText("" + pf.getIntForKey(PreferenceManager.ORDER_quantity, 1));
            }

            int gst = 450;

            numberOfQuantity.setText("" + pf.getIntForKey(PreferenceManager.ORDER_quantity, 1));

            int totalQuantity = Integer.parseInt(item_count.getText().toString());



            int totalPrice = (priceOrgValue * totalQuantity) + gst;

            priceValue.setText(String.valueOf(totalPrice));


            int addedItem = pf.getIntForKeyAddedItem(PreferenceManager.ORDER_quantity, 1);
            if (addedItem == 0) {
                viewAddedItem.setVisibility(View.GONE);

            } else {
                viewAddedItem.setText("view added items");
                viewAddedItem.setVisibility(View.VISIBLE);
            }


        } catch (Exception e) {


            e.printStackTrace();

        }

    }

    @OnClick(R.id.viewAddedItem)
    public void viewAddedItem() {
        showItemContainPopup();
    }

    private void showItemContainPopup() {

        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.item_detail_view_popup, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

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

    @OnClick(R.id.monthly)
    public void mothyClicked()
    {
        monthly.setBackground(getResources().getDrawable(R.drawable.border_primary));
        weekly.setBackground(getResources().getDrawable(R.drawable.border_gray));


        dinnerPrice.setText(String.valueOf(monthlyValue));

        lunchPrice.setText(String.valueOf(monthlyValue));

        bothPrice.setText(String.valueOf(monthlyValue+monthlyValue));

    }

    @OnClick(R.id.weekly)
    public void weeklyCliked()
    {
        monthly.setBackground(getResources().getDrawable(R.drawable.border_gray));
        weekly.setBackground(getResources().getDrawable(R.drawable.border_primary));


            dinnerPrice.setText(String.valueOf(weeklyValue));

            lunchPrice.setText(String.valueOf(weeklyValue));

            bothPrice.setText(String.valueOf(weeklyValue+weeklyValue));

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

        if (getRadioSelected() == R.id.radioDinner)
        {
            dinnerPrice.setText(String.valueOf(weeklyValue));
        }
        if (getRadioSelected() == R.id.radioLunch)
        {
            lunchPrice.setText(String.valueOf(weeklyValue));
        }

        if (getRadioSelected() == R.id.radioBoth)
        {
            bothPrice.setText(String.valueOf(weeklyValue+weeklyValue));
        }

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

    @OnClick(R.id.sub_item)
    public void SubFoodQuantity() {
        int count = 0;
        if (pf.getIntForKey(PreferenceManager.USER_ID, 0) != 0 ){
            count = pf.getIntForKey(PreferenceManager.ORDER_quantity, 0);
        }

        if (count > 0) {
            count--;
            pf.saveIntForKey(PreferenceManager.ORDER_quantity,count);
        }else{
            pf.clearPref(this, PreferenceManager.ORDER_PREFERENCES_FILE);
            //homeToolbar.setVisibility(View.GONE);
        }
        item_count.setText(String.valueOf(count));
    }

    @OnClick(R.id.add_item)
    public void AddFoodQuantity() {
        int count = 0;
        if (pf.getIntForKey(PreferenceManager.USER_ID, 0) != 0){
            count = pf.getIntForKey(PreferenceManager.ORDER_quantity, 0);
        }
        count++;
        pf.saveIntForKey(PreferenceManager.ORDER_quantity,count);

        item_count.setText(String.valueOf(count));
    }



    @OnClick(R.id.addAddress)
    public void AddAddressValue()
    {

        Intent enterOtherAct = new Intent(this,EnterFullAdressActivity.class);
        enterOtherAct.putExtra("Address",UserCUrrentAdd);
        enterOtherAct.putExtra("From","OrderDetails");
        startActivity(enterOtherAct);

    }

    @OnClick(R.id.backArrow)
    public void backArrow()
    {
        finish();
    }

    @OnClick(R.id.processedButton)
    public  void processedButton()
    {
        Intent yourCart = new Intent(this, YourCartActivity.class);
        yourCart.putExtra("data",data);
        startActivity(yourCart);
    }

    @Override
    public void updateAddressInterface(String address) {
        userDelAddress.setText(address);
    }


    private int getRadioSelected() {


        int selectedId = radioAction.getCheckedRadioButtonId();

          return selectedId;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
