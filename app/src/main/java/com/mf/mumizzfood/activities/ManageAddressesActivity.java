package com.mf.mumizzfood.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.mf.mumizzfood.location.EnterFullAdressActivity;
import com.mf.mumizzfood.R;
import com.mf.mumizzfood.adapters.AddressRecycelrview;
import com.mf.mumizzfood.base.BaseActivity;
import com.mf.mumizzfood.models.ProfileModel;
import com.mf.mumizzfood.utils.AppConstants;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mf.mumizzfood.fragments.ProfileFragment.addressesList;

public class ManageAddressesActivity extends BaseActivity implements AddressRecycelrview.takeActionOnAddress{

    //@BindView(R.id.toolbar)
  //  Toolbar toolbar;

    @BindView(R.id.addressRecyclerView)
    RecyclerView addressRecyclerView;

    private int listPos;
    AddressRecycelrview foodDataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_manage_addresses);
        ButterKnife.bind(this);

      //  setSupportActionBar(toolbar);
        //toolbar.setTitle(getString(R.string.manage_address));
        //toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        //setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ///getSupportActionBar().setDisplayShowTitleEnabled(false);


        LinearLayoutManager linearLayoutManager = null;
        try {
            linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
            addressRecyclerView.setHasFixedSize(true);
            addressRecyclerView.scrollToPosition(0);
            addressRecyclerView.setLayoutManager(linearLayoutManager);
            addressRecyclerView.setItemAnimator(new DefaultItemAnimator());
            foodDataAdapter = new AddressRecycelrview(this, addressesList,this);
            addressRecyclerView.setAdapter(foodDataAdapter);
        } catch (Exception e) {

            e.printStackTrace();
        }


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
        NetworkCallForActionOnAdress(position,"Edit");
    }

    private void NetworkCallForActionOnAdress(int position,String action)
    {

        listPos = position;
        if (action.equalsIgnoreCase("Edit"))
        {
            Intent enterOtherAct = new Intent(ManageAddressesActivity.this,EnterFullAdressActivity.class);
            enterOtherAct.putExtra("Address",addressesList.get(position).complete_address);
            enterOtherAct.putExtra("city",addressesList.get(position).city);
            enterOtherAct.putExtra("lat",String.valueOf(addressesList.get(position).latitude));
            enterOtherAct.putExtra("long",String.valueOf(addressesList.get(position).longitude));
            enterOtherAct.putExtra("pincode",addressesList.get(position).pin);
            enterOtherAct.putExtra("Position",position);
            enterOtherAct.putExtra("state",addressesList.get(position).state);
            enterOtherAct.putExtra("From","OrderDetails");
            enterOtherAct.putExtra("landMark",addressesList.get(position).landmark);
            enterOtherAct.putExtra("flatNo",addressesList.get(position).house_no);
            startActivityForResult(enterOtherAct,200);
        }else if (action.equalsIgnoreCase("Delete"))
        {
            deleteAddressConfirmDialog();

        }else
        {
            networkCallForAsavingAddress(position);
        }

    }


    @OnClick(R.id.addNewAdd)
    public void addNewAdd()
    {
        Intent enterOtherAct = new Intent(ManageAddressesActivity.this,EnterFullAdressActivity.class);
        if (addressesList.size() != 0)
        {
            enterOtherAct.putExtra("AddNew","Yes");
            enterOtherAct.putExtra("lat",String.valueOf(addressesList.get(listPos).latitude));
            enterOtherAct.putExtra("long",String.valueOf(addressesList.get(listPos).longitude));
        }

        startActivityForResult(enterOtherAct,200);
    }

    private void networkCallForAsavingAddress(final int pos) {


        Call<ResponseBody>  addressModelCall   = AppConstants.restAPI.postAddressDelete(addressesList.get(pos).id);

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
                    addressesList.add(model);
                    foodDataAdapter = new AddressRecycelrview(this, addressesList,this);
                    addressRecyclerView.setAdapter(foodDataAdapter);
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
}
