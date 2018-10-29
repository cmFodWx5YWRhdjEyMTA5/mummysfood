package in.mummysfood.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.io.IOException;
import java.text.ParseException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.mummysfood.R;
import in.mummysfood.base.BaseActivity;
import in.mummysfood.data.pref.PreferenceManager;
import in.mummysfood.models.SubscribtionModel;
import in.mummysfood.models.UserProfileModel;
import in.mummysfood.utils.AppConstants;
import in.mummysfood.widgets.CkdTextview;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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


    private UserProfileModel.Orders orders;

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
            orders = (UserProfileModel.Orders) getIntent().getSerializableExtra("order");

            remainPlates = getIntent().getIntExtra("remainingPlates",0);


            try {
                mobile = getIntent().getIntExtra("mobile",0);
            } catch (Exception e) {
                e.printStackTrace();
            }

            payment_type_value.setText(orders.price);

            try {
                order_created_value.setText(getDate(orders.created_at));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            order_id_value.setText(String.valueOf(orders.id));

            PhoneNUm_value.setText(mobileNumber);

            delivery_Add_value.setText(userName+" at "+orders.address_type +" Location :"+orders.landmark);
        }

        if (remainPlates != 0)
        {
            yourorderDetails.setText("It's Active Order");
            OrderActive.setVisibility(View.GONE);
            SkipOrderForTodayLayout.setVisibility(View.VISIBLE);
            RemainingPlates_value.setText(String.valueOf(remainPlates));
        }else
        {
            yourorderDetails.setText("Order Details");
            OrderActive.setText("Repeat order");
            OrderActive.setVisibility(View.VISIBLE);
            SkipOrderForTodayLayout.setVisibility(View.GONE);
            remainingLayout.setVisibility(View.GONE);
        }
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
            yourCart.putExtra("data",orders);
            if (orders.number_of_days == 1)
            {
                yourCart.putExtra("typeOfPackage","today");
            }else if (orders.number_of_days <= 7)
            {
                yourCart.putExtra("typeOfPackage","weekly");
            }else if (orders.number_of_days <=15 )
            {
                yourCart.putExtra("typeOfPackage","montly");
            }

            yourCart.putExtra("isLunch",orders.is_lunch);
            yourCart.putExtra("isDinner",orders.is_dinner);
            yourCart.putExtra("numberOfDays",orders.number_of_days);
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

    /*private void updateOrder(int remainingPlates) {

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

    }*/

}
