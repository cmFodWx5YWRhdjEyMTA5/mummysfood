package in.mummysfood.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.RelativeLayout;

import in.mummysfood.Location.UserLocationActivtiy;
import in.mummysfood.R;
import in.mummysfood.adapters.OnBoardingViewPagerAdapter;
import in.mummysfood.base.BaseActivity;
import in.mummysfood.data.network.model.LoginRequest;
import in.mummysfood.data.pref.PreferenceManager;
import in.mummysfood.models.DashBoardModel;
import in.mummysfood.models.UserModel;
import in.mummysfood.utils.AppConstants;
import in.mummysfood.widgets.CkdTextview;
//import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
//import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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
    private boolean newUser = false;

    private static final Integer[] IMAGES = {R.mipmap.sign_up_bg_01, R.mipmap.sign_up_bg_02,R.mipmap.sign_up_bg_01, R.mipmap.sign_up_bg_02};
    private ArrayList<Integer> ImagesArray = new ArrayList<Integer>();
    private OnBoardingViewPagerAdapter onBoardingViewPagerAdapter;
    private int currentPage = 0;
    private int NUM_PAGES = 0;
    private Runnable Update;
    private Handler handler;
    private   OnCompleteListener<AuthResult> completeListener;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_and_signup);
        ButterKnife.bind(this);

        pf = new PreferenceManager(this,PreferenceManager.LOGIN_PREFERENCES_FILE);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        init();


    }

    private void init() {

        for (int i = 0; i < IMAGES.length; i++)
            ImagesArray.add(IMAGES[i]);


        onBoardingViewPagerAdapter = new OnBoardingViewPagerAdapter(LoginAndSignupActivity.this, ImagesArray);
        signUpViewpager.setAdapter(onBoardingViewPagerAdapter);

        signUpViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });


        viewPagerIndicator.setViewPager(signUpViewpager);


        NUM_PAGES = IMAGES.length + 1;

        // Auto start of viewpager
        handler = new Handler();
        Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                signUpViewpager.setCurrentItem(currentPage++, true);
            }
        };

        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 2000, 2000);



        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();

        configureSignIn();

    }

    public void configureSignIn(){

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
    public void GmailLogin(){
        signIn();
    }

    @OnClick(R.id.mobile_login)
    public void MobileLogin(){


        /*Intent i = new Intent(LoginAndSignupActivity.this,ProfileUpdateActivity.class);
        i.putExtra("mobile", "8828376477");
        i.putExtra("logintype","mobile");
        startActivity(i);*/
        showToast("mobile login");
        Intent intent = new Intent(this,MobileOtpVerificationActivity.class);
        startActivity(intent);
    }

    /*@Override
    public void onStart() {
        super.onStart();
        // Check if user is sign    Ned in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser,true);
    }*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            GoogleSignInAccount account = result.getSignInAccount();
            firebaseAuthWithGoogle(account);
        }
    }

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        showProgress("loading");

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            final FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

                                rootRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {


                                        for (DataSnapshot booksSnapshot : dataSnapshot.child("users").getChildren()) {
                                            String bookskey = booksSnapshot.getKey();
                                            UserModel value = booksSnapshot.getValue(UserModel.class);
                                            showToast(value.email);
                                            if (!value.email.equalsIgnoreCase(user.getEmail())){
                                                networkcallForCheckUserInDb(user);
                                            }else {
                                             //   networkcallForSaveUserInDb(user);
                                               // updateUI(user,true);
                                            }
                                        }

                                        networkcallForCheckUserInDb(user);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }


                                });
                            }
                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                           Snackbar.make(findViewById(R.id.mainRelative), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();

                        }

                        dismissProgress();
                    }
                });
    }

    private void setValueinFirebaseDb(FirebaseUser user) {

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");

        String userId = mDatabase.push().getKey();

        UserModel userN = new UserModel(user.getDisplayName(), user.getEmail());

        mDatabase.child(userId).setValue(userN);
    }

    private void checkUserAvailableOrNot()
    {


    }

    private void networkcallForCheckUserInDb(final FirebaseUser user) {


        startActivity(new Intent(LoginAndSignupActivity.this,UserLocationActivtiy.class));
        finish();

        LoginRequest request = new LoginRequest();

        request.email = user.getEmail();

        Call<LoginRequest>loginRequestCall = AppConstants.restAPI.saveUserInfo(request);

        loginRequestCall.enqueue(new Callback<LoginRequest>() {
            @Override
            public void onResponse(Call<LoginRequest> call, Response<LoginRequest> response) {

                if (response != null){

                    if (response.isSuccessful()){
                        LoginRequest res = response.body();
                        if (res.status != null && res.status.equalsIgnoreCase(AppConstants.SUCCESS)){

                            Intent intent = new Intent(LoginAndSignupActivity.this,ProfileUpdateActivity.class);
                            intent.putExtra("fullname",user.getDisplayName());
                            intent.putExtra("email",user.getEmail());
                            intent.putExtra("profile_image",user.getPhotoUrl().toString());
                            intent.putExtra("logintype","google");
                            startActivity(intent);
                        }else if (res.status != null && res.status.equalsIgnoreCase(AppConstants.ALREADY)){
                            DashBoardModel.Data data = res.data.get(0);
                            pf.saveIntForKey(PreferenceManager.USER_ID, data.id);
                            pf.saveStringForKey(PreferenceManager.FIRST_NM, data.f_name);
                            pf.saveStringForKey(PreferenceManager.USER_PROFILE_PIC, data.profile_image);
                            pf.saveStringForKey(PreferenceManager.USER_EMAIl_Id, data.email);
                            pf.saveStringForKey(PreferenceManager.USER_MOBILE, data.mobile);

                            String savedLocation = pf.getStringForKey("SaveLocation","");
                            if (savedLocation != null &&savedLocation.equalsIgnoreCase("gotitlocation")){
                                startActivity(new Intent(LoginAndSignupActivity.this,MainBottomBarActivity.class));
                                finish();
                            }else{
                                startActivity(new Intent(LoginAndSignupActivity.this,UserLocationActivtiy.class));
                                finish();
                            }
                        }

                    }else {

                        try {
                            Log.e("Response is not success",""+response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }else {
                    try {
                        Log.e("Response is null",""+response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginRequest> call, Throwable t) {
                Log.e("Response is failure", ""+t);

            }
        });

    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);

        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    private void updateUI(FirebaseUser user,boolean newUserValue) {
        if (user != null){
            Intent intent = new Intent(this,MainBottomBarActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
