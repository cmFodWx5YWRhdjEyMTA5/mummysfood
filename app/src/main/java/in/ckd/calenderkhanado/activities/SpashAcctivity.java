package in.ckd.calenderkhanado.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.ckd.calenderkhanado.location.UserLocationActivtiy;
import in.ckd.calenderkhanado.R;
import in.ckd.calenderkhanado.base.BaseActivity;
import in.ckd.calenderkhanado.data.pref.PreferenceManager;

/**
 * Created by acer on 4/15/2018.
 */

public class SpashAcctivity extends BaseActivity
{

    @BindView(R.id.mainSpash)
    ProgressBar mainSpash;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);

        ButterKnife.bind(this);

        pf = new PreferenceManager(this,PreferenceManager.LOGIN_PREFERENCES_FILE);

        PreferenceManager pff = new PreferenceManager(this);

        int userId = pff.getIntForKey("user_id",0);

        String firstName = pff.getStringForKey("FirstName","");

        if (userId != 0)
        {
            showToast(String.valueOf(userId));

            if (!firstName.equalsIgnoreCase("Full"))
            {
                Intent intent = new Intent(SpashAcctivity.this,ProfileUpdateActivity.class);
                intent.putExtra("fullname","");
                intent.putExtra("email",pff.getStringForKey("email",""));
                intent.putExtra("profile_image",pff.getStringForKey("profileImage",""));
                intent.putExtra("mobile",pff.getStringForKey("mobile",""));
                intent.putExtra("logintype",pff.getStringForKey("loginType",""));
                 startActivity(intent);
                finish();
            }else
            {
                pf = new PreferenceManager(this);
                String currentAdd = pf.getStringForKey("CurrentAddress","");

                if (currentAdd != null &&!"".equalsIgnoreCase(currentAdd)){
                    startActivity(new Intent(SpashAcctivity.this,MainBottomBarActivity.class));
                    finish();
                }else{
                    startActivity(new Intent(SpashAcctivity.this,UserLocationActivtiy.class));
                    finish();
                }
            }

        }else {
            startActivity(new Intent(SpashAcctivity.this,LoginAndSignupActivity.class));
            finish();

        }
    }
}
