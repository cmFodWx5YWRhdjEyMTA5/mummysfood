package in.mummysfood.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.mummysfood.R;
import in.mummysfood.base.BaseActivity;
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

    private UserProfileModel.Orders orders;

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

            payment_type_value.setText(orders.payment_type);

            order_created_value.setText(String.valueOf(orders.created_at));

            order_id_value.setText(String.valueOf(orders.id));

            PhoneNUm_value.setText(String.valueOf(882837677));

            delivery_Add_value.setText("KHajrana");
        }

        if (remainPlates != 0)
        {
            yourorderDetails.setText("It's Active Order");
            OrderActive.setText("CanCel Order");
        }else
        {
            yourorderDetails.setText("Order Details");
            OrderActive.setText("Repeat Order");
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
