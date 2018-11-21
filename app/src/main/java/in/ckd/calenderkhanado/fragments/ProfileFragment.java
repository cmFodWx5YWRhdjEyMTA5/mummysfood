package in.ckd.calenderkhanado.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import butterknife.OnClick;
import in.ckd.calenderkhanado.HelpSupportActivity;
import in.ckd.calenderkhanado.ManageAddressesActivity;
import in.ckd.calenderkhanado.ManagePaymentActivity;
import in.ckd.calenderkhanado.R;
import in.ckd.calenderkhanado.activities.LoginAndSignupActivity;
import in.ckd.calenderkhanado.base.BaseFragment;
import in.ckd.calenderkhanado.data.pref.PreferenceManager;
import in.ckd.calenderkhanado.models.ProfileModel;
import in.ckd.calenderkhanado.utils.AppConstants;
import in.ckd.calenderkhanado.widgets.CkdTextview;

import com.google.firebase.auth.FirebaseAuth;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    private FirebaseAuth mAuth;

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
        loggedInUserId = pf.getIntForKey(PreferenceManager.USER_ID,0);

        Bundle bundle = getArguments();
        if (bundle != null){
            userId = bundle.getInt("user_id",0);
            //type = bundle.getString("type",null);
            if (userId == 0) {
                userId = loggedInUserId;
            }
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

                                try {
                                    addressesList = userData.addresses;

                                    Log.d("ListSize",String.valueOf(userData.addresses.size()));
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

        if (userData.name != null && !"".equalsIgnoreCase(userData.name)){
            profileUsername.setText(userData.name.trim());
        }
    }

    @OnClick(R.id.manage_address)
    public void ManageAddress(){
        Intent intent=new Intent(context,ManageAddressesActivity.class);
        context.startActivity(intent);
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
}
