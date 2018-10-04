package in.mummysfood.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.mummysfood.R;
import in.mummysfood.activities.ActiveOrderActivtiy;
import in.mummysfood.activities.CongratualtionsActivtiy;
import in.mummysfood.activities.YourCartActivity;
import in.mummysfood.adapters.OrderStatusAdapter;
import in.mummysfood.base.BaseFragment;
import in.mummysfood.data.network.RetrofitApiService;
import in.mummysfood.data.pref.PreferenceManager;
import in.mummysfood.models.OrderModel;
import in.mummysfood.models.ProfileModel;
import in.mummysfood.models.SubscribtionModel;
import in.mummysfood.models.UserModel;
import in.mummysfood.models.UserProfileModel;
import in.mummysfood.utils.AppConstants;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by acer on 8/7/2018.
 */

public class OrderStatusFragment extends BaseFragment implements OrderStatusAdapter.CancelOrderListener{


    @BindView(R.id.orderStausLayout)
    RecyclerView recyclerView;

    private Context context;
    private PreferenceManager pf;

    private List<UserProfileModel.Orders>ordersList;
    private List<UserProfileModel.Subscribes>subscribesList;

    private OrderStatusAdapter oAdapter;

    private int orderedPlate ;
    private int orderId ;
    private int numberOfDays ;
    private int remainingPlates ;
    private int subsribeId;

     private  SubscribtionModel.Data subscribtionModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)

    {

        View rootView = inflater.inflate(R.layout.order_status_layout, container, false);

        context = getContext();

        ButterKnife.bind(this, rootView);

        pf = new PreferenceManager(getActivity(),PreferenceManager.USER_ID);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        networkCallForGetOrder();

        return rootView;


    }

    private void networkCallForGetOrder()
    {

        pf = new PreferenceManager(context,PreferenceManager.LOGIN_PREFERENCES_FILE);

        int userIf = pf.getIntForKey(PreferenceManager.USER_ID,0);

        String url = AppConstants.restAPI.BASEURL+"user/"+userIf;

        Call<UserProfileModel>userModelCall = AppConstants.restAPI.getProfileUserDataForOrder(url);


        userModelCall.enqueue(new Callback<UserProfileModel>() {
            @Override
            public void onResponse(Call<UserProfileModel> call, Response<UserProfileModel> response)
            {
                if (response.isSuccessful())
                {
                    if (response != null)
                    {


                        subscribesList = response.body().data.get(0).subscribes;

                        if (subscribesList.size() != 0)
                        {

                            ordersList = response.body().data.get(0).subscribes.get(0).orders;

                            setAdapterForOrder(ordersList,subscribesList);



                        }else
                        {
                            showToast(String.valueOf(subscribesList.size()));
                        }


                            /* ordersList  = response.body().data.get(0).;

                             if (ordersList.size() != 0)
                             {

                                 showToast("OrderList"+String.valueOf(ordersList.size()));

                                 subscribesList = response.body().data.get(0).subscribes;

                                 if (ordersList.size() != 0)
                                 {
                                     setAdapterForOrder(ordersList,subscribesList);
                                 }

                             }*/
                         }


                }else
                {
                    try {
                        Log.d("ErrorMsg",response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserProfileModel> call, Throwable t) {

            }
        });

    }

    private void setAdapterForOrder(List<UserProfileModel.Orders> ordersList, List<UserProfileModel.Subscribes> subscribesList) {



        oAdapter = new OrderStatusAdapter(getActivity(),ordersList,subscribesList,this);

        recyclerView.setAdapter(oAdapter);

    }

    @Override
    public void actionOnOrder(int position,String status,int remainingPlates)
    {

        if (status.equalsIgnoreCase("Show"))
        {
            Intent i = new Intent(getActivity(), ActiveOrderActivtiy.class);
            i.putExtra("order",ordersList.get(position));
            i.putExtra("remainingPlates",remainingPlates);
            startActivity(i);
        }else if (status.equalsIgnoreCase("Delete"))
        {
            showToast("Deleted");

        }else if (status.equalsIgnoreCase("Update"))
        {
            networkcallforOrderAction(position);
        }

    }

    public void networkcallforOrderAction(int pos)
    {
        updateOrderData(ordersList.get(pos));

    }

    private void updateOrderData(final UserProfileModel.Orders orderModelUpdate)
    {


       /* pf = new PreferenceManager(context,PreferenceManager.LOGIN_PREFERENCES_FILE);

        int userIf = pf.getIntForKey(PreferenceManager.USER_ID,0);
*/
        subsribeId = orderModelUpdate.subscribe_id;


        final Call<SubscribtionModel>subModel = AppConstants.restAPI.subscribeOrderById(RetrofitApiService.BASEURL+"subscribe/"+subsribeId);


        subModel.enqueue(new Callback<SubscribtionModel>() {
            @Override
            public void onResponse(Call<SubscribtionModel> call, Response<SubscribtionModel> response) {
                if (response.isSuccessful())
                {
                    if (response != null)
                    {
                        subscribtionModel = response.body().data.get(0);
                        orderedPlate = response.body().data.get(0).ordered_plates;
                        numberOfDays = response.body().data.get(0).number_of_days;

                        remainingPlates = numberOfDays - orderedPlate;

                        updateOrder(remainingPlates);

                    }else
                    {
                        showToast("response null");

                    }
                }else {
                    try {
                        Log.d("Response",response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SubscribtionModel> call, Throwable t) {
                showToast("response falier");

            }
        });





      /*  Call<OrderModel.Data> loginRequestCall = AppConstants.restAPI.subscribeOrder(orderModel);

        loginRequestCall.enqueue(new Callback<OrderModel.Data>() {
            @Override
            public void onResponse(Call<OrderModel.Data> call, Response<OrderModel.Data> response) {

                if (response != null){

                    if (response.isSuccessful()){
                        OrderModel.Data res = response.body();
                        if (res.status != null && res.status.equalsIgnoreCase(AppConstants.SUCCESS)){

                            Intent ActIntent = new Intent(YourCartActivity.this,CongratualtionsActivtiy.class);
                            ActIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(ActIntent);
                            finish();
                        }else{
                        }

                    }else {

                        try {
                            Log.e("Response is not success",""+response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }else {
                    try {
                        Log.e("Response is null",""+response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<OrderModel.Data> call, Throwable t) {
                Log.e("Response is failure", ""+t);

            }
        });*/
    }

    private void updateOrder(int remainingPlates) {

        if (remainingPlates != 0)
        {
            orderedPlate =orderedPlate+1;

            subscribtionModel.ordered_plates = orderedPlate;

            Call<SubscribtionModel>subModelUpdate = AppConstants.restAPI.updateSubscribeOrder(subsribeId,subscribtionModel);


            subModelUpdate.enqueue(new Callback<SubscribtionModel>() {
                @Override
                public void onResponse(Call<SubscribtionModel> call, Response<SubscribtionModel> response) {
                    if (response.isSuccessful())
                    {
                        if (response != null)
                        {
                            showToast("order Update");
                        }else
                        {
                            showToast("response null");

                        }
                    }else {
                        try {
                            Log.d("Response",response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<SubscribtionModel> call, Throwable t) {
                    showToast("response falier");

                }
            });

        }else {
            showToast("This order has completed");
        }

    }


}
