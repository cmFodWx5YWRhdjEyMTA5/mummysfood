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

    @BindView(R.id.search_recyclerview)
    RecyclerView search_recyclerview;

    @BindView(R.id.searchView)
    SearchView searchView;

    Context context;
    private AppCompatActivity actionBar;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        context = getActivity();
        ButterKnife.bind(this, rootView);

        actionBar = (AppCompatActivity) getActivity();

        searchView.setBackgroundColor(Color.WHITE);
        searchView.setQueryHint("Search for food captain and dish");
        SearchManager searchManager = (SearchManager) context.getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(actionBar.getComponentName()));
        searchView.onActionViewExpanded();
        searchView.setFocusable(false);
        searchView.clearFocus();

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.home_menu, menu);
    }


}