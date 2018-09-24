package in.mummysfood.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.firebase.auth.UserInfo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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
            placeOrderprice.setTextColor(getResources().getColor(R.color.white));
            placeOrderButtonCheckout.setTextColor(getResources().getColor(R.color.white));
        }
    }

    @OnClick(R.id.infoChange)
    public void inFochage()
    {

    }


    @OnClick(R.id.addressChange)
    public void addressChange()
    {

    }

    @OnClick(R.id.patmentChange)
    public void patmentChange()
    {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
