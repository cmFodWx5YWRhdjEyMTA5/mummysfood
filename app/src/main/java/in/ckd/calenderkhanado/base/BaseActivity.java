package in.ckd.calenderkhanado.base;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import in.ckd.calenderkhanado.R;
import in.ckd.calenderkhanado.adapters.OnBoardingViewPagerAdapter;
import in.ckd.calenderkhanado.data.pref.PreferenceManager;
import me.relex.circleindicator.CircleIndicator;


public class BaseActivity extends AppCompatActivity {

    public ProgressDialog pd;
    public PreferenceManager pf;
    public Toolbar toolbar;
    private static final Integer[] IMAGES = {R.mipmap.sign_up_bg_01, R.mipmap.sign_up_bg_02,R.mipmap.sign_up_bg_01, R.mipmap.sign_up_bg_02};
    private ArrayList<Integer> ImagesArray = new ArrayList<Integer>();
    private OnBoardingViewPagerAdapter onBoardingViewPagerAdapter;
    private int currentPage = 0;
    private int NUM_PAGES = 0;
    private Runnable Update;
    private Handler handler;


    public void showProgress(String msg)
    {
        if (pd != null && pd.isShowing()) {
            dismissProgress();
        } else {
            pd = getProgressDialog(this, msg);
            pd.show();
        }
    }

    public static ProgressDialog getProgressDialog(Context mCon, String Msg) {

        ProgressDialog pd = new ProgressDialog(mCon);

        pd.setCancelable(false);
        pd.setMessage(Msg);

        return pd;

    }

    public void dismissProgress() {
        if (pd != null) {
            pd.dismiss();
            pd = null;
        }
    }

    public void showSnackBar(View view, String msg){

        Snackbar snackbar = Snackbar
                .make(view,msg, Snackbar.LENGTH_LONG);
        snackbar.setActionTextColor(Color.parseColor("#ffffff"));

        snackbar.show();
    }

    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    public void showDialogBox(String msg, String btn_txt) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage(msg);
        alert.setPositiveButton(btn_txt, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (dialogInterface != null){
                    dialogInterface.dismiss();
                }
            }
        });
        alert.show();
    }

    // this listener will be called when there is change in firebase user session
    @Override
    public void onBackPressed() {
        return;
    }


    public boolean checkInternetConenction() {
        // get Connectivity Manager object to check connection
        ConnectivityManager connec
                =(ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        // Check for network connections
        if ( connec.getNetworkInfo(0).getState() ==
                NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() ==
                        NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() ==
                        NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED ) {
            return true;
        }else if (
                connec.getNetworkInfo(0).getState() ==
                        NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() ==
                                NetworkInfo.State.DISCONNECTED  ) {
            return false;
        }
        return false;
    }


    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);


        try {
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setupToolbar();
            //pf = new PreferenceManager(this);
        }catch (Exception e){
            e.printStackTrace();
        }


    }


    protected void setupToolbar() {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
    }

    protected void setupBackButton() {
        if (toolbar != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setIcon(android.R.color.transparent);


        }
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    private void showSnackBar(String message) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                message, Snackbar.LENGTH_SHORT);
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView
                .findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(ContextCompat.getColor(this, android.R.color.white));
        snackbar.show();
    }






    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    public void ImageSlider(final ViewPager signUpViewpager, CircleIndicator viewPagerIndicator) {

        for (int i = 0; i < IMAGES.length; i++)
            ImagesArray.add(IMAGES[i]);


        onBoardingViewPagerAdapter = new OnBoardingViewPagerAdapter(this, ImagesArray);
        signUpViewpager.setAdapter(onBoardingViewPagerAdapter);

        signUpViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });


        viewPagerIndicator.setViewPager(signUpViewpager);


        NUM_PAGES = IMAGES.length + 1;

        // Auto start of viewpager
        handler = new Handler();
        Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                signUpViewpager.setCurrentItem(currentPage++, true);
            }
        };

        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 2000, 2000);




    }

    public static String getDate(String dateStr) throws ParseException {

        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = inputFormat.parse(dateStr);
        SimpleDateFormat df = new SimpleDateFormat("dd MMMM, yyyy");
        String formattedDate = df.format(date);
        return formattedDate;

    }

}

