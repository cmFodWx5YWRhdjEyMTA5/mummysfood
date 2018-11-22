package in.ckd.calenderkhanado.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.ckd.calenderkhanado.R;
import in.ckd.calenderkhanado.base.BaseActivity;

public class ManagePaymentActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.cod_option_layout)
    RelativeLayout codOptionLayout;

    @BindView(R.id.cod_check_img)
    ImageView codCheckImg;

    @BindView(R.id.paytm_option_layout)
    RelativeLayout paytmOptionLayout;

    @BindView(R.id.paytm_check_img)
    ImageView paytmCheckImg;

    private int active_payment_option = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_payment);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        toolbar.setTitle(getString(R.string.manage_address));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        checkPaymentOption();
    }

    private void checkPaymentOption() {
        if (active_payment_option == 0){
            paytmOptionLayout.setVisibility(View.VISIBLE);
            paytmCheckImg.setVisibility(View.GONE);
        }else if (active_payment_option == 1){
            paytmOptionLayout.setVisibility(View.GONE);
            paytmCheckImg.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.cod_option_layout)
    public void CodOptionActive (){
        active_payment_option = 0;
        checkPaymentOption();
    }

    @OnClick(R.id.paytm_option_layout)
    public void PaytmOptionActive (){
        active_payment_option = 1;
        checkPaymentOption();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
