package in.mummysfood.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.RelativeLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import in.mummysfood.R;
import in.mummysfood.adapters.HomeFilterAdapter;
import in.mummysfood.adapters.HomePilotCardAdapter;
import in.mummysfood.adapters.HomeSpecialCardAdapter;
import in.mummysfood.base.BaseFragment;
import in.mummysfood.data.db.DataBaseHelperNew;
import in.mummysfood.data.network.RetrofitApiService;
import in.mummysfood.data.pref.PreferenceManager;
import in.mummysfood.models.DashBoardModel;
import in.mummysfood.models.FilterModel;
import in.mummysfood.utils.AppConstants;
import in.mummysfood.widgets.CkdButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.mummysfood.widgets.CkdTextview;
import in.mummysfood.widgets.RecyclerItemClickListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends BaseFragment implements HomePilotCardAdapter.OrderListner, HomeFilterAdapter.FilterListner {

    @BindView(R.id.filter_recyclerview)
    RecyclerView filter_recyclerview;
    @BindView(R.id.recommended_recyclerview)
    RecyclerView recommended_recyclerview;
    @BindView(R.id.near_you_recyclerview)
    RecyclerView near_you_recyclerview;
    @BindView(R.id.activeOrder)
    ImageView activeOrder;

    @BindView(R.id.home_add_to_cart_icon)
    ImageView home_add_to_cart_icon;

    Context context;
    private LinearLayoutManager linearLayoutManager;
    private List<DashBoardModel.Data> fetchData = new ArrayList<>();
    private HomePilotCardAdapter pilotCardAdapter;
    private HomeSpecialCardAdapter specialCardAdapter;
    private PreferenceManager pf;
    private PreferenceManager addPref;
    private int item_quantity = 0;
    private Dialog dialog;
    private orderActionListner orderActionListner;
    private List<DashBoardModel.Data>dModel;
    private List<FilterModel> filterList;
    private String globalUrl = "";

    public interface orderActionListner
    {
        void activeOrder();
    }

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home_new, container, false);
        context = getContext();
        ButterKnife.bind(this,rootView);

        pf = new PreferenceManager(context, PreferenceManager.ORDER_PREFERENCES_FILE);

        //homeToolbar.setVisibility(View.GONE);


        DataBaseHelperNew db = null;
        try {
            db = new DataBaseHelperNew(getActivity());
            dModel = db.getAddToCartItem();

            if (dModel != null)
            {
                if (dModel.size() != 0)
                {
                      home_add_to_cart_icon.setImageResource(R.drawable.itemsincart);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }



        DashBoardModel json = new DashBoardModel();
     /*   json.lat = 22.7368;
        json.lng = 75.9086;*/

        addPref = new PreferenceManager(getActivity());

        Double lat = addPref.getDoubleForKey("latitude",0);
        Double longArea = addPref.getDoubleForKey("lognitude",0);


       globalUrl= RetrofitApiService.BASEURL+"geoUser?lat="+String.valueOf(lat)+"&lng="+String.valueOf(longArea);

        networkCallForData(globalUrl);

        near_you_recyclerview.setNestedScrollingEnabled(false);

        showProgress("Loading...");
        setHasOptionsMenu(true);


        near_you_recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE)
                {
                    //mBottomNav.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0 ||dy<0 && recyclerView.isShown())
                {
                    //  mBottomNav.setVisibility(View.GONE);
                }
            }
        });

        setFilterData();

        filter_recyclerview.addOnItemTouchListener(new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position)
            {

                if (position == 0)
                {
                    networkCallForData(globalUrl);
                }else if (position ==1)
                {
                    networkCallForData(RetrofitApiService.BASEURL+"explorechef");
                }else if (position ==2)
                {
                    networkCallForData(RetrofitApiService.BASEURL+"topfoodoption?filter=top");
                }
                else if (position ==3)
                {
                    networkCallForData(RetrofitApiService.BASEURL+"zarahatke?filter=zarahatke");


                }
                else if (position ==4)
                {
                    networkCallForData(RetrofitApiService.BASEURL+"trysomethingnew?filter=trysomethingnew");
                }
                else if (position ==5)
                {
                    networkCallForData(globalUrl);
                    //    networkCallForData(AppConstants.BASE_URL+"trysomethingnew?filter=trysomethingnew");
                }

            }
        }));


        return rootView;
    }

    private void networkCallForData(String url) {


        Call<DashBoardModel> chefData = AppConstants.restAPI.getChefData(url);

        chefData.enqueue(new Callback<DashBoardModel>() {
            @Override
            public void onResponse(Call<DashBoardModel> call, Response<DashBoardModel> response) {
                dismissProgress();
                if (response.isSuccessful())
                {
                    if (response != null){
                        DashBoardModel res = response.body();
                         if (res.status != null) {
                            if ( res.status.equals(AppConstants.SUCCESS))
                            {
                                fetchData = res.data;

                                setAdapterData(recommended_recyclerview, 0);
                                setAdapterData(near_you_recyclerview, 1);
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
            public void onFailure(Call<DashBoardModel> call, Throwable t) {

                Log.d("Msgggg",t.getMessage());
                dismissProgress();
            }
        });



    }

    private void setFilterData() {

        try {
            filterList = new ArrayList<>();
            filterList.add(new FilterModel(0,getString(R.string.option_near_me),false));
            filterList.add(new FilterModel(1,getString(R.string.explore),false));
            filterList.add(new FilterModel(2,getString(R.string.top_food_options),false));
            filterList.add(new FilterModel(3,getString(R.string.zara_hatke),false));
            filterList.add(new FilterModel(4,getString(R.string.try_something_new),false));
            filterList.add(new FilterModel(5,getString(R.string.my_favourite),false));

            GridLayoutManager gridLayoutManager = new GridLayoutManager(context,3,LinearLayoutManager.VERTICAL,false);
            filter_recyclerview.setHasFixedSize(true);
            filter_recyclerview.setLayoutManager(gridLayoutManager);
            filter_recyclerview.setItemAnimator(new DefaultItemAnimator());
            HomeFilterAdapter pilotCardAdapter = new HomeFilterAdapter(getActivity(),filterList, this);
            filter_recyclerview.setAdapter(pilotCardAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setAdapterData(RecyclerView recyclerview, int type) {

        if (type == 0) {
            linearLayoutManager = new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false);
            recyclerview.setHasFixedSize(true);
            recyclerview.setLayoutManager(linearLayoutManager);
            recyclerview.setItemAnimator(new DefaultItemAnimator());
            pilotCardAdapter = new HomePilotCardAdapter(getActivity(),fetchData, this);
            recyclerview.setAdapter(pilotCardAdapter);
        }else{
            linearLayoutManager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
            recyclerview.setHasFixedSize(true);
            recyclerview.setLayoutManager(linearLayoutManager);
            recyclerview.setItemAnimator(new DefaultItemAnimator());
            specialCardAdapter = new HomeSpecialCardAdapter(getActivity(),fetchData);
            recyclerview.setAdapter(specialCardAdapter);
        }
    }

    @OnClick(R.id.activeOrder)
    public void activeOrder()
    {
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

    @OnClick(R.id.home_add_to_cart_icon)
    public void AddToCart(){
        try {

            if (dModel != null)
            {
                if (dModel.size() != 0)
                {
                    Intent i = new Intent(context,OrderDetailsActivity.class);
                    i.putExtra("order_id",dModel.get(0).id);
                    i.putExtra("location","Cart");
                    i.putExtra("data",dModel.get(0));
                    context.startActivity(i);
                }else
                {
                    showItemsIntoCart();
                }
            }else
            {
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
    public void AddToCart(final int i){

        if (pf.getIntForKey(PreferenceManager.ORDER_ID,0) == 0){
            // setOrderItemData(i);
        }else{
            String msg = "Your cart contains dishes from "+ pf.getStringForKey(PreferenceManager.ORDER_NAME,null)+". Do you want to discard the selection and add dishes from "+fetchData.get(i).food_detail.get(0).name+" ?";
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(msg)
                    .setCancelable(false)
                    .setNegativeButton(R.string.no_txt,new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if (dialog != null) {
                                dialog.dismiss();
                            }
                        }
                    })
                    .setPositiveButton(R.string.yes_txt, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            pf.clearPref(context,PreferenceManager.ORDER_PREFERENCES_FILE);
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

    private void showDialogBasedOnAddToCart(final int i) {
        dialog = new Dialog(context);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        dialog = builder.create();
        dialog.setTitle("Select your plan");

        LayoutInflater factory = LayoutInflater.from(context);
        final View content = factory.inflate(R.layout.dialog_for_subscription, null);
        CkdButton dialogButton = content.findViewById(R.id.submitThali);
        final RadioGroup  radioGroup = content. findViewById(R.id.radioGroup);

        item_quantity = 0;

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //showToast(String.valueOf(radioGroup.getCheckedRadioButtonId()));
                switch (radioGroup.getCheckedRadioButtonId()){
                    case R.id.radioButton1:
                        item_quantity = 60;
                        break;
                    case R.id.radioButton2:
                        item_quantity = 15;
                        break;
                    case R.id.radioButton3:
                        item_quantity = 1;
                        break;
                    case R.id.radioButton4:
                        item_quantity = 1;
                        break;
                }

                //fetchData.get(i).quantity = item_quantity;
                sharePref(i,item_quantity);
                pilotCardAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });

        builder.setView(content);
        dialog = builder.show();

    }

    private void sharePref(int i, int quantity) {
        //   pf.saveIntForKey(PreferenceManager.USER_ID,fetchData.get(i).id);
        pf.saveIntForKey(PreferenceManager.ORDER_ID,fetchData.get(i).food_detail.get(0).id);
        pf.saveIntForKey(PreferenceManager.ODRDER_USER_ID,fetchData.get(i).food_detail.get(0).user_id);
        pf.saveIntForKey(PreferenceManager.ORDER_CATEGORY_ID,fetchData.get(i).food_detail.get(0).category_id);
        pf.saveStringForKey(PreferenceManager.ORDER_NAME,fetchData.get(i).food_detail.get(0).name);
        pf.saveStringForKey(PreferenceManager.ORDER_DETAILS,fetchData.get(i).food_detail.get(0).details);
        pf.saveStringForKey(PreferenceManager.ORDER_PRICE,fetchData.get(i).food_detail.get(0).price);
        pf.saveIntForKey(PreferenceManager.ORDER_quantity,quantity);
    }


    @Override
    public void AddFoodQuantity(int position) {
        int count = 0;
        if (pf.getIntForKey(PreferenceManager.USER_ID, 0) != 0 && pf.getIntForKey(PreferenceManager.USER_ID, 0) == fetchData.get(position).id){
            count = pf.getIntForKey(PreferenceManager.ORDER_quantity, 0);
        }
        count++;
        pf.saveIntForKey(PreferenceManager.ORDER_quantity,count);

        pilotCardAdapter.notifyDataSetChanged();
    }

    @Override
    public void SubFoodQuantity(int position) {
        int count = 0;
        if (pf.getIntForKey(PreferenceManager.USER_ID, 0) != 0 && pf.getIntForKey(PreferenceManager.USER_ID, 0) == fetchData.get(position).id){
            count = pf.getIntForKey(PreferenceManager.ORDER_quantity, 0);
        }

        if (count > 0) {
            count--;
            pf.saveIntForKey(PreferenceManager.ORDER_quantity,count);
        }else{
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
        dialogd.setContentView(R.layout.dialog);


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

    @Override
    public void clickOnFilter(int position) {

    }
}