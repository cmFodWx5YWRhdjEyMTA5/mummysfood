package in.ckd.calenderkhanado.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.text.Html;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import in.ckd.calenderkhanado.location.UserLocationActivtiy;
import in.ckd.calenderkhanado.R;
import in.ckd.calenderkhanado.base.BaseActivity;
import in.ckd.calenderkhanado.data.network.model.LoginRequest;
import in.ckd.calenderkhanado.data.network.model.UploadMedia;
import in.ckd.calenderkhanado.data.pref.PreferenceManager;
import in.ckd.calenderkhanado.models.DashBoardModel;
import in.ckd.calenderkhanado.models.UserInsert;
import in.ckd.calenderkhanado.utils.AppConstants;
import in.ckd.calenderkhanado.utils.FilePath;
import in.ckd.calenderkhanado.utils.Permission;
import in.ckd.calenderkhanado.widgets.CkdEditText;
import in.ckd.calenderkhanado.widgets.CkdTextview;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OneSignal;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileUpdateActivity extends BaseActivity {

    @BindView(R.id.profile_image)
    CircularImageView profileImage;
    @BindView(R.id.full_name)
    TextInputEditText fullName;
    @BindView(R.id.email_id_layout)
    TextInputLayout emailIdLayout;
    @BindView(R.id.full_name_layout)
    TextInputLayout fullNameLayout;
    @BindView(R.id.email_id)
    TextInputEditText emailId;
    @BindView(R.id.user_status)
    CkdEditText userStatus;
    @BindView(R.id.next_upload_profile)
    CkdTextview next_upload_profile;
    @BindView(R.id.genderSwitch)
    SwitchCompat chooseGender;
    @BindView(R.id.female_icon)
    ImageView femaleIcon;
    @BindView(R.id.male_icon)
    ImageView maleIcon;
    @BindView(R.id.choose_food_type_layout)
    RadioGroup radioAction;


    public final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;

    private int email_verification;
    private String loginType, userGender = "female", mobileNumber, userEmail, userName, userProfileImage;

    //for image upload
    private final int SELECT_PHOTO = 1;
    private final int CAMERA_REQUEST = 1888;
    private Uri mImageUri;
    private Bitmap bitmapImage = null;
    private String userChoosenTask;

    private PreferenceManager pf;
    private PreferenceManager pfd;

    private String imageName;
    private int user_id;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    private String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_update);
        ButterKnife.bind(this);

        pf = new PreferenceManager(this, PreferenceManager.LOGIN_PREFERENCES_FILE);

        pfd = new PreferenceManager(this);

        user_id = pfd.getIntForKey("user_id", 0);

        Bundle intent = getIntent().getExtras();
        if (intent != null) {
            loginType = intent.getString("logintype");
            userName = intent.getString("fullname");
            userEmail = intent.getString("email");
            userProfileImage = intent.getString("profile_image");
            mobileNumber = intent.getString("mobile");
        }

        chooseGender.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    userGender = "male";
                    maleIcon.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                    femaleIcon.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.black));
                } else {
                    userGender = "female";
                    femaleIcon.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                    maleIcon.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.black));
                }

            }
        });

        if (loginType != null && loginType.equalsIgnoreCase(AppConstants.GOOGLE)) {
            fullName.setEnabled(true);
            emailId.setEnabled(false);

            if (!userName.isEmpty()) {
                fullName.setText(userName);
            }

            if (!userEmail.isEmpty()) {
                emailId.setText(userEmail);
            }

            if (!userProfileImage.isEmpty()) {
                Glide.with(ProfileUpdateActivity.this).load(userProfileImage)
                        .placeholder(R.mipmap.default_usr_img)
                        .into(profileImage);
            }
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }

        if (loginType.equalsIgnoreCase("Profile")) {

            fullName.setEnabled(true);

            try {
                if (!userName.isEmpty()) {
                    fullName.setText(userName);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            try {
                if (!userEmail.isEmpty()) {
                    emailId.setText(userEmail);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (!userProfileImage.isEmpty()) {
                    Glide.with(ProfileUpdateActivity.this).load(userProfileImage)
                            .placeholder(R.mipmap.default_usr_img)
                            .into(profileImage);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @OnClick({R.id.profile_image})
    public void uploadProfileImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = checkPermission(ProfileUpdateActivity.this);
                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";

                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";

                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    @OnClick(R.id.next_upload_profile)
    public void UpdateUserData() {
        if (loginType != null && loginType.equals("mobile")) {
            if (!fullName.getText().toString().isEmpty()) {
                if (!emailId.getText().toString().isEmpty() && emailId.getText().toString().trim().length() > 0 && email_verification != 1) {
                    //  Log.e("gcheck", "16");
                    if (!emailValidateField(emailId.getText().toString().trim())) {
                        //    Log.e("gcheck", "17");
                        emailIdLayout.setError("Please enter a valid email");
                    } else {
                        String popUpMsg = "We will send a confirmation email on " + "<b>" + emailId.getText().toString().trim() + "</b> " + " which will be used for all the important communications. Click continue to proceed";
                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(ProfileUpdateActivity.this);
                        alertBuilder.setCancelable(false);
                        alertBuilder.setMessage(Html.fromHtml(popUpMsg));
                        alertBuilder.setNegativeButton("Change", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                        alertBuilder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                            @TargetApi(Build.VERSION_CODES.M)
                            public void onClick(DialogInterface dialog, int which) {
                                nexttoupload();
                            }
                        });
                        alertBuilder.setCancelable(false);
                        AlertDialog alert = alertBuilder.create();

                        alert.show();
                    }
                } else if (!emailId.getText().toString().isEmpty() && email_verification == 1) {
                    nexttoupload();
                } else if (emailId.getText().toString().isEmpty()) {
                    nexttoupload();
                }
            } else {
                fullNameLayout.setError("Full name can't be empty");
            }
        } else {

            if (!loginType.equalsIgnoreCase("Profile")) {
                if (!fullName.getText().toString().trim().isEmpty() && !emailId.getText().toString().trim().isEmpty()) {
                    nexttoupload();
                } else {
                    showToast("All information should be valid");
                }
            } else {
                nexttoupload();
            }


        }
    }

    private void nexttoupload() {
        //getting unique id for device

        // save image into shareprefresnce
        pfd.saveStringForKey("FirstName","Full");
        pfd.saveStringForKey("ImageUrl",imageName);

        OSPermissionSubscriptionState status = OneSignal.getPermissionSubscriptionState();
        String userId = status.getSubscriptionStatus().getUserId();

        String device_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        String osVersion = android.os.Build.VERSION.RELEASE;

        LoginRequest request = new LoginRequest();

        request.f_name = fullName.getText().toString();
        request.name = fullName.getText().toString();
        request.email = emailId.getText().toString();
        request.mobile = mobileNumber;
        request.profile_image = imageName;
        request.type = AppConstants.SEEKER;
        request.player_id = userId;

        //saving name into sharedprefrence

        pfd.saveStringForKey("Name",request.f_name);

        if (loginType.equalsIgnoreCase("mobile")) {
            request.is_mobile_verified = "1";
            request.is_email_verified = "0";
        } else {
            request.is_mobile_verified = "0";
            request.is_email_verified = "1";
        }
        request.gender = userGender;
        request.is_vagitarian = String.valueOf(getRadioSelected());
        request.device_id = device_id;
        request.os = osVersion;

        Call<ResponseBody> loginRequestCall = AppConstants.restAPI.updateUserInfo( request,user_id);

        loginRequestCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                if (response != null) {
                    if (response.isSuccessful()) {
                        try {
                            String resp = response.body().string();

                            JSONObject json = new JSONObject(resp);
                            UserInsert.Data data = new UserInsert.Data();

                            if (json.getString("status").equalsIgnoreCase(AppConstants.SUCCESS)) {
                                data.id = json.getJSONArray("data").getJSONObject(0).getInt("id");
                                data.mobile = json.getJSONArray("data").getJSONObject(0).getString("mobile");
                                data.f_name = json.getJSONArray("data").getJSONObject(0).getString("f_name");
                                data.profile_image = json.getJSONArray("data").getJSONObject(0).getString("profile_image");
                                data.email = json.getJSONArray("data").getJSONObject(0).getString("email");
                                sharePrefrenceIntentActivity(data);


                            } else if (json.getString("status").equalsIgnoreCase(AppConstants.ALREADY)) {
                                data.id = json.getJSONArray("data").getJSONObject(0).getInt("id");
                                data.mobile = json.getJSONArray("data").getJSONObject(0).getString("mobile");
                                data.f_name = json.getJSONArray("data").getJSONObject(0).getString("f_name");
                                data.profile_image = json.getJSONArray("data").getJSONObject(0).getString("profile_image");
                                data.email = json.getJSONArray("data").getJSONObject(0).getString("email");
                                sharePrefrenceIntentActivity(data);
                            } else {
                                showDialogBox("Authentication Failed", "OK");
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            Log.e("status", response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    Log.e("response", "null ");

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Response is failure", "" + t);
            }
        });
    }

    private void saveSharePrefrenceData(DashBoardModel.Data data) {
        pf.saveIntForKey(PreferenceManager.USER_ID, data.id);
        pf.saveStringForKey(PreferenceManager.FIRST_NM, data.f_name);
        pf.saveStringForKey(PreferenceManager.USER_PROFILE_PIC, data.profile_image);
        pf.saveStringForKey(PreferenceManager.USER_EMAIl_Id, data.email);
        pf.saveStringForKey(PreferenceManager.USER_MOBILE, data.mobile);
    }

    private void sharePrefrenceIntentActivity(UserInsert.Data data) {
        pf.saveIntForKey(PreferenceManager.USER_ID, data.id);
        if (data.f_name != null && data.f_name.isEmpty())
            pf.saveStringForKey(PreferenceManager.FIRST_NM, data.f_name);
        if (data.profile_image != null && data.profile_image.isEmpty())
            pf.saveStringForKey(PreferenceManager.USER_PROFILE_PIC, data.profile_image);
        if (data.email != null && data.email.isEmpty())
            pf.saveStringForKey(PreferenceManager.USER_EMAIl_Id, data.email);
        if (data.mobile != null && data.mobile.isEmpty())
            pf.saveStringForKey(PreferenceManager.USER_MOBILE, data.mobile);

        if (loginType.equalsIgnoreCase("Profile")) {
            finish();
        }

        String savedLocation = pf.getStringForKey("CurrentAddress", "");
        if (savedLocation != null && savedLocation.equalsIgnoreCase("gotitlocation")) {
            Intent intent =   new Intent(ProfileUpdateActivity.this, MainBottomBarActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } else {
            startActivity(new Intent(ProfileUpdateActivity.this, UserLocationActivtiy.class));
            finish();
        }
    }

    public boolean emailValidateField(String fieldvalue) {
        String emailPattern = "^(([^_~`!#$%^&={}|/?+\\-<>()\\[\\]\\\\.,;:\\s@\"]+(\\.[^_*~`!#$%^&*={}|/?+\\-<>()\\[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        if (fieldvalue.matches(emailPattern)) {
            return true;
        } else {
            return false;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Permission.onRequestPermissionsResult(
                requestCode, permissions, grantResults, this);

        switch (requestCode) {
            case AppConstants.PERMISSION_READ_EXTERNAL_STORAGE : {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask != null && userChoosenTask.equals("Choose from Library")) {
                        galleryIntent();
                    } else if (userChoosenTask != null && userChoosenTask.equals("Take Photo")) {
                        cameraIntent();
                    }
                } else {
                }
                return;
            }
        }

    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST);
    }

    private void galleryIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_PHOTO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //image upload
        if (resultCode != 0 && requestCode == SELECT_PHOTO ) {
            mImageUri = data.getData();
            if (mImageUri == null) {
                showToast("Error in uploading image.Please try again.");
            } else {
                try {
                    fileName = FilePath.getPath(ProfileUpdateActivity.this, mImageUri);
                    bitmapImage = MediaStore.Images.Media.getBitmap(getContentResolver(), mImageUri);
                    bitmapImage.createScaledBitmap(bitmapImage, 400, 400, true);
                    profileImage.setImageBitmap(bitmapImage);
                    uploadFile(fileName, user_id);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        } else if (resultCode != 0 && requestCode == CAMERA_REQUEST) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
            mImageUri = getImageUri(ProfileUpdateActivity.this, photo);
            if (mImageUri == null) {
                showToast("Error in uploading image.Please try again.");
            } else {
                try {
                    fileName = getRealPathFromURI(mImageUri);
                    bitmapImage = MediaStore.Images.Media.getBitmap(getContentResolver(), mImageUri);
                    bitmapImage.createScaledBitmap(bitmapImage, 400, 400, true);
                    profileImage.setImageBitmap(bitmapImage);
                    uploadFile(fileName, user_id);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            bitmapImage = null;
        } else if (data == null) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            bitmapImage = null;
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                //Log.e("image uri", resultUri.toString());
                if (resultUri == null) {
                    showToast("Could not upload profile image. Please try again.");
                } else {
                    try {
                        bitmapImage = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUri);
                        profileImage.setImageBitmap(bitmapImage);
                        uploadFile(fileName, user_id);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    public void performCrop() {
        CropImage.activity(mImageUri)
                .setMinCropResultSize(600, 600)
                .setAspectRatio(1, 1)
                .setRequestedSize(400, 400)
                .setOutputCompressQuality(100)
                .setScaleType(CropImageView.ScaleType.CENTER_CROP)
                .start(this);
    }

    private void uploadFile(String filename, int entity_id) {

        try {

            File file = new File(filename);

            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);

            MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), reqFile);

            RequestBody name = RequestBody.create(MediaType.parse("text/plain"),"user");

            Call<UploadMedia> call = AppConstants.restAPI.uploadImage(body, entity_id, entity_id, name);

            call.enqueue(new Callback<UploadMedia>() {
                @Override
                public void onResponse(Call<UploadMedia> call, Response<UploadMedia> response) {

                    if (response.isSuccessful()) {
                        if (response != null) {
                            try {

                                imageName = response.body().data.name;
                                imageName = "http://cdn.mummysfood.in/" + imageName;

                                Log.d("ImageName", imageName);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        try {
                            Log.e("Error", response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<UploadMedia> call, Throwable t) {
                    dismissProgress();
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

    private int getRadioSelected() {

        int selectedId = radioAction.getCheckedRadioButtonId();
        int value = 0;

        if (selectedId == R.id.veg) {
            pf.saveIntForKey("UserFoodType", 0);
            value = 0;
            return value;

        } else {
            pf.saveIntForKey("UserFoodType", 1);
            value = 1;
            return value;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
