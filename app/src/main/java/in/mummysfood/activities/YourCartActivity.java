package in.mummysfood.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.google.firebase.auth.UserInfo;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.mummysfood.Location.EnterFullAdressActivity;
import in.mummysfood.R;
import in.mummysfood.base.BaseActivity;
import in.mummysfood.data.pref.PreferenceManager;
import in.mummysfood.models.DashBoardModel;
import in.mummysfood.models.OrderModel;
import in.mummysfood.utils.AppConstants;
import in.mummysfood.widgets.CkdTextview;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class YourCartActivity extends BaseActivity {

    @BindView(R.id.order_price_finalTotal)
    CkdTextview order_price_finalTotal;

    @BindView(R.id.totalValue)
    CkdTextview totalValue;

    @BindView(R.id.order_taxes)
    CkdTextview order_taxes;

    @BindView(R.id.addressMain)
    CkdTextview addressMain;

    @BindView(R.id.payatm)
    CkdTextview payatm;

    @BindView(R.id.payatmOption)
    CkdTextview payatmOption;


    @BindView(R.id.personInfo)
    CkdTextview personInfo;

    @BindView(R.id.order_titile)
    CkdTextview order_titile;

    @BindView(R.id.order_price)
    CkdTextview order_price;

    @BindView(R.id.order_price_basedQuantity)
    CkdTextview order_price_basedQuantity;

    @BindView(R.id.placeOrderButton)
    RelativeLayout placeOrderButton;

    @BindView(R.id.placeOrderprice)
    CkdTextview placeOrderprice;

    @BindView(R.id.placeOrderButtonCheckout)
    CkdTextview placeOrderButtonCheckout;

    @BindView(R.id.vegSysmbol)
    ImageView vegSysmbol;
    @BindView(R.id.backArrowFinish)
    ImageView backArrowFinish;



    @BindView(R.id.scrollchange)
    ScrollView scrollChange;

    @BindView(R.id.checkedCod)
    ImageView checkedCod;

    @BindView(R.id.paytmChecked)
    ImageView paytmChecked;

    @BindView(R.id.changePaymentOption)
    LinearLayout changePaymentOption;


    private DashBoardModel.Data modelData;
    PreferenceManager pfUName;
    PreferenceManager pfUMobile;
    PreferenceManager pfUAddress;

    private int radioValue;

    private String userAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_cart);

         ButterKnife.bind(this);

        if (getIntent() != null)
        {
            modelData = (DashBoardModel.Data) getIntent().getSerializableExtra("data");
            radioValue = getIntent().getIntExtra("radioAction",0);
        }


        try {
            pfUName = new PreferenceManager(this,PreferenceManager.FIRST_NM);
            pfUAddress = new PreferenceManager(this,PreferenceManager.USER_ADDRESS);
            pfUMobile = new PreferenceManager(this,PreferenceManager.USER_MOBILE);


            userAdd = pfUAddress.getStringForKey(PreferenceManager.USER_ADDRESS,"");
            addressMain.setText(userAdd);
            personInfo.setText(pfUAddress.getStringForKey(PreferenceManager.USER_MOBILE,"")+" , "+pfUAddress.getStringForKey(PreferenceManager.USER_MOBILE,""));

        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            order_titile.setText(modelData.food_detail.name);
            order_price.setText(modelData.food_detail.price);
            order_price_basedQuantity.setText(modelData.food_detail.price);
            totalValue.setText(modelData.food_detail.price);
            order_taxes.setText(modelData.food_detail.taxes);
            placeOrderprice.setText(modelData.food_detail.price);
            payatm.setText("Payatm "+"Rs."+modelData.food_detail.price+"/-");
            placeOrderprice.setText("Pay Rs."+modelData.food_detail.price+"/-");


        } catch (Exception e) {
            e.printStackTrace();
        }


        if (addressMain == null && "".equalsIgnoreCase(userAdd))
        {
            placeOrderButton.setBackground(getResources().getDrawable(R.drawable.fill_rounded_white_smoke));
            placeOrderprice.setTextColor(getResources().getColor(R.color.black));
            placeOrderButtonCheckout.setTextColor(getResources().getColor(R.color.black));

        }


        try {
            payatmOption.setText("Pay Rs."+modelData.food_detail.price+"/-");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @OnClick(R.id.placeOrderButton)
    public void placceOrder()
    {
        if (addressMain == null && "".equalsIgnoreCase(userAdd))
        {

            placeOrderButton.setBackground(getResources().getDrawable(R.drawable.fill_rounded_white_smoke));
            placeOrderprice.setTextColor(getResources().getColor(R.color.black));
            placeOrderButtonCheckout.setTextColor(getResources().getColor(R.color.black));
            addressMain.setError("Insert Valid Address");

        }else
        {
            placeOrderButton.setBackground(getResources().getDrawable(R.drawable.fill_rounded_full_primary));
            placeOrderprice.setTextColor(getResources().getColor(R.color.black));
            placeOrderButtonCheckout.setTextColor(getResources().getColor(R.color.black));
        }

        placeOrderDialog();
    }

    @OnClick(R.id.infoChange)
    public void inFochage()
    {

    }


    @OnClick(R.id.addressChange)
    public void addressChange()
    {
        String addrsss = addressMain.getText().toString();
        Intent adresIntent = new Intent(YourCartActivity.this,EnterFullAdressActivity.class);
        adresIntent.putExtra("Address",addrsss);
        adresIntent.putExtra("From","OrderDetails");
        startActivityForResult(adresIntent,200);
    }

    @OnClick(R.id.patmentChange)
    public void patmentChange()
    {

        scrollChange.setVisibility(View.GONE);
        changePaymentOption.setVisibility(View.VISIBLE);

    }

    @OnClick(R.id.COD)
    public void COD()
    {
        checkedCod.setVisibility(View.VISIBLE);
        changePaymentOption.setVisibility(View.GONE);
        scrollChange.setVisibility(View.VISIBLE);

        checkedCod.setVisibility(View.GONE);
        paytmChecked.setVisibility(View.GONE);
        try {
            payatm.setText("COD "+"Rs."+modelData.food_detail.price+"/-");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @OnClick(R.id.logoPyatm)
    public void setPayatm()
    {
        paytmChecked.setVisibility(View.VISIBLE);
        changePaymentOption.setVisibility(View.GONE);
        scrollChange.setVisibility(View.VISIBLE);
        checkedCod.setVisibility(View.GONE);
        paytmChecked.setVisibility(View.GONE);
        try {
            payatm.setText("Payatm "+"Rs."+modelData.food_detail.price+"/-");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @OnClick(R.id.payatmOption)
    public void payatmOption()
    {


        try {
            payatm.setText("Payatm "+"Rs."+modelData.food_detail.price+"/-");
        } catch (Exception e) {
            e.printStackTrace();
        }

        paytmChecked.setVisibility(View.VISIBLE);
        changePaymentOption.setVisibility(View.GONE);
        scrollChange.setVisibility(View.VISIBLE);
        checkedCod.setVisibility(View.GONE);
        paytmChecked.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (changePaymentOption.getVisibility() ==View.VISIBLE)
        {
            changePaymentOption.setVisibility(View.GONE);
            scrollChange.setVisibility(View.VISIBLE);

        }else
        {
            finish();
        }

    }

    @OnClick(R.id.backArrowFinish)
    public void backArrowFinish()
    {
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check that it is the SecondActivity with an OK result
        if (requestCode == 200) {
            if (resultCode == RESULT_OK) {

                String dataValue = data.getStringExtra("Address");

                addressMain.setText(dataValue);

                showToast(dataValue);
            }
        }
    }

    public void placeOrderDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Place Order")
                .setCancelable(false)
                .setNegativeButton(R.string.no_txt,new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                    }
                })
                .setPositiveButton(R.string.yes_txt, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        placeOrderData();
                    }
                });
        // Create the AlertDialog object and return it
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void placeOrderData()
    {

        OrderModel.Data orderModel = new OrderModel.Data();
        orderModel.food_user_id = modelData.chef_detail.user_id;
        orderModel.order_by = modelData.id;
        orderModel.order_for = modelData.chef_detail.user_id;
        orderModel.food_detail = modelData.food_detail.details;
        orderModel.food_name = modelData.food_detail.name;
        orderModel.chef_name = modelData.f_name;
        orderModel.house_no = "Address";
        orderModel.landmark = "vijay nagar";
        orderModel.street = "scheme no. 74";
        orderModel.city = "indore";
        orderModel.state = "MP";
        orderModel.pincode = "452010";
        orderModel.address_type = modelData.f_name;
        orderModel.price = modelData.food_detail.price;
        orderModel.quantity =1;
        orderModel.payment_status = "confirm";
        orderModel.is_order_confirmed = 1;
        orderModel.user_id = 1;
        orderModel.subscribe_to = modelData.chef_detail.user_id;
        orderModel.number_of_days = radioValue;
        orderModel.status = "Active";
        orderModel.ordered_plates = 2;



        Call<OrderModel.Data> loginRequestCall = AppConstants.restAPI.subscribeOrder(orderModel);

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

}
