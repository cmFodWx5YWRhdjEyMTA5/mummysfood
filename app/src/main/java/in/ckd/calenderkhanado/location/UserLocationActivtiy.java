package in.ckd.calenderkhanado.location;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import in.ckd.calenderkhanado.R;
import in.ckd.calenderkhanado.activities.MainBottomBarActivity;
import in.ckd.calenderkhanado.base.BaseActivity;
import in.ckd.calenderkhanado.data.pref.PreferenceManager;
import in.ckd.calenderkhanado.models.AddressModel;
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

import static in.ckd.calenderkhanado.fragments.ProfileFragment.addressesList;


public class UserLocationActivtiy extends BaseActivity

        implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    private Location mylocation;
    private GoogleApiClient googleApiClient;
    private final static int REQUEST_CHECK_SETTINGS_GPS = 0x1;
    private final static int REQUEST_ID_MULTIPLE_PERMISSIONS = 0x2;
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
    private String AddNew = "";

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

    String OrderDetails = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_user_location_activtiy);

        ButterKnife.bind(this);

        loading.setVisibility(View.GONE);

        LottieAnimationView lottieAnimationViewPlace = findViewById(R.id.lottieAnimationgetLoc);
        lottieAnimationViewPlace.playAnimation();


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerviewSearch.setLayoutManager(mLayoutManager);
        recyclerviewSearch.setItemAnimator(new DefaultItemAnimator());


        handler = new Handler();

        pf = new PreferenceManager(this);
        try {
            AddNew = getIntent().getStringExtra("AddNew");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            OrderDetails   = getIntent().getStringExtra("From");
        } catch (Exception e) {
            e.printStackTrace();
        }

        searchText.setQueryHint("search your location");

        searchText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!query.isEmpty()) {
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

        setUpGClient();
    }


    @OnClick(R.id.detectAutomatically)
    public void detectAutomatically() {
    }


    public void autocomplete(String input) {

        Call<LocationModel> call = AppConstants.restAPI.getTopRatedMovies(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON + "?key=" + API_KEY + "&components=country:in&input=" + input);


        call.enqueue(new Callback<LocationModel>() {
            @Override
            public void onResponse(Call<LocationModel> call, Response<LocationModel> response) {
                if (response.isSuccessful()) {
                    recyclerviewSearch.setVisibility(View.VISIBLE);
                    loading.setVisibility(View.GONE);
                    if (response != null) {
                        if (response.body().predictions.size() != 0) {
                            noDataFound.setVisibility(View.GONE);
                            //setAdapter(response.body().predictions);
                        } else {
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

    public void locationBased(Double latitude, Double longitude) {

        Context contex = this;
        try {
           /* if (ActivityCompat.checkSelfPermission(this, mPermission)
                    != MockPackageManager.PERMISSION_GRANTED) {

                checkLocationPermission();
            }else {*/


            Geocoder geocoder;
            List<Address> addresses;

            geocoder = new Geocoder(this, Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1);

                if (addresses.size() != 0) {

                    String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

                    showToast(address);
                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();
                    String knownName = addresses.get(0).getFeatureName();
                    latitudeS = latitude;
                    lognitudeS = longitude;
                    pin_code = postalCode;

                    pf.saveStringForKey("CurrentAddress", address);
                    pf.saveStringForKey("Address", address);
                    pf.saveDoubleForKey("latitude", latitude);
                    pf.saveDoubleForKey("lognitude", longitude);

                    Intent enterOtherAct = new Intent(UserLocationActivtiy.this, EnterFullAdressActivity.class);
                    enterOtherAct.putExtra("Address", address);
                    enterOtherAct.putExtra("city", city);
                    enterOtherAct.putExtra("lat", String.valueOf(latitude));
                    enterOtherAct.putExtra("long", String.valueOf(longitude));
                    enterOtherAct.putExtra("pincode", postalCode);
                    enterOtherAct.putExtra("state", state);
                    startActivity(enterOtherAct);

                    finish();


                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            // }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (noDataFound.getVisibility() == View.VISIBLE) {
            noDataFound.setVisibility(View.GONE);
            automaticLocation.setVisibility(View.VISIBLE);
        } else {
            finish();
        }
    }

    private synchronized void setUpGClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0, this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        mylocation = location;
        if (mylocation != null) {
            Double latitude = mylocation.getLatitude();
            Double longitude = mylocation.getLongitude();

            locationBased(latitude, longitude);

            //Or Do whatever you want with your location
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        checkPermissions();
    }

    @Override
    public void onConnectionSuspended(int i) {
        //Do whatever you need
        //You can display a message here
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        //You can display a message here
    }

    private void getMyLocation() {
        if (googleApiClient != null) {
            if (googleApiClient.isConnected()) {
                int permissionLocation = ContextCompat.checkSelfPermission(UserLocationActivtiy.this,
                        Manifest.permission.ACCESS_FINE_LOCATION);
                if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                    mylocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                    LocationRequest locationRequest = new LocationRequest();
                    locationRequest.setInterval(3000);
                    locationRequest.setFastestInterval(3000);
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                            .addLocationRequest(locationRequest);
                    builder.setAlwaysShow(true);
                    LocationServices.FusedLocationApi
                            .requestLocationUpdates(googleApiClient, locationRequest, this);
                    PendingResult<LocationSettingsResult> result =
                            LocationServices.SettingsApi
                                    .checkLocationSettings(googleApiClient, builder.build());
                    result.setResultCallback(new ResultCallback<LocationSettingsResult>() {

                        @Override
                        public void onResult(LocationSettingsResult result) {
                            final Status status = result.getStatus();
                            switch (status.getStatusCode()) {
                                case LocationSettingsStatusCodes.SUCCESS:
                                    // All location settings are satisfied.
                                    // You can initialize location requests here.
                                    int permissionLocation = ContextCompat
                                            .checkSelfPermission(UserLocationActivtiy.this,
                                                    Manifest.permission.ACCESS_FINE_LOCATION);
                                    if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                                        mylocation = LocationServices.FusedLocationApi
                                                .getLastLocation(googleApiClient);
                                    }
                                    break;
                                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                    // Location settings are not satisfied.
                                    // But could be fixed by showing the user a dialog.
                                    try {
                                        // Show the dialog by calling startResolutionForResult(),
                                        // and check the result in onActivityResult().
                                        // Ask to turn on GPS automatically
                                        status.startResolutionForResult(UserLocationActivtiy.this,
                                                REQUEST_CHECK_SETTINGS_GPS);
                                    } catch (IntentSender.SendIntentException e) {
                                        // Ignore the error.
                                    }
                                    break;
                                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                    // Location settings are not satisfied.
                                    // However, we have no way
                                    // to fix the
                                    // settings so we won't show the dialog.
                                    // finish();
                                    break;
                            }
                        }
                    });
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS_GPS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        getMyLocation();
                        break;
                    case Activity.RESULT_CANCELED:
                        finish();
                        break;
                }
                break;
        }
    }

    private void checkPermissions() {
        int permissionLocation = ContextCompat.checkSelfPermission(UserLocationActivtiy.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(this,
                        listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            }
        } else {
            getMyLocation();
        }

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        int permissionLocation = ContextCompat.checkSelfPermission(UserLocationActivtiy.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
            getMyLocation();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
