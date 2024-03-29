package com.mf.mumizzfood.activities;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.mf.mumizzfood.models.ProfileModel;
import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OneSignal;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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

public class YourCartActivity extends BaseActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

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

    @BindView(R.id.deliveryChargeApplicable)
    CkdTextview deliveryChargeApplicable;

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
    int deliveryCharges = 0;

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

    private double latitudeS;
    private double lognitudeS;
    private String pin_code;
    private String AddNew = "";
    int priceValue;
    int totalValueRs;
    int value;
    double totalDistance;

    private Location mylocation;
    private GoogleApiClient googleApiClient;
    private final static int REQUEST_CHECK_SETTINGS_GPS = 0x1;
    private final static int REQUEST_ID_MULTIPLE_PERMISSIONS = 0x2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_cart);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
      //  requestPermission();

        db = new DataBaseHelperNew(this);

        pf = new PreferenceManager(this);

        if (getIntent() != null) {

            try {
                location = getIntent().getStringExtra("From");
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                totalDistance = getIntent().getDoubleExtra("totalDistance",0);

                int totaldistance = (int) totalDistance;
                if (totaldistance >3)
                {
                    deliveryChargeApplicable.setVisibility(View.VISIBLE);
                    if (totaldistance<=5)
                    {
                        deliveryChargeApplicable.setText(" Rs. 10 Delivery charge applicable after 3km");
                        deliveryCharges = 10;
                    }else if (totaldistance<=7)
                    {
                        deliveryChargeApplicable.setText(" Rs. 15 Delivery charge applicable after 5km");
                        deliveryCharges = 15;
                    }else if (totaldistance<=10)
                    {
                        deliveryChargeApplicable.setText("Rs. 20 Delivery charge applicable after 7km");
                        deliveryCharges = 20;
                    }else if (totaldistance>10)
                    {
                        deliveryChargeApplicable.setText("we are in starting phase so can't cover all the areas right now,Let's us know how can we help you.");
                        placeOrderButtonCheckout.setEnabled(false);
                        placeOrderButton.setEnabled(false);
                        placeOrderButton.setBackgroundResource(R.color.white_smoke);
                        placeOrderButtonCheckout.setVisibility(View.GONE);
                        placeOrderprice.setVisibility(View.GONE);
                        placeOrderButtonCheckout.setTextColor(getResources().getColor(R.color.black_effective));
                    }


                }else
                {
                    deliveryChargeApplicable.setVisibility(View.GONE);
                }
          //      showToast(String.valueOf(new DecimalFormat("##.##").format(totalDistance)));
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
            } else
                {
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
                placeOrderprice.setText(getResources().getString(R.string.rs_symbol) + String.valueOf(Integer.parseInt(ordersSub.orders.get(0).price)+deliveryCharges));
                payatm.setText(paymentType);
                placeOrderprice.setText("Pay " + getResources().getString(R.string.rs_symbol) + String.valueOf(Integer.parseInt(ordersSub.orders.get(0).price)+deliveryCharges));
                payatmOption.setText("Pay " + getResources().getString(R.string.rs_symbol) + ordersSub.orders.get(0).price);
                order_taxes.setText(getResources().getString(R.string.rs_symbol) + String.valueOf(0));

                grandTotalvalue.setVisibility(View.GONE);
               // order_price_finalTotal.setText(getResources().getString(R.string.rs_symbol) + String.valueOf(orderPriceInt + 0));

            } else {
                float orderPrice = Float.parseFloat(modelData.price);
                int orderPriceInt = 0;
                orderPriceInt = (int) orderPrice;
                if (typeOfPackage.equalsIgnoreCase("monthly")) {
                    //orderPriceInt = orderPriceInt * numberOfDays;

                    if (isDinner == 1&& isLunch == 1)
                    {
                        orderPriceInt = modelData.monthly_lunch_price+modelData.monthly_dinner_price;
                    }
                    else if (isDinner == 1)
                    {
                        orderPriceInt = modelData.monthly_dinner_price;
                    }else  if (isLunch == 1)
                    {
                        orderPriceInt = modelData.monthly_lunch_price;
                    }


                } else if (typeOfPackage.equalsIgnoreCase("weekly")) {
                    //orderPriceInt = orderPriceInt * numberOfDays;


                    if (isDinner == 1&& isLunch == 1)
                    {
                        orderPriceInt = modelData.weekly_dinner_price+modelData.weekly_lunch_price;
                    }else if (isDinner == 1)
                    {
                        orderPriceInt = modelData.weekly_dinner_price;
                    }else  if (isLunch == 1)
                    {
                        orderPriceInt = modelData.weekly_lunch_price;
                    }

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

                placeOrderprice.setText(String.valueOf(orderPriceInt + deliveryCharges));
                payatm.setText(paymentType);
                placeOrderprice.setText("Pay " + getResources().getString(R.string.rs_symbol) + String.valueOf(orderPriceInt + deliveryCharges));
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


             value = (int) valuep;


            priceValue = value * totalCount;
            order_price_basedQuantity.setText(getResources().getString(R.string.rs_symbol) + String.valueOf(priceValue));
             totalValueRs = priceValue;

            totalValue.setText(getResources().getString(R.string.rs_symbol) + String.valueOf(totalValueRs));

            placeOrderprice.setText(getResources().getString(R.string.rs_symbol) + String.valueOf(totalValueRs+deliveryCharges));
            order_price_finalTotal.setText(getResources().getString(R.string.rs_symbol) + String.valueOf(totalValueRs + 0));
            payatm.setText(paymentType);
            placeOrderprice.setText("Pay " + getResources().getString(R.string.rs_symbol) + String.valueOf(totalValueRs + 0+deliveryCharges));
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

            value = (int) valuep;

             priceValue = value * totalCount;

            order_price_basedQuantity.setText(getResources().getString(R.string.rs_symbol) + String.valueOf(priceValue));

            totalValueRs = priceValue;

            totalValue.setText(getResources().getString(R.string.rs_symbol) + String.valueOf(totalValueRs));

            placeOrderprice.setText(getResources().getString(R.string.rs_symbol) + String.valueOf(totalValueRs+deliveryCharges));
            payatm.setText(paymentType);
            placeOrderprice.setText("Pay " + getResources().getString(R.string.rs_symbol) + String.valueOf(totalValueRs+deliveryCharges));
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
                payatm.setText(paymentType );


            } else {
                pf.saveStringForKey("paymentType", "COD");
                paymentType = pf.getStringForKey("paymentType", "");
                payatm.setText(paymentType );

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @OnClick(R.id.paytm_linear)
    public void setPayatm()
    {
        paytmChecked.setVisibility(View.VISIBLE);
        changePaymentOption.setVisibility(View.GONE);
        scrollChange.setVisibility(View.VISIBLE);
        checkedCod.setVisibility(View.GONE);
        paytmChecked.setVisibility(View.GONE);

        try {
            if (location.equalsIgnoreCase("RepeatOrder")) {
                pf.saveStringForKey("paymentType", "Paytm");
                paymentType = pf.getStringForKey("paymentType", "");
                payatm.setText(paymentType );

            } else {
                pf.saveStringForKey("paymentType", "Paytm");
                paymentType = pf.getStringForKey("paymentType", "");
                payatm.setText(paymentType );

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @OnClick(R.id.paymentRelative)
    public void payatmOption() {


        try {
            if (location.equalsIgnoreCase("RepeatOrder")) {
                payatm.setText(paymentType);
            } else {
                payatm.setText(paymentType );
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
        } else  if(requestCode ==REQUEST_CHECK_SETTINGS_GPS){
                switch (resultCode) {
            case Activity.RESULT_OK:
                getMyLocation();
                break;
            case Activity.RESULT_CANCELED:
                finish();
                break;
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


        palceOrderViaMethod.setText(" It will take maximum 45 minutes to " +
                "deliver because Our Providers are housewives.\n Place Order ? ");


      /*  palceOrderViaMethod.setText("Your order will be placing via " + paymentType + " payment method" +'\n'+ " It will take maximum 1 hour to " +
                "deliver because Our Providers are housewives.\n Place Order ? ");
*/

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

       /* try {
          setUpGClient();
            getMyLocation();
        } catch (Exception e) {
            e.printStackTrace();
        }
*/


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
                orderModel.price = String.valueOf(((int)Float.parseFloat(modelData.price))+deliveryCharges);
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
                        //    showToast("Paytm Integration is in process you can place order using COD");
                            //paytmPlaceOrder();
                            orderViaCodMethod();
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
                    Intent userLoc = new Intent(YourCartActivity.this, YourCartActivity.class);
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

    private void newtowrkCallToplaceOrder(final OrderModel.Data orderModel, int itemCountText) {


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
                            sendEmail(orderModel.order_by,orderModel.chef_name,orderModel.order_for,orderModel.ordered_plates,orderModel.price,orderModel.landmark,orderModel.food_detail,
                                    orderModel.is_dinner,orderModel.is_lunch,orderModel.payment_status, orderModel.payment_type);

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

    private void sendEmail(int order_by, String chef_name, int order_for, int ordered_plates, String price, String landmark, String food_detail, int is_dinner, int is_lunch, String payment_status, String payment_type) {


        //Getting content for email
        String email = "ckd.khana24@gmail.com";
        String email1 = "ckd.khana12@gmail.com";
        String email2 = "ckd.khana@gmail.com";
        String subject = "Wake Up New Order Arrived";
        String message = "Order by this user Id - "+order_by+"\n"+"Chef Name - "+chef_name+"\n"+"Order for this chef UserId -"+order_for+"\n"
                +"Number of plates Ordered - "+ordered_plates+"\n"+"Price for this Order - "+price+"\n"+"Land Mark = "+landmark+"\n"
                +"Food Details - "+food_detail+"\n"+"Is Dinner -"+is_dinner+"\n"+"Is Lunch ="+is_lunch+"\n"+"Payment Method -"+payment_status
                +"\n"+"Payment Type -"+payment_type+"\n"+"URL-----"+"mummysfood.in/user/"+order_by;

        //Creating SendMail object
        SendMail sm = new SendMail(this, email, subject, message);

        //Executing sendmail to send email
        sm.execute();

        //Creating SendMail object
        SendMail sm1 = new SendMail(this, email1, subject, message);

        //Executing sendmail to send email
        sm1.execute();

        //Creating SendMail object
        SendMail sm2 = new SendMail(this, email2, subject, message);

        //Executing sendmail to send email
        sm2.execute();


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
    /*private void requestPermission() {
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
    }*/

    private void sendNotification() {


        OSPermissionSubscriptionState status = OneSignal.getPermissionSubscriptionState();
        String userId = status.getSubscriptionStatus().getUserId();
        boolean isSubscribed = status.getSubscriptionStatus().getSubscribed();

        if (!isSubscribed)
            return;

        try {
            JSONObject notificationContent = new JSONObject("{'contents': {'en': 'Your Order has placed.we will reach soon till then listen some songs'}," +
                    "'include_player_ids': ['" + userId + "'], " +
                    "'headings': {'en': 'MumizzFood'}, " +
                    "'big_picture': '"+foodImage+"'}");
            OneSignal.postNotification(notificationContent, null);
        } catch (JSONException e) {
            e.printStackTrace();
        }

     //   networkcallForBindu();

    }

    private void networkcallForBindu() {


        try {
            Call<ProfileModel> profileData = AppConstants.restAPI.getProfileUserData(35);
            profileData.enqueue(new Callback<ProfileModel>() {
                @Override
                public void onResponse(Call<ProfileModel> call, Response<ProfileModel> response) {

                    if (response != null){
                        ProfileModel res = response.body();
                        if (res.status != null) {
                            if ( res.status.equals(AppConstants.SUCCESS)){
                                try {
                                    JSONObject notificationContent = new JSONObject("{'contents': {'en': 'Bindu Wake UP Order arrived check Mail or db'}," +
                                            "'include_player_ids': ['" + res.data.get(0).player_id + "'], " +
                                            "'headings': {'en': 'MumizzFood'}, " +
                                            "'big_picture': '"+foodImage+"'}");
                                    OneSignal.postNotification(notificationContent, null);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                netWorkCallForSeema();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<ProfileModel> call, Throwable t) {
                    Log.e("error",""+t);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void netWorkCallForSeema()
    {
        try {
            Call<ProfileModel> profileData = AppConstants.restAPI.getProfileUserData(35);
            profileData.enqueue(new Callback<ProfileModel>() {
                @Override
                public void onResponse(Call<ProfileModel> call, Response<ProfileModel> response) {

                    if (response != null){
                        ProfileModel res = response.body();
                        if (res.status != null) {
                            if ( res.status.equals(AppConstants.SUCCESS)){

                                try {
                                    JSONObject notificationContent = new JSONObject("{'contents': {'en': 'Seema Wake UP Order arrived check Mail or db'}," +
                                            "'include_player_ids': ['" + res.data.get(0).player_id + "'], " +
                                            "'headings': {'en': 'MumizzFood'}, " +
                                            "'big_picture': '"+foodImage+"'}");
                                    OneSignal.postNotification(notificationContent, null);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<ProfileModel> call, Throwable t) {
                    Log.e("error",""+t);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //Get location While order

    public void locationBased(Double latitude, Double longitude) {

        Context contex = this;
        try {
           /* if (ActivityCompat.checkSelfPermission(this, mPermission)
                    != MockPackageManager.PERMISSION_GRANTED) {

                checkLocationPermission();
            }else {*/


            Geocoder geocoder;
            List<Address> addresses;

            geocoder = new Geocoder(this, Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1);

                if (addresses.size() != 0) {

                    String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

                  //  showToast(address);
                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();
                    String knownName = addresses.get(0).getFeatureName();
                    latitudeS = latitude;
                    lognitudeS = longitude;
                    pin_code = postalCode;



                    if (city.equalsIgnoreCase("Indore"))
                    {
                        pf.saveStringForKey("CurrentAddress", address);
                        pf.saveStringForKey("Address", address);
                        pf.saveDoubleForKey("latitude", latitude);
                        pf.saveDoubleForKey("lognitude", longitude);
                     /*   Intent enterOtherAct = new Intent(YourCartActivity.this, EnterFullAdressActivity.class);
                        enterOtherAct.putExtra("Address", address);
                        enterOtherAct.putExtra("city", city);
                        enterOtherAct.putExtra("lat", String.valueOf(latitude));
                        enterOtherAct.putExtra("long", String.valueOf(longitude));
                        enterOtherAct.putExtra("pincode", postalCode);
                        enterOtherAct.putExtra("state", state);
                        startActivity(enterOtherAct);*/
                    }else
                    {
                        pf.saveStringForKey("outofregion","Yes");
                        Intent i = new Intent(this,OutOfRegion.class);
                        startActivity(i);
                    }




                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            // }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }



    private synchronized void setUpGClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0, this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        mylocation = location;
        if (mylocation != null) {
            Double latitude = mylocation.getLatitude();
            Double longitude = mylocation.getLongitude();

            locationBased(latitude, longitude);

            //Or Do whatever you want with your location
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        checkPermissions();
    }

    @Override
    public void onConnectionSuspended(int i) {
        //Do whatever you need
        //You can display a message here
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        //You can display a message here
    }

    private void getMyLocation() {
        if (googleApiClient != null) {
            if (googleApiClient.isConnected()) {
                int permissionLocation = ContextCompat.checkSelfPermission(YourCartActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION);
                if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                    mylocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                    LocationRequest locationRequest = new LocationRequest();
                    locationRequest.setInterval(3000);
                    locationRequest.setFastestInterval(3000);
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                            .addLocationRequest(locationRequest);
                    builder.setAlwaysShow(true);
                    LocationServices.FusedLocationApi
                            .requestLocationUpdates(googleApiClient, locationRequest, this);
                    PendingResult<LocationSettingsResult> result =
                            LocationServices.SettingsApi
                                    .checkLocationSettings(googleApiClient, builder.build());
                    result.setResultCallback(new ResultCallback<LocationSettingsResult>() {

                        @Override
                        public void onResult(LocationSettingsResult result) {
                            final Status status = result.getStatus();
                            switch (status.getStatusCode()) {
                                case LocationSettingsStatusCodes.SUCCESS:
                                    // All location settings are satisfied.
                                    // You can initialize location requests here.
                                    int permissionLocation = ContextCompat
                                            .checkSelfPermission(YourCartActivity.this,
                                                    Manifest.permission.ACCESS_FINE_LOCATION);
                                    if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                                        mylocation = LocationServices.FusedLocationApi
                                                .getLastLocation(googleApiClient);
                                    }
                                    break;
                                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                    // Location settings are not satisfied.
                                    // But could be fixed by showing the user a dialog.
                                    try {
                                        // Show the dialog by calling startResolutionForResult(),
                                        // and check the result in onActivityResult().
                                        // Ask to turn on GPS automatically
                                        status.startResolutionForResult(YourCartActivity.this,
                                                REQUEST_CHECK_SETTINGS_GPS);
                                    } catch (IntentSender.SendIntentException e) {
                                        // Ignore the error.
                                    }
                                    break;
                                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                    // Location settings are not satisfied.
                                    // However, we have no way
                                    // to fix the
                                    // settings so we won't show the dialog.
                                    // finish();
                                    break;
                            }
                        }
                    });
                }
            }
        }
    }



    private void checkPermissions() {
        int permissionLocation = ContextCompat.checkSelfPermission(YourCartActivity.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(this,
                        listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            }
        } else {
            getMyLocation();
        }

    }

    @Override
    public void onPause() {
        super.onPause();

        try {
            googleApiClient.stopAutoManage(this);
            googleApiClient.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public void orderViaCodMethod() {

        final Dialog dialogd = new Dialog(this);
        dialogd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogd.setContentView(R.layout.remove_items_from_cart);


        CkdTextview placeOrderok = (CkdTextview) dialogd.findViewById(R.id.placeOrderok);
        CkdTextview palceOrderViaMethod = (CkdTextview) dialogd.findViewById(R.id.palceOrderViaMethod);
        CkdTextview notNow = (CkdTextview) dialogd.findViewById(R.id.notNow);
        //      LottieAnimationView lottieAnimationViewPlace = (LottieAnimationView) dialogd.findViewById(R.id.lottieAnimationViewPlace);
//        lottieAnimationViewPlace.playAnimation();

        notNow.setVisibility(View.GONE);

        placeOrderok.setText("Okay");


        palceOrderViaMethod.setText("Paytm integration is in process please select COD payment type for now.");

        placeOrderok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogd.dismiss();
             //   finish();
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
}