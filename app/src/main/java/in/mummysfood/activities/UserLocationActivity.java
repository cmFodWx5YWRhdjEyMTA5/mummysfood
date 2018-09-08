package in.mummysfood.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import in.mummysfood.R;
import in.mummysfood.adapters.PlacesRecyclerViewAdapter;
import in.mummysfood.base.BaseActivity;
import in.mummysfood.data.pref.PreferenceManager;
import in.mummysfood.fragments.PlaceOnMapFragment;
import in.mummysfood.interfac.ClickListener;
import in.mummysfood.models.AddressModel;
import in.mummysfood.utils.AppConstants;
import in.mummysfood.widgets.RecyclerTouchListener;
;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;

import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.RuntimeExecutionException;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserLocationActivity extends BaseActivity implements PlacesRecyclerViewAdapter.locationListner {

    public static final String TAG = "CurrentLocNearByPlaces";
    private static final int LOC_REQ_CODE = 1;

    protected GeoDataClient geoDataClient;
    protected PlaceDetectionClient placeDetectionClient;
    protected RecyclerView recyclerView;
    private RelativeLayout mainRelativeLocationHolder;

    private Handler h = new Handler();
    private int delay = 2000;
    private Runnable runnable;
    private GoogleApiClient googleApiClient;
    final static int REQUEST_LOCATION = 199;

    private List<Place> placesList = new ArrayList<Place>();
    private int MY_ACCESS_FINE_LOCATION = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_location);

        pf = new PreferenceManager(this, PreferenceManager.LOGIN_PREFERENCES_FILE);

        ButterKnife.bind(this);


        recyclerView = findViewById(R.id.places_lst);
        mainRelativeLocationHolder = findViewById(R.id.mainRelativeLocationHolder);

        LinearLayoutManager recyclerLayoutManager =
                new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerLayoutManager);

        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(recyclerView.getContext(),
                        recyclerLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        placeDetectionClient = Places.getPlaceDetectionClient(this, null);
        checkGpsStatus();
        checkPermission(UserLocationActivity.this);


    }

    private void NetworkCallForSaveLocation(final Place place) {

        Log.d("Address", String.valueOf(place.getAddress()));

        Geocoder coder = new Geocoder(this);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(String.valueOf(place.getAddress()), 5);


            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude());


            AddressModel.Data addressModel = new AddressModel.Data();

            addressModel.city = "Indore";
            addressModel.landmark = String.valueOf(place.getAddress());
            addressModel.latitude = location.getLatitude();
            addressModel.longitude = location.getLongitude();
            addressModel.user_id = 3;


            Call<AddressModel.Data> addressCall = AppConstants.restAPI.postAddress(addressModel);


            addressCall.enqueue(new Callback<AddressModel.Data>() {
                @Override
                public void onResponse(Call<AddressModel.Data> call, Response<AddressModel.Data> response) {
                    if (response.isSuccessful()) {

                        if (response != null) {

                            pf.saveStringForKey("SaveLocation", "gotitlocation");
                            startActivity(new Intent(UserLocationActivity.this, MainBottomBarActivity.class));
                            finish();
                        }
                    } else {
                        try {
                            Log.d("ErrorBody", response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<AddressModel.Data> call, Throwable t) {

                    showToast("Please try Again");
                }
            });
        } catch (Exception ex) {

            ex.printStackTrace();
        }

    }

    private void getCurrentPlaceItems() {

        getCurrentPlaceData();
    }

    private void getCurrentPlaceData() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            checkPermission(this);
            return;
        }else{
            Task<PlaceLikelihoodBufferResponse> placeResult = placeDetectionClient.
                    getCurrentPlace(null);
            placeResult.addOnCompleteListener(new OnCompleteListener<PlaceLikelihoodBufferResponse>() {
                @Override
                public void onComplete(@NonNull Task<PlaceLikelihoodBufferResponse> task) {
                    Log.d(TAG, "current location places info");

                    try {

                        try {
                            if (h != null){
                                if (runnable != null)
                                    h.removeCallbacks(runnable);
                            }
                        }catch (NullPointerException e){
                            e.printStackTrace();
                        }


                        PlaceLikelihoodBufferResponse likelyPlaces = task.getResult();
                        for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                            placesList.add(placeLikelihood.getPlace().freeze());
                        }
                        likelyPlaces.release();

                        dismissProgress();
                        PlacesRecyclerViewAdapter recyclerViewAdapter = new
                                PlacesRecyclerViewAdapter(placesList,
                                UserLocationActivity.this,UserLocationActivity.this);
                        recyclerView.setAdapter(recyclerViewAdapter);

                    }catch (RuntimeExecutionException e){
                        e.printStackTrace();
                        h.postDelayed( runnable = new Runnable() {
                            public void run() {

                                dismissProgress();
                                getCurrentPlaceItems();
                                h.postDelayed(runnable, delay);
                            }
                        }, delay);
                        showSnackBar(mainRelativeLocationHolder,"Please turn on your location");

                    }

                }
            });

        }




