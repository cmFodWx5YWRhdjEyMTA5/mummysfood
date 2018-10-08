package in.mummysfood.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.mummysfood.R;
import in.mummysfood.base.BaseActivity;
import in.mummysfood.data.pref.PreferenceManager;
import in.mummysfood.models.UserProfileModel;
import in.mummysfood.widgets.CkdTextview;

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


        if (getIntent() != null)
        {
            orders = (UserProfileModel.Orders) getIntent().getSerializableExtra("order");

            remainPlates = getIntent().getIntExtra("remainingPlates",0);


            try {
                mobile = getIntent().getIntExtra("mobile",0);
            } catch (Exception e) {
                e.printStackTrace();
            }

            payment_type_value.setText(orders.payment_type);

            order_created_value.setText(String.valueOf(orders.created_at));

            order_id_value.setText(String.valueOf(orders.id));

            PhoneNUm_value.setText(String.valueOf(mobile));

            delivery_Add_value.setText(orders.landmark);
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
            yourorderDetails.setText("Order Details");
            OrderActive.setText("Repeat Order");
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
}
