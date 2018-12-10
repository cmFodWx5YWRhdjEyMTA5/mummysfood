package in.ckd.calenderkhanado.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.airbnb.lottie.LottieAnimationView;
import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.ckd.calenderkhanado.location.EnterFullAdressActivity;
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
    RelativeLayout scrollChange;

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

    private DashBoardModel.Data modelData;
    private DashBoardModel.Food_detail model;
    PreferenceManager pfUName;
    PreferenceManager pfUMobile;
    PreferenceManager pfUAddress;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_cart);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

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
                modelData = (DashBoardModel.Data) getIntent().getSerializableExtra("data");
                typeOfPackage = getIntent().getStringExtra("typeOfPackage");
                numberOfDays = getIntent().getIntExtra("numberOfDays", 0);
                isDinner = getIntent().getIntExtra("isDinner", 0);
                isLunch = getIntent().getIntExtra("isLunch", 0);
                foodImage = getIntent().getStringExtra("foodImage");

            }
        }

        checkPaymentOption();

        if (typeOfPackage.equalsIgnoreCase("today")) {

            if (isLunch == 1 && isDinner == 1)
            {
                add_to_cart_item_layout.setVisibility(View.GONE);
                order_price_basedQuantity.setVisibility(View.GONE);
            }else
            {
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
            pfUAddress = new PreferenceManager(this);
            pfUMobile = new PreferenceManager(this, PreferenceManager.USER_MOBILE);
            pf = new PreferenceManager(this);

            try {
                personInfo.setText(pf.getStringForKey("Username", "") + "  " + pf.getStringForKey("Mobile", ""));
            } catch (Exception e) {
                e.printStackTrace();
            }

            userAdd = pfUAddress.getStringForKey("CurrentAddress", "");

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
                order_price.setText(getResources().getString(R.string.rs_symbol) + ordersSub.orders.get(0).price);


                order_price_basedQuantity.setText(getResources().getString(R.string.rs_symbol) + ordersSub.orders.get(0).price);
                totalValue.setText(getResources().getString(R.string.rs_symbol) + ordersSub.orders.get(0).price);
                order_taxes.setText(getResources().getString(R.string.rs_symbol) + String.valueOf(50));
                placeOrderprice.setText(getResources().getString(R.string.rs_symbol) + ordersSub.orders.get(0).price);
                payatm.setText(paymentType + " " + getResources().getString(R.string.rs_symbol) + ordersSub.orders.get(0).price);
                placeOrderprice.setText("Pay " + getResources().getString(R.string.rs_symbol) + ordersSub.orders.get(0).price);
                payatmOption.setText("Pay " + getResources().getString(R.string.rs_symbol) + ordersSub.orders.get(0).price);
                order_taxes.setText(getResources().getString(R.string.rs_symbol) + String.valueOf(50));

            } else {
                float orderPrice = Float.parseFloat(modelData.food_detail.get(0).price);
                int orderPriceInt = 0;
                orderPriceInt = (int) orderPrice;
                if (typeOfPackage.equalsIgnoreCase("monthly")) {
                    orderPriceInt = orderPriceInt * numberOfDays;
                } else if (typeOfPackage.equalsIgnoreCase("weekly")) {
                    orderPriceInt = orderPriceInt * numberOfDays;
                }else if (typeOfPackage.equalsIgnoreCase("today")) {
                    if (isDinner ==1 &&isLunch ==1)
                    {
                        orderPriceInt = orderPriceInt * numberOfDays;
                        orderPriceInt = orderPriceInt+orderPriceInt;
                    }else
                    {
                        orderPriceInt = orderPriceInt * numberOfDays;
                    }

                }

                order_titile.setText(modelData.food_detail.get(0).name);
                order_price.setText(getResources().getString(R.string.rs_symbol) + modelData.food_detail.get(0).price);
                order_price_basedQuantity.setText(getResources().getString(R.string.rs_symbol) + modelData.food_detail.get(0).price);
                order_price_finalTotal.setText(getResources().getString(R.string.rs_symbol) + String.valueOf(orderPriceInt + modelData.food_detail.get(0).taxes));
                totalValue.setText(getResources().getString(R.string.rs_symbol) + String.valueOf(orderPriceInt + modelData.food_detail.get(0).taxes));


                placeOrderprice.setText(String.valueOf(orderPriceInt + modelData.food_detail.get(0).taxes));
                payatm.setText(paymentType + " " + getResources().getString(R.string.rs_symbol) + String.valueOf(orderPriceInt + modelData.food_detail.get(0).taxes));
                placeOrderprice.setText("Pay " + getResources().getString(R.string.rs_symbol) + String.valueOf(orderPriceInt + modelData.food_detail.get(0).taxes));
                payatmOption.setText("Pay " + getResources().getString(R.string.rs_symbol) + String.valueOf(orderPriceInt + modelData.food_detail.get(0).taxes));
                order_taxes.setText(getResources().getString(R.string.rs_symbol) + modelData.food_detail.get(0).taxes);

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
        int itemTaxesText = Integer.parseInt(String.valueOf(modelData.food_detail.get(0).taxes));


        if (itemCountText > 1) {
            int totalCount = itemCountText - 1;
            item_count.setText(String.valueOf(totalCount));
            if (location.equalsIgnoreCase("RepeatOrder")) {
                valuep = Float.parseFloat(ordersSub.orders.get(0).price);
            } else {
                valuep = Float.parseFloat(modelData.food_detail.get(0).price);
            }


            int value = (int) valuep;



            int priceValue = value * totalCount;
            order_price_basedQuantity.setText(getResources().getString(R.string.rs_symbol) + String.valueOf(priceValue));
            int totalValueRs = priceValue;

            totalValue.setText(getResources().getString(R.string.rs_symbol) + String.valueOf(totalValueRs));

            placeOrderprice.setText(getResources().getString(R.string.rs_symbol) + String.valueOf(totalValueRs));
            order_price_finalTotal.setText(getResources().getString(R.string.rs_symbol) + String.valueOf(totalValueRs + modelData.food_detail.get(0).taxes));
            payatm.setText(paymentType + " " + getResources().getString(R.string.rs_symbol) + String.valueOf(totalValueRs + modelData.food_detail.get(0).taxes));
            placeOrderprice.setText("Pay " + getResources().getString(R.string.rs_symbol) + String.valueOf(totalValueRs + modelData.food_detail.get(0).taxes));
            payatmOption.setText("Pay " + getResources().getString(R.string.rs_symbol) + String.valueOf(totalValueRs + modelData.food_detail.get(0).taxes));

        }
    }

    @OnClick(R.id.add_item)
    public void add_item() {
        float valuep;
        int itemCountText = Integer.parseInt(item_count.getText().toString());
//        int itemTaxesText = Integer.parseInt(String.valueOf(modelData.food_detail.get(0).taxes));
        int totalCount = itemCountText + 1;
        item_count.setText(String.valueOf(totalCount));

        if (location.equalsIgnoreCase("RepeatOrder")) {
            valuep = Float.parseFloat(ordersSub.orders.get(0).price);
        } else {
            valuep = Float.parseFloat(modelData.food_detail.get(0).price);
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
                payatm.setText(paymentType + " " + getResources().getString(R.string.rs_symbol) + modelData.food_detail.get(0).price);

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
                payatm.setText(paymentType + " " + getResources().getString(R.string.rs_symbol) + modelData.food_detail.get(0).price);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @OnClick(R.id.payatmOption)
    public void payatmOption() {


        try {
            if (location.equalsIgnoreCase("RepeatOrder")) {
                payatm.setText(paymentType + " " + getResources().getString(R.string.rs_symbol) + getResources().getString(R.string.rs_symbol) + ordersSub.orders.get(0).price);
            } else {
                payatm.setText(paymentType + " " + getResources().getString(R.string.rs_symbol) + modelData.food_detail.get(0).price);
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
       /* AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Place Order")
                .setCancelable(false)
                .setNegativeButton(R.string.no_txt, new DialogInterface.OnClickListener() {
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
        alert.show();*/

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
                        } else {
                            newtowrkCallToplaceOrder(orderModel, itemCountText);
                        }
                    } else {
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

                if ("".equalsIgnoreCase(paymetTYpe))
                {
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
                orderModel.food_detail = modelData.food_detail.get(0).details;
                orderModel.food_name = modelData.food_detail.get(0).name;

                orderModel.street = CurrentAddress;
                orderModel.city = "indore";
                orderModel.state = "MP";

                orderModel.price = modelData.food_detail.get(0).price;
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


                if ("".equalsIgnoreCase(landmarkEdit))
                {
                    orderModel.landmark = CurrentAddress;
                }else
                {
                    orderModel.landmark = landmarkEdit;
                }



                if (CurrentAddress != null && !"".equalsIgnoreCase(CurrentAddress)) {


                    if (paymentType != null && !"".equalsIgnoreCase(paymentType)) {
                        if (paymentType.equalsIgnoreCase("Paytm")) {
                            showToast("Paytm Integration is in process you can place order using COD");
                        } else {
                            newtowrkCallToplaceOrder(orderModel, itemCountText);
                        }
                    } else {
                        showToast("This order has placed via COD payment method");
                        newtowrkCallToplaceOrder(orderModel, itemCountText);
                    }

                } else {

                    showToast("add your address");
                    Intent userLoc = new Intent(YourCartActivity.this, EnterFullAdressActivity.class);
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

                            sendNotification();

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