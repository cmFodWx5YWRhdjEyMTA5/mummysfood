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
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import butterknife.OnClick;
import in.mummysfood.R;
import in.mummysfood.activities.ProfileUpdateActivity;
import in.mummysfood.adapters.HomeSpecialCardAdapter;
import in.mummysfood.adapters.ReviewAdapter;
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

public class ProfileFragment extends BaseFragment {

    @BindView(R.id.profile_image)
    CircularImageView profileImage;
    @BindView(R.id.profile_username)
    CkdTextview profileUsername;
    @BindView(R.id.profile_about)
    CkdTextview profileAbout;
    /*@BindView(R.id.is_vagitarian)
    SwitchCompat is_vagitarian;*/
    @BindView(R.id.user_rating_layout)
    LinearLayout user_rating_layout;
    @BindView(R.id.expert_layout)
    LinearLayout expert_layout;
    @BindView(R.id.active_time_layout)
    LinearLayout active_time_layout;
//    @BindView(R.id.active_hour_see_more)
//    CkdTextview active_hour_see_more;
    @BindView(R.id.kitchen_media_layout)
    LinearLayout kitchen_media_layout;
//    @BindView(R.id.edit_about_img)
//    ImageView edit_about_img;
//    @BindView(R.id.update_profile_image)
//    RelativeLayout update_profile_image;
    @BindView(R.id.review_recyclerview)
    RecyclerView review_recyclerview;

    private Context context;
    private int userId;
    private DashBoardModel.Data userData;
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

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        context = inflater.getContext();
        ButterKnife.bind(this,rootView);

        pf = new PreferenceManager(context, PreferenceManager.LOGIN_PREFERENCES_FILE);
        loggedInUserId = pf.getIntForKey(PreferenceManager.USER_ID,0);

        Bundle bundle = getArguments();
        if (bundle != null){
            userId = bundle.getInt("user_id",0);
            if (userId == loggedInUserId)
                userId = loggedInUserId;
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
        return rootView;
    }

    private void prepareUserData() {
        if (userData.profile_image != null && !userData.profile_image.isEmpty()){
            Glide.with(context).load(userData.profile_image).into(profileImage);
        }

        if (userData.f_name != null && !"".equalsIgnoreCase(userData.f_name)){
            profileUsername.setText(userData.f_name.trim());
        }

        if (userId == loggedInUserId && userData.type != null && !userData.type.equalsIgnoreCase(AppConstants.CHEF)){
            user_rating_layout.setVisibility(View.GONE);
            expert_layout.setVisibility(View.GONE);
            active_time_layout.setVisibility(View.GONE);
//            active_hour_see_more.setVisibility(View.GONE);
            kitchen_media_layout.setVisibility(View.GONE);
//            edit_about_img.setVisibility(View.VISIBLE);
//            update_profile_image.setVisibility(View.VISIBLE);
        }else if (userData.type != null && userData.type.equalsIgnoreCase(AppConstants.CHEF)){
            user_rating_layout.setVisibility(View.VISIBLE);
            expert_layout.setVisibility(View.VISIBLE);
            active_time_layout.setVisibility(View.VISIBLE);
//            active_hour_see_more.setVisibility(View.VISIBLE);
            kitchen_media_layout.setVisibility(View.VISIBLE);
//            edit_about_img.setVisibility(View.GONE);
//            update_profile_image.setVisibility(View.GONE);
        }

        if (userData.chef_detail != null && userData.chef_detail.about != null && !"".equalsIgnoreCase(userData.chef_detail.about)){
            profileAbout.setText(userData.chef_detail.about.trim());
        }

        /*if (userData.is_vagitarian == 0){
            is_vagitarian.setChecked(true);
        }else {
            is_vagitarian.setChecked(false);
        }*/

        if (userData.type != null && !userData.type.isEmpty() && userData.type.equalsIgnoreCase(AppConstants.CHEF)){

        }else if (userData.type != null && !userData.type.isEmpty() && userData.type.equalsIgnoreCase(AppConstants.SEEKER)){

        }

        ReviewListPrepare();
        linearLayoutManager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
        review_recyclerview.setHasFixedSize(true);
        review_recyclerview.setLayoutManager(linearLayoutManager);
        review_recyclerview.setItemAnimator(new DefaultItemAnimator());
        ReviewAdapter reviewAdapter = new ReviewAdapter(getActivity(),reviewDataList);
        review_recyclerview.setAdapter(reviewAdapter);
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
                .start(getActivity());
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
