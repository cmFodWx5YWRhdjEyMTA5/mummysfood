package in.mummysfood.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.mummysfood.R;
import in.mummysfood.adapters.OrderStatusAdapter;
import in.mummysfood.base.BaseFragment;
import in.mummysfood.data.pref.PreferenceManager;
import in.mummysfood.models.ProfileModel;
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

        int user_id = pf.getIntForKey("user_id",0);

        String url = AppConstants.restAPI.BASEURL+"user/"+1;

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
    public void actionOnOrder(int position,String status)
    {

        if (status.equalsIgnoreCase("Cancel"))
        {
            showToast("Cancelled");
        }else
        {
            showToast("Deleted");

        }

    }

    public void networkcallforOrderAction()
    {

    }

}
