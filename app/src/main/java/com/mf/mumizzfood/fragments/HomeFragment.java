package com.mf.mumizzfood.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.mf.mumizzfood.activities.FilterActivtiy;

import com.mf.mumizzfood.R;

import com.mf.mumizzfood.activities.PartyCorporateActivity;
import com.mf.mumizzfood.adapters.HomeFilterAdapter;
import com.mf.mumizzfood.adapters.HomePilotCardAdapter;
import com.mf.mumizzfood.adapters.HomeSpecialCardAdapter;
import com.mf.mumizzfood.adapters.HomeWhatsNewAdapter;
import com.mf.mumizzfood.base.BaseFragment;
import com.mf.mumizzfood.data.db.DataBaseHelperNew;
import com.mf.mumizzfood.data.network.RetrofitApiService;
import com.mf.mumizzfood.data.pref.PreferenceManager;
import com.mf.mumizzfood.models.FilterModel;
import com.mf.mumizzfood.models.HomeFeed;
import com.mf.mumizzfood.utils.AppConstants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.mf.mumizzfood.widgets.CkdTextview;
import com.mf.mumizzfood.widgets.RecyclerItemClickListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends BaseFragment implements HomePilotCardAdapter.OrderListner, HomeFilterAdapter.FilterListner {

    @BindView(R.id.filter_recyclerview)
    RecyclerView filter_recyclerview;

    @BindView(R.id.recommended_recyclerview)
    RecyclerView recommended_recyclerview;

    @BindView(R.id.whats_recyclerview)
    RecyclerView whats_recyclerview;

    @BindView(R.id.lottieAnimationViewLoading)
    LottieAnimationView lottieAnimationViewLoading;

    @BindView(R.id.near_you_recyclerview)
    RecyclerView near_you_recyclerview;
    @BindView(R.id.scrollNes)
    NestedScrollView scrollNes;


    @BindView(R.id.activeOrder)
    ImageView activeOrder;

    @BindView(R.id.home_add_to_cart_icon)
    ImageView home_add_to_cart_icon;

    @BindView(R.id.headerText)
    CkdTextview headerText;

    @BindView(R.id.explore_food)
            LinearLayout explore_food;
    /*@BindView(R.id.filterFood)
    ImageView filterFood;*/

    /*@BindView(R.id.filterFoodApplied)
    View filterFoodApplied;*/


  /*  @BindView(R.id.swiperefresh)
    SwipeRefreshLayout swipeRefreshLayout;*/

    Context context;
    private LinearLayoutManager linearLayoutManager;
    private List<HomeFeed.Data> fetchDataHome = new ArrayList<>();
    private List<HomeFeed.Data> fetchAllDataHome = new ArrayList<>();
    private HomePilotCardAdapter pilotCardAdapter;
    private HomeWhatsNewAdapter homeWhatsNewAdapter;
    private HomeSpecialCardAdapter specialCardAdapter;
    private PreferenceManager pf, filterPref;
    private PreferenceManager addPref;
    private int item_quantity = 0;
    private Dialog dialog;
    private orderActionListner orderActionListner;
    private List<HomeFeed.Data> dModel;
    private List<FilterModel> filterList;
    private String globalUrl = "";

    private int UserFoodType;
    private PreferenceManager pref;
    private static final Integer[] IMAGES = {R.drawable.banner1, R.drawable.rate_us,R.mipmap.image3};

    private DataBaseHelperNew db = null;
    int filter_value;

    public interface orderActionListner {
        void activeOrder();
    }

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home_new, container, false);
        context = getActivity();
        ButterKnife.bind(this, rootView);

        pf = new PreferenceManager(context, PreferenceManager.ORDER_PREFERENCES_FILE);
        pref = new PreferenceManager(getActivity());
        filterPref = new PreferenceManager(context, PreferenceManager.FILTER_PREFERENCES_FILE);

        UserFoodType = pref.getIntForKey("UserFoodType", 0);

        /*if (filterPref.getBooleanForKey(PreferenceManager.FILTER_APPLY,false)){
            filterFoodApplied.setVisibility(View.VISIBLE);

            setFilterBg(UserFoodType);
        }else{
            filterFoodApplied.setVisibility(View.GONE);
        }*/

        //homeToolbar.setVisibility(View.GONE);


        try {
            db = new DataBaseHelperNew(getActivity());
            dModel = db.getAddToCartItem();

            if (dModel != null) {
                if (dModel.size() != 0) {
                    home_add_to_cart_icon.setImageResource(R.drawable.itemsincart);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


     /*   json.lat = 22.7368;
        json.lng = 75.9086;*/

        addPref = new PreferenceManager(getActivity());

        Double lat = addPref.getDoubleForKey("latitude", 0);
        Double longArea = addPref.getDoubleForKey("lognitude", 0);


        //lottieAnimationViewLoading.setVisibility(View.VISIBLE);

        scrollNes.setVisibility(View.VISIBLE);

        //   lottieAnimationViewLoading.playAnimation();

        globalUrl = RetrofitApiService.BASEURL + "geoUser?lat=" + String.valueOf(lat) + "&lng=" + String.valueOf(longArea) + "&is_vegitarian=" + UserFoodType;
        //  globalUrl = RetrofitApiService.BASEURL + "geoUser?lat=" + "22.7244" + "&lng=" + "75.8839" + "&is_vegitarian=" + UserFoodType;

        networkCallForData(globalUrl, "nearBy");

        near_you_recyclerview.setNestedScrollingEnabled(false);

        showProgress("Loading...");

        setHasOptionsMenu(true);

        near_you_recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //mBottomNav.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0 || dy < 0 && recyclerView.isShown()) {
                    //  mBottomNav.setVisibility(View.GONE);
                }
            }
        });


        near_you_recyclerview.setNestedScrollingEnabled(false);
        recommended_recyclerview.setNestedScrollingEnabled(false);
        whats_recyclerview.setNestedScrollingEnabled(false);
        setWhatsNewAdapter();

        setFilterData();

        filter_recyclerview.addOnItemTouchListener(new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                if (position == 0) {

//                    globalUrl = RetrofitApiService.BASEURL + "trysomethingnew" + "?is_vegitarian=" + UserFoodType;
//
//                    Intent i = new Intent(getActivity(), FilterActivtiy.class);
//
//                    i.putExtra("url", globalUrl);
//
//                    getActivity().startActivity(i);
                    //near_you_recyclerview.getLayoutManager().scrollToPosition(1);


                    scrollNes.scrollTo(0, (int)explore_food.getY());

                } else if (position == 1) {
                    globalUrl = RetrofitApiService.BASEURL + "trysomethingnew?filter=zarahatke" + "&is_vegitarian=" + UserFoodType;

                    Intent i = new Intent(getActivity(), FilterActivtiy.class);

                    i.putExtra("url", globalUrl);

                    getActivity().startActivity(i);
                } else if (position == 2) {
                    Intent intent = new Intent(context, PartyCorporateActivity.class);
                    context.startActivity(intent);

                } /*else if (position == 1) {
                    globalUrl = RetrofitApiService.BASEURL + "trysomethingnew?filter=trysomethingnew" + "&is_vegitarian=" + UserFoodType;

                    Intent i = new Intent(getActivity(), FilterActivtiy.class);

                    i.putExtra("url", globalUrl);

                    getActivity().startActivity(i);
                } else if (position == 2) {
                    globalUrl = RetrofitApiService.BASEURL + "trysomethingnew?filter=myfav" + "&is_vegitarian=" + UserFoodType;

                    Intent i = new Intent(getActivity(), FilterActivtiy.class);

                    i.putExtra("url", globalUrl);

                    getActivity().startActivity(i);
                } else if (position == 3) {

                    globalUrl = RetrofitApiService.BASEURL + "trysomethingnew?filter=zarahatke" + "&is_vegitarian=" + UserFoodType;
                    Intent i = new Intent(getActivity(), FilterActivtiy.class);

                    i.putExtra("url", globalUrl);

                    getActivity().startActivity(i);

                } else if (position == 4) {
                    globalUrl = RetrofitApiService.BASEURL + "trysomethingnew?filter=top" + "&is_vegitarian=" + UserFoodType;

                    Intent i = new Intent(getActivity(), FilterActivtiy.class);

                    i.putExtra("url", globalUrl);

                    getActivity().startActivity(i);
                } else if (position == 5) {
                    Intent i = new Intent(getActivity(), FilterActivtiy.class);
                    globalUrl = RetrofitApiService.BASEURL + "trysomethingnew" + "?is_vegitarian=" + UserFoodType;

                    i.putExtra("url", globalUrl);

                    getActivity().startActivity(i);

                }*/

            }
        }));


      /*  swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        networkCallForData(globalUrl);
                    }
                }
        );*/


    /*    radioAction.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                   int value =  getRadioSelected();

                globalUrl = RetrofitApiService.BASEURL+"explorechef"+"?is_vegitarian="+value;
                networkCallForData(globalUrl);
            }
        });
*/


        String outofregion = pf.getStringForKey("outofregion", "");
        if (outofregion.equalsIgnoreCase("Yes")) {

            headerText.setText("Thank you for coming on our platform, We really appreciate it but we are not available in your area Right now.");
        }

        return rootView;
    }

    private void setWhatsNewAdapter() {

        linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        whats_recyclerview.setHasFixedSize(true);
        whats_recyclerview.setLayoutManager(linearLayoutManager);
        whats_recyclerview.setItemAnimator(new DefaultItemAnimator());
        homeWhatsNewAdapter = new HomeWhatsNewAdapter(getActivity(), IMAGES);
        whats_recyclerview.setAdapter(homeWhatsNewAdapter);
    }

    /*private void setFilterBg(int userFoodType) {

        if (userFoodType ==1)
        {
            filterFoodApplied.setBackgroundColor(getResources().getColor(R.color.red));
        }else if (userFoodType == 0)
        {
            filterFoodApplied.setBackgroundColor(getResources().getColor(R.color.green));
        }else
        {
            filterFoodApplied.setVisibility(View.GONE);
        }
    }*/

    private void networkCallForData(String url, final String filter) {


        Call<HomeFeed> chefData = AppConstants.restAPI.getChefDataP(url);

        chefData.enqueue(new Callback<HomeFeed>() {
            @Override
            public void onResponse(Call<HomeFeed> call, Response<HomeFeed> response) {
                dismissProgress();
                scrollNes.setVisibility(View.VISIBLE);
                if (response.isSuccessful()) {
                    if (response != null) {
                        HomeFeed res = response.body();
                        if (res.status != null) {
                            if (res.status.equals(AppConstants.SUCCESS)) {

                                Log.d("SuccessValue", "I am");

                                if (filter != null && !filter.isEmpty() && filter.equalsIgnoreCase("nearBy")) {
                                    fetchDataHome = res.data;

                                    //showToast(String.valueOf(fetchDataHome.size()));
                                    setAdapterData(recommended_recyclerview, 0);
                                    globalUrl = RetrofitApiService.BASEURL + "trysomethingnew" + "?is_vegitarian=" + UserFoodType;
                                    networkCallForData(globalUrl, "All");
                                }else if (filter != null && !filter.isEmpty() && filter.equalsIgnoreCase("All")) {
                                    fetchAllDataHome = res.data;
                                    setAdapterData(near_you_recyclerview, 1);
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

                Log.d("Msgggg", t.getMessage());
                dismissProgress();
            }
        });

     /*   chefData.enqueue(new Callback<DashBoardModel>() {
            @Override
            public void onResponse(Call<DashBoardModel> call, Response<DashBoardModel> response) {
                dismissProgress();
                scrollNes.setVisibility(View.VISIBLE);
                if (response.isSuccessful()) {
                    if (response != null) {
                        DashBoardModel res = response.body();
                        if (res.status != null) {
                            if (res.status.equals(AppConstants.SUCCESS)) {
                                fetchData = res.data;

                                setAdapterData(recommended_recyclerview, 0);
                                setAdapterData(near_you_recyclerview, 1);
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
            public void onFailure(Call<DashBoardModel> call, Throwable t) {

                Log.d("Msgggg", t.getMessage());
                dismissProgress();
            }
        });*/


    }

    private void setFilterData() {

        try {
            filterList = new ArrayList<>();
            filterList.add(new FilterModel(0, getString(R.string.daily_meal), false));
            filterList.add(new FilterModel(1, getString(R.string.snacks), false));
            filterList.add(new FilterModel(2, getString(R.string.corporates_party), false));
//            filterList.add(new FilterModel(3, getString(R.string.zara_hatke), false));
//            filterList.add(new FilterModel(4, getString(R.string.top_food_options), false));
//            filterList.add(new FilterModel(5, getString(R.string.explore), false));


            LinearLayoutManager gridLayoutManager =new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            filter_recyclerview.setHasFixedSize(true);
            filter_recyclerview.setLayoutManager(gridLayoutManager);
            filter_recyclerview.setItemAnimator(new DefaultItemAnimator());
            filter_recyclerview.setNestedScrollingEnabled(false);
            HomeFilterAdapter pilotCardAdapter = new HomeFilterAdapter(getActivity(), filterList, this);
            filter_recyclerview.setAdapter(pilotCardAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setAdapterData(RecyclerView recyclerview, int type) {

        if (type == 0) {
            linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            recyclerview.setHasFixedSize(true);
            recyclerview.setLayoutManager(linearLayoutManager);
            recyclerview.setItemAnimator(new DefaultItemAnimator());
            pilotCardAdapter = new HomePilotCardAdapter(getActivity(), fetchDataHome, this);
            recyclerview.setAdapter(pilotCardAdapter);
        } else {
            linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            recyclerview.setHasFixedSize(true);
            recyclerview.setLayoutManager(linearLayoutManager);
            recyclerview.setItemAnimator(new DefaultItemAnimator());
            specialCardAdapter = new HomeSpecialCardAdapter(getActivity(), fetchAllDataHome);
            recyclerview.setAdapter(specialCardAdapter);
        }
    }

    @OnClick(R.id.activeOrder)
    public void activeOrder() {
        orderActionListner.activeOrder();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.home_menu, menu);
    }


  /*  @OnClick(R.id.home_user_profile_icon)
    public void RedirectToProfile(){
        ProfileFragment fragment1 = new ProfileFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putInt("user_id", loggedInUserId);
        bundle1.putString("type", AppConstants.SEEKER);
        fragment1.setArguments(bundle1);
        FragmentManager fragmentManager1 = (((AppCompatActivity) context).getSupportFragmentManager());
        FragmentTransaction fragmentTransaction1 = fragmentManager1
                .beginTransaction();
        fragmentTransaction1.addToBackStack(fragment1.getClass().getSimpleName());
        fragmentTransaction1.replace(R.id.content_frame, fragment1);
        fragmentTransaction1.commit();
    }*/


    /*@OnClick(R.id.filterFood)
    public void filterFood() {
        showFilterDialog();
    }*/

    @OnClick(R.id.home_add_to_cart_icon)
    public void AddToCart() {
        try {

            try {
                dModel = db.getAddToCartItem();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (dModel != null) {
                if (dModel.size() != 0) {
                    Intent i = new Intent(context, OrderDetailsActivity.class);
                    i.putExtra("order_id", dModel.get(0).id);
                    i.putExtra("location", "Cart");
                    i.putExtra("data", dModel.get(0));
                    context.startActivity(i);
                } else {
                    showItemsIntoCart();
                }
            } else {
                showItemsIntoCart();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        return super.onOptionsItemSelected(menuItem);
    }

    /*@OnClick(R.id.order_checkout)
    public void setOrderCheckout(){
        OrderDetailsActivity fragment = new OrderDetailsActivity();
        Bundle bundle = new Bundle();
        bundle.putInt("order_id", pf.getIntForKey(PreferenceManager.ORDER_ID,0));
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = (((AppCompatActivity) context).getSupportFragmentManager());
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.commit();
    }*/

    @Override
    public void AddToCart(final int i) {

        if (pf.getIntForKey(PreferenceManager.ORDER_ID, 0) == 0) {
            // setOrderItemData(i);
        } else {
            String msg = "Your cart contains dishes from " + pf.getStringForKey(PreferenceManager.ORDER_NAME, null) + ". Do you want to discard the selection and add dishes from " + fetchDataHome.get(0).name + " ?";
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(msg)
                    .setCancelable(false)
                    .setNegativeButton(R.string.no_txt, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if (dialog != null) {
                                dialog.dismiss();
                            }
                        }
                    })
                    .setPositiveButton(R.string.yes_txt, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            pf.clearPref(context, PreferenceManager.ORDER_PREFERENCES_FILE);
                            //setOrderItemData(i);
                        }
                    });
            // Create the AlertDialog object and return it
            AlertDialog alert = builder.create();
            alert.show();
        }

    }

/*
    private void setOrderItemData(int i) {
        if (!fetchData.get(i)..add_food) {
            fetchData.get(i).add_food = true;
            showDialogBasedOnAddToCart(i);
            //homeToolbar.setVisibility(View.VISIBLE);

        }else{
            fetchData.get(i).quantity = 0;
            pf.clearPref(context, PreferenceManager.ORDER_PREFERENCES_FILE);
            //homeToolbar.setVisibility(View.GONE);
            pilotCardAdapter.notifyDataSetChanged();
        }
    }*/


    @Override
    public void AddFoodQuantity(int position) {
        int count = 0;
        if (pf.getIntForKey(PreferenceManager.USER_ID, 0) != 0 && pf.getIntForKey(PreferenceManager.USER_ID, 0) == fetchDataHome.get(position).id) {
            count = pf.getIntForKey(PreferenceManager.ORDER_quantity, 0);
        }
        count++;
        pf.saveIntForKey(PreferenceManager.ORDER_quantity, count);

        pilotCardAdapter.notifyDataSetChanged();
    }

    @Override
    public void SubFoodQuantity(int position) {
        int count = 0;
        if (pf.getIntForKey(PreferenceManager.USER_ID, 0) != 0 && pf.getIntForKey(PreferenceManager.USER_ID, 0) == fetchDataHome.get(position).id) {
            count = pf.getIntForKey(PreferenceManager.ORDER_quantity, 0);
        }

        if (count > 0) {
            count--;
            pf.saveIntForKey(PreferenceManager.ORDER_quantity, count);
        } else {
            //  fetchData.get(position).add_food = false;
            pf.clearPref(context, PreferenceManager.ORDER_PREFERENCES_FILE);
            //homeToolbar.setVisibility(View.GONE);
        }
        pilotCardAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        orderActionListner = (HomeFragment.orderActionListner) context;
    }


    private void showItemsIntoCart() {
        final Dialog dialogd = new Dialog(getActivity());
        dialogd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogd.setContentView(R.layout.emptycartdialog);


        CkdTextview textDesc = (CkdTextview) dialogd.findViewById(R.id.permissionDesc);
        CkdTextview textPos = (CkdTextview) dialogd.findViewById(R.id.permissionPos);


        textPos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogd.dismiss();
            }
        });

        dialogd.show();

    }


    /*private void showFilterDialog() {
        final Dialog dialogd = new Dialog(getActivity(),android.R.style.Theme_Translucent_NoTitleBar);
        dialogd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogd.setContentView(R.layout.dialog_for_food_fiter);
        Window window = dialogd.getWindow();
        window.setGravity(Gravity.TOP);
        //window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams lp = dialogd.getWindow().getAttributes();
        lp.dimAmount=0.4f;
        dialogd.getWindow().setAttributes(lp);
        dialogd.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        ImageView cross_popup = dialogd.findViewById(R.id.cross_popup);
        CkdTextview reset_filter = dialogd.findViewById(R.id.reset_filter);
        LinearLayout filter_veg_layout = dialogd.findViewById(R.id.filter_veg_layout);
        LinearLayout filter_non_veg_layout = dialogd.findViewById(R.id.filter_non_veg_layout);
        CkdTextview apply_filter = dialogd.findViewById(R.id.apply_filter);
        final ImageView vegSysmbol = dialogd.findViewById(R.id.vegSysmbol);
        final ImageView non_VegSysmbol = dialogd.findViewById(R.id.non_VegSysmbol);

        if (filterPref.getBooleanForKey(PreferenceManager.FILTER_APPLY,false)){
            filterFoodApplied.setVisibility(View.VISIBLE);
            if (filterPref.getIntForKey(PreferenceManager.FILTER_FOOD_TYPE,0) == 1){
                vegSysmbol.setColorFilter(context.getResources().getColor(R.color.gray));
                non_VegSysmbol.setColorFilter(context.getResources().getColor(R.color.red));
            }else {
                vegSysmbol.setColorFilter(context.getResources().getColor(R.color.green));
                non_VegSysmbol.setColorFilter(context.getResources().getColor(R.color.gray));
            }
        }else{
            vegSysmbol.setColorFilter(context.getResources().getColor(R.color.gray));
            non_VegSysmbol.setColorFilter(context.getResources().getColor(R.color.gray));
        }

        cross_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogd.isShowing())
                    dialogd.dismiss();
            }
        });
        reset_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogd.isShowing())
                    dialogd.dismiss();
                filterFoodApplied.setVisibility(View.GONE);
                filterPref.clearPref(context,PreferenceManager.FILTER_PREFERENCES_FILE);
            }
        });

        filter_veg_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter_value = 0;
                vegSysmbol.setColorFilter(context.getResources().getColor(R.color.green));
                non_VegSysmbol.setColorFilter(context.getResources().getColor(R.color.gray));
            }
        });
        filter_non_veg_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter_value = 1;
                vegSysmbol.setColorFilter(context.getResources().getColor(R.color.gray));
                non_VegSysmbol.setColorFilter(context.getResources().getColor(R.color.red));
            }
        });

        apply_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogd.isShowing())
                    dialogd.dismiss();

                if (filter_value == 0 || filter_value == 1) {
                    filterFoodApplied.setVisibility(View.VISIBLE);
                    filterPref.saveBooleanForKey(PreferenceManager.FILTER_APPLY, true);
                    filterPref.saveIntForKey(PreferenceManager.FILTER_FOOD_TYPE, filter_value);
                    pf.saveIntForKey("UserFoodType", filter_value);
                }else{
                    filterFoodApplied.setVisibility(View.GONE);
                }

                setFilterBg(filter_value);
                globalUrl = RetrofitApiService.BASEURL + "trysomethingnew" + "?is_vegitarian=" + UserFoodType;
                networkCallForData(globalUrl);
            }
        });

        dialogd.show();

    }*/

    @Override
    public void clickOnFilter(int position) {

    }

    private int getRadioSelected(int selectedId) {

        int value = 0;


        if (selectedId == R.id.veg) {
            pf.saveIntForKey("UserFoodType", 0);
            value = 0;
            return value;

        } else {
            pf.saveIntForKey("UserFoodType", 1);
            value = 1;
            return value;
        }


    }
}