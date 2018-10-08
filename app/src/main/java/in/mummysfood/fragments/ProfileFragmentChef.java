package in.mummysfood.fragments;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import butterknife.OnClick;
import in.mummysfood.R;
import in.mummysfood.activities.ProfileUpdateActivity;
import in.mummysfood.adapters.FoodDataAdapter;
import in.mummysfood.adapters.HomeSpecialCardAdapter;
import in.mummysfood.adapters.ReviewAdapter;
import in.mummysfood.base.BaseActivity;
import in.mummysfood.base.BaseFragment;
import in.mummysfood.data.pref.PreferenceManager;
import in.mummysfood.models.DashBoardModel;
import in.mummysfood.models.ProfileModel;
import in.mummysfood.models.ReviewModel;
import in.mummysfood.utils.AppConstants;
import in.mummysfood.widgets.CkdTextview;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.theartofdev.edmodo.cropper.CropImage.hasPermissionInManifest;

public class ProfileFragmentChef extends BaseActivity {

    @BindView(R.id.profile_image)
    CircularImageView profileImage;

    @BindView(R.id.profile_username)
    CkdTextview profileUsername;

    @BindView(R.id.profile_about)
    CkdTextview profileAbout;

    @BindView(R.id.user_rating_layout)
    LinearLayout user_rating_layout;

    @BindView(R.id.expert_layout)
    LinearLayout expert_layout;

    @BindView(R.id.kitchen_media_layout)
    LinearLayout kitchen_media_layout;

    @BindView(R.id.review_recyclerview)
    RecyclerView review_recyclerview;

    @BindView(R.id.user_rating_txt)
    CkdTextview userRating;

    @BindView(R.id.is_vegitarian)
    CkdTextview is_vegitarian;

    @BindView(R.id.active_time_layout)
    LinearLayout activeTimeLayout;

    @BindView(R.id.first_day)
    LinearLayout first_day;
    @BindView(R.id.second_day)
    LinearLayout second_day;
    @BindView(R.id.third_day)
    LinearLayout third_day;
    @BindView(R.id.fourth_day)
    LinearLayout fourth_day;
    @BindView(R.id.fifth_day)
    LinearLayout fifth_day;
    @BindView(R.id.sixth_day)
    LinearLayout sixth_day;
    @BindView(R.id.seventh_day)
    LinearLayout seventh_day;

    @BindView(R.id.first_day_open)
    CkdTextview first_day_open;
    @BindView(R.id.second_day_open)
    CkdTextview second_day_open;
    @BindView(R.id.third_day_open)
    CkdTextview third_day_open;
    @BindView(R.id.fourth_day_open)
    CkdTextview fourth_day_open;
    @BindView(R.id.fifth_day_open)
    CkdTextview fifth_day_open;
    @BindView(R.id.sixth_day_open)
    CkdTextview sixth_day_open;
    @BindView(R.id.seventh_day_open)
    CkdTextview seventh_day_open;

    @BindView(R.id.first_day_close)
    CkdTextview first_day_close;
    @BindView(R.id.second_day_close)
    CkdTextview second_day_close;
    @BindView(R.id.third_day_close)
    CkdTextview third_day_close;
    @BindView(R.id.fourth_day_close)
    CkdTextview fourth_day_close;
    @BindView(R.id.fifth_day_close)
    CkdTextview fifth_day_close;
    @BindView(R.id.sixth_day_close)
    CkdTextview sixth_day_close;
    @BindView(R.id.seventh_day_close)
    CkdTextview seventh_day_close;

    @BindView(R.id.food_recyclerview)
    RecyclerView foodRecyclerview;

    private Context context;
    private int userId;
    private ProfileModel.Data userData;
    private int loggedInUserId;
    //for image upload
    private String userChoosenTask;
    private final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    private final int SELECT_PHOTO = 1;
    private final int CAMERA_REQUEST = 1888;
    private Uri mImageUri;
    private Bitmap bitmapImage = null;
    private PreferenceManager pf;
    private ArrayList<ReviewModel> reviewDataList;
    private LinearLayoutManager linearLayoutManager;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_profile);
        ButterKnife.bind(this);


        review_recyclerview.setNestedScrollingEnabled(false);
