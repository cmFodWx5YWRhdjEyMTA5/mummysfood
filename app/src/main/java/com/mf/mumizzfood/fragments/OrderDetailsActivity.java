package com.mf.mumizzfood.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.mf.mumizzfood.location.EnterFullAdressActivity;
import com.mf.mumizzfood.R;
import com.mf.mumizzfood.activities.YourCartActivity;
import com.mf.mumizzfood.base.BaseActivity;
import com.mf.mumizzfood.data.db.DataBaseHelperNew;
import com.mf.mumizzfood.data.pref.PreferenceManager;
import com.mf.mumizzfood.models.HomeFeed;
import com.mf.mumizzfood.widgets.CkdTextview;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.mf.mumizzfood.widgets.DistanceCalculator;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.mf.mumizzfood.data.db.DataBaseHelperNew.TABLE_ADD_TO_CART;

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

    @BindView(R.id.onlyForToday)
    CkdTextview onlyForToday;


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

    @BindView(R.id.add_to_cart)
    CkdTextview add_to_cart;

    @BindView(R.id.add_to_cart_item_layout)
    LinearLayout add_to_cart_item_layout;


    private int orderId;
    private HomeFeed.Data data;
    private PreferenceManager pf;
    private PreferenceManager mainpf;
    private PreferenceManager userPf;
    private int monthlyValue;
    private int weeklyValue;
    private int priceOrgValue;
    private String UserCUrrentAdd = "";
    private Context context;
    private int item_quantity;

    private String typeOfPackage = "";
    private double totalDistance;

    private List<HomeFeed.Data> dModel;


    private int numberOfDays;
    private int isLunch = 1;
    private int isDinner;
    private String location = "";
    private String foodImage = "";


    public OrderDetailsActivity() {
        // Required empty public constructor
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.order_detail_layout);
        pf = new PreferenceManager(this, PreferenceManager.ORDER_PREFERENCES_FILE);
        userPf = new PreferenceManager(this, PreferenceManager.USER_ADDRESS);
        mainpf = new PreferenceManager(this);

        UserCUrrentAdd = userPf.getStringForKey("CurrentAddress", "");


        ButterKnife.bind(this);

        if (getIntent() != null) {

            try {
                orderId = getIntent().getIntExtra("order_id", 0);
                data = (HomeFeed.Data) getIntent().getSerializableExtra("data");

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                location = getIntent().getStringExtra("location");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        try {

            double latji = mainpf.getDoubleForKey("latitude", 22.7149);
            double longji = mainpf.getDoubleForKey("lognitude", 75.8899);

            DistanceCalculator distance = new DistanceCalculator();

            totalDistance = distance.greatCircleInKilometers(data.addresses.get(0).latitude, data.addresses.get(0).longitude, latji, longji);

        } catch (Exception e) {
            e.printStackTrace();
        }

        radioAction.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int selectedId) {
                if (selectedId == R.id.radioDinner) {
                    if (typeOfPackage.equalsIgnoreCase("weekly")) {
                        isDinner = 1;
                        isLunch = 0;
                        numberOfDays = 6;
                    } else if (typeOfPackage.equalsIgnoreCase("monthly")) {
                        isDinner = 1;
                        isLunch = 0;
                        numberOfDays = 30;
                    } else if (typeOfPackage.equalsIgnoreCase("today")) {
                        isDinner = 1;
                        isLunch = 0;
                        numberOfDays = 1;
                    }

                }
                if (selectedId == R.id.radioLunch) {

                    if (typeOfPackage.equalsIgnoreCase("weekly")) {
                        isLunch = 1;
                        isDinner = 0;
                        numberOfDays = 6;
                    } else if (typeOfPackage.equalsIgnoreCase("monthly")) {
                        isLunch = 1;
                        isDinner = 0;
                        numberOfDays = 30;
                    } else if (typeOfPackage.equalsIgnoreCase("today")) {
                        isDinner = 1;
                        isLunch = 0;
                        numberOfDays = 1;
                    }

                }

                if (selectedId == R.id.radioBoth) {
                    if (typeOfPackage.equalsIgnoreCase("weekly")) {
                        isDinner = 1;
                        isLunch = 1;
                        numberOfDays = 12;
                    } else if (typeOfPackage.equalsIgnoreCase("monthly")) {
                        isDinner = 1;
                        isLunch = 1;
                        numberOfDays = 60;
                    } else if (typeOfPackage.equalsIgnoreCase("today")) {
                        isDinner = 1;
                        isLunch = 1;
                        numberOfDays = 2;
                    }

                }
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

        setDefaultPackage();
    }

    private void setDefaultPackage() {
        typeOfPackage = "today";

        onlyForToday.setBackground(getResources().getDrawable(R.drawable.border_primary));
        weekly.setBackground(getResources().getDrawable(R.drawable.border_gray));
        monthly.setBackground(getResources().getDrawable(R.drawable.border_gray));


        dinnerPrice.setText(getResources().getString(R.string.rs_symbol) + String.valueOf(data.price));
        lunchPrice.setText(getResources().getString(R.string.rs_symbol) + String.valueOf(data.price));


        float valuep = Float.parseFloat(data.price);

        int value = (int) valuep;

        value = value + value;

        bothPrice.setText(getResources().getString(R.string.rs_symbol) + String.valueOf(value));

    }


    private void prepareOrderDetails() {


        try {
            //  Glide.with(this).load(data.profile_image).into(order_chef_profile_img);


            try {
                String chars = capitalize(data.user.name);
                order_chef_name.setText(chars);
            } catch (Exception e) {
                e.printStackTrace();
            }


            order_titile.setText(data.name);
            order_detail.setText(data.details);
            order_price.setText(getResources().getString(R.string.rs_symbol) + data.price);
            userDelAddress.setText(UserCUrrentAdd);


            double price = Double.parseDouble(data.price);

            priceOrgValue = (int) price;


            monthlyValue = priceOrgValue * 31;
            weeklyValue = priceOrgValue * 7;

            dinnerPrice.setText(getResources().getString(R.string.rs_symbol) + String.valueOf(data.weekly_dinner_price));
            lunchPrice.setText(getResources().getString(R.string.rs_symbol) + String.valueOf(data.weekly_lunch_price));
            bothPrice.setText(getResources().getString(R.string.rs_symbol) + String.valueOf(data.weekly_dinner_price + data.weekly_lunch_price));

            if (data.food_media.size() != 0) {
                try {
                    String imageUrl = "http://cdn.mummysfood.in/" + data.food_media.get(data.food_media.size()-1).media.name;
                    Log.d("ImageUrl", imageUrl);
                    Glide.with(this).load(imageUrl).into(order_image);

                    foodImage = imageUrl;
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            } else {
                order_image.setImageResource(R.mipmap.foodimage);
            }


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


    @OnClick(R.id.onlyForToday)
    public void onlyForToday() {

        typeOfPackage = "today";

        onlyForToday.setBackground(getResources().getDrawable(R.drawable.border_primary));
        weekly.setBackground(getResources().getDrawable(R.drawable.border_gray));
        monthly.setBackground(getResources().getDrawable(R.drawable.border_gray));


        dinnerPrice.setText(getResources().getString(R.string.rs_symbol) + String.valueOf(data.price));
        lunchPrice.setText(getResources().getString(R.string.rs_symbol) + String.valueOf(data.price));


        float valuep = Float.parseFloat(data.price);

        int value = (int) valuep;

        value = value + value;

        bothPrice.setText(getResources().getString(R.string.rs_symbol) + String.valueOf(value));

    }

    @OnClick(R.id.monthly)
    public void mothyClicked() {

        typeOfPackage = "monthly";

        monthly.setBackground(getResources().getDrawable(R.drawable.border_primary));
        weekly.setBackground(getResources().getDrawable(R.drawable.border_gray));
        onlyForToday.setBackground(getResources().getDrawable(R.drawable.border_gray));


        dinnerPrice.setText(getResources().getString(R.string.rs_symbol) + String.valueOf(data.monthly_dinner_price));
        lunchPrice.setText(getResources().getString(R.string.rs_symbol) + String.valueOf(data.monthly_lunch_price));
        bothPrice.setText(getResources().getString(R.string.rs_symbol) + String.valueOf(data.monthly_dinner_price + data.monthly_lunch_price));


    }


    @OnClick(R.id.weekly)
    public void weeklyCliked() {
        typeOfPackage = "weekly";

        monthly.setBackground(getResources().getDrawable(R.drawable.border_gray));
        weekly.setBackground(getResources().getDrawable(R.drawable.border_primary));
        onlyForToday.setBackground(getResources().getDrawable(R.drawable.border_gray));

        dinnerPrice.setText(getResources().getString(R.string.rs_symbol) + String.valueOf(data.weekly_dinner_price));
        lunchPrice.setText(getResources().getString(R.string.rs_symbol) + String.valueOf(data.weekly_lunch_price));
        bothPrice.setText(getResources().getString(R.string.rs_symbol) + String.valueOf(data.weekly_dinner_price + data.weekly_lunch_price));

    }


   /* @OnClick(R.id.sub_item)
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
    }*/


    @OnClick(R.id.addAddress)
    public void AddAddressValue() {

        Intent enterOtherAct = new Intent(this, EnterFullAdressActivity.class);
        enterOtherAct.putExtra("Address", UserCUrrentAdd);
        enterOtherAct.putExtra("From", "OrderDetails");
        startActivity(enterOtherAct);

    }

    @OnClick(R.id.backArrow)
    public void backArrow() {
        finish();
    }

    @OnClick(R.id.processedButton)
    public void processedButton() {
        DataBaseHelperNew db = new DataBaseHelperNew(this);

        if (location == null || "".equalsIgnoreCase(location)) {
            List<HomeFeed.Data> modelData = new ArrayList<>();

            modelData.add(data);

            try {
                db = new DataBaseHelperNew(this);
                dModel = db.getAddToCartItem();

                if (dModel != null) {
                    if (dModel.size() != 0) {
                        db.deleteTable(TABLE_ADD_TO_CART);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            db.insertAddToCart(modelData);
        }


        String paymentType = null;
        try {
            paymentType = mainpf.getStringForKey("paymentType", "");
            if ("".equalsIgnoreCase(paymentType)) {
                mainpf.saveStringForKey("paymentType", "Paytm");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("HH");
        String strDate = mdformat.format(calendar.getTime());
        ;

        SimpleDateFormat minuteFormat = new SimpleDateFormat("mm");
        String minuteFormatString = mdformat.format(calendar.getTime());
        ;


        int timeVlaue = Integer.parseInt(strDate);
        int minute = Integer.parseInt(minuteFormatString);


        if (typeOfPackage.equalsIgnoreCase("today")) {

         /*   if (timeVlaue <=20)
            {
                placeOrderDialog("Both");
            }else
            {*/
         if (timeVlaue>7)
         {
             if (isLunch == 1 && isDinner == 1) {
                 if (timeVlaue <= 13) {
                     setCartForPregress();

                 } else {
                     placeOrderDialog("Both");
                 }
             } else if (isDinner == 1) {
                 if (timeVlaue <= 20) {
                     if (timeVlaue == 20) {
                         if (minute > 01) {
                             showToast("You can place dinner request before 8PM");
                         } else {
                             setCartForPregress();
                         }
                     } else {
                         setCartForPregress();
                     }


                 } else {
                     placeOrderDialog("Dinner");
                 }
             } else if (isLunch == 1) {
                 if (timeVlaue <= 13) {
                     if (timeVlaue == 13) {
                         if (minute > 45) {
                             showToast("You can place lunch Request before 1:45 in Afternoon");
                         } else {
                             setCartForPregress();
                         }

                     } else {
                         setCartForPregress();
                     }

                 } else

                 {
                       /* if (timeVlaue <=20)
                        {
                            setCartForPregress();

                        }else
                        {*/
                     placeOrderDialog("Lunch");
                     //  }
                 }

             }
         }else
         {
             showToast("You can place order after 9 AM");
         }

        }else
        {
            if (timeVlaue <=8|| timeVlaue>=20)
            {
                showToast("You can place order after 9 AM");
            }else {
                setCartForPregress();
            }
        }


    }/*else
        {
            if (timeVlaue <=9|| timeVlaue>=20)
            {
                showToast("You can place order after 10 pm in morning");
            }else {
                setCartForPregress();
            }
        }*/




    private void setCartForPregress() {

        String outofregion = pf.getStringForKey("outofregion","");

        if (outofregion.equalsIgnoreCase("Yes"))
        {

            showToast("We are not available in your Area right now");
        }else
        {
            Intent yourCart = new Intent(this, YourCartActivity.class);
            yourCart.putExtra("data",data);

            yourCart.putExtra("typeOfPackage",typeOfPackage);
            yourCart.putExtra("isLunch",isLunch);
            yourCart.putExtra("isDinner",isDinner);
            yourCart.putExtra("foodImage",foodImage);
            yourCart.putExtra("totalDistance",totalDistance);

            yourCart.putExtra("numberOfDays",getRadioSelected());
            startActivity(yourCart);
        }

    }

    @Override
    public void updateAddressInterface(String address) {
        userDelAddress.setText(address);
    }

    @OnClick(R.id.order_pilot_profile)
    public void aVoid()
    {

        Intent profileIntent = new Intent(this,ProfileFragmentChef.class);
        profileIntent.putExtra("user_id",data.chef_detail.user_id);
        startActivity(profileIntent);
    }

    private int getRadioSelected() {


        int selectedId = radioAction.getCheckedRadioButtonId();



        if (selectedId== R.id.radioDinner)
        {
            if (typeOfPackage.equalsIgnoreCase("weekly"))
            {
                isDinner = 1;
                isLunch =0;
                numberOfDays = 7;
            }else if (typeOfPackage.equalsIgnoreCase("monthly"))
            {
                isDinner = 1;
                isLunch = 0;
                numberOfDays = 30;
            }else if (typeOfPackage.equalsIgnoreCase("today"))
            {
                isDinner = 1;
                isLunch = 0;
                numberOfDays = 1;
            }

        }
        if (selectedId == R.id.radioLunch)
        {

            if (typeOfPackage.equalsIgnoreCase("weekly"))
            {
                 isLunch = 1;
                 isDinner =0;
                numberOfDays = 7;
            }else if (typeOfPackage.equalsIgnoreCase("monthly"))
            {
                isLunch = 1;
                isDinner =0;
                numberOfDays = 30;
            }else if (typeOfPackage.equalsIgnoreCase("today"))
            {
                isDinner = 0;
                isLunch = 1;
                numberOfDays = 1;
            }

        }

        if (selectedId == R.id.radioBoth)
        {
            if (typeOfPackage.equalsIgnoreCase("weekly"))
            {
                isDinner = 1;
                isLunch = 1;
                numberOfDays = 14;
            }else if (typeOfPackage.equalsIgnoreCase("monthly"))
            {
                isDinner = 1;
                isLunch = 1;
                numberOfDays = 60;
            }else if (typeOfPackage.equalsIgnoreCase("today"))
            {
                isDinner = 1;
                isLunch = 1;
                numberOfDays = 2;
            }

        }

          return numberOfDays;
    }

    public void placeOrderDialog(String type) {

        final Dialog dialogd = new Dialog(this);
        dialogd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogd.setContentView(R.layout.dialog_place_order);


        CkdTextview placeOrderok = (CkdTextview) dialogd.findViewById(R.id.placeOrderok);
        CkdTextview palceOrderViaMethod = (CkdTextview) dialogd.findViewById(R.id.palceOrderViaMethod);
        CkdTextview notNow = (CkdTextview) dialogd.findViewById(R.id.notNow);
        LottieAnimationView lottieAnimationViewPlace = (LottieAnimationView) dialogd.findViewById(R.id.lottieAnimationViewPlace);

        lottieAnimationViewPlace.setAnimation("crying.json");
        lottieAnimationViewPlace.playAnimation();

        placeOrderok.setVisibility(View.GONE);

        String msg = "";

        if (type.equalsIgnoreCase("Dinner"))
        {
            msg = "We take dinner request before 8PM for one day subscription.\n" +
                    "You can place lunch request in morning after 9.Thank you for understanding, Always be with us.";
        }else if (type.equalsIgnoreCase("Lunch"))
        {
            msg = "Sorry It's too late for both. Now you can place dinner request only for today." +
                    "Thank you for understanding, Always be with us.";

        }else
        {
            msg = "We take lunch request after 9 AM.\n" +
                    "You can place dinner request in evening before 8 PM.Thank you for understanding, Always be with us.";
        }

        palceOrderViaMethod.setText(msg);

        placeOrderok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogd.dismiss();

            }
        });

        notNow.setText("Okay");

        notNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogd.dismiss();

            }
        });

        dialogd.show();
    }


    private String capitalize(String capString){
        StringBuffer capBuffer = new StringBuffer();
        Matcher capMatcher = Pattern.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(capString);
        while (capMatcher.find()){
            capMatcher.appendReplacement(capBuffer, capMatcher.group(1).toUpperCase() + capMatcher.group(2).toLowerCase());
        }

        return capMatcher.appendTail(capBuffer).toString();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
