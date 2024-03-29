package com.mf.mumizzfood.activities;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.mf.mumizzfood.R;
import com.mf.mumizzfood.base.BaseActivity;
import com.mf.mumizzfood.data.pref.PreferenceManager;
import com.mf.mumizzfood.fragments.HomeFragment;
import com.mf.mumizzfood.fragments.ProfileFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.mf.mumizzfood.fragments.SearchFragment;
import com.mf.mumizzfood.widgets.UtilLocation;

import java.text.DateFormat;

public class MainBottomBarActivity extends BaseActivity implements HomeFragment.orderActionListner {

    @BindView(R.id.bottom_navigation)
    BottomNavigationView mBottomNav;

    private int mSelectedItem;
    private static final String SELECTED_ITEM = "selected_item";

    private PreferenceManager pff ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_bottom_bar);

        ButterKnife.bind(this);
        pf = new PreferenceManager(this,PreferenceManager.LOGIN_PREFERENCES_FILE);
        pf.getIntForKey(PreferenceManager.USER_ID,0);

        pff = new PreferenceManager(this);

        String outofregion = pff.getStringForKey("outofregion","");


        try {
            Location loc = UtilLocation.getLastKnownLoaction(true,this);

            pff.saveDoubleForKey("latitude", loc.getLatitude());
            pff.saveDoubleForKey("lognitude", loc.getLongitude());
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (outofregion.equalsIgnoreCase("Yes"))
        {
            Intent i = new Intent(this,OutOfRegion.class);
            startActivity(i);
            finish();
        }else
        {
            pf.saveIntForKey(PreferenceManager.USER_ID,1);
            if (pf.getIntForKey(PreferenceManager.USER_ID,0) != 0){
                setBottomBar(savedInstanceState);
            }else {
                Intent intent = new Intent(MainBottomBarActivity.this, LoginAndSignupActivity.class);
                startActivity(intent);
                finish();
            }
        }

    }

    private void setBottomBar(Bundle savedInstanceState) {
        mBottomNav.setVisibility(View.VISIBLE);

        BottomNavigationMenuView menuView = (BottomNavigationMenuView) mBottomNav.getChildAt(0);
        /*for (int i = 0; i < menuView.getChildCount(); i++) {
            final View iconView = menuView.getChildAt(i).findViewById(android.support.design.R.id.icon);

            final ViewGroup.LayoutParams layoutParams = iconView.getLayoutParams();
            final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            if(i == 1){
                mBottomNav.getMenu().getItem(i).getIcon().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
            }

            iconView.setLayoutParams(layoutParams);
        }*/


        mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                mSelectedItem = item.getItemId();
                selectFragment(item);
                return true;
            }
        });

        if (savedInstanceState != null) {
            mSelectedItem = savedInstanceState.getInt(SELECTED_ITEM, 0);
            MenuItem selectedItem = mBottomNav.getMenu().findItem(mSelectedItem);
            selectFragment(selectedItem);
        } else {
            try {
                Fragment fragment = new HomeFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commitAllowingStateLoss();
                selectBottomView(mBottomNav.getMenu().getItem(0));
            }catch (IllegalStateException e){

            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(SELECTED_ITEM, mSelectedItem);
        super.onSaveInstanceState(outState);
    }

    private void selectBottomView(MenuItem item) {

        mSelectedItem = item.getItemId();

        for (int i = 0; i< mBottomNav.getMenu().size(); i++) {
            MenuItem menuItem = mBottomNav.getMenu().getItem(i);
            menuItem.setChecked(false);
        }

        switch (item.getItemId()) {
            case R.id.navigation_home:
                item.setChecked(true);
                //Toast.makeText(MainBottombarActivity.this, "Home", Toast.LENGTH_SHORT).show();
                break;
            case R.id.navigation_order:
                item.setChecked(true);
                //item.getIcon().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
                //Toast.makeText(MainBottombarActivity.this, "Add", Toast.LENGTH_SHORT).show();
                break;
            case R.id.navigation_profile:
                item.setChecked(true);
                //Toast.makeText(MainBottombarActivity.this, "Profile", Toast.LENGTH_SHORT).show();
                break;

        }

    }

    private void selectFragment(MenuItem item) {
        Fragment frag = null;

        mSelectedItem = item.getItemId();

        for (int i = 0; i< mBottomNav.getMenu().size(); i++) {
            MenuItem menuItem = mBottomNav.getMenu().getItem(i);
            menuItem.setChecked(false);
        }

        switch (item.getItemId()) {
            case R.id.navigation_home:
                item.setChecked(true);
                frag = new HomeFragment();
                replaceFragment(frag);
                break;
            case R.id.navigation_order:
                //item.getIcon().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
                frag = new SearchFragment();
                replaceFragment(frag);
                break;
            case R.id.navigation_profile:
                item.setChecked(true);
                frag = new ProfileFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("user_id",pf.getIntForKey(PreferenceManager.USER_ID,0));
                frag.setArguments(bundle);
                replaceFragment(frag);

                break;
        }

    }

    private void replaceFragment(Fragment frag) {
        if (frag != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, frag, frag.getTag());
            ft.commit();
        }
    }

    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();
        Log.e("bak","back appmain count "+count);

        if (count == 0) {

            if (mBottomNav != null){

                MenuItem homeItem = mBottomNav.getMenu().getItem(0);
                Log.e("onBackPressed"," homeItem "+homeItem);
                if (mSelectedItem != homeItem.getItemId()) {
                    selectFragment(homeItem);
                }else {
                    finish();
                }


            }else {
                super.onBackPressed();
            }

            //additional code
        } else {
            if(getSupportFragmentManager().getBackStackEntryCount()>0){
                if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount()-1).getName() != null){
                    getSupportFragmentManager().popBackStack();
                }else {
                    getSupportFragmentManager().popBackStack();
                }
            }else{

                getSupportFragmentManager().popBackStack();
            }
        }
    }

    @Override
    public void activeOrder()
    {
         mBottomNav.setSelectedItemId(R.id.navigation_order);
    }


}
