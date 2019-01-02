package in.ckd.calenderkhanado.activities;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.RelativeLayout;

import in.ckd.calenderkhanado.location.UserLocationActivtiy;
import in.ckd.calenderkhanado.R;
import in.ckd.calenderkhanado.base.BaseActivity;
import in.ckd.calenderkhanado.data.network.model.LoginRequest;
import in.ckd.calenderkhanado.data.pref.PreferenceManager;
import in.ckd.calenderkhanado.models.UserInsert;
import in.ckd.calenderkhanado.models.UserModel;
import in.ckd.calenderkhanado.utils.AppConstants;
import in.ckd.calenderkhanado.widgets.CkdTextview;
//import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
//import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginAndSignupActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener {

    @BindView(R.id.gmail_login)
    CkdTextview gmail_login;

    @BindView(R.id.mobile_login)
    CkdTextview mobile_login;

    @BindView(R.id.sign_up_viewpager)
    ViewPager signUpViewpager;

    @BindView(R.id.indicator)
    me.relex.circleindicator.CircleIndicator viewPagerIndicator;

    @BindView(R.id.mainRelative)
    RelativeLayout mainRelative;

    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]
    private DatabaseReference mDatabase;
    private PreferenceManager ppref;
    private boolean newUser = false;


    private OnCompleteListener<AuthResult> completeListener;
    private GoogleApiClient mGoogleApiClient;

    static ArrayList<String> contactListArray = new ArrayList<>();
    private int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private int SMS_PERMISSION = 200;
    private static final String[] PROJECTION = new String[]{
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
    };
    public static Boolean syncContact;
    private String mobile_data;
    int user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_and_signup);
        ButterKnife.bind(this);

        pf = new PreferenceManager(this, PreferenceManager.LOGIN_PREFERENCES_FILE);
        ppref = new PreferenceManager(this);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        ImageSlider(signUpViewpager, viewPagerIndicator);

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();

        configureSignIn();

        //sms permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_SMS}, SMS_PERMISSION);
        }
        //read mobile contact list


    }


    public void configureSignIn() {

// Configure sign-in to request the user's basic profile like name and email

        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)

                .requestIdToken(getString(R.string.default_web_client_id))

                .requestEmail()

                .build();


        // Build a GoogleApiClient with access to GoogleSignIn.API and the options above.

        mGoogleApiClient = new GoogleApiClient.Builder(getBaseContext())

                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)

                .addApi(Auth.GOOGLE_SIGN_IN_API, options)

                .build();

        mGoogleApiClient.connect();

    }

    private int getItem(int i) {
        return signUpViewpager.getCurrentItem() + i;
    }

    @OnClick(R.id.gmail_login)
    public void GmailLogin() {
        signIn();
    }

    @OnClick(R.id.mobile_login)
    public void MobileLogin() {

        Intent intent = new Intent(this, MobileOtpVerificationActivity.class);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.terms_services)
    public void terms_services()
    {
        Intent i = new Intent(LoginAndSignupActivity.this,TermsAndCondition.class);
        startActivity(i);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result != null) {
                if (result.isSuccess()) {
                    // Google Sign In was successful, authenticate with Firebase
                    GoogleSignInAccount account = result.getSignInAccount();
                    firebaseAuthWithGoogle(account, result);
                } else {
                    // Google Sign In failed, update UI appropriately
                    //popupDismiss();
                    showToast("Authentication failed");
                }
            } else {
                // Google Sign In failed, update UI appropriately
                //popupDismiss();
                showToast("Authentication failed");
            }
        }
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct, final GoogleSignInResult result) {

        //get the shared instance of the FirebaseAuth object
        mAuth = FirebaseAuth.getInstance();
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //   Log.e("Google LOgin", "signInWithCredential:onComplete:" + task.isSuccessful());
                // If sign in fails, display a message to the user. If sign in succeeds
                // the auth state listener will be notified and logic to handle the
                // signed in user can be handled in the listener.
                if (!task.isSuccessful()) {
                    //  popupDismiss();
                    //     Log.e("Google LOgin", "signInWithCredential", task.getException());
                    showToast("Authentication failed");
                } else {
                    networkcallForCheckUserInDb(acct);
                }
            }
        });
    }


    private void networkcallForCheckUserInDb(final GoogleSignInAccount user) {

        LoginRequest request = new LoginRequest();
        request.email = user.getEmail();
        request.f_name = user.getDisplayName();
        request.profile_image = String.valueOf(user.getPhotoUrl());
        request.is_email_verified = "1";
        request.is_mobile_verified = "0";
        request.is_vagitarian = "0";

        Call<ResponseBody> loginRequestCall = AppConstants.restAPI.saveUserInfo(request);

        loginRequestCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {

                    if (response != null) {

                        try {
                            String resp = response.body().string();

                            JSONObject json = new JSONObject(resp);
                            UserInsert.Data data = new UserInsert.Data();

                            if (json.getString("status").equalsIgnoreCase(AppConstants.SUCCESS)) {
                                Intent intent = new Intent(LoginAndSignupActivity.this, ProfileUpdateActivity.class);
                                intent.putExtra("fullname", user.getDisplayName());
                                intent.putExtra("email", user.getEmail());
                                intent.putExtra("profile_image", user.getPhotoUrl().toString());
                                intent.putExtra("logintype", AppConstants.GOOGLE);

                                try {
                                    saveValue(json, AppConstants.GOOGLE);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                startActivity(intent);

                            } else if (json.getString("status").equalsIgnoreCase(AppConstants.ALREADY)) {
                                data.id = json.getJSONArray("data").getJSONObject(0).getInt("id");
                                data.mobile = json.getJSONArray("data").getJSONObject(0).getString("mobile");
                                data.f_name = json.getJSONArray("data").getJSONObject(0).getString("f_name");
                                data.profile_image = json.getJSONArray("data").getJSONObject(0).getString("profile_image");
                                data.email = json.getJSONArray("data").getJSONObject(0).getString("email");
                                ppref.saveIntForKey("user_id", data.id);

                                try {
                                    saveValue(json, "");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                sharePrefrenceIntentActivity(data);


                            } else {
                                showToast("Please try again");
                                finish();
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    } else {

                        try {
                            Log.e("null", "" + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                } else {
                    try {
                        Log.e("response", "" + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Response is failure", "" + t);
            }
        });

    }

    private void saveValue(JSONObject json, String type) throws JSONException {


        int Id = 0;
        try {
            Id = json.getJSONObject("data").getInt("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String email = null;
        try {
            email = json.getJSONObject("data").getString("email");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String profile = null;
        try {
            profile = json.getJSONObject("data").getString("profile_image");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String mobile = null;
        try {
            mobile = json.getJSONObject("data").getString("mobile");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String loginType = null;
        try {
            loginType = type;
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (profile != null) {
            ppref.saveStringForKey("profileImage", profile);
        } else {
            ppref.saveStringForKey("profileImage", "");
        }

        ppref.saveStringForKey("loginType", loginType);

        if (email != null) {
            ppref.saveStringForKey("email", email);
        } else {
            ppref.saveStringForKey("email", "");
        }

        if (mobile != null) {
            ppref.saveStringForKey("mobile", mobile);
        } else {
            ppref.saveStringForKey("mobile", "");
        }

        if (Id != 0) {
            ppref.saveIntForKey("user_id", Id);
        }


    }

    private void sharePrefrenceIntentActivity(UserInsert.Data data) {
        pf.saveIntForKey(PreferenceManager.USER_ID, data.id);

        if (data.f_name != null && !data.f_name.equalsIgnoreCase("null") && !"".equalsIgnoreCase(data.f_name)) {
            pf.saveStringForKey(PreferenceManager.FIRST_NM, data.f_name);
            pf.saveStringForKey("FirstName", "Full");
        } else {
            pf.saveStringForKey("FirstName", "Empty");
            Intent intent = new Intent(LoginAndSignupActivity.this, ProfileUpdateActivity.class);
            intent.putExtra("fullname", "");
            intent.putExtra("email", data.email);
            intent.putExtra("profile_image", data.profile_image);
            intent.putExtra("logintype", AppConstants.GOOGLE);
            startActivity(intent);
            finish();
        }


        if (data.profile_image != null && data.profile_image.isEmpty())
            pf.saveStringForKey(PreferenceManager.USER_PROFILE_PIC, data.profile_image);
        if (data.email != null && data.email.isEmpty())
            pf.saveStringForKey(PreferenceManager.USER_EMAIl_Id, data.email);
        if (data.mobile != null && data.mobile.isEmpty())
            pf.saveStringForKey(PreferenceManager.USER_MOBILE, data.mobile);
        String savedLocation = pf.getStringForKey("CurrentAddress", "");
        if (savedLocation != null && savedLocation.equalsIgnoreCase("gotitlocation")) {

            Intent mainIntent = new Intent(LoginAndSignupActivity.this, MainBottomBarActivity.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mainIntent);


            finish();
        } else {
            startActivity(new Intent(LoginAndSignupActivity.this, UserLocationActivtiy.class));
            finish();
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);

        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void updateUI(FirebaseUser user, boolean newUserValue) {
        if (user != null) {
            Intent intent = new Intent(this, MainBottomBarActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    /*read mobile contact no.*/
    /*private void readContacts() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS}, 0);
        }
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {
            AsyncTaskRunner runner = new AsyncTaskRunner();
            runner.execute("import_contact");
        }
    }*/


    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == SMS_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted

            } else {
                //Log.e("Permission","Deny");
            }
        }
    }

    private class AsyncTaskRunner extends AsyncTask<Object, Object, ArrayList<String>> {
        @Override
        protected ArrayList<String> doInBackground(Object... strings) {
            getContacts();
            return contactListArray;
        }

        @Override
        protected void onPostExecute(ArrayList<String> s) {
            super.onPostExecute(s);

            //  Log.e("mobile ","on post execute");
            syncContact = true;
            syncContacts(contactListArray);
        }

    }

    public void getContacts() {
        //Log.e("Permission","start import contact");
        long startnow;
        long endnow;

        startnow = android.os.SystemClock.uptimeMillis();

        ContentResolver cr = getContentResolver();
        Cursor cursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PROJECTION, null, null, null);
        if (cursor != null) {
            try {
                final int nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                final int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

                String number;
                ArrayList<String> mobileArr = new ArrayList<>();
                while (cursor.moveToNext()) {
                    if (cursor != null) {
                        if (cursor.getString(numberIndex) != null) {
                            number = cursor.getString(numberIndex);
                            if (number != null) {
                                number = number.replaceAll("[\\s\\-()]", "");
                                number = number.replaceAll("[^0-9]", "");
                                if (number.length() > 10) {
                                    int len = number.length() - 10;
                                    number = number.substring(len);
                                }
                                if (!mobileArr.contains(number)) {
                                    if (number.trim().length() == 10) {
                                        mobileArr.add(number);
                                        contactListArray.add(number);
                                        mobile_data = mobile_data + " , " + number;
                                        //Log.e("Mobile"," Name : " + name + " number : " + number);
                                    }
                                }
                            }
                        }
                    }

                }
            } finally {
                cursor.close();
            }

        }

        endnow = android.os.SystemClock.uptimeMillis();
        //Log.e("Contact", "TimeForContacts " + (endnow - startnow) + " ms");

    }

    private void syncContacts(final ArrayList<String> contactListArray) {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                //showToast("Api Call");
            }
        }, 500);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
