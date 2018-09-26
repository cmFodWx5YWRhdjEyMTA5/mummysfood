package in.mummysfood.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.google.firebase.auth.UserInfo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.mummysfood.Location.EnterFullAdressActivity;
import in.mummysfood.R;
import in.mummysfood.base.BaseActivity;
import in.mummysfood.data.pref.PreferenceManager;
import in.mummysfood.models.DashBoardModel;
import in.mummysfood.widgets.CkdTextview;

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

    private String userAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_cart);

         ButterKnife.bind(this);

        if (getIntent() != null)
        {
            modelData = (DashBoardModel.Data) getIntent().getSerializableExtra("data");
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
            order_taxes.setText(modelData.food_detail.price);
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
}
