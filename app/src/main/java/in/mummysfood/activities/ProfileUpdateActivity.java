package in.mummysfood.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import in.mummysfood.Location.UserLocationActivtiy;
import in.mummysfood.R;
import in.mummysfood.base.BaseActivity;
import in.mummysfood.data.network.model.LoginRequest;
import in.mummysfood.data.pref.PreferenceManager;
import in.mummysfood.models.DashBoardModel;
import in.mummysfood.utils.AppConstants;
import in.mummysfood.widgets.CkdButton;
import in.mummysfood.widgets.CkdEditText;
import in.mummysfood.widgets.CkdTextview;
import com.google.firebase.auth.FirebaseUser;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnTextChanged;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_update);
        ButterKnife.bind(this);

        pf = new PreferenceManager(this,PreferenceManager.LOGIN_PREFERENCES_FILE);

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
                boolean result = checkPermission(ProfileUpdateActivity.this);
                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
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
        //request.profile_image = user.getPhotoUrl().toString();
        request.type = "Seeker";
        request.is_email_verified = 1;
        request.is_mobile_verified = 1;
        request.is_vagitarian = 0;
        request.os = "";
        request.l_name = "";

        //LoginAndSignupActivity
    }

    private void saveSharePrefrenceData(DashBoardModel.Data data) {
        pf.saveIntForKey(PreferenceManager.USER_ID, data.id);
        pf.saveStringForKey(PreferenceManager.FIRST_NM, data.f_name);
        pf.saveStringForKey(PreferenceManager.USER_PROFILE_PIC, data.profile_image);
        pf.saveStringForKey(PreferenceManager.USER_EMAIl_Id, data.email);
        pf.saveStringForKey(PreferenceManager.USER_MOBILE, data.mobile);
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

    //permissions for image selection
    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean checkPermission(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (hasPermissionInManifest(context, Manifest.permission.READ_EXTERNAL_STORAGE) && hasPermissionInManifest(context, Manifest.permission.CAMERA)) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE) && ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.CAMERA)) {
                    android.app.AlertDialog.Builder alertBuilder = new android.app.AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(DialogInterface dialog, int which) {
                            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    android.app.AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
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
