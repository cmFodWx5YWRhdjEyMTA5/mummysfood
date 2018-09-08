package in.mummysfood.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import in.mummysfood.R;
import in.mummysfood.data.pref.PreferenceManager;
import in.mummysfood.models.DashBoardModel;
import in.mummysfood.models.ProfileModel;
import in.mummysfood.utils.AppConstants;
import in.mummysfood.widgets.CkdTextview;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    @BindView(R.id.profile_image)
    CircularImageView profileImage;
    @BindView(R.id.profile_username)
    CkdTextview profileUsername;
    @BindView(R.id.profile_about)
    CkdTextview profileAbout;
    @BindView(R.id.is_vagitarian)
    SwitchCompat is_vagitarian;
    @BindView(R.id.user_rating_layout)
    LinearLayout user_rating_layout;
    @BindView(R.id.expert_layout)
    LinearLayout expert_layout;
    @BindView(R.id.active_time_layout)
    LinearLayout active_time_layout;
    @BindView(R.id.active_hour_see_more)
    CkdTextview active_hour_see_more;
    @BindView(R.id.kitchen_media_layout)
    LinearLayout kitchen_media_layout;
    @BindView(R.id.edit_about_img)
    ImageView edit_about_img;
    @BindView(R.id.update_profile_image)
    RelativeLayout update_profile_image;

    private Context context;
    private int userId;
    private DashBoardModel.Data userData;
    private PreferenceManager pf;
    private int loggedInUserId;

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
            active_hour_see_more.setVisibility(View.GONE);
            kitchen_media_layout.setVisibility(View.GONE);
            edit_about_img.setVisibility(View.VISIBLE);
            update_profile_image.setVisibility(View.VISIBLE);
        }else if (userData.type != null && userData.type.equalsIgnoreCase(AppConstants.CHEF)){
            user_rating_layout.setVisibility(View.VISIBLE);
            expert_layout.setVisibility(View.VISIBLE);
            active_time_layout.setVisibility(View.VISIBLE);
            active_hour_see_more.setVisibility(View.VISIBLE);
            kitchen_media_layout.setVisibility(View.VISIBLE);
            edit_about_img.setVisibility(View.GONE);
            update_profile_image.setVisibility(View.GONE);
        }

        if (userData.chef_detail != null && userData.chef_detail.about != null && !"".equalsIgnoreCase(userData.chef_detail.about)){
            profileAbout.setText(userData.chef_detail.about.trim());
        }

        if (userData.is_vagitarian == 0){
            is_vagitarian.setChecked(true);
        }else {
            is_vagitarian.setChecked(false);
        }

        if (userData.type != null && !userData.type.isEmpty() && userData.type.equalsIgnoreCase(AppConstants.CHEF)){

        }else if (userData.type != null && !userData.type.isEmpty() && userData.type.equalsIgnoreCase(AppConstants.SEEKER)){

        }
    }
}
