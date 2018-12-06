package in.ckd.calenderkhanado.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.ckd.calenderkhanado.R;
import in.ckd.calenderkhanado.adapters.AddressRecycelrview;
import in.ckd.calenderkhanado.adapters.SubscriptionUser;
import in.ckd.calenderkhanado.base.BaseActivity;
import in.ckd.calenderkhanado.data.pref.PreferenceManager;
import in.ckd.calenderkhanado.location.EnterFullAdressActivity;
import in.ckd.calenderkhanado.models.ProfileModel;
import in.ckd.calenderkhanado.models.SubscribtionModel;
import in.ckd.calenderkhanado.models.UserProfileModel;
import in.ckd.calenderkhanado.utils.AppConstants;
import in.ckd.calenderkhanado.widgets.CkdTextview;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static in.ckd.calenderkhanado.fragments.ProfileFragment.addressesList;

public class SubscriptionUpdate_Activity extends BaseActivity implements SubscriptionUser.takeActionOnAddress{

    @BindView(R.id.addressRecyclerView)
    RecyclerView addressRecyclerView;

    @BindView(R.id.addNewAdd)
    CkdTextview addNewAdd;

    @BindView(R.id.addCons)
    CkdTextview addCons;

    private int listPos;
    SubscriptionUser foodDataAdapter;

    List<SubscribtionModel.Data>models;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_manage_addresses);
        ButterKnife.bind(this);

        models = new ArrayList<>();

        addNewAdd.setVisibility(View.GONE);

        addCons.setText("All Active Order List");

       networkCallForgetSubUserList();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void editAddress(int position)
    {
        NetworkCallgetUserData(position);
    }

    private void NetworkCallgetUserData(final int position) {


        Call<UserProfileModel>modelUser = AppConstants.restAPI.getProfileUserDataForOrder(models.get(position).user_id);
        modelUser.enqueue(new Callback<UserProfileModel>() {
            @Override
            public void onResponse(Call<UserProfileModel> call, Response<UserProfileModel> response) {
                if (response.isSuccessful())
                {
                    if (response != null)
                    {
                        UserProfileModel.Data model = response.body().data.get(0);

                             setAction(model,position);
                    }
                }else
                {
                    try {
                        Log.d("Error",response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserProfileModel> call, Throwable t)
            {
                Log.d("Error",t.getMessage());
            }
        });


    }

    private void setAction(UserProfileModel.Data model, int position) {

        Intent send = new Intent(this,UpdateuserOrderSubActivity.class);
        send.putExtra("Model",model);
        send.putExtra("SubModel",models.get(position));
        startActivity(send);
    }

    private void NetworkCallForActionOnAdress(int position,String action)
    {


            networkCallForAsavingAddress(position);


    }


    @OnClick(R.id.addNewAdd)
    public void addNewAdd()
    {
        Intent enterOtherAct = new Intent(SubscriptionUpdate_Activity.this,EnterFullAdressActivity.class);
        if (addressesList.size() != 0)
        {
            enterOtherAct.putExtra("AddNew","Yes");
            enterOtherAct.putExtra("lat",String.valueOf(addressesList.get(listPos).latitude));
            enterOtherAct.putExtra("long",String.valueOf(addressesList.get(listPos).longitude));
        }

        startActivityForResult(enterOtherAct,200);
    }

    private void networkCallForAsavingAddress(final int pos) {
        pf = new PreferenceManager(this,PreferenceManager.LOGIN_PREFERENCES_FILE);


        Call<ResponseBody> addressModelCall   = AppConstants.restAPI.postAddressDelete(addressesList.get(pos).id);

        addressModelCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful())
                {
                    if (response.body() != null)
                    {
                        showToast("Success");


                        addressesList.remove(pos);

                        foodDataAdapter.notifyDataSetChanged();
                    }
                }else
                {
                    try {
                        Log.d("Error",response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                try {
                    Log.d("Error",t.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }


    @Override
    public void deleteAddress(int position)
    {
        NetworkCallForActionOnAdress(position,"Delete");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check that it is the SecondActivity with an OK result
        if (requestCode == 200) {
            if (resultCode == RESULT_OK) {

                String dataValue = data.getStringExtra("Address");
                String landmark = data.getStringExtra("landMark");
                String flatNo = data.getStringExtra("flatNo");

                ProfileModel.Addresses model = addressesList.get(listPos);

                model.complete_address = dataValue;
                model.landmark = landmark;
                model.house_no = flatNo;

                if (addressesList.size() != 0)
                {
                    addressesList.set(listPos,model);
                }

                if (foodDataAdapter != null)
                {
                    foodDataAdapter.notifyItemChanged(listPos);
                }else
                {
                    /*addressesList.add(model);
                    foodDataAdapter = new Su(this, addressesList,this);
                    addressRecyclerView.setAdapter(foodDataAdapter);*/
                }


            }
        }
    }

    public void deleteAddressConfirmDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure to delete this address?")
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
                        networkCallForAsavingAddress(listPos);
                    }
                });
        // Create the AlertDialog object and return it
        AlertDialog alert = builder.create();
        alert.show();
    }


    private void networkCallForgetSubUserList() {
        pf = new PreferenceManager(this,PreferenceManager.LOGIN_PREFERENCES_FILE);


        String url = AppConstants.BASE_URL+"subscribe";
        Call<SubscribtionModel> addressModelCall   = AppConstants.restAPI.getSubscrbtionOrder(url);

        addressModelCall.enqueue(new Callback<SubscribtionModel>() {
            @Override
            public void onResponse(Call<SubscribtionModel> call, Response<SubscribtionModel> response) {
                if (response.isSuccessful())
                {
                    if (response.body() != null)
                    {



                       setAddapter(response.body().data);
                        models = response.body().data;
                    }
                }else
                {
                    try {
                        Log.d("Error",response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SubscribtionModel> call, Throwable t) {

                try {
                    Log.d("Error",t.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }

    private void setAddapter(List<SubscribtionModel.Data> data) {

        LinearLayoutManager linearLayoutManager = null;
        try {
            linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            addressRecyclerView.setHasFixedSize(true);
            addressRecyclerView.setLayoutManager(linearLayoutManager);
            addressRecyclerView.setItemAnimator(new DefaultItemAnimator());
            foodDataAdapter = new SubscriptionUser(this,data ,this);
            addressRecyclerView.setAdapter(foodDataAdapter);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

}
