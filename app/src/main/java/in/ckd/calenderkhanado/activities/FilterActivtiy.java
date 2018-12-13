package in.ckd.calenderkhanado.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.ckd.calenderkhanado.R;
import in.ckd.calenderkhanado.adapters.HomeSpecialCardAdapter;
import in.ckd.calenderkhanado.base.BaseActivity;
import in.ckd.calenderkhanado.models.DashBoardModel;
import in.ckd.calenderkhanado.models.HomeFeed;
import in.ckd.calenderkhanado.utils.AppConstants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilterActivtiy extends BaseActivity {

    @BindView(R.id.filter_recyclerview_act)
    RecyclerView recyclerview;

    @BindView(R.id.searchValue)
    SearchView searchValue;

    @BindView(R.id.backButton)
    ImageView backButton;

    private LinearLayoutManager linearLayoutManager;

    private String url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_activtiy);
        ButterKnife.bind(this);

        linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(linearLayoutManager);
        recyclerview.setItemAnimator(new DefaultItemAnimator());

        if (getIntent() != null)
        {
            url = getIntent().getStringExtra("url");

        }


        searchValue.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query != null && !"".equalsIgnoreCase(query))
                {
                    String urlFilter = url+"&search="+query;
                    networkCallForData(urlFilter);
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {

                if (query != null && !"".equalsIgnoreCase(query))
                {
                //    showToast(query);
                }

                return false;
            }
        });



        searchValue.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                networkCallForData(url);
                return false;
            }
        });

        networkCallForData(url) ;
    }

    private void networkCallForData(String url) {


        Call<HomeFeed> chefData = AppConstants.restAPI.getChefDataP(url);

        chefData.enqueue(new Callback<HomeFeed>() {
            @Override
            public void onResponse(Call<HomeFeed> call, Response<HomeFeed> response) {
                dismissProgress();
                if (response.isSuccessful())
                {
                    if (response != null){
                        HomeFeed res = response.body();
                        if (res.status != null) {
                            if ( res.status.equals(AppConstants.SUCCESS))
                            {

                                setAdapterData(recyclerview,res.data);
                            }
                        }
                    }
                }else {
                    try {
                        Log.d("Msgggg",response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


            }

            @Override
            public void onFailure(Call<HomeFeed> call, Throwable t) {

                Log.d("Msgggg",t.getMessage());
                dismissProgress();
            }
        });



    }

    private void setAdapterData(RecyclerView recyclerview, List<HomeFeed.Data> data) {

       if (data.size() != 0)
        {
            HomeSpecialCardAdapter specialCardAdapter = new HomeSpecialCardAdapter(this,data);
            recyclerview.setAdapter(specialCardAdapter);
        }else
        {
            showToast("No data found");
        }

    }

    @OnClick(R.id.backButton)
    public void backButton()
    {
        onBackPressed();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


}
