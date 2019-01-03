package com.mf.mumizzfood.activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OneSignal;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.mf.mumizzfood.location.EnterFullAdressActivity;
import com.mf.mumizzfood.R;
import com.mf.mumizzfood.base.BaseActivity;
import com.mf.mumizzfood.data.db.DataBaseHelperNew;
import com.mf.mumizzfood.data.pref.PreferenceManager;
import com.mf.mumizzfood.location.UserLocationActivtiy;
import com.mf.mumizzfood.models.HomeFeed;
import com.mf.mumizzfood.models.OrderModel;
import com.mf.mumizzfood.models.UserProfileModel;
import com.mf.mumizzfood.utils.AppConstants;
import com.mf.mumizzfood.utils.SendMail;
import com.mf.mumizzfood.widgets.CkdButton;
import com.mf.mumizzfood.widgets.CkdTextview;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.SEND_SMS;

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
    RelativeLayout scrollChange;
    @BindView(R.id.grandTotalvalue)
    RelativeLayout grandTotalvalue;



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

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private HomeFeed.Data modelData;
    PreferenceManager pfUName;
    PreferenceManager pfUMobile;
  //  PreferenceManager pfUAddress;
    PreferenceManager loginPref;

    private int radioValue;
    private int numberOfDays, isLunch, isDinner;

    UserProfileModel.Subscribes ordersSub = new UserProfileModel.Subscribes();

    private String userAdd;
    private String landmarkEdit = "";
    private String hounseNoEdit = "";
    private String typeOfPackage;
    private String location = "";
    private String foodImage = "";
    private String paymentType = "";
    private String mobileNumber = "";
    private DataBaseHelperNew db;
    final int SMS_PERMISSION = 200;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_cart);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        requestPermission();

        db = new DataBaseHelperNew(this);

        pf = new PreferenceManager(this);

        if (getIntent() != null) {

            try {
                location = getIntent().getStringExtra("From");
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (location == null) {
                location = "";
            }
            if (location.equalsIgnoreCase("RepeatOrder")) {
                ordersSub = (UserProfileModel.Subscribes) getIntent().getSerializableExtra("data");
                typeOfPackage = getIntent().getStringExtra("typeOfPackage");
                numberOfDays = getIntent().getIntExtra("numberOfDays", 0);
                isDinner = getIntent().getIntExtra("isDinner", 0);
                isLunch = getIntent().getIntExtra("isLunch", 0);
                foodImage = ordersSub.orders.get(0).food_image;
            } else {
                modelData = (HomeFeed.Data) getIntent().getSerializableExtra("data");
                typeOfPackage = getIntent().getStringExtra("typeOfPackage");
                numberOfDays = getIntent().getIntExtra("numberOfDays", 0);
                isDinner = getIntent().getIntExtra("isDinner", 0);
                isLunch = getIntent().getIntExtra("isLunch", 0);
                foodImage = getIntent().getStringExtra("foodImage");

            }
        }

        checkPaymentOption();

        if (typeOfPackage.equalsIgnoreCase("today")) {

            if (isLunch == 1 && isDinner == 1) {
                add_to_cart_item_layout.setVisibility(View.GONE);
                order_price_basedQuantity.setVisibility(View.GONE);
            } else {
                add_to_cart_item_layout.setVisibility(View.VISIBLE);
                order_price_basedQuantity.setVisibility(View.VISIBLE);
            }

        } else if (typeOfPackage.equalsIgnoreCase("monthly")) {
            add_to_cart_item_layout.setVisibility(View.GONE);
            order_price_basedQuantity.setVisibility(View.GONE);
        } else if (typeOfPackage.equalsIgnoreCase("weekly")) {
            add_to_cart_item_layout.setVisibility(View.GONE);
            order_price_basedQuantity.setVisibility(View.GONE);
        }


        try {

            pfUName = new PreferenceManager(this, PreferenceManager.FIRST_NM);
            loginPref = new PreferenceManager(this, PreferenceManager.LOGIN_PREFERENCES_FILE);
          //  pfUAddress = new PreferenceManager(this);
            pfUMobile = new PreferenceManager(this, PreferenceManager.USER_MOBILE);
            pf = new PreferenceManager(this);

            try {
                personInfo.setText(pf.getStringForKey("Username", "") + "  " + pf.getStringForKey("Mobile", ""));
            } catch (Exception e) {
                e.printStackTrace();
            }

            userAdd = pf.getStringForKey("CurrentAddress", "");

            if (location.equalsIgnoreCase("RepeatOrder")) {

                try {
                    addressMain.setText(ordersSub.orders.get(0).landmark);
                } catch (Exception e) {
                    e.printStackTrace();
                    addressMain.setText(userAdd);
                }
            } else {
                addressMain.setText(userAdd);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            if (location.equalsIgnoreCase("RepeatOrder")) {


                order_titile.setText(ordersSub.orders.get(0).food_name);
                order_price.setText(getResources().getString(R.string.rs_symbol) + ordersSub.orders.get(0).price +" for one plate");


                order_price_basedQuantity.setText(getResources().getString(R.string.rs_symbol) + ordersSub.orders.get(0).price);
                totalValue.setText(getResources().getString(R.string.rs_symbol) + ordersSub.orders.get(0).price);
                order_taxes.setText(getResources().getString(R.string.rs_symbol) + String.valueOf(50));
                placeOrderprice.setText(getResources().getString(R.string.rs_symbol) + ordersSub.orders.get(0).price);
                payatm.setText(paymentType + " " + getResources().getString(R.string.rs_symbol) + ordersSub.orders.get(0).price);
                placeOrderprice.setText("Pay " + getResources().getString(R.string.rs_symbol) + ordersSub.orders.get(0).price);
                payatmOption.setText("Pay " + getResources().getString(R.string.rs_symbol) + ordersSub.orders.get(0).price);
                order_taxes.setText(getResources().getString(R.string.rs_symbol) + String.valueOf(0));

                grandTotalvalue.setVisibility(View.GONE);
               // order_price_finalTotal.setText(getResources().getString(R.string.rs_symbol) + String.valueOf(orderPriceInt + 0));

            } else {
                float orderPrice = Float.parseFloat(modelData.price);
                int orderPriceInt = 0;
                orderPriceInt = (int) orderPrice;
                if (typeOfPackage.equalsIgnoreCase("monthly")) {
                    orderPriceInt = orderPriceInt * numberOfDays;
                } else if (typeOfPackage.equalsIgnoreCase("weekly")) {
                    orderPriceInt = orderPriceInt * numberOfDays;
                } else if (typeOfPackage.equalsIgnoreCase("today")) {
                    if (isDinner == 1 && isLunch == 1) {
                        orderPriceInt = orderPriceInt + orderPriceInt;
                    } else {
                        orderPriceInt = orderPriceInt * numberOfDays;
                    }
                }

                grandTotalvalue.setVisibility(View.VISIBLE);
                order_titile.setText(modelData.name);
                order_price.setText(getResources().getString(R.string.rs_symbol) + modelData.price +" for one plate");
                order_price_basedQuantity.setText(getResources().getString(R.string.rs_symbol) + modelData.price);
                order_price_finalTotal.setText(getResources().getString(R.string.rs_symbol) + String.valueOf(orderPriceInt + 0));
                totalValue.setText(getResources().getString(R.string.rs_symbol) + String.valueOf(orderPriceInt + 0));

                placeOrderprice.setText(String.valueOf(orderPriceInt + 0));
                payatm.setText(paymentType + " " + getResources().getString(R.string.rs_symbol) + String.valueOf(orderPriceInt + 0));
                placeOrderprice.setText("Pay " + getResources().getString(R.string.rs_symbol) + String.valueOf(orderPriceInt + 0));
                payatmOption.setText("Pay " + getResources().getString(R.string.rs_symbol) + String.valueOf(orderPriceInt + 0));
                order_taxes.setText(getResources().getString(R.string.rs_symbol) + 0);

            }


        } catch (Exception e) {
            e.printStackTrace();
        }


        /*if (addressMain == null && "".equalsIgnoreCase(userAdd)) {
            placeOrderButton.setBackground(getResources().getDrawable(R.drawable.fill_rounded_white_smoke));
            placeOrderprice.setTextColor(getResources().getColor(R.color.black));
            placeOrderButtonCheckout.setTextColor(getResources().getColor(R.color.black));

        }*/


        try {

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void checkPaymentOption() {

        paymentType = pf.getStringForKey("paymentType", "");

        //   if (payatm == null ||payatm.)

    }

    @OnClick(R.id.placeOrderButton)
    public void placceOrder() {
        if (addressMain == null && "".equalsIgnoreCase(userAdd)) {

            //     placeOrderButton.setBackground(getResources().getDrawable(R.drawable.fill_rounded_white_smoke));
            //     placeOrderprice.setTextColor(getResources().getColor(R.color.black));
            //   placeOrderButtonCheckout.setTextColor(getResources().getColor(R.color.black));
            addressMain.setError("Insert Valid Address");

        } else {
            //   placeOrderButton.setBackground(getResources().getDrawable(R.drawable.fill_rounded_full_primary));
            //    placeOrderprice.setTextColor(getResources().getColor(R.color.black));
            // placeOrderButtonCheckout.setTextColor(getResources().getColor(R.color.black));
        }

        placeOrderDialog();
    }

    @OnClick(R.id.infoChange)
    public void inFochage() {

    }

    @OnClick(R.id.item_count)
    public void itemCount() {

    }

    @OnClick(R.id.sub_item)
    public void sub_item() {
        float valuep;
        int itemCountText = Integer.parseInt(item_count.getText().toString());
        int itemTaxesText = Integer.parseInt(String.valueOf(0));


        if (itemCountText > 1) {
            int totalCount = itemCountText - 1;
            item_count.setText(String.valueOf(totalCount));
            if (location.equalsIgnoreCase("RepeatOrder")) {
                valuep = Float.parseFloat(ordersSub.orders.get(0).price);
            } else {
                valuep = Float.parseFloat(modelData.price);
            }


            int value = (int) valuep;


            int priceValue = value * totalCount;
            order_price_basedQuantity.setText(getResources().getString(R.string.rs_symbol) + String.valueOf(priceValue));
            int totalValueRs = priceValue;

            totalValue.setText(getResources().getString(R.string.rs_symbol) + String.valueOf(totalValueRs));

            placeOrderprice.setText(getResources().getString(R.string.rs_symbol) + String.valueOf(totalValueRs));
            order_price_finalTotal.setText(getResources().getString(R.string.rs_symbol) + String.valueOf(totalValueRs + 0));
            payatm.setText(paymentType + " " + getResources().getString(R.string.rs_symbol) + String.valueOf(totalValueRs + 0));
            placeOrderprice.setText("Pay " + getResources().getString(R.string.rs_symbol) + String.valueOf(totalValueRs + 0));
            payatmOption.setText("Pay " + getResources().getString(R.string.rs_symbol) + String.valueOf(totalValueRs + 0));

        }else if (itemCountText == 1)
        {
            removeOrderDialog();
        }
    }

    @OnClick(R.id.add_item)
    public void add_item() {
        float valuep;
        int itemCountText = Integer.parseInt(item_count.getText().toString());
//        int itemTaxesText = Integer.parseInt(String.valueOf(0));
        int totalCount = itemCountText + 1;
        if (totalCount <= 5)
        {
            item_count.setText(String.valueOf(totalCount));

            if (location.equalsIgnoreCase("RepeatOrder")) {
                valuep = Float.parseFloat(ordersSub.orders.get(0).price);
            } else {
                valuep = Float.parseFloat(modelData.price);
            }

            int value = (int) valuep;

            int priceValue = value * totalCount;

            order_price_basedQuantity.setText(getResources().getString(R.string.rs_symbol) + String.valueOf(priceValue));

            int totalValueRs = priceValue;

            totalValue.setText(getResources().getString(R.string.rs_symbol) + String.valueOf(totalValueRs));

            placeOrderprice.setText(getResources().getString(R.string.rs_symbol) + String.valueOf(totalValueRs));
            payatm.setText(paymentType + " " + getResources().getString(R.string.rs_symbol) + String.valueOf(totalValueRs));
            placeOrderprice.setText("Pay " + getResources().getString(R.string.rs_symbol) + String.valueOf(totalValueRs));
            payatmOption.setText("Pay " + getResources().getString(R.string.rs_symbol) + String.valueOf(totalValueRs));
            order_price_finalTotal.setText(getResources().getString(R.string.rs_symbol) + String.valueOf(totalValueRs));

        }else
        {
            showToast("At a time maximum 5 order can be place");
        }


    }

    @OnClick(R.id.addressChange)
    public void addressChange() {
        String addrsss = addressMain.getText().toString();
        Intent adresIntent = new Intent(YourCartActivity.this, EnterFullAdressActivity.class);
        adresIntent.putExtra("Address", addrsss);
        adresIntent.putExtra("From", "OrderDetails");

        try {
            if (landmarkEdit != null && !"".equalsIgnoreCase(landmarkEdit)) {
                adresIntent.putExtra("landMark", landmarkEdit);
            } else {
                adresIntent.putExtra("landMark", ordersSub.orders.get(0).landmark);
            }

            if (hounseNoEdit != null && !"".equalsIgnoreCase(hounseNoEdit)) {
                adresIntent.putExtra("flatNo", hounseNoEdit);
            } else {
                adresIntent.putExtra("flatNo", ordersSub.orders.get(0).house_no);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        adresIntent.putExtra("AddNew", "Yes");
        startActivityForResult(adresIntent, 200);
    }

    @OnClick(R.id.patmentChange)
    public void patmentChange() {

        scrollChange.setVisibility(View.GONE);
        changePaymentOption.setVisibility(View.VISIBLE);

    }

    @OnClick(R.id.infoChange)
    public void infoChange() {

        Intent adresIntent = new Intent(YourCartActivity.this, MobileOtpVerificationActivity.class);
        adresIntent.putExtra("PlaceOrder", "PlaceOrder");
        startActivityForResult(adresIntent, 201);

        //  scrollChange.setVisibility(View.GONE);
        // changePersonInfo.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.nameSave)
    public void nameSave() {
        String username = userNameUpdated.getText().toString();
        mobileNumber = mobileNumberUpdation.getText().toString();

        scrollChange.setVisibility(View.VISIBLE);
        changePersonInfo.setVisibility(View.GONE);

        if (!"".equalsIgnoreCase(username)) {
            pf = new PreferenceManager(this);
            pf.saveStringForKey("Username", username);
            pf.saveStringForKey("Mobile", mobileNumber);
            personInfo.setText(username + "  " + mobileNumber);
        }
    }

    @OnClick(R.id.cod_linear)
    public void COD() {
        checkedCod.setVisibility(View.VISIBLE);
        changePaymentOption.setVisibility(View.GONE);
        scrollChange.setVisibility(View.VISIBLE);

        checkedCod.setVisibility(View.GONE);
        paytmChecked.setVisibility(View.GONE);
        try {
            if (location.equalsIgnoreCase("RepeatOrder")) {
                pf.saveStringForKey("paymentType", "COD");

                paymentType = pf.getStringForKey("paymentType", "");
                payatm.setText(paymentType + " " + getResources().getString(R.string.rs_symbol) + getResources().getString(R.string.rs_symbol) + ordersSub.orders.get(0).price);


            } else {
                pf.saveStringForKey("paymentType", "COD");
                paymentType = pf.getStringForKey("paymentType", "");
                payatm.setText(paymentType + " " + getResources().getString(R.string.rs_symbol) + modelData.price);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @OnClick(R.id.paytm_linear)
    public void setPayatm() {
        paytmChecked.setVisibility(View.VISIBLE);
        changePaymentOption.setVisibility(View.GONE);
        scrollChange.setVisibility(View.VISIBLE);
        checkedCod.setVisibility(View.GONE);
        paytmChecked.setVisibility(View.GONE);

        try {
            if (location.equalsIgnoreCase("RepeatOrder")) {
                pf.saveStringForKey("paymentType", "Paytm");
                paymentType = pf.getStringForKey("paymentType", "");
                payatm.setText(paymentType + " " + getResources().getString(R.string.rs_symbol) + ordersSub.orders.get(0).price);

            } else {
                pf.saveStringForKey("paymentType", "Paytm");
                paymentType = pf.getStringForKey("paymentType", "");
                payatm.setText(paymentType + " " + getResources().getString(R.string.rs_symbol) + modelData.price);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @OnClick(R.id.paymentRelative)
    public void payatmOption() {


        try {
            if (location.equalsIgnoreCase("RepeatOrder")) {
                payatm.setText(paymentType + " " + getResources().getString(R.string.rs_symbol) + getResources().getString(R.string.rs_symbol) + ordersSub.orders.get(0).price);
            } else {
                payatm.setText(paymentType + " " + getResources().getString(R.string.rs_symbol) + modelData.price);
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

        if (changePaymentOption.getVisibility() == View.VISIBLE) {
            changePaymentOption.setVisibility(View.GONE);
            scrollChange.setVisibility(View.VISIBLE);

        } else if (changePaymentOption.getVisibility() == View.VISIBLE) {
            changePersonInfo.setVisibility(View.GONE);
            scrollChange.setVisibility(View.VISIBLE);
        } else if (changePersonInfo.getVisibility() == View.VISIBLE) {
            scrollChange.setVisibility(View.VISIBLE);
            changePersonInfo.setVisibility(View.GONE);
        } else {
            finish();
        }

    }

    @OnClick(R.id.backArrowFinish)
    public void backArrowFinish() {
        if (changePaymentOption.getVisibility() == View.VISIBLE) {
            changePaymentOption.setVisibility(View.GONE);
            scrollChange.setVisibility(View.VISIBLE);

        } else if (changePaymentOption.getVisibility() == View.VISIBLE) {
            changePersonInfo.setVisibility(View.GONE);
            scrollChange.setVisibility(View.VISIBLE);
        } else if (changePersonInfo.getVisibility() == View.VISIBLE) {
            scrollChange.setVisibility(View.VISIBLE);
            changePersonInfo.setVisibility(View.GONE);
        } else {
            finish();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check that it is the SecondActivity with an OK result
        if (requestCode == 200) {
            if (resultCode == RESULT_OK) {


                String dataValue = null;
                try {
                    dataValue = data.getStringExtra("Address");
                    addressMain.setText(dataValue);
                    landmarkEdit = data.getStringExtra("landMark");
                    hounseNoEdit = data.getStringExtra("flatNo");
                } catch (Exception e) {
                    e.printStackTrace();
                }


                //   showToast(dataValue);
            }
        } else if (requestCode == 201) {

            try {
                String MobileNumber = data.getStringExtra("MobileNumber");
                personInfo.setText(MobileNumber);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void placeOrderDialog() {

        final Dialog dialogd = new Dialog(this);
        dialogd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogd.setContentView(R.layout.dialog_place_order);


        CkdTextview placeOrderok = (CkdTextview) dialogd.findViewById(R.id.placeOrderok);
        CkdTextview palceOrderViaMethod = (CkdTextview) dialogd.findViewById(R.id.palceOrderViaMethod);
        CkdTextview notNow = (CkdTextview) dialogd.findViewById(R.id.notNow);
        LottieAnimationView lottieAnimationViewPlace = (LottieAnimationView) dialogd.findViewById(R.id.lottieAnimationViewPlace);
        lottieAnimationViewPlace.playAnimation();


        palceOrderViaMethod.setText("Your order will be placing via " + paymentType + " payment method" + '\n' + "Place Order ?");

        placeOrderok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogd.dismiss();
                placeOrderData();
            }
        });

        notNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogd.dismiss();

            }
        });

        dialogd.show();


    }

    public void removeOrderDialog() {

        final Dialog dialogd = new Dialog(this);
        dialogd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogd.setContentView(R.layout.remove_items_from_cart);


        CkdTextview placeOrderok = (CkdTextview) dialogd.findViewById(R.id.placeOrderok);
        CkdTextview palceOrderViaMethod = (CkdTextview) dialogd.findViewById(R.id.palceOrderViaMethod);
        CkdTextview notNow = (CkdTextview) dialogd.findViewById(R.id.notNow);
  //      LottieAnimationView lottieAnimationViewPlace = (LottieAnimationView) dialogd.findViewById(R.id.lottieAnimationViewPlace);
//        lottieAnimationViewPlace.playAnimation();


        palceOrderViaMethod.setText("Do you really want to remove this order ?");

        placeOrderok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogd.dismiss();
                finish();
            }
        });

        notNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogd.dismiss();

            }
        });

        dialogd.show();


    }


    private void placeOrderData() {

        mobileNumber = pf.getStringForKey("Mobile", "");


        String CurrentAddress = pf.getStringForKey("CurrentAddress", "");

        if (mobileNumber != null && !"".equalsIgnoreCase(mobileNumber)) {
            OrderModel.Data orderModel = new OrderModel.Data();
            int itemCountText = Integer.parseInt(item_count.getText().toString());
            orderModel.landmark = addressMain.getText().toString();

            if (location.equalsIgnoreCase("RepeatOrder")) {

                String paymetTYpe = ordersSub.orders.get(0).payment_type;
                orderModel.house_no = ordersSub.orders.get(0).house_no;
                orderModel.pincode = ordersSub.orders.get(0).pincode;
                orderModel.address_type = ordersSub.orders.get(0).address_type;
                orderModel.payment_type = paymetTYpe;

                orderModel.food_user_id = ordersSub.orders.get(0).order_for;
                orderModel.order_by = ordersSub.orders.get(0).order_by;
                orderModel.order_for = ordersSub.orders.get(0).order_for;
                orderModel.food_detail = ordersSub.orders.get(0).food_detail;
                orderModel.food_name = ordersSub.orders.get(0).food_name;

                orderModel.street = CurrentAddress;
                orderModel.city = "indore";
                orderModel.state = "MP";

                orderModel.price = ordersSub.orders.get(0).price;
                orderModel.quantity = 1;
                orderModel.payment_status = "confirm";
                orderModel.is_order_confirmed = 1;
                orderModel.user_id = ordersSub.orders.get(0).user_id;
                orderModel.subscribe_to = ordersSub.orders.get(0).order_for;
                orderModel.number_of_days = ordersSub.number_of_days;
                orderModel.status = "active";
                orderModel.is_dinner = ordersSub.orders.get(0).is_dinner;
                orderModel.is_lunch = ordersSub.orders.get(0).is_dinner;
                ;
                orderModel.ordered_plates = itemCountText;
                orderModel.chef_name = "privacy concern so name is not here";
                orderModel.food_image = foodImage;

                if (CurrentAddress != null && !"".equalsIgnoreCase(CurrentAddress)) {
                    if (paymentType != null && !"".equalsIgnoreCase(paymentType)) {
                        if (paymentType.equalsIgnoreCase("Paytm")) {
                            showToast("Paytm Integration is in process you can place order using COD");
                            //paytmPlaceOrder();
                        } else {
                            //paytmPlaceOrder();

                            newtowrkCallToplaceOrder(orderModel, itemCountText);
                        }
                    } else {
                        //paytmPlaceOrder();

                        showToast("This order has placed via COD payment method");
                        newtowrkCallToplaceOrder(orderModel, itemCountText);
                    }

                } else {

                    showToast("add your address");
                    Intent userLoc = new Intent(YourCartActivity.this, EnterFullAdressActivity.class);
                    userLoc.putExtra("AddNew", "Yes");
                    startActivityForResult(userLoc, 200);

                }


            } else {

                String house_no = pf.getStringForKey("house_no", "");
                String type = pf.getStringForKey("type", "");
                String pincode = pf.getStringForKey("pincode", "");
                String paymetTYpe = pf.getStringForKey("paymentType", "");
                orderModel.house_no = hounseNoEdit;

                if ("".equalsIgnoreCase(paymetTYpe)) {
                    paymetTYpe = "Paytm";
                }

                try {

                } catch (Exception e) {
                    e.printStackTrace();
                }
                orderModel.pincode = pincode;
                orderModel.address_type = type;
                orderModel.payment_type = paymetTYpe;

                orderModel.food_user_id = modelData.chef_detail.user_id;
                orderModel.order_by = pf.getIntForKey("user_id", 0);
                orderModel.order_for = modelData.chef_detail.user_id;
                orderModel.food_detail = modelData.details;
                orderModel.food_name = modelData.name;

                orderModel.street = CurrentAddress;
                orderModel.city = "indore";
                orderModel.state = "MP";

                orderModel.price = modelData.price;
                orderModel.quantity = itemCountText;
                orderModel.payment_status = "confirm";
                orderModel.is_order_confirmed = 1;
                orderModel.user_id = pf.getIntForKey("user_id", 0);
                orderModel.subscribe_to = modelData.chef_detail.user_id;
                orderModel.number_of_days = numberOfDays;
                orderModel.status = "active";
                orderModel.is_dinner = isDinner;
                orderModel.is_lunch = isLunch;
                orderModel.ordered_plates = itemCountText;
                orderModel.chef_name = "privacy concern so name is not here";
                orderModel.food_image = foodImage;
                orderModel.house_no = "Call them";


                if ("".equalsIgnoreCase(landmarkEdit)) {
                    orderModel.landmark = CurrentAddress;
                } else {
                    orderModel.landmark = landmarkEdit;
                }


                if (CurrentAddress != null && !"".equalsIgnoreCase(CurrentAddress)) {


                    if (paymentType != null && !"".equalsIgnoreCase(paymentType)) {
                        if (paymentType.equalsIgnoreCase("Paytm")) {
                            showToast("Paytm Integration is in process you can place order using COD");
                            //paytmPlaceOrder();
                        } else {
                            //paytmPlaceOrder();

                            newtowrkCallToplaceOrder(orderModel, itemCountText);
                        }
                    } else {
                        //paytmPlaceOrder();
                        showToast("This order has placed via COD payment method");
                        newtowrkCallToplaceOrder(orderModel, itemCountText);
                    }

                } else {

                    showToast("add your address");
                    Intent userLoc = new Intent(YourCartActivity.this, UserLocationActivtiy.class);
                    userLoc.putExtra("AddNew", "Yes");
                    startActivityForResult(userLoc, 200);

                }


            }

        } else {
            Intent adresIntent = new Intent(YourCartActivity.this, MobileOtpVerificationActivity.class);
            adresIntent.putExtra("PlaceOrder", "PlaceOrder");
            startActivityForResult(adresIntent, 201);
            showToast("Please insert Mobile Number");
        }

    }

    private void paytmPlaceOrder() {
        PaytmPGService Service = PaytmPGService.getStagingService();


        HashMap<String, String> paramMap = new HashMap<String, String>();
        paramMap.put( "MID" , "XfLrgI86715347032972");
// Key in your staging and production MID available in your dashboard
        paramMap.put( "ORDER_ID" , "ORDER45676");
        paramMap.put( "CUST_ID" , "CUST0001453");
        paramMap.put( "MOBILE_NO" , "8602639858");
        //paramMap.put( "EMAIL" , "seemanagar86@gmail.com");
        paramMap.put( "CHANNEL_ID" , "WAP");
        paramMap.put( "TXN_AMOUNT" , "10.00");
        paramMap.put( "WEBSITE" , "APPSTAGING");
// This is the staging value. Production value is available in your dashboard
        paramMap.put( "INDUSTRY_TYPE_ID" , "Retail");
// This is the staging value. Production value is available in your dashboard
        paramMap.put( "CALLBACK_URL", "https://securegw.paytm.in/theia/paytmCallback?ORDER_ID=ORDER45676");
        paramMap.put( "CHECKSUMHASH" , "w2QDRMgp1234567JEAPCIOmNgQvsi+BhpqijfM9KvFfRiPmGSt3Ddzw+oTaGCLneJwxFFq5mqTMwJXdQE2EzK4px2xruDqKZjHupz9yXev4=");
        PaytmOrder Order = new PaytmOrder(paramMap);
        Service.initialize(Order, null);

        Service.startPaymentTransaction(this, true, true, new PaytmPaymentTransactionCallback() {
            /*Call Backs*/
            public void someUIErrorOccurred(String inErrorMessage) {
                Log.e("someUIErrorOccurred",inErrorMessage);
                Toast.makeText(getApplicationContext(), "UI Error " + inErrorMessage , Toast.LENGTH_LONG).show();
            }
            public void onTransactionResponse(Bundle inResponse) {
                Log.e("inResponse",inResponse.toString());
                Toast.makeText(getApplicationContext(), "Payment Transaction response " + inResponse.toString(), Toast.LENGTH_LONG).show();

            }
            public void networkNotAvailable() {
                Log.e("networkNotAvailable","");
                Toast.makeText(getApplicationContext(), "Network connection error: Check your internet connectivity", Toast.LENGTH_LONG).show();
            }
            public void clientAuthenticationFailed(String inErrorMessage) {
                Log.e("AuthenticationFailed","inErrorMessage");
                Toast.makeText(getApplicationContext(), "Authentication failed: Server error" + inErrorMessage.toString(), Toast.LENGTH_LONG).show();
            }
            public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {
                Log.e("onErrorLoadingWebPage",iniErrorCode+" "+inErrorMessage+" "+inFailingUrl);
                Toast.makeText(getApplicationContext(), "Unable to load webpage " + inErrorMessage.toString(), Toast.LENGTH_LONG).show();
            }
            public void onBackPressedCancelTransaction() {
                Log.e("CancelTransaction","onBackPressedCancelTransaction");
                Toast.makeText(getApplicationContext(), "Transaction cancelled" , Toast.LENGTH_LONG).show();
            }
            public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                Log.e("onTransactionCancel",inErrorMessage+" "+inResponse);
                Toast.makeText(getApplicationContext(), "Transaction Cancelled" + inResponse.toString(), Toast.LENGTH_LONG).show();
            }
        });


    }

    private void newtowrkCallToplaceOrder(OrderModel.Data orderModel, int itemCountText) {


        Call<OrderModel.Data> loginRequestCall = AppConstants.restAPI.subscribeOrder(orderModel);

        loginRequestCall.enqueue(new Callback<OrderModel.Data>() {
            @Override
            public void onResponse(Call<OrderModel.Data> call, Response<OrderModel.Data> response) {

                if (response != null) {

                    if (response.isSuccessful()) {
                        OrderModel.Data res = response.body();
                        if (res.status != null && res.status.equalsIgnoreCase(AppConstants.SUCCESS)) {

                            db.deleteTable(DataBaseHelperNew.TABLE_ADD_TO_CART);

                            Intent ActIntent = new Intent(YourCartActivity.this, CongratualtionsActivtiy.class);
                            ActIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(ActIntent);
                            finish();

                          /*  sendSms(res.order_by,res.chef_name,res.order_for,res.ordered_plates,res.price,res.landmark,res.food_detail,
                                    res.is_dinner,res.is_lunch,res.payment_status);*/
                            sendEmail(res.order_by,res.chef_name,res.order_for,res.ordered_plates,res.price,res.landmark,res.food_detail,
                                    res.is_dinner,res.is_lunch,res.payment_status);

                            //sendNotification();

                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    sendNotification();
                                }
                            }, 5000);

                        } else {
                        }

                    } else {

                        try {
                            Log.e("Response is not success", "" + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                } else {
                    try {
                        Log.e("Response is null", "" + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<OrderModel.Data> call, Throwable t) {
                Log.e("Response is failure", "" + t);

            }
        });
    }

    private void sendEmail(int order_by, String chef_name, int order_for, int ordered_plates, String price, String landmark, String food_detail, int is_dinner, int is_lunch, String payment_status) {


        //Getting content for email
        String email = "ckd.khana24@gmail.com";
        String email1 = "ckd.khana12@gmail.com";
        String subject = "Wake Up New Order Arrived";
        String message = "Order by this user Id - "+order_by+"\n"+"Chef Name - "+chef_name+"\n"+"Order for this chef UserId -"+order_for+"\n"
                +"Number of plates Ordered - "+ordered_plates+"\n"+"Price for this Order - "+price+"\n"+"Land Mark = "+landmark+"\n"
                +"Food Details - "+food_detail+"\n"+"Is Dinner -"+is_dinner+"\n"+"Is Lunch ="+is_lunch+"\n"+"Payment Method -"+payment_status;

        //Creating SendMail object
        SendMail sm = new SendMail(this, email, subject, message);

        //Executing sendmail to send email
        sm.execute();

        //Creating SendMail object
        SendMail sm1 = new SendMail(this, email1, subject, message);

        //Executing sendmail to send email
        sm1.execute();


    }

  /*  private void sendSms(int order_by, String chef_name, int order_for, int ordered_plates, String price, String landmark, String food_detail, int is_dinner, int is_lunch, String payment_status) {
        String phone = "8828376477";
        String phone1 = "8602639858";
        String message = "Order by this user Id - "+order_by+"\n"+"Chef Name - "+chef_name+"\n"+"Order for this chef UserId -"+order_for+"\n"
                +"Number of plates Ordered - "+ordered_plates+"\n"+"Price for this Order - "+price+"\n"+"Land Mark = "+landmark+"\n"
                +"Food Details - "+food_detail+"\n"+"Is Dinner -"+is_dinner+"\n"+"Is Lunch ="+is_lunch+"\n"+"Payment Method -"+payment_status;


        //Check if the phoneNumber is empty
        if (phone.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please Enter a Valid Phone Number", Toast.LENGTH_SHORT).show();
        } else {

            SmsManager sms = SmsManager.getDefault();
            // if message length is too long messages are divided
            List<String> messages = sms.divideMessage(message);
            for (String msg : messages) {

                PendingIntent sentIntent = PendingIntent.getBroadcast(this, 0, new Intent("SMS_SENT"), 0);
                PendingIntent deliveredIntent = PendingIntent.getBroadcast(this, 0, new Intent("SMS_DELIVERED"), 0);
                sms.sendTextMessage(phone, null, msg, sentIntent, deliveredIntent);

            }

            for (String msg : messages) {

                PendingIntent sentIntent = PendingIntent.getBroadcast(this, 0, new Intent("SMS_SENT"), 0);
                PendingIntent deliveredIntent = PendingIntent.getBroadcast(this, 0, new Intent("SMS_DELIVERED"), 0);
                sms.sendTextMessage(phone1, null, msg, sentIntent, deliveredIntent);

            }
        }
    }
*/
    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{SEND_SMS}, SMS_PERMISSION);
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case SMS_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //sendSms();

                } else {
                    Toast.makeText(getApplicationContext(), "Permission Denied, You cannot access and sms", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(SEND_SMS)) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                requestPermissions(new String[]{SEND_SMS},
                                        SMS_PERMISSION);
                            }
                            return;
                        }
                    }
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void sendNotification() {


        OSPermissionSubscriptionState status = OneSignal.getPermissionSubscriptionState();
        String userId = status.getSubscriptionStatus().getUserId();
        boolean isSubscribed = status.getSubscriptionStatus().getSubscribed();

        if (!isSubscribed)
            return;

        try {
            JSONObject notificationContent = new JSONObject("{'contents': {'en': 'Your Order has placed.we will reach soon till then listen some songs'}," +
                    "'include_player_ids': ['" + userId + "'], " +
                    "'headings': {'en': 'Calender Khana do'}, " +
                    "'big_picture': 'https://www.shoutlo.com/uploads/articles/header-img-places-to-get-north-indian-food-in-hyderabad.jpg'}");
            OneSignal.postNotification(notificationContent, null);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}