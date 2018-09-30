package in.mummysfood.Location;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.test.mock.MockPackageManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import in.mummysfood.R;
import in.mummysfood.base.BaseActivity;
import in.mummysfood.data.pref.PreferenceManager;
import in.mummysfood.utils.AppConstants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserLocationActivtiy extends BaseActivity implements  ContactsAdapter.ContactsAdapterListener
{

    @BindView(R.id.searchText)
    SearchView searchText;

    @BindView(R.id.recyclerviewSearch)
    RecyclerView recyclerviewSearch;

    @BindView(R.id.loading)
    ProgressBar loading;

    @BindView(R.id.noDataFound)
    TextView noDataFound;

    @BindView(R.id.detectAutomatically)
    TextView detectAutomatically;

    @BindView(R.id.automaticLocation)
    LinearLayout automaticLocation;




    private String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;
    private GpsTracker gps;

    private double latitudeS;
    private double lognitudeS;
    private String pin_code;

    private final int REQUEST_CODE_PERMISSION = 2;

    private Handler handler;


    private static final String LOG_TAG = "Google Places Autocomplete";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    private static final String API_KEY = "AIzaSyBbiDK90-7ghYlvEYFscFNcFApa6ks2oAo";
    private ArrayList<String> resultLis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_user_location_activtiy);

        ButterKnife.bind(this);

        loading.setVisibility(View.GONE);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerviewSearch.setLayoutManager(mLayoutManager);
        recyclerviewSearch.setItemAnimator(new DefaultItemAnimator());


        handler = new Handler();

        pf = new PreferenceManager(this);

        searchText.setQueryHint("search your location");

        searchText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!query.isEmpty())
                {
                    loading.setVisibility(View.VISIBLE);

                    autocomplete(query);
                    automaticLocation.setVisibility(View.GONE);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {


                return false;
            }
        });



    }


    @OnClick(R.id.detectAutomatically)
    public void detectAutomatically()
    {
        locationBased();

        //timerForsettingOn();
    }


    public void  autocomplete(String input)
    {

        Call<LocationModel> call = AppConstants.restAPI.getTopRatedMovies(PLACES_API_BASE+TYPE_AUTOCOMPLETE+OUT_JSON+"?key="+API_KEY+"&components=country:in&input="+input);


        call.enqueue(new Callback<LocationModel>() {
            @Override
            public void onResponse(Call<LocationModel> call, Response<LocationModel> response) {
                if (response.isSuccessful())
                {
                    recyclerviewSearch.setVisibility(View.VISIBLE);
                    loading.setVisibility(View.GONE);
                    if (response != null)
                    {
                        if (response.body().predictions.size() != 0)
                        {
                            noDataFound.setVisibility(View.GONE);
                            setAdapter(response.body().predictions);
                        }else
                        {
                            noDataFound.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<LocationModel> call, Throwable t) {

            }
        });

    }

    private void setAdapter(List<LocationModel.Predictions> predictions) {

        ContactsAdapter mAdapter = new ContactsAdapter(this, predictions, this);
        recyclerviewSearch.setAdapter(mAdapter);
    }

    @Override
    public void onContactSelected(LocationModel.Predictions contact) {
        Toast.makeText(getApplicationContext(), "Selected: " + contact.description , Toast.LENGTH_LONG).show();

        pf.saveStringForKey("CurrentAddress",contact.description);
        Intent enterOtherAct = new Intent(UserLocationActivtiy.this,EnterFullAdressActivity.class);
        enterOtherAct.putExtra("Address",contact.description);
        startActivity(enterOtherAct);
        finish();

    }

    public  void locationBased()
    {

        try {
            if (ActivityCompat.checkSelfPermission(this, mPermission)
                    != MockPackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{mPermission},
                        REQUEST_CODE_PERMISSION);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        gps = new GpsTracker(this);

        // check if GPS enabled
        if(gps.canGetLocation()){

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();




            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this, Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1);

                if (addresses.size() != 0){

                    String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();
                    String knownName = addresses.get(0).getFeatureName();


                    pf.saveStringForKey("CurrentAddress",address);
                    Intent enterOtherAct = new Intent(UserLocationActivtiy.this,EnterFullAdressActivity.class);
                    enterOtherAct.putExtra("Address",address);
                    startActivity(enterOtherAct);
                    finish();

                    latitudeS = latitude;
                    lognitudeS = longitude;
                    pin_code =postalCode;

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
                showSettingsAlert();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION:
            {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {

                    locationBased();

                }

            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }



    public void checkGpsStatus()
    {

        final LocationManager manager = (LocationManager) UserLocationActivtiy.this.getSystemService(Context.LOCATION_SERVICE);
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(UserLocationActivtiy.this)) {

            Toast.makeText(UserLocationActivtiy.this,"Gps already enabled", Toast.LENGTH_SHORT).show();

            locationBased();

        }

        if(!hasGPSDevice(UserLocationActivtiy.this)){
            Toast.makeText(UserLocationActivtiy.this,"Gps not Supported", Toast.LENGTH_SHORT).show();
        }

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(UserLocationActivtiy.this)) {

            showSettingsAlert();

        }else {

            locationBased();
        }



    }

    private boolean hasGPSDevice(Context context) {
        final LocationManager mgr = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        if (mgr == null)
            return false;
        final List<String> providers = mgr.getAllProviders();
        if (providers == null)
            return false;
        return providers.contains(LocationManager.GPS_PROVIDER);
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    public void timerForsettingOn() {
        handler.postDelayed(new Runnable() {
            public void run() {

                checkGpsStatus();
            }

        }, 1 * 3000);
    }


    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
                timerForsettingOn();
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (noDataFound.getVisibility() == View.VISIBLE)
        {
            noDataFound.setVisibility(View.GONE);
            automaticLocation.setVisibility(View.VISIBLE);
        }else {
            finish();
        }
    }
}
