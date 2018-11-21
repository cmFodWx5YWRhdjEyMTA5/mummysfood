package in.ckd.calenderkhanado.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import java.io.IOException;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.ckd.calenderkhanado.Location.EnterFullAdressActivity;
import in.ckd.calenderkhanado.R;
import in.ckd.calenderkhanado.base.BaseActivity;
import in.ckd.calenderkhanado.data.db.DataBaseHelperNew;
import in.ckd.calenderkhanado.data.pref.PreferenceManager;
import in.ckd.calenderkhanado.models.DashBoardModel;
import in.ckd.calenderkhanado.models.OrderModel;
import in.ckd.calenderkhanado.models.UserProfileModel;
import in.ckd.calenderkhanado.utils.AppConstants;
import in.ckd.calenderkhanado.widgets.CkdButton;
import in.ckd.calenderkhanado.widgets.CkdTextview;
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

    @BindView(R.id.add_to_cart_item_layout)
    LinearLayout add_to_cart_item_layout;

    @BindView(R.id.sub_item)
    CkdTextview subItem;

    @BindView(R.id.add_item)
    CkdTextview add_item;

    @BindView(R.id.item_count)
    CkdTextview item_count;

    @BindView(R.id.userNameUpdated)
    EditText userNameUpdated;

    @BindView(R.id.mobileNumberUpdation)
    EditText mobileNumberUpdation;

    @BindView(R.id.changePersonInfo)
    LinearLayout changePersonInfo;

    @BindView(R.id.nameSave)
    CkdButton nameSave;

    private DashBoardModel.Data modelData;
    private DashBoardModel.Food_detail model;
    PreferenceManager pfUName;
    PreferenceManager pfUMobile;
    PreferenceManager pfUAddress;
    PreferenceManager loginPref;

    private int radioValue;
    private int numberOfDays,isLunch,isDinner;

    UserProfileModel.Orders orders = new UserProfileModel.Orders();

    private String userAdd;
    private String typeOfPackage;
    private String location = "";
    private String foodImage = "";
    private DataBaseHelperNew db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_cart);

         ButterKnife.bind(this);


         db = new DataBaseHelperNew(this);


        if (getIntent() != null)
        {

            try {
                location = getIntent().getStringExtra("From");
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (location == null)
            {
                location = "";
            }
            if (location.equalsIgnoreCase("RepeatOrder"))
            {
                orders = (UserProfileModel.Orders) getIntent().getSerializableExtra("data");
                typeOfPackage = getIntent().getStringExtra("typeOfPackage");
                numberOfDays = getIntent().getIntExtra("numberOfDays",0);
                isDinner = getIntent().getIntExtra("isDinner",0);
                isLunch = getIntent().getIntExtra("isLunch",0);
                foodImage = orders.food_image;
            }else
            {
                modelData = (DashBoardModel.Data) getIntent().getSerializableExtra("data");
                typeOfPackage = getIntent().getStringExtra("typeOfPackage");
                numberOfDays = getIntent().getIntExtra("numberOfDays",0);
                isDinner = getIntent().getIntExtra("isDinner",0);
                isLunch = getIntent().getIntExtra("isLunch",0);
                foodImage = getIntent().getStringExtra("foodImage");

            }
        }

            if (typeOfPackage.equalsIgnoreCase("today"))
            {
                add_to_cart_item_layout.setVisibility(View.VISIBLE);
                order_price_basedQuantity.setVisibility(View.VISIBLE);

            }else if (typeOfPackage.equalsIgnoreCase("monthly"))
            {
                add_to_cart_item_layout.setVisibility(View.GONE);
                order_price_basedQuantity.setVisibility(View.GONE);
            }else if (typeOfPackage.equalsIgnoreCase("weekly"))
            {
                add_to_cart_item_layout.setVisibility(View.GONE);
                order_price_basedQuantity.setVisibility(View.GONE);
            }


        try {

            pfUName = new PreferenceManager(this,PreferenceManager.FIRST_NM);
            loginPref = new PreferenceManager(this,PreferenceManager.LOGIN_PREFERENCES_FILE);
            pfUAddress = new PreferenceManager(this);
            pfUMobile = new PreferenceManager(this,PreferenceManager.USER_MOBILE);
            pf = new PreferenceManager(this);


            try {
                personInfo.setText(pf.getStringForKey("Username","")+" , "+pf.getStringForKey("Mobile",""));
            } catch (Exception e) {
                e.printStackTrace();
            }

            userAdd = pfUAddress.getStringForKey("CurrentAddress","");

            if (location.equalsIgnoreCase("RepeatOrder"))
            {

                try {
                    addressMain.setText(orders.landmark);
                } catch (Exception e) {
                    e.printStackTrace();
                    addressMain.setText(userAdd);
                }
            }else
            {
                addressMain.setText(userAdd);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            if (location.equalsIgnoreCase("RepeatOrder"))
            {


                order_titile.setText(orders.food_name);
                order_price.setText(orders.price);


                order_price_basedQuantity.setText(orders.price );
                totalValue.setText(orders.price);
                order_taxes.setText(String.valueOf(50));
                placeOrderprice.setText(orders.price);
                payatm.setText("Payatm "+"Rs."+orders.price+"/-");
                placeOrderprice.setText("Pay Rs."+orders.price+"/-");
                payatmOption.setText("Pay Rs."+orders.price+"/-");
                order_taxes.setText(String.valueOf(50));

            }else
            {
              float  orderPrice  = Float.parseFloat(modelData.food_detail.get(0).price);
                int orderPriceInt = 0;
                orderPriceInt  = (int) orderPrice;
                if (typeOfPackage.equalsIgnoreCase("monthly"))
              {
                  orderPriceInt = orderPriceInt*30;
              }else if (typeOfPackage.equalsIgnoreCase("weekly"))
              {
                  orderPriceInt = orderPriceInt*7;

              }


                order_titile.setText(modelData.food_detail.get(0).name);
                order_price.setText(modelData.food_detail.get(0).price);
                order_price_basedQuantity.setText(modelData.food_detail.get(0).price);
                order_price_finalTotal.setText(String.valueOf(orderPriceInt+modelData.food_detail.get(0).taxes));
                totalValue.setText(String.valueOf(orderPriceInt+modelData.food_detail.get(0).taxes));


                placeOrderprice.setText(String.valueOf(orderPriceInt+modelData.food_detail.get(0).taxes));
                payatm.setText("Payatm "+"Rs."+String.valueOf(orderPriceInt +modelData.food_detail.get(0).taxes)+"/-");
                placeOrderprice.setText("Pay Rs."+String.valueOf(orderPriceInt+modelData.food_detail.get(0).taxes)+"/-");
                payatmOption.setText("Pay Rs."+String.valueOf(orderPriceInt+modelData.food_detail.get(0).taxes)+"/-");
                order_taxes.setText(String.valueOf(modelData.food_detail.get(0).taxes));

            }


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

    @OnClick(R.id.item_count)
    public void itemCount()
    {

    }

    @OnClick(R.id.sub_item)
    public void sub_item()
    {
        float valuep;
        int itemCountText = Integer.parseInt(item_count.getText().toString());
        int itemTaxesText = Integer.parseInt(order_taxes.getText().toString());


        if (itemCountText > 1)
        {
            int totalCount = itemCountText - 1;
            item_count.setText(String.valueOf(totalCount));
            if (location.equalsIgnoreCase("RepeatOrder"))
            {
                valuep  = Float.parseFloat(orders.price);
            }else
            {
                valuep  = Float.parseFloat(modelData.food_detail.get(0).price);
            }


            int value  = (int) valuep;

            int priceValue = value * totalCount;
            order_price_basedQuantity.setText(String.valueOf(priceValue));
            int totalValueRs = priceValue;

            totalValue.setText(String.valueOf(totalValueRs));

            placeOrderprice.setText(String.valueOf(totalValueRs));
            order_price_finalTotal.setText(String.valueOf(totalValueRs+modelData.food_detail.get(0).taxes));
            payatm.setText("Payatm "+"Rs."+String.valueOf(totalValueRs+modelData.food_detail.get(0).taxes)+"/-");
            placeOrderprice.setText("Pay Rs."+String.valueOf(totalValueRs+modelData.food_detail.get(0).taxes)+"/-");
            payatmOption.setText("Pay Rs."+String.valueOf(totalValueRs+modelData.food_detail.get(0).taxes)+"/-");

        }
    }

    @OnClick(R.id.add_item)
    public void add_item()
    {
        float valuep;
            int itemCountText = Integer.parseInt(item_count.getText().toString());
            int itemTaxesText = Integer.parseInt(order_taxes.getText().toString());
            int totalCount = itemCountText + 1;
            item_count.setText(String.valueOf(totalCount));

        if (location.equalsIgnoreCase("RepeatOrder"))
        {
            valuep  = Float.parseFloat(orders.price);
        }else
        {
            valuep  = Float.parseFloat(modelData.food_detail.get(0).price);
        }

        int value  = (int) valuep;

        int priceValue = value * totalCount;

        order_price_basedQuantity.setText(String.valueOf(priceValue));

        int totalValueRs = priceValue;

        totalValue.setText(String.valueOf(totalValueRs));

        placeOrderprice.setText(String.valueOf(totalValueRs));
        payatm.setText("Payatm "+"Rs."+String.valueOf(totalValueRs)+"/-");
        placeOrderprice.setText("Pay Rs."+String.valueOf(totalValueRs)+"/-");
        payatmOption.setText("Pay Rs."+String.valueOf(totalValueRs)+"/-");
        order_price_finalTotal.setText(String.valueOf(totalValueRs+modelData.food_detail.get(0).taxes));


    }

    @OnClick(R.id.addressChange)
    public void addressChange()
    {
        String addrsss = addressMain.getText().toString();
        Intent adresIntent = new Intent(YourCartActivity.this,EnterFullAdressActivity.class);
        adresIntent.putExtra("Address",addrsss);
        adresIntent.putExtra("From","OrderDetails");
        adresIntent.putExtra("landMark",orders.landmark);
        adresIntent.putExtra("flatNo",orders.house_no);
        startActivityForResult(adresIntent,200);
    }

    @OnClick(R.id.patmentChange)
    public void patmentChange()
    {

        scrollChange.setVisibility(View.GONE);
        changePaymentOption.setVisibility(View.VISIBLE);

    }

    @OnClick(R.id.infoChange)
    public void infoChange()
    {
        scrollChange.setVisibility(View.GONE);
        changePersonInfo.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.nameSave)
    public void nameSave()
    {
        String username  = userNameUpdated.getText().toString();
        String mobileName   = mobileNumberUpdation.getText().toString();

        scrollChange.setVisibility(View.VISIBLE);
        changePersonInfo.setVisibility(View.GONE);

        if (!"".equalsIgnoreCase(username))
        {
            pf = new PreferenceManager(this);
            pf.saveStringForKey("Username",username);
            pf.saveStringForKey("Mobile",mobileName);
            personInfo.setText(username +" , "+mobileName);
        }
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
            if (location.equalsIgnoreCase("RepeatOrder"))
            {
                payatm.setText("COD "+"Rs."+orders.price+"/-");
                pf.saveStringForKey("paymentType","COD");

            }else
            {
                payatm.setText("COD "+"Rs."+modelData.food_detail.get(0).price+"/-");
                pf.saveStringForKey("paymentType","COD");
            }

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
            if (location.equalsIgnoreCase("RepeatOrder"))
            {
                payatm.setText("Payatm "+"Rs."+orders.price+"/-");
                pf.saveStringForKey("paymentType","Payatm");
            }else
            {
                payatm.setText("Payatm "+"Rs."+modelData.food_detail.get(0).price+"/-");
                pf.saveStringForKey("paymentType","Payatm");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @OnClick(R.id.payatmOption)
    public void payatmOption()
    {


        try {
            if (location.equalsIgnoreCase("RepeatOrder"))
            {
                payatm.setText("Payatm "+"Rs."+orders.price+"/-");
            }else
            {
                payatm.setText("Payatm "+"Rs."+modelData.food_detail.get(0).price+"/-");
            }

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

        }else  if(changePaymentOption.getVisibility() ==View.VISIBLE)
        {
            changePersonInfo.setVisibility(View.GONE);
            scrollChange.setVisibility(View.VISIBLE);
        }else
        {
            finish();
        }

    }

    @OnClick(R.id.backArrowFinish)
    public void backArrowFinish()
    {
        if (changePaymentOption.getVisibility() ==View.VISIBLE)
        {
            changePaymentOption.setVisibility(View.GONE);
            scrollChange.setVisibility(View.VISIBLE);

        }else  if(changePaymentOption.getVisibility() ==View.VISIBLE)
        {
            changePersonInfo.setVisibility(View.GONE);
            scrollChange.setVisibility(View.VISIBLE);
        }else
        {
            finish();
        }

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
        int itemCountText = Integer.parseInt(item_count.getText().toString());

        if (location.equalsIgnoreCase("RepeatOrder"))
        {

            String paymetTYpe = orders.payment_type;
            orderModel.house_no = orders.house_no;
            orderModel.landmark = orders.landmark;
            orderModel.pincode = orders.pincode;
            orderModel.address_type = orders.address_type;
            orderModel.payment_type = paymetTYpe;

            orderModel.food_user_id = orders.order_for;
            orderModel.order_by =orders.order_by;
            orderModel.order_for =  orders.order_for;
            orderModel.food_detail = orders.food_detail;
            orderModel.food_name = orders.food_name;

            orderModel.street = "";
            orderModel.city = "indore";
            orderModel.state = "MP";

            orderModel.price = orders.price;
            orderModel.quantity =1;
            orderModel.payment_status = "confirm";
            orderModel.is_order_confirmed = 1;
            orderModel.user_id = orders.user_id;
            orderModel.subscribe_to = orders.subscribe_to;
            orderModel.number_of_days = orders.number_of_days;
            orderModel.status = "active";
            orderModel.is_dinner = orders.is_dinner;
            orderModel.is_lunch =  orders.is_dinner;;
            orderModel.ordered_plates = itemCountText;
            orderModel.chef_name = "privacy concern so name is not here";
            orderModel.food_image = foodImage;

        }else
        {
            String landmark = pfUAddress.getStringForKey("landmark","");
            String house_no = pfUAddress.getStringForKey("house_no","");
            String type = pfUAddress.getStringForKey("type","");
            String pincode = pfUAddress.getStringForKey("pincode","");
            String paymetTYpe = pf.getStringForKey("paymentType","");
            orderModel.house_no = house_no;
            orderModel.landmark = landmark;
            orderModel.pincode = pincode;
            orderModel.address_type = type;
            orderModel.payment_type = paymetTYpe;

            orderModel.food_user_id = modelData.chef_detail.user_id;
            orderModel.order_by =loginPref.getIntForKey("user_id",0);
            orderModel.order_for = modelData.chef_detail.user_id;
            orderModel.food_detail = modelData.food_detail.get(0).details;
            orderModel.food_name = modelData.food_detail.get(0).name;

            orderModel.street = "";
            orderModel.city = "indore";
            orderModel.state = "MP";

            orderModel.price = modelData.food_detail.get(0).price;
            orderModel.quantity =itemCountText;
            orderModel.payment_status = "confirm";
            orderModel.is_order_confirmed = 1;
            orderModel.user_id = loginPref.getIntForKey("user_id",0);
            orderModel.subscribe_to = modelData.chef_detail.user_id;
            orderModel.number_of_days = numberOfDays;
            orderModel.status = "active";
            orderModel.is_dinner = isDinner;
            orderModel.is_lunch = isLunch;
            orderModel.ordered_plates = itemCountText;
            orderModel.chef_name = "privacy concern so name is not here";
            orderModel.food_image = foodImage;
        }

        Call<OrderModel.Data> loginRequestCall = AppConstants.restAPI.subscribeOrder(orderModel);

        loginRequestCall.enqueue(new Callback<OrderModel.Data>() {
            @Override
            public void onResponse(Call<OrderModel.Data> call, Response<OrderModel.Data> response) {

                if (response != null){

                    if (response.isSuccessful()){
                        OrderModel.Data res = response.body();
                        if (res.status != null && res.status.equalsIgnoreCase(AppConstants.SUCCESS)){

                            db.deleteTable(DataBaseHelperNew.TABLE_ADD_TO_CART);

                            Intent ActIntent = new Intent(YourCartActivity.this,CongratualtionsActivtiy.class);
                            ActIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(ActIntent);
                            finish();
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