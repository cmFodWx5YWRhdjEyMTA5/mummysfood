package in.ckd.calenderkhanado.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.text.Html;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import in.ckd.calenderkhanado.Location.UserLocationActivtiy;
import in.ckd.calenderkhanado.R;
import in.ckd.calenderkhanado.base.BaseActivity;
import in.ckd.calenderkhanado.data.network.model.LoginRequest;
import in.ckd.calenderkhanado.data.network.model.MediaRequest;
import in.ckd.calenderkhanado.data.network.model.UploadMedia;
import in.ckd.calenderkhanado.data.pref.PreferenceManager;
import in.ckd.calenderkhanado.models.DashBoardModel;
import in.ckd.calenderkhanado.models.UserInsert;
import in.ckd.calenderkhanado.utils.AppConstants;
import in.ckd.calenderkhanado.utils.FilePath;
import in.ckd.calenderkhanado.widgets.CkdEditText;
import in.ckd.calenderkhanado.widgets.CkdTextview;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Date;

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

    public final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;

    private int email_verification;
    private String loginType, userGender, mobileNumber, userEmail, userName, userProfileImage;

    //for image upload
    private String userChoosenTask;
    private final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    private final int SELECT_PHOTO = 1;
    private final int CAMERA_REQUEST = 1888;
    private Uri mImageUri;
    private Bitmap bitmapImage = null;
    private PreferenceManager pf;
    private PreferenceManager pfd;

    private String imageName;
    private int user_id;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_update);
        ButterKnife.bind(this);

        pf = new PreferenceManager(this,PreferenceManager.LOGIN_PREFERENCES_FILE);

        pfd = new PreferenceManager(this);

        user_id = pfd.getIntForKey("user_id",0);



        Bundle intent = getIntent().getExtras();
        if(intent != null){
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

        if (loginType != null && loginType.equalsIgnoreCase(AppConstants.GOOGLE)){
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


    }

    @OnClick({R.id.profile_image})
    public void uploadProfileImage(){
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (ContextCompat.checkSelfPermission(ProfileUpdateActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_ID_MULTIPLE_PERMISSIONS);
                }else {
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

            }
        });
        builder.show();
    }

    private void cameraIntent() {
        final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(Environment.getExternalStorageDirectory() + "/DCIM/", "image" + new Date().getTime() + ".png");
        Uri imgUri = FileProvider.getUriForFile(ProfileUpdateActivity.this,
                ProfileUpdateActivity.this.getApplicationContext()
                        .getPackageName() + ".provider",
                file);
        mImageUri = imgUri;
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
        startActivityForResult(intent, CAMERA_REQUEST);
    }

    private void galleryIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_PHOTO);
    }

    @OnClick(R.id.next_upload_profile)
    public void UpdateUserData(){
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

            if (!fullName.getText().toString().trim().isEmpty() &&!emailId.getText().toString().trim().isEmpty() ){
                nexttoupload();
            }else {
                showToast("All information should be valid");
            }

        }
    }

    private void nexttoupload() {


        LoginRequest request = new LoginRequest();

        request.f_name = fullName.getText().toString();
        request.email = emailId.getText().toString();
        request.mobile = mobileNumber;
        request.profile_image = imageName;
        request.type = "Seeker";
        request.is_email_verified = 1;
        request.is_mobile_verified = 1;
        request.is_vagitarian = 0;
        request.id = user_id;
        request.os = "";
        request.l_name = "";

        Call<ResponseBody> loginRequestCall = AppConstants.restAPI.updateUserInfo(user_id,request);

        loginRequestCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {



                if (response != null){
                    if (response.isSuccessful()){
                        try {
                            String resp = response.body().string();

                            JSONObject json = new JSONObject(resp)
                                    ;
                            UserInsert.Data data = new UserInsert.Data();

                            if (json.getString("status").equalsIgnoreCase(AppConstants.SUCCESS)) {
                                data.id = json.getJSONArray("data").getJSONObject(0).getInt("id");
                                data.mobile = json.getJSONArray("data").getJSONObject(0).getString("mobile");
                                data.f_name = json.getJSONArray("data").getJSONObject(0).getString("f_name");
                                data.profile_image = json.getJSONArray("data").getJSONObject(0).getString("profile_image");
                                data.email =json.getJSONArray("data").getJSONObject(0).getString("email");
                                sharePrefrenceIntentActivity(data);

                            }else if(json.getString("status").equalsIgnoreCase(AppConstants.ALREADY)){
                                data.id = json.getJSONArray("data").getJSONObject(0).getInt("id");
                                data.mobile = json.getJSONArray("data").getJSONObject(0).getString("mobile");
                                data.f_name = json.getJSONArray("data").getJSONObject(0).getString("f_name");
                                data.profile_image = json.getJSONArray("data").getJSONObject(0).getString("profile_image");
                                data.email =json.getJSONArray("data").getJSONObject(0).getString("email");
                                sharePrefrenceIntentActivity(data);
                            }else {
                                showDialogBox("Authentication Failed","OK");
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else{
                        try {
                            Log.e("status",response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }else {
                    Log.e("response","null ");

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Response is failure",""+t);
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
        String savedLocation = pf.getStringForKey("CurrentAddress","");
        if (savedLocation != null &&savedLocation.equalsIgnoreCase("gotitlocation")){
        startActivity(new Intent(ProfileUpdateActivity.this,MainBottomBarActivity.class));
        finish();
       }else{
            startActivity(new Intent(ProfileUpdateActivity.this,UserLocationActivtiy.class));
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //image upload
        if (resultCode != 0 && requestCode == SELECT_PHOTO ) {
            mImageUri = data.getData();
            if (mImageUri == null) {
                showToast("Error in uploading image.Please try again.");
            } else {
                uploadFile(mImageUri);
                performCrop();
            }
        } else if (resultCode != 0 && requestCode == CAMERA_REQUEST) {
            if (mImageUri == null) {
                showToast("Error in uploading image.Please try again.");
            } else {
                try {
                    bitmapImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mImageUri);
                    bitmapImage.createScaledBitmap(bitmapImage, 400, 400, true);

                    performCrop();

                    uploadFile(mImageUri);

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
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    profileImage.setImageBitmap(bitmapImage);
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

    private void uploadFile(Uri mImageUri) {

        //   showProgress(getLangMappingBasedOnKeyOnevalue("loading"));
        String filename = "";

        try{

            filename = FilePath.getPath(this, mImageUri);

            //  filename = compressImage(mImageUri.toString());

        }catch (NullPointerException e){
            e.printStackTrace();
        }

        File file = new File(filename);

        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);

        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), reqFile);


        MediaRequest mediaRequest = new MediaRequest();
        mediaRequest.user_id = user_id;
        mediaRequest.entity_id = 0;
        mediaRequest.entity_type = "user";
        mediaRequest.image = body;
        Call<UploadMedia> call = AppConstants.restAPI.uploadImage(body,0,user_id,"user");

        call.enqueue(new Callback<UploadMedia>() {
            @Override
            public void onResponse(Call<UploadMedia> call, Response<UploadMedia> response) {

                if (response.isSuccessful()) {
                    if (response != null){
                        try {

                            imageName = response.body().data.name;
                            imageName = "http://cdn.mummysfood.in/"+imageName;

                            Log.d("ImageName",imageName);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }else{
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
    }
}
