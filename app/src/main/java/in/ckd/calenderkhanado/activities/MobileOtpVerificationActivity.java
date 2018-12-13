package in.ckd.calenderkhanado.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import in.ckd.calenderkhanado.location.UserLocationActivtiy;
import in.ckd.calenderkhanado.R;
import in.ckd.calenderkhanado.base.BaseActivity;
import in.ckd.calenderkhanado.data.network.model.LoginRequest;
import in.ckd.calenderkhanado.data.pref.PreferenceManager;
import in.ckd.calenderkhanado.models.UserInsert;
import in.ckd.calenderkhanado.utils.AppConstants;
import com.eralp.circleprogressview.CircleProgressView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MobileOtpVerificationActivity extends BaseActivity implements View.OnClickListener {
    boolean otpactive = false;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]
    private boolean mVerificationInProgress = false;
    private static final String TAG = "NumberVerification";
    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";
    String countryCode = "91";
    private static final int STATE_INITIALIZED = 1;
    private static final int STATE_CODE_SENT = 2;
    private static final int STATE_VERIFY_FAILED = 3;
    private static final int STATE_VERIFY_SUCCESS = 4;
    private static final int STATE_SIGNIN_FAILED = 5;
    private static final int STATE_SIGNIN_SUCCESS = 6;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private boolean mobile_verify = true;
    private boolean active = false;
    PreferenceManager pf;
    PreferenceManager ppref;
    private  String fromOrderPlace = "";



    @BindView(R.id.signIn_editText_mobile)
    EditText mobile;
    @BindView(R.id.signIn_login_button)
    Button generateOTP;
    @BindView(R.id.resend)
    TextView resend;
    @BindView(R.id.signIn_editText_otp)
    EditText otp;
    @BindView(R.id.mobile_login_button)
    Button login;
    @BindView(R.id.msg_manual_otp)
    TextView msg_manual_otp;

    @BindView(R.id.circle_progress_view)
    CircleProgressView mCircleProgressView;

    @BindView(R.id.sign_up_viewpager)
    ViewPager signUpViewpager;

    @BindView(R.id.indicator)
    me.relex.circleindicator.CircleIndicator viewPagerIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_otp_verification);

        ButterKnife.bind(this);
        pf = new PreferenceManager(this,PreferenceManager.LOGIN_PREFERENCES_FILE);
        ppref = new PreferenceManager(this);


        if (getIntent() != null)
        {
            fromOrderPlace = getIntent().getStringExtra("PlaceOrder");
        }

        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();

        ImageSlider(signUpViewpager, viewPagerIndicator);


        mobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 10) {
                    otpactive = true;
                    generateOTP.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                    ///Log.e("mobile 1", "char " + charSequence);
                } else if (charSequence.length() < 10 || charSequence.length() > 10) {
                    otpactive = false;
                    generateOTP.setBackgroundColor(getResources().getColor(R.color.gray));
                    //Log.e("mobile 1", "char " + charSequence);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        otp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 6) {
                    active = true;
                    login.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    //Log.e("mobile 9", "char " + charSequence);
                } else if (charSequence.length() < 6 || charSequence.length() > 6) {
                    active = false;
                    login.setBackgroundColor(getResources().getColor(R.color.gray));
                    //Log.e("mobile 10", "char " + charSequence);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        // [START phone_auth_callbacks]
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {

                Log.e(TAG, "onVerificationCompleted:" + credential);
                mVerificationInProgress = false;

                // Update the UI and attempt sign in with the phone credential
                updateUI(STATE_VERIFY_SUCCESS, credential);
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.e(TAG, "onVerificationFailed", e);
                mVerificationInProgress = false;

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    //mobile.setError("Invalid phone number.");
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    //Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.", Snackbar.LENGTH_SHORT).show();
                }

                // Show a message and update the UI
                updateUI(STATE_VERIFY_FAILED);
            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
                mVerificationId = verificationId;
                mResendToken = token;

                updateUI(STATE_CODE_SENT);
            }
        };

        generateOTP.setOnClickListener(this);
        login.setOnClickListener(this);
        resend.setOnClickListener(this);

    }

    private void startPhoneNumberVerification(String phoneNumber) {

        Log.e("Firebase","start  log  in");
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
        Log.e("Firebase","end  log  in");
        mVerificationInProgress = true;
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {

        Log.e("mobile login","verifyPhoneNumberWithCode "+mVerificationId+"  code "+code);
        if(verificationId != null &&  code!= null){
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
            Log.e("mobile login","verifyPhoneNumberWithCode "+credential);
            signInWithPhoneAuthCredential(credential);
        }else {
            error("Something went wrong. Please try again");
        }

    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {

        //Log.e("resend","mobile resend 1");
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.e(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            updateUI(STATE_SIGNIN_SUCCESS, user);
                        } else {
                            Log.e(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                //mVerificationField.setError("Invalid code.");
                            }
                            updateUI(STATE_SIGNIN_FAILED);
                        }
                    }
                });
    }

    private void updateUI(int uiState) {
        updateUI(uiState, mAuth.getCurrentUser(), null);
    }

    private void updateUI(int uiState, FirebaseUser user) {
        updateUI(uiState, user, null);
    }

    private void updateUI(int uiState, PhoneAuthCredential cred) {
        updateUI(uiState, null, cred);
    }

    private void updateUI(int uiState, FirebaseUser user, PhoneAuthCredential cred) {
        switch (uiState) {
            case STATE_INITIALIZED:
                     Log.e("Step 1",""+STATE_INITIALIZED);

                break;
            case STATE_CODE_SENT:
                    Log.e("Step 2",""+STATE_CODE_SENT);
                break;
            case STATE_VERIFY_FAILED:
                     Log.e("Step 3",""+STATE_VERIFY_FAILED);
                break;
            case STATE_VERIFY_SUCCESS:
                     Log.e("Step 4",""+STATE_VERIFY_SUCCESS+"  "+cred);

                // Set the verification text based on the credential
                if (cred != null) {
                    if (cred.getSmsCode() != null) {
                                 Log.e("Step 5"," "+cred.getSmsCode());
                        //msg_manual_otp.setText(cred.getSmsCode());
                    } else {
                               Log.e("Step 6","");
                        //msg_manual_otp.setText(R.string.instant_validation);
                    }
                }

                break;
            case STATE_SIGNIN_FAILED:
                  Log.e("Step 7",""+STATE_SIGNIN_FAILED);
                // No-op, handled by sign-in check
                //mDetailText.setText(R.string.status_sign_in_failed);
                break;
            case STATE_SIGNIN_SUCCESS:
                 Log.e("Step 8"," "+STATE_SIGNIN_SUCCESS);
                // Np-op, handled by sign-in check
                break;
        }

        if (user == null) {

            Log.e("Step 9","Signed out");
            if (otp.getText().toString() != null && mobile_verify){
                if (!otp.getText().toString().isEmpty() && otp.getText().toString().equals(AppConstants.MANUALOTP)){
                    Toast.makeText(MobileOtpVerificationActivity.this,"Mobile number verified successfully",Toast.LENGTH_SHORT).show();
                    verifyMobile();
                }
            }

        } else {
            //    Log.e("Step 10","Signed in"+user.getUid());
            Toast.makeText(MobileOtpVerificationActivity.this,"Mobile number verified successfully",Toast.LENGTH_SHORT).show();
            verifyMobile();
        }
    }

    private void verifyMobile() {

        pf = new PreferenceManager(this);

        pf.saveStringForKey("Mobile",mobile.getText().toString());

        LoginRequest request = new LoginRequest();

        request.mobile = mobile.getText().toString();
        request.login_type = "mobile";
        request.is_email_verified = 1;
        request.is_mobile_verified = 1;
        request.is_vagitarian = 1;
        request.type = AppConstants.SEEKER;

        Call<ResponseBody> loginRequestCall = AppConstants.restAPI.saveUserInfo(request);

        loginRequestCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()){

                    if (response != null){

                        try {
                            String resp = response.body().string();

                            JSONObject json = new JSONObject(resp)
;
                            UserInsert.Data data = new UserInsert.Data();

                            pf = new PreferenceManager(MobileOtpVerificationActivity.this);


                            if (fromOrderPlace != null && !"".equalsIgnoreCase(fromOrderPlace)&&fromOrderPlace.equalsIgnoreCase("PlaceOrder"))
                            {

                                Intent output = new Intent();
                                output.putExtra("MobileNumber",  mobile.getText().toString());

                                setResult(RESULT_OK, output);
                                finish();

                            }else if (json.getString("status").equalsIgnoreCase(AppConstants.SUCCESS)) {

                                saveValue(json.getJSONObject("data").getInt("id")
                                        ,json.getJSONObject("data").getString("email"),json.getJSONObject("data").getString("profileImage"),"mobile",json.getJSONObject("data").getString("mobile"));

                                pf.saveIntForKey("user_id",json.getJSONObject("data").getInt("id"));
                                Intent i = new Intent(MobileOtpVerificationActivity.this,ProfileUpdateActivity.class);
                                i.putExtra("mobile", mobile.getText().toString());
                                i.putExtra("logintype","mobile");
                                startActivity(i);
                                finish();
                            }else if (json.getString("status").equalsIgnoreCase(AppConstants.ALREADY)) {
                                pf.saveIntForKey("user_id",json.getJSONArray("data").getJSONObject(0).getInt("id"));
                                data.id =json.getJSONArray("data").getJSONObject(0).getInt("id");
                                data.mobile = json.getJSONArray("data").getJSONObject(0).getString("mobile");
                                data.f_name = json.getJSONArray("data").getJSONObject(0).getString("f_name");
                                data.profile_image = json.getJSONArray("data").getJSONObject(0).getString("profile_image");
                                data.email =json.getJSONArray("data").getJSONObject(0).getString("email");
                                saveValue(data.id,data.email,data.profile_image,"mobile",data.mobile);

                                sharePrefrenceIntentActivity(data);

                            }else {
                                  showToast("Please try again");
                                  finish();
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }else {

                        try {
                            Log.e("null",""+response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }else {
                    try {
                        Log.e("response",""+response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Response is failure",""+t);

            }
        });

    }

    private void sharePrefrenceIntentActivity(UserInsert.Data data) {
        pf.saveIntForKey(PreferenceManager.USER_ID, data.id);

        if (data.profile_image != null && data.profile_image.isEmpty())
            pf.saveStringForKey(PreferenceManager.USER_PROFILE_PIC, data.profile_image);
        if (data.email != null && data.email.isEmpty())
            pf.saveStringForKey(PreferenceManager.USER_EMAIl_Id, data.email);
        if (data.mobile != null && data.mobile.isEmpty())
            pf.saveStringForKey(PreferenceManager.USER_MOBILE, data.mobile);

        if (data.f_name != null && !data.f_name.equalsIgnoreCase("null")&&!"".equalsIgnoreCase(data.f_name))
        {
            pf.saveStringForKey(PreferenceManager.FIRST_NM, data.f_name);
            pf.saveStringForKey("FirstName","Full");
        }else
        {
            pf.saveStringForKey("FirstName","Empty");
            Intent i = new Intent(MobileOtpVerificationActivity.this,ProfileUpdateActivity.class);
            i.putExtra("mobile", mobile.getText().toString());
            i.putExtra("logintype","mobile");
            startActivity(i);
            finish();
            return;
        }


        String savedLocation = pf.getStringForKey("CurrentAddress","");
        if (savedLocation != null &&savedLocation.equalsIgnoreCase("gotitlocation")){
            startActivity(new Intent(MobileOtpVerificationActivity.this,MainBottomBarActivity.class));
            finish();
        }else{
            startActivity(new Intent(MobileOtpVerificationActivity.this,UserLocationActivtiy.class));
            finish();
        }
    }

    private void error(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MobileOtpVerificationActivity.this);
        builder.setMessage(message)
                .setPositiveButton(R.string.content_dialog_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        // Create the AlertDialog object and return it
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.signIn_login_button:
                if (otpactive) {

                    //Log.e("mobile 2", "active 111111111111111");
                    InputMethodManager inputManager = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);

                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    generateOTP.setVisibility(View.GONE);
                    msg_manual_otp.setVisibility(View.VISIBLE);
                    otp.setVisibility(View.VISIBLE);
                    login.setVisibility(View.VISIBLE);

                    mCircleProgressView.setVisibility(View.VISIBLE);
                    mCircleProgressView.setProgress(0);
                    mCircleProgressView.setTextEnabled(false);
                    mCircleProgressView.setInterpolator(new AccelerateDecelerateInterpolator());
                    mCircleProgressView.setProgressWithAnimation(100, 15000);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mCircleProgressView.setVisibility(View.GONE);
                            resend.setVisibility(View.VISIBLE);
                            mCircleProgressView.setProgress(0);
                        }
                    }, 15 * 1000);
                    startPhoneNumberVerification("+91"+mobile.getText().toString());
                }
                break;
            case R.id.mobile_login_button:

                //Log.e("mobile","login");
                String code = otp.getText().toString();
                if (TextUtils.isEmpty(code)) {
                    otp.setError("Cannot be empty.");
                    return;
                }

                if (active) {
                    verifyPhoneNumberWithCode(mVerificationId, code);
                }

                break;
            case R.id.resend:

                resend.setVisibility(View.GONE);

                mCircleProgressView.setProgress(0);
                mCircleProgressView.setVisibility(View.VISIBLE);
                mCircleProgressView.setTextEnabled(false);
                mCircleProgressView.setInterpolator(new AccelerateDecelerateInterpolator());
                mCircleProgressView.setProgressWithAnimation(100, 15000);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mCircleProgressView.setVisibility(View.GONE);
                        resend.setVisibility(View.VISIBLE);
                        mCircleProgressView.setProgress(0);
                    }
                }, 15 * 1000);
                resendVerificationCode(mobile.getText().toString(), mResendToken);
                break;
        }
    }


    private void saveValue(int anInt, String jsonObject, String string, String google,String mobileV)
    {

        if (string != null)
        {
            ppref.saveStringForKey("profileImage",string);
        }else
        {
            ppref.saveStringForKey("profileImage","");
        }

        ppref.saveStringForKey("loginType",google);

        if (jsonObject != null)
        {
            ppref.saveStringForKey("email",jsonObject);
        }else
        {
            ppref.saveStringForKey("email","");
        }

        if (mobileV != null)
        {
            ppref.saveStringForKey("mobile",mobileV);
        }else
        {
            ppref.saveStringForKey("mobile","");
        }

        if (anInt != 0)
        {
            ppref.saveIntForKey("user_id",anInt);
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
