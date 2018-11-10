package in.mummysfood.Location;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import java.io.IOException;

import in.mummysfood.R;
import in.mummysfood.activities.MainBottomBarActivity;
import in.mummysfood.base.BaseActivity;
import in.mummysfood.data.pref.PreferenceManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.mummysfood.models.AddressModel;
import in.mummysfood.utils.AppConstants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static in.mummysfood.fragments.ProfileFragment.addressesList;


public class EnterFullAdressActivity extends BaseActivity {


    @BindView(R.id.locationMain)
    EditText locationMain;

    @BindView(R.id.saveUs)
    Button saveUs;


    @BindView(R.id.radioTypeAction)
    RadioGroup radioAction;

    @BindView(R.id.landMark)
    EditText landMark;

    @BindView(R.id.flatNo)
    EditText flatNo;

    private  String address;
    private  String city;
    private  String latitude;
    private  String postalCode;
    private  String longitude;
    private  String state;
    private String OrderDetails = "";
    private String AddNew = "";
    private int Position;
    private PreferenceManager pf;
    private String updatedText = "";
    private int user_id;


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
         city   = getIntent().getStringExtra("city");
         latitude   = getIntent().getStringExtra("lat");
         longitude   = getIntent().getStringExtra("long");
         state   = getIntent().getStringExtra("state");
         postalCode   = getIntent().getStringExtra("postalCode");

            try {
                OrderDetails   = getIntent().getStringExtra("From");
                String landMarkString   = getIntent().getStringExtra("landMark");
                String flatNoString = getIntent().getStringExtra("flatNo");

                  landMark.setText(landMarkString);
                  flatNo.setText(flatNoString);

            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Position   = getIntent().getIntExtra("Position",0);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                locationMain.setText(address);
            } catch (Exception e) {
                e.printStackTrace();
            }


            try {
                AddNew = getIntent().getStringExtra("AddNew");
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

    @OnClick(R.id.saveUs)
    public void setSaveUs()
    {

        updatedText   = locationMain.getText().toString();


        if (OrderDetails != null &&!"".equalsIgnoreCase(OrderDetails)&&OrderDetails.equalsIgnoreCase("OrderDetails"))
        {

            Intent output = new Intent();
            output.putExtra("Address", updatedText);
            output.putExtra("landMark", landMark.getText().toString());
            output.putExtra("flatNo", flatNo.getText().toString());
            setResult(RESULT_OK, output);
            finish();
            networkCallForAsavingAddress("Update");
        }else
        {

            networkCallForAsavingAddress("Save");

        }


    }

    private void networkCallForAsavingAddress(final String action) {
        pf = new PreferenceManager(this,PreferenceManager.LOGIN_PREFERENCES_FILE);

      //  user_id = pf.getIntForKey(PreferenceManager.USER_ID,0);
        user_id = 1;

        String landMarkText = landMark.getText().toString();
        String flatNoText = flatNo.getText().toString();
        Call<AddressModel>addressModelCall;

        AddressModel.Data data = new AddressModel.Data();

        data.city = city;
        data.state = state;
        data.pin= postalCode;


        try {

            data.latitude = Double.parseDouble(latitude);
            data.longitude= Double.parseDouble(longitude);
        } catch (NumberFormatException e) {
            e.printStackTrace();

         //   String address =      pf.getStringForKey("CurrentAddress","");
            data.latitude = pf.getDoubleForKey("latitude",0);
            data.longitude = pf.getDoubleForKey ("lognitude",0);
        }

        data.type = getRadioSelected();
        data.landmark = landMarkText;
        data.house_no = flatNoText;
        data.user_id = user_id;
        data.complete_address = address;


        if (action.equalsIgnoreCase("Save"))
        {
            addressModelCall   = AppConstants.restAPI.postAddress(data);
        }else
        {
            if (Position != 0)
            {
                addressModelCall   = AppConstants.restAPI.postAddressUpdate(addressesList.get(Position).id,data);
            }else
            {
                addressModelCall   = AppConstants.restAPI.postAddress(data);
            }


        }



        addressModelCall.enqueue(new Callback<AddressModel>() {
            @Override
            public void onResponse(Call<AddressModel> call, Response<AddressModel> response) {
                if (response.isSuccessful())
                {
                    if (action.equalsIgnoreCase("Save"))
                    {
                        if (AddNew !=null){
                            if (AddNew.equalsIgnoreCase("Yes"))
                            {
                                Intent output = new Intent();
                                output.putExtra("Address", updatedText);
                                output.putExtra("landMark", landMark.getText().toString());
                                output.putExtra("flatNo", flatNo.getText().toString());
                                setResult(RESULT_OK, output);
                                finish();
                            }
                        }
                      else
                        {
                            pf.saveStringForKey("CurrentAddress",updatedText);
                            pf.saveStringForKey("type",updatedText);
                            pf.saveStringForKey("landmark",updatedText);
                            pf.saveStringForKey("house_no",updatedText);
                            pf.saveStringForKey("pincode",postalCode);
                            Intent mainIntent = new Intent(EnterFullAdressActivity.this, MainBottomBarActivity.class);
                            startActivity(mainIntent);
                            finish();
                        }

                    }else
                    {
                        showToast("Updated");
                    }

                }else {

                    try {
                        Log.d("Error",response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<AddressModel> call, Throwable t) {

            }
        });
    }



    private String getRadioSelected() {

        String type = "";
        int selectedId = radioAction.getCheckedRadioButtonId();

        if (selectedId== R.id.radioHome)
        {
            type = "Home";
        }
        if (selectedId == R.id.radioOffice)
        {
            type = "Office";

        }

        if (selectedId == R.id.radioOther)
        {
            type = "Other";
        }

        return type;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
