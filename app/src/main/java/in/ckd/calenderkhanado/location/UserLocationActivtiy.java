package in.ckd.calenderkhanado.location;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import in.ckd.calenderkhanado.R;
import in.ckd.calenderkhanado.base.BaseActivity;
import in.ckd.calenderkhanado.data.pref.PreferenceManager;
import in.ckd.calenderkhanado.utils.AppConstants;

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

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;


    private static final String LOG_TAG = "Google Places Autocomplete";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    private static final String API_KEY = "AIzaSyAiLBFmkx2Q0T7JwBUERsv6H8JH03ZGpJc";
    private ArrayList<String> resultLis;
    LocationManager locationManager;
    String provider;

    BroadcastReceiver gpsReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_user_location_activtiy);

        ButterKnife.bind(this);

        loading.setVisibility(View.GONE);


        //-------Status trigger when user on gps----------

/*
         gpsReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().matches(LocationManager.PROVIDERS_CHANGED_ACTION)) {
                  finish();
                  showToast("yesINdu");
                  startActivity(new Intent(UserLocationActivtiy.this,UserLocationActivtiy.class));
                }
            }
        };

        registerReceiver(gpsReceiver, new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));
*/

        //----------------------------------

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        provider = locationManager.getBestProvider(new Criteria(), false);



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

        checkLocationPermission();

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
    public void onContactSelected(LocationModel.Predictions contact)
    {
        Toast.makeText(getApplicationContext(), "Selected: " + contact.description , Toast.LENGTH_LONG).show();

        pf.saveStringForKey("CurrentAddress",contact.description);
        Intent enterOtherAct = new Intent(UserLocationActivtiy.this,EnterFullAdressActivity.class);
        enterOtherAct.putExtra("lat",latitudeS);
        enterOtherAct.putExtra("long",lognitudeS);
        enterOtherAct.putExtra("Address",contact.description);
        startActivity(enterOtherAct);
        finish();

    }

    public  void locationBased()
    {

        Context contex = this;
        try {
           /* if (ActivityCompat.checkSelfPermission(this, mPermission)
                    != MockPackageManager.PERMISSION_GRANTED) {

                checkLocationPermission();
            }else {*/
                gps = new GpsTracker(UserLocationActivtiy.this);

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

                            showToast(address);
                            String city = addresses.get(0).getLocality();
                            String state = addresses.get(0).getAdminArea();
                            String country = addresses.get(0).getCountryName();
                            String postalCode = addresses.get(0).getPostalCode();
                            String knownName = addresses.get(0).getFeatureName();
                            latitudeS = latitude;
                            lognitudeS = longitude;
                            pin_code =postalCode;

                            pf.saveStringForKey("CurrentAddress",address);
                            pf.saveDoubleForKey("latitude",latitude);
                            pf.saveDoubleForKey("lognitude",longitude);

                            Intent enterOtherAct = new Intent(UserLocationActivtiy.this,EnterFullAdressActivity.class);
                            enterOtherAct.putExtra("Address",address);
                            enterOtherAct.putExtra("city",city);
                            enterOtherAct.putExtra("lat",String.valueOf(latitude));
                            enterOtherAct.putExtra("long",String.valueOf(longitude));
                            enterOtherAct.putExtra("pincode",postalCode);
                            enterOtherAct.putExtra("state",state);
                            startActivity(enterOtherAct);
                            finish();



                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    gps.showSettingsAlert();
                }
           // }
        } catch (Exception e) {
            e.printStackTrace();
        }



    }

    public void checkGpsStatus(String yes)
    {

        /*final LocationManager manager = (LocationManager) UserLocationActivtiy.this.getSystemService(Context.LOCATION_SERVICE);
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(UserLocationActivtiy.this)) {

            Toast.makeText(UserLocationActivtiy.this,"Gps already enabled", Toast.LENGTH_SHORT).show();

            locationBased();

        }*/

        if (yes.equalsIgnoreCase("Done"))
        {
            locationBased();
        }


        if(!hasGPSDevice(UserLocationActivtiy.this)){
            Toast.makeText(UserLocationActivtiy.this,"Gps not Supported", Toast.LENGTH_SHORT).show();
        }

       /* if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(UserLocationActivtiy.this)) {

            showSettingsAlert();

        }else {

            locationBased();
        }*/
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


    public void timerForsettingOn() {
        handler.postDelayed(new Runnable() {
            public void run() {

                checkGpsStatus("Done");
            }

        }, 1 * 3000);
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



    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("location needed")
                        .setMessage("Access this need location permission")
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(UserLocationActivtiy.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            locationBased();
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                      locationBased();
                        //Request location updates:
                    //    locationManager.requestLocationUpdates(provider, 400, 1, UserLocationActivtiy.this);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }


    @Override
    public void onResume() {
        super.onResume();
      checkLocationPermission();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
      //  LocalBroadcastManager.getInstance(this).unregisterReceiver(gpsReceiver);
    }
}
