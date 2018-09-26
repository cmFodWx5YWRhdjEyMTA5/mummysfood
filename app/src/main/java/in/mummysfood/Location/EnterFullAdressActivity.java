package in.mummysfood.Location;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import in.mummysfood.R;
import in.mummysfood.activities.MainBottomBarActivity;
import in.mummysfood.activities.YourCartActivity;
import in.mummysfood.base.BaseActivity;
import in.mummysfood.data.pref.PreferenceManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class EnterFullAdressActivity extends BaseActivity {


    @BindView(R.id.locationMain)
    EditText locationMain;

    @BindView(R.id.saveUs)
    Button saveUs;

    private  String address;
    private String OrderDetails = "";
    private PreferenceManager pf;

    private updateAdd listner;

    public interface updateAdd{

        void updateAddressInterface(String address);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_enter_full_adress);
        ButterKnife.bind(this);

        pf = new PreferenceManager(this,PreferenceManager.USER_ADDRESS);

        if (getIntent() != null) {

         address   = getIntent().getStringExtra("Address");

            try {
                OrderDetails   = getIntent().getStringExtra("From");
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                locationMain.setText(address);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @OnClick(R.id.saveUs)
    public void setSaveUs()
    {

        String updatedText= locationMain.getText().toString();
        pf.saveStringForKey("CurrentAddress",updatedText);

        if (OrderDetails != null &&!"".equalsIgnoreCase(OrderDetails)&&OrderDetails.equalsIgnoreCase("OrderDetails"))
        {
            /*Intent mainIntent = new Intent(EnterFullAdressActivity.this, MainBottomBarActivity.class);

            startActivity(mainIntent);
            finish();*/


    //        listner.updateAdd(address);
          //  listner.updateAddressInterface(updatedText);

            Intent output = new Intent();
            output.putExtra("Address", updatedText);
            setResult(RESULT_OK, output);
            finish();

        }else
        {
            Intent mainIntent = new Intent(EnterFullAdressActivity.this, MainBottomBarActivity.class);

            startActivity(mainIntent);
            finish();
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