//        pf = new PreferenceManager(context, PreferenceManager.LOGIN_PREFERENCES_FILE);

     //   loggedInUserId = pf.getIntForKey(PreferenceManager.USER_ID,0);


        if (getIntent() != null)
        {
            userId = getIntent().getIntExtra("user_id",0);
        }

        if (userId != 0){
            Call<ProfileModel> profileData = AppConstants.restAPI.getProfileUserData(userId);
            profileData.enqueue(new Callback<ProfileModel>() {
                @Override
                public void onResponse(Call<ProfileModel> call, Response<ProfileModel> response) {

                    if (response != null){
                        ProfileModel res = response.body();
                        if (res.status != null) {
                            if ( res.status.equals(AppConstants.SUCCESS)){
                                userData = res.data.get(0);
                                prepareUserData();

                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<ProfileModel> call, Throwable t) {
                    Log.e("error",""+t);
                }
            });
        }

    }

    private void prepareUserData() {
        if (userData.profile_image != null && !userData.profile_image.isEmpty()){
            Glide.with(context).load(userData.profile_image).into(profileImage);
        }

        if (userData.name != null && !"".equalsIgnoreCase(userData.name)){
            profileUsername.setText(userData.name.trim());
        }

        if (userId == loggedInUserId && userData.type != null && !userData.type.equalsIgnoreCase(AppConstants.CHEF)){
            user_rating_layout.setVisibility(View.GONE);
            expert_layout.setVisibility(View.GONE);
            //active_time_layout.setVisibility(View.GONE);
            kitchen_media_layout.setVisibility(View.GONE);

        }else if (userData.type != null && userData.type.equalsIgnoreCase(AppConstants.CHEF)){
            user_rating_layout.setVisibility(View.VISIBLE);
            expert_layout.setVisibility(View.VISIBLE);
            //active_time_layout.setVisibility(View.VISIBLE);
            kitchen_media_layout.setVisibility(View.VISIBLE);
            userRating.setText(""+userData.chef_detail.cooking_score);

            //active days/hours
            ActiveTimePrepare();

            //food
            FoodDataPrepare();
            //review
            ReviewListPrepare();
            linearLayoutManager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
            review_recyclerview.setHasFixedSize(true);
            review_recyclerview.setLayoutManager(linearLayoutManager);
            review_recyclerview.setItemAnimator(new DefaultItemAnimator());
            ReviewAdapter reviewAdapter = new ReviewAdapter(this,reviewDataList);
            review_recyclerview.setAdapter(reviewAdapter);
        }

        if (userData.chef_detail != null && userData.chef_detail.about != null && !"".equalsIgnoreCase(userData.chef_detail.about)){
            profileAbout.setText(userData.chef_detail.about.trim());
        }

        if (userData.is_vagitarian == 0){
            is_vegitarian.setText(getString(R.string.vegetarian));
            is_vegitarian.setTextColor(getResources().getColor(R.color.green));
        }else {
            is_vegitarian.setText(getString(R.string.non_vegetarian));
            is_vegitarian.setTextColor(getResources().getColor(R.color.red));
        }
    }

    private void FoodDataPrepare() {
        linearLayoutManager = new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false);
        foodRecyclerview.setHasFixedSize(true);
        foodRecyclerview.setLayoutManager(linearLayoutManager);
        foodRecyclerview.setItemAnimator(new DefaultItemAnimator());
        FoodDataAdapter foodDataAdapter = new FoodDataAdapter(this,userData.food_detail.food_media);
        foodRecyclerview.setAdapter(foodDataAdapter);
    }

    private void ActiveTimePrepare() {
        ProfileModel.Chef_open_days activeTimeData = userData.chef_open_days;
        if (!activeTimeData.mon_open_hr.isEmpty())
            first_day_open.setText(activeTimeData.mon_open_hr);
        if (!activeTimeData.mon_close_hr.isEmpty())
            first_day_close.setText(activeTimeData.mon_close_hr);

        if (!activeTimeData.tues_open_hr.isEmpty())
            second_day_open.setText(activeTimeData.tues_open_hr);
        if (!activeTimeData.tues_close_hr.isEmpty())
            second_day_close.setText(activeTimeData.tues_close_hr);

        if (!activeTimeData.wed_open_hr.isEmpty())
            third_day_open.setText(activeTimeData.wed_open_hr);
        if (!activeTimeData.wed_close_hr.isEmpty())
            third_day_close.setText(activeTimeData.wed_close_hr);

        if (!activeTimeData.thus_open_hr.isEmpty())
            fourth_day_open.setText(activeTimeData.thus_open_hr);
        if (!activeTimeData.thus_close_hr.isEmpty())
            fourth_day_close.setText(activeTimeData.thus_close_hr);

        if (!activeTimeData.fri_open_hr.isEmpty())
            fifth_day_open.setText(activeTimeData.fri_open_hr);
        if (!activeTimeData.fri_close_hr.isEmpty())
            fifth_day_close.setText(activeTimeData.fri_close_hr);

        if (!activeTimeData.sat_open_hr.isEmpty())
            sixth_day_open.setText(activeTimeData.sat_open_hr);
        if (!activeTimeData.sat_close_hr.isEmpty())
            sixth_day_close.setText(activeTimeData.sat_close_hr);

        if (!activeTimeData.sun_open_hr.isEmpty())
            seventh_day_open.setText(activeTimeData.sun_open_hr);
        if (!activeTimeData.sun_close_hr.isEmpty())
            seventh_day_close.setText(activeTimeData.sun_close_hr);

    }

    private void ReviewListPrepare() {
        reviewDataList = new ArrayList<>();
        for (int i = 0; i<5; i++){
            reviewDataList.add(new ReviewModel());
        }
    }

    @OnClick({R.id.profile_image})
    public void uploadProfileImage(){
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = checkPermission(context);
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
        Uri imgUri = FileProvider.getUriForFile(context,
                context.getApplicationContext()
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
                    bitmapImage = MediaStore.Images.Media.getBitmap(context.getContentResolver(), mImageUri);
                    bitmapImage.createScaledBitmap(bitmapImage, 400, 400, true);
                    performCrop();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            bitmapImage = null;
        } else if (data == null) {
            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
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
                        bitmapImage = MediaStore.Images.Media.getBitmap(context.getContentResolver(), resultUri);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