/*       Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId)
                .setResultCallback(new ResultCallback<PlaceBuffer>() {
                    @Override
                    public void onResult(PlaceBuffer places) {
                        if (places.getStatus().isSuccess()) {
                            final Place myPlace = places.get(0);
                            LatLng queriedLocation = myPlace.getLatLng();
                            Log.v("Latitude is", "" + queriedLocation.latitude);
                            Log.v("Longitude is", "" + queriedLocation.longitude);
                        }
                        places.release();
                    }
                });*/

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.notification:
                getCurrentPlaceItems();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }




    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

            Log.d("LatLong",String.valueOf(location.getLatitude()));


        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }

    private void enableLoc() {

        if (geoDataClient == null) {
            googleApiClient = new GoogleApiClient.Builder(UserLocationActivity.this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {

                        }

                        @Override
                        public void onConnectionSuspended(int i) {
                            googleApiClient.connect();
                        }
                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult connectionResult) {

                            Log.d("Location error","Location error " + connectionResult.getErrorCode());
                        }
                    }).build();
            googleApiClient.connect();

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            builder.setAlwaysShow(true);

            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {

                                status.startResolutionForResult(UserLocationActivity.this, REQUEST_LOCATION);

                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                    }
                }
            });
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_LOCATION) {
            if (resultCode == RESULT_OK)
            {
                showProgress("Loading...");
                checkGpsStatus();

            }else {
                showToast("Please turn on your location");
                finish();
            }
        }
    }


    public void checkGpsStatus()
    {

        final LocationManager manager = (LocationManager) UserLocationActivity.this.getSystemService(Context.LOCATION_SERVICE);
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(UserLocationActivity.this)) {

            Toast.makeText(UserLocationActivity.this,"Gps already enabled",Toast.LENGTH_SHORT).show();

            getCurrentPlaceItems();

        }

        if(!hasGPSDevice(UserLocationActivity.this)){
            Toast.makeText(UserLocationActivity.this,"Gps not Supported",Toast.LENGTH_SHORT).show();
        }

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(UserLocationActivity.this)) {
          //  Toast.makeText(UserLocationActivity.this,"Gps not enabled",Toast.LENGTH_SHORT).show();
            enableLoc();

            Toast.makeText(UserLocationActivity.this,"Gps not enabled",Toast.LENGTH_SHORT).show();

            enableLoc();

/*

                boolean result = checkPermission(UserLocationActivity.this);
                if (result)
                    enableLoc();

*/

        }else {

            getCurrentPlaceItems();
            Toast.makeText(UserLocationActivity.this,"Gps already enabled",Toast.LENGTH_SHORT).show();

          /*  boolean result = checkPermission(UserLocationActivity.this);
            if (result)
                getCurrentPlaceItems();*/
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
    protected void onPause() {
        h.removeCallbacks(runnable); //stop handler when activity not visible
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        h.removeCallbacks(runnable);
    }

    @Override
    public void locationService(int posiiton, Place place) {
        NetworkCallForSaveLocation(place);
    }


    /* private void showOnMap(Place place){

        Geocoder coder = new Geocoder(this);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(String.valueOf(place.getAddress()), 5);

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

            Log.d("LatLong",String.valueOf(location.getLatitude()));

            FragmentManager fm = getSupportFragmentManager();

            Bundle bundle=new Bundle();
            bundle.putString("name", (String)place.getName());
            bundle.putString("address", (String)place.getAddress());
            bundle.putDouble("lat",location.getLatitude());
            bundle.putDouble("lng",location.getLongitude());

            PlaceOnMapFragment placeFragment = new PlaceOnMapFragment();
            placeFragment.setArguments(bundle);

            fm.beginTransaction().replace(R.id.map_frame, placeFragment).commit();
        } catch (IOException ex) {

            ex.printStackTrace();
        }

    }*/


    public boolean checkPermission(final Context context) {

            if (hasPermissionInManifest(context, Manifest.permission.ACCESS_FINE_LOCATION)) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("Location Access permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_ACCESS_FINE_LOCATION);
                            }
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_ACCESS_FINE_LOCATION);
                    }
                } else {

                }
                return false;
            } else {
                return true;
            }

    }

    public boolean hasPermissionInManifest(Context context, String permissionName) {
        final String packageName = context.getPackageName();
        try {
            final PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(packageName, PackageManager.GET_PERMISSIONS);
            final String[] declaredPermisisons = packageInfo.requestedPermissions;
            if (declaredPermisisons != null && declaredPermisisons.length > 0) {
                for (String p : declaredPermisisons) {
                    if (p.equals(permissionName)) {
                        return true;
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {

        }
        return false;
    }
}