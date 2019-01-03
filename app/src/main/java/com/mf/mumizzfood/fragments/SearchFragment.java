package com.mf.mumizzfood.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.mf.mumizzfood.R;
import com.mf.mumizzfood.adapters.HomeSpecialCardAdapter;
import com.mf.mumizzfood.base.BaseFragment;
import com.mf.mumizzfood.data.network.RetrofitApiService;
import com.mf.mumizzfood.models.HomeFeed;
import com.mf.mumizzfood.utils.AppConstants;
import com.mf.mumizzfood.widgets.CkdTextview;
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

    @BindView(R.id.no_data)
    CkdTextview no_data;

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
        searchView.setFocusable(true);

        search_recyclerview.setVisibility(View.GONE);
        no_data.setVisibility(View.GONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length() > 1) {
                    globalUrl = RetrofitApiService.BASEURL + "searchhomefeed?search="+query;
                    networkCallForData(globalUrl);
                }else {

                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return rootView;
    }

    private void networkCallForData(String url) {
        showProgress("Loading...");
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
                                if (res.data != null && res.data.size()>0) {
                                    search_recyclerview.setVisibility(View.VISIBLE);
                                    setAdapterData();
                                }else {
                                    search_recyclerview.setVisibility(View.GONE);
                                    no_data.setVisibility(View.VISIBLE);
                                }
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
                search_recyclerview.setVisibility(View.GONE);
                no_data.setVisibility(View.VISIBLE);
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