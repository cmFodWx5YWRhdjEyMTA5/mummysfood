package com.mf.mumizzfood.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.Scanner;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.mf.mumizzfood.R;
import com.mf.mumizzfood.base.BaseActivity;
import com.mf.mumizzfood.data.pref.PreferenceManager;
import com.mf.mumizzfood.models.UserProfileModel;
import com.mf.mumizzfood.utils.AppConstants;
import com.mf.mumizzfood.widgets.CkdTextview;

/**
 * Created by acer on 10/4/2018.
 */

public class ActiveOrderActivtiy extends BaseActivity
{
    @BindView(R.id.delivery_Add_value)
    CkdTextview delivery_Add_value;
    @BindView(R.id.order_id_value)
    CkdTextview order_id_value;

    @BindView(R.id.paymentType)
    CkdTextview paymentType;
    @BindView(R.id.payment_type_value)
    CkdTextview payment_type_value;
    @BindView(R.id.PhoneNUm_value)
    CkdTextview PhoneNUm_value;
    @BindView(R.id.order_created_value)
    CkdTextview order_created_value;

    @BindView(R.id.OrderActive)
    CkdTextview OrderActive;

    @BindView(R.id.yourorderDetails)
    CkdTextview yourorderDetails;

    @BindView(R.id.remainingLayout)
    LinearLayout remainingLayout;

    @BindView(R.id.RemainingPlates_value)
    CkdTextview RemainingPlates_value;

    @BindView(R.id.SkipOrderForTodayLayout)
    RelativeLayout SkipOrderForTodayLayout;

    @BindView(R.id.lunchSkip)
    CkdTextview lunchSkip;

    @BindView(R.id.skipDinner)
    CkdTextview skipDinner;


    @BindView(R.id.skipBoth)
    CkdTextview skipBoth;


    private UserProfileModel.Subscribes ordersSubs;

    private int mobile;

    private int remainPlates =0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.active_orders);

        ButterKnife.bind(this);

        pf = new PreferenceManager(this);
        String userName  =     pf.getStringForKey("Username","");
        String mobileNumber = pf.getStringForKey("Mobile","");


        if (getIntent() != null)
        {
            ordersSubs = (UserProfileModel.Subscribes) getIntent().getSerializableExtra("order");

            remainPlates = getIntent().getIntExtra("remainingPlates",0);

            try {
                mobile = getIntent().getIntExtra("mobile",0);
            } catch (Exception e) {
                e.printStackTrace();
            }

           if (ordersSubs.orders.get(0).is_dinner == 1 && ordersSubs.orders.get(0).is_lunch==1)
                {

                    payment_type_value.setText(String.valueOf(ordersSubs.number_of_days * Integer.parseInt(ordersSubs.orders.get(0).price))+" Lunch and dinner both for "+String.valueOf(ordersSubs.number_of_days));
                }else {

                if (ordersSubs.number_of_days == 1)
                {
                    payment_type_value.setText(ordersSubs.ordered_plates * Integer.parseInt(ordersSubs.orders.get(0).price)+ " for  "+String.valueOf(ordersSubs.number_of_days)+ " days");

                }else
                    {
                        payment_type_value.setText(String.valueOf(ordersSubs.number_of_days * Integer.parseInt(ordersSubs.orders.get(0).price))+" for "+String.valueOf(ordersSubs.number_of_days));

                    }
                }


            try {
                order_created_value.setText(getDate(ordersSubs.created_at));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            order_id_value.setText(String.valueOf(ordersSubs.id));

            PhoneNUm_value.setText(mobileNumber);

            delivery_Add_value.setText(userName+" at "+ordersSubs.orders.get(0).landmark);
        }

        if (remainPlates != 0)
        {
            yourorderDetails.setText("It's Active Order");
            OrderActive.setVisibility(View.GONE);
            SkipOrderForTodayLayout.setVisibility(View.GONE);
            RemainingPlates_value.setText(String.valueOf(remainPlates));
        }else
        {
            yourorderDetails.setText("Order Details");
            OrderActive.setText("Repeat order");
            OrderActive.setVisibility(View.VISIBLE);
            SkipOrderForTodayLayout.setVisibility(View.GONE);
            remainingLayout.setVisibility(View.GONE);
        }


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
    }

    @OnClick(R.id.OrderActive)
    public void OrderActive()
    {
        if (remainPlates != 0)
        {
   /*         Intent yourCart = new Intent(this, YourCartActivity.class);
            yourCart.putExtra("data",orders);
            yourCart.putExtra("typeOfPackage",typeOfPackage);
            yourCart.putExtra("isLunch",isLunch);
            yourCart.putExtra("isDinner",isDinner);
            yourCart.putExtra("numberOfDays",numberOfDays);*/
           // startActivity(yourCart);
            finish();
        }else
        {
            Intent yourCart = new Intent(this, YourCartActivity.class);
            yourCart.putExtra("data",ordersSubs);
            if (ordersSubs.number_of_days == 1)
            {
                yourCart.putExtra("typeOfPackage","today");
            }else if (ordersSubs.number_of_days <= 7)
            {
                yourCart.putExtra("typeOfPackage","weekly");
            }else if (ordersSubs.number_of_days <=15 )
            {
                yourCart.putExtra("typeOfPackage","montly");
            }

            yourCart.putExtra("isLunch",ordersSubs.orders.get(0).is_lunch);
            yourCart.putExtra("isDinner",ordersSubs.orders.get(0).is_dinner);
            yourCart.putExtra("numberOfDays",ordersSubs.number_of_days);
            yourCart.putExtra("From","RepeatOrder");
            startActivity(yourCart);
            finish();
        }
    }


    @OnClick(R.id.help)
    public void OnHelp()
    {

        showToast("Send as email at supportmummysfood@gmail.com");
    }




    @OnClick(R.id.lunchSkip)
    public void setLunchSkip()
    {
        showToast("Lunch Skipped");
    }

    @OnClick(R.id.skipDinner)
    public void setskipDinner()
    {
        showToast("Dinner Skipped");
        sendNotificationForSkippedDinner("Dinner");

    }

    private void sendNotificationForSkippedDinner(String dinner) {
        try {
            String jsonResponse;

            URL url = new URL("https://onesignal.com/api/v1/notifications");
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setUseCaches(false);
            con.setDoOutput(true);
            con.setDoInput(true);

            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setRequestProperty("Authorization", "Basic NGEwMGZmMjItY2NkNy0xMWUzLTk5ZDUtMDAwYzI5NDBlNjJj");
            con.setRequestMethod("POST");

            String strJsonBody = "{"
                    +   "\"app_id\": "+AppConstants.ONESINGAL +","
                    +   "\"included_segments\": [\"All\"],"
                    +   "\"data\": {\"foo\": \"bar\"},"
                    +   "\"contents\": {\"en\": \"English Message\"}"
                    + "}";
            System.out.println("strJsonBody:\n" + strJsonBody);

            byte[] sendBytes = strJsonBody.getBytes("UTF-8");
            con.setFixedLengthStreamingMode(sendBytes.length);

            OutputStream outputStream = con.getOutputStream();
            outputStream.write(sendBytes);

            int httpResponse = con.getResponseCode();
            System.out.println("httpResponse: " + httpResponse);

            if (  httpResponse >= HttpURLConnection.HTTP_OK
                    && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
                Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
                jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                scanner.close();
            }
            else {
                Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
                jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                scanner.close();
            }
            System.out.println("jsonResponse:\n" + jsonResponse);

        } catch(Throwable t) {
            t.printStackTrace();
        }
    }

    @OnClick(R.id.skipBoth)
    public void setskipBoth()
    {
         showToast("Both Skipped");
    }

    @OnClick(R.id.backArrow)
    public void OnbackArrow()
    {
        finish();
  }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }



/*
    private void updateOrder(int remainingPlates) {

        if (remainingPlates != 0)
        {

          int  orderedPlate   =remainingPlates -1;

            subscribtionModel.ordered_plates = orderedPlate;

            Call<SubscribtionModel> subModelUpdate = AppConstants.restAPI.updateSubscribeOrder(subsribeId,subscribtionModel);


            subModelUpdate.enqueue(new Callback<SubscribtionModel>() {
                @Override
                public void onResponse(Call<SubscribtionModel> call, Response<SubscribtionModel> response) {
                    if (response.isSuccessful())
                    {
                        if (response != null)
                        {
                            showToast("order Update");
                        }else
                        {
                            showToast("response null");

                        }
                    }else {
                        try {
                            Log.d("Response",response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<SubscribtionModel> call, Throwable t) {
                    showToast("response falier");

                }
            });

        }else {
            showToast("This order has completed");
        }

    }
*/

}
