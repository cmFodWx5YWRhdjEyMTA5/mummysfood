package in.ckd.calenderkhanado.fragments;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import butterknife.OnClick;
import in.ckd.calenderkhanado.activities.HelpSupportActivity;
import in.ckd.calenderkhanado.activities.ManageAddressesActivity;
import in.ckd.calenderkhanado.activities.ManagePaymentActivity;
import in.ckd.calenderkhanado.R;
import in.ckd.calenderkhanado.activities.LoginAndSignupActivity;
import in.ckd.calenderkhanado.activities.ProfileUpdateActivity;
import in.ckd.calenderkhanado.base.BaseFragment;
import in.ckd.calenderkhanado.data.network.model.LoginRequest;
import in.ckd.calenderkhanado.data.network.model.MediaRequest;
import in.ckd.calenderkhanado.data.network.model.UploadMedia;
import in.ckd.calenderkhanado.data.pref.PreferenceManager;
import in.ckd.calenderkhanado.models.ProfileModel;
import in.ckd.calenderkhanado.utils.AppConstants;
import in.ckd.calenderkhanado.utils.CapsName;
import in.ckd.calenderkhanado.utils.FilePath;
import in.ckd.calenderkhanado.widgets.CkdTextview;

import com.google.firebase.auth.FirebaseAuth;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends BaseFragment {

    @BindView(R.id.profile_image)
    CircularImageView profileImage;

    @BindView(R.id.profile_username)
    CkdTextview profileUsername;

    private Context context;
    private int userId;
    //private String type;
    private ProfileModel.Data userData;
    public static List<ProfileModel.Addresses> addressesList;
    private int loggedInUserId;
    private PreferenceManager pf;
    private PreferenceManager pfpp;
    private FirebaseAuth mAuth;

    //for image upload
    private final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private final int SELECT_PHOTO = 1;
    private final int CAMERA_REQUEST = 1888;
    private Uri mImageUri;
    private Bitmap bitmapImage = null;
    private String imageName;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_user_profile, container, false);

        context = inflater.getContext();
        ButterKnife.bind(this,rootView);

        addressesList = new ArrayList<>();

        pf = new PreferenceManager(context, PreferenceManager.LOGIN_PREFERENCES_FILE);
        pfpp = new PreferenceManager(context);
        loggedInUserId = pf.getIntForKey(PreferenceManager.USER_ID,0);
        loggedInUserId = pfpp.getIntForKey("user_id",0);

        userId = loggedInUserId;
      /*  Bundle bundle = getArguments();
        if (bundle != null){
            userId = bundle.getInt("user_id",0);
            //type = bundle.getString("type",null);
            if (userId == 0) {
                userId = loggedInUserId;
            }
        }*/

        if (loggedInUserId != 0){
            Call<ProfileModel> profileData = AppConstants.restAPI.getProfileUserData(userId);
            profileData.enqueue(new Callback<ProfileModel>() {
                @Override
                public void onResponse(Call<ProfileModel> call, Response<ProfileModel> response) {

                    if (response != null){
                        ProfileModel res = response.body();
                        if (res.status != null) {
                            if ( res.status.equals(AppConstants.SUCCESS)){

                                try {
                                    userData = res.data.get(0);
                                    addressesList = userData.addresses;

                                    //Log.d("ListSize",String.valueOf(userData.addresses.size()));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
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
            String name = CapsName.CapitalizeFullName(userData.f_name.trim());
            profileUsername.setText(name);
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
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_ID_MULTIPLE_PERMISSIONS);
                }else {
                    if (items[item].equals("Take Photo")) {

                        cameraIntent();

                    } else if (items[item].equals("Choose from Library")) {

                        galleryIntent();

                    } else if (items[item].equals("Cancel")) {
                        dialog.dismiss();
                    }
                }

            }
        });
        builder.show();
    }

    @OnClick(R.id.manage_address)
    public void ManageAddress(){
        Intent intent=new Intent(context,ManageAddressesActivity.class);
        context.startActivity(intent);
    }

    @OnClick(R.id.manageProfile)
    public void mana(){
        Intent intent=new Intent(context,ProfileUpdateActivity.class);


        try {
            intent.putExtra("logintype","Profile");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            intent.putExtra("fullname",userData.f_name);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            intent.putExtra("email",userData.email);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            intent.putExtra("profile_image",userData.profile_image);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            intent.putExtra("mobile",userData.mobile);
        } catch (Exception e) {
            e.printStackTrace();
        }

      startActivity(intent);
    }


    @OnClick(R.id.your_orders)
    public void RedirectToOrders(){
        Fragment frag = new OrderStatusFragment();
        if (frag != null) {
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, frag, frag.getTag());
            ft.commit();
        }
    }

    @OnClick(R.id.manage_payment_options)
    public void ManagePaymentOptions(){
        Intent intent=new Intent(context,ManagePaymentActivity.class);
        context.startActivity(intent);
    }

    @OnClick(R.id.help_support)
    public void HelpSupport(){
        Intent intent=new Intent(context,HelpSupportActivity.class);
        context.startActivity(intent);
    }

    @OnClick(R.id.logout)
    public void LogOut(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Are you sure you want to log out?")
                .setTitle("Logout")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mAuth = FirebaseAuth.getInstance();

                        mAuth.signOut();

                        //--- clear user  preferences  ----//
                        pf.clearPref(context, pf.LOGIN_PREFERENCES_FILE);
                        pf.clearPref(context, pf.ORDER_PREFERENCES_FILE);

                        //-- start new with login --//
                        Intent intent=new Intent(context,LoginAndSignupActivity.class);
                        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                        ((AppCompatActivity)context).finish();
                        showToast(context.getString(R.string.toast_logout));
                    }})
                .setNegativeButton("NO",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {}});

        // Create the AlertDialog object and return it
        AlertDialog alert = builder.create();
        alert.show();

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
                uploadFile(mImageUri);
            }
        } else if (resultCode != 0 && requestCode == CAMERA_REQUEST) {
            if (mImageUri == null) {
                showToast("Error in uploading image.Please try again.");
            } else {
                try {
                    bitmapImage = MediaStore.Images.Media.getBitmap(context.getContentResolver(), mImageUri);
                    bitmapImage.createScaledBitmap(bitmapImage, 400, 400, true);
                    performCrop();
                    uploadFile(mImageUri);

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

    private void uploadFile(Uri mImageUri) {

        String filename = "";
        try{
            filename = FilePath.getPath(context, mImageUri);

        }catch (NullPointerException e){
            e.printStackTrace();
        }

        File file = new File(filename);
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), reqFile);

        MediaRequest mediaRequest = new MediaRequest();
        mediaRequest.user_id = userId;
        mediaRequest.entity_id = 0;
        mediaRequest.entity_type = "user";
        mediaRequest.image = body;
        Call<UploadMedia> call = AppConstants.restAPI.uploadImage(body,0,userId,"user");

        call.enqueue(new Callback<UploadMedia>() {
            @Override
            public void onResponse(Call<UploadMedia> call, Response<UploadMedia> response) {

                if (response.isSuccessful()) {
                    if (response != null){
                        try {

                            imageName = response.body().data.name;
                            imageName = "http://cdn.mummysfood.in/"+imageName;
                            Glide.with(context).load(imageName).into(profileImage);

                            updateUserProfilePic(response.body().data.name);

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
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateUserProfilePic(String image_name) {
        LoginRequest request = new LoginRequest();

        request.profile_image = image_name;

        Call<ResponseBody> loginRequestCall = AppConstants.restAPI.updateUserInfo(userId,request);

        loginRequestCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {

                    try {
                        String resp = response.body().string();
                        JSONObject json = new JSONObject(resp);
                        Log.e("status", json.getString("status"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
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
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Response is failure",""+t);
            }
        });
    }
}
