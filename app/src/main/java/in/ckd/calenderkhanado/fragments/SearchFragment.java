package in.ckd.calenderkhanado.fragments;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.airbnb.lottie.LottieAnimationView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.ckd.calenderkhanado.R;
import in.ckd.calenderkhanado.activities.FilterActivtiy;
import in.ckd.calenderkhanado.adapters.HomeFilterAdapter;
import in.ckd.calenderkhanado.adapters.HomePilotCardAdapter;
import in.ckd.calenderkhanado.adapters.HomeSpecialCardAdapter;
import in.ckd.calenderkhanado.base.BaseFragment;
import in.ckd.calenderkhanado.data.db.DataBaseHelperNew;
import in.ckd.calenderkhanado.data.network.RetrofitApiService;
import in.ckd.calenderkhanado.data.pref.PreferenceManager;
import in.ckd.calenderkhanado.models.FilterModel;
import in.ckd.calenderkhanado.models.HomeFeed;
import in.ckd.calenderkhanado.utils.AppConstants;
import in.ckd.calenderkhanado.widgets.CkdTextview;
import in.ckd.calenderkhanado.widgets.RecyclerItemClickListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.SEARCH_SERVICE;


public class SearchFragment extends BaseFragment {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.searchView)
    SearchView searchView;

    @BindView(R.id.search_recyclerview)
    RecyclerView search_recyclerview;

    Context context;
    private AppCompatActivity actionBar;
    private String globalUrl;
    private LinearLayoutManager linearLayoutManager;
    private List<HomeFeed.Data> fetchDataHome = new ArrayList<>();
    private HomeSpecialCardAdapter specialCardAdapter;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        context = getActivity();
        ButterKnife.bind(this, rootView);

        actionBar = (AppCompatActivity) getActivity();

        actionBar.setSupportActionBar(toolbar);
        actionBar.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        actionBar.getSupportActionBar().setDisplayShowHomeEnabled(true);
        actionBar.getSupportActionBar().setDisplayShowTitleEnabled(false);

        searchView.setQueryHint("Search for food captain and dish");
        SearchManager searchManager = (SearchManager) context.getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(actionBar.getComponentName()));
        searchView.onActionViewExpanded();
        searchView.setFocusable(false);
        searchView.clearFocus();

        globalUrl = RetrofitApiService.BASEURL + "trysomethingnew";
        networkCallForData(globalUrl);

        return rootView;
    }

    private void networkCallForData(String url) {

        Call<HomeFeed> chefData = AppConstants.restAPI.getChefDataP(url);

        chefData.enqueue(new Callback<HomeFeed>() {
            @Override
            public void onResponse(Call<HomeFeed> call, Response<HomeFeed> response) {
                dismissProgress();
                if (response.isSuccessful()) {
                    if (response != null) {
                        HomeFeed res = response.body();
                        if (res.status != null) {
                            if (res.status.equals(AppConstants.SUCCESS)) {

                                fetchDataHome = res.data;

                                //showToast(String.valueOf(fetchDataHome.size()));
                                setAdapterData();
                            }
                        }
                    }
                } else {
                    try {
                        Log.d("Msgggg", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


            }

            @Override
            public void onFailure(Call<HomeFeed> call, Throwable t) {

                dismissProgress();
            }
        });

    }

    private void setAdapterData() {

        linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        search_recyclerview.setHasFixedSize(true);
        search_recyclerview.setLayoutManager(linearLayoutManager);
        search_recyclerview.setItemAnimator(new DefaultItemAnimator());
        specialCardAdapter = new HomeSpecialCardAdapter(getActivity(), fetchDataHome);
        search_recyclerview.setAdapter(specialCardAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.home_menu, menu);
    }

    @OnClick(R.id.backArrowFinish)
    public void backArrowFinish() {

        actionBar.finish();

    }
}