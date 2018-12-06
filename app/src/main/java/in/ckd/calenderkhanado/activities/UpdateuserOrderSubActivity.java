package in.ckd.calenderkhanado.activities;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.ckd.calenderkhanado.R;
import in.ckd.calenderkhanado.base.BaseActivity;
import in.ckd.calenderkhanado.models.OneSignalModel;
import in.ckd.calenderkhanado.models.SubscribtionModel;
import in.ckd.calenderkhanado.models.UserProfileModel;
import in.ckd.calenderkhanado.utils.AppConstants;
import in.ckd.calenderkhanado.utils.CapsName;
import in.ckd.calenderkhanado.widgets.CkdTextview;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateuserOrderSubActivity extends BaseActivity
{
    @BindView(R.id.user_name)
    CkdTextview userName;

    @BindView(R.id.profile_image)
    ImageView profileImage;

    @BindView(R.id.updaetSub)
    CkdTextview updaetSub;

    private UserProfileModel.Data userData ;
    private SubscribtionModel.Data subscribtionModel;

    private String[] title = {
            "Abundance",
            "Anxiety",
            "Bruxism",
            "Discipline",
            "Drug Addiction"
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updateuser_order_sub);
        ButterKnife.bind(this);


        if (getIntent() != null)
        {
            userData = (UserProfileModel.Data) getIntent().getSerializableExtra("Model");
            subscribtionModel = (SubscribtionModel.Data) getIntent().getSerializableExtra("SubModel");

            if (userData.profile_image != null && !userData.profile_image.isEmpty()){
                Glide.with(this).load(userData.profile_image).into(profileImage);
            }

            if (userData.f_name != null && !"".equalsIgnoreCase(userData.f_name)){
                String name = CapsName.CapitalizeFullName(userData.f_name.trim());
                userName.setText(name);
            }

        }
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);


    }

    @OnClick(R.id.updaetSub)
    public void upDateOrder()
    {


        int totalPlates = subscribtionModel.number_of_days;
        int orderPlates = subscribtionModel.ordered_plates;

        int remmainPlates = totalPlates - orderPlates;

        if (orderPlates == 1){

            if (subscribtionModel.deliverd_order == 0)
            {
                updateOrder();
            }else {
                showToast("This Order has completed");
                finish();
            }
        }else {
            if (remmainPlates > 0)
            {
                updateOrder();
            }else
            {
                showToast("This Order has completed");
                finish();
            }
        }


    }


    private void updateOrder() {

            subscribtionModel.ordered_plates = subscribtionModel.ordered_plates+1;
            subscribtionModel.deliverd_order = 1;


            Call<SubscribtionModel> subModelUpdate = AppConstants.restAPI.updateSubscribeOrder(subscribtionModel.id,subscribtionModel);


            subModelUpdate.enqueue(new Callback<SubscribtionModel>() {
                @Override
                public void onResponse(Call<SubscribtionModel> call, Response<SubscribtionModel> response) {
                    if (response.isSuccessful())
                    {
                        if (response != null)
                        {
                            showToast("order Update");
                            sendNotification();
                            finish();
                        }else
                        {
                            showToast("response null");
                            finish();

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


    }

    private void sendNotification() {


        OSPermissionSubscriptionState status = OneSignal.getPermissionSubscriptionState();
        String userId = status.getSubscriptionStatus().getUserId();
        boolean isSubscribed = status.getSubscriptionStatus().getSubscribed();

        if (!isSubscribed)
            return;

        try {
            JSONObject notificationContent = new JSONObject("{'contents': {'en': 'Order deliverd,Love your mom always'}," +
                    "'include_player_ids': ['" + userId + "'], " +
                    "'headings': {'en': 'Solution for your craving'}, " +
                    "'big_picture': 'https://www.shoutlo.com/uploads/articles/header-img-places-to-get-north-indian-food-in-hyderabad.jpg'}");
            OneSignal.postNotification(notificationContent, null);
        } catch (JSONException e) {
            e.printStackTrace();
        }


      /*      try {
                String jsonResponse;

                URL url = new URL("https://onesignal.com/api/v1/notifications");
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.setUseCaches(false);
                con.setDoOutput(true);
                con.setDoInput(true);

                con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                con.setRequestProperty("Authorization", "Basic ZWFjMzI2NWItNjc0OC00MTU0LThlNjYtNDFhNzk3YmU5MTdk");
                con.setRequestMethod("POST");

                OneSignalModel model = new OneSignalModel();

                model.app_id= AppConstants.ONESINGAL;
                model.included_segments = title;


                String strJsonBody = "{"
                        +   "\"app_id\": \""+AppConstants.ONESINGAL+"\","
                        +   "\"filters\": [{\"field\": \"tag\", \"key\": \"level\", \"relation\": \">\", \"value\": \"10\"},{\"operator\": \"OR\"},{\"field\": \"amount_spent\", \"relation\": \">\",\"value\": \"0\"}],"
                        +   "\"data\": {\"foo\": \"bar\"},"
                        +   "\"contents\": {\"en\": \"English Message\"}"
                        + "}";


                System.out.println("strJsonBody:\n" +strJsonBody );

                byte[] sendBytes = strJsonBody.getBytes("UTF-8");
                con.setFixedLengthStreamingMode(sendBytes.length);

                OutputStream outputStream = con.getOutputStream();
                outputStream.write(sendBytes);

                int httpResponse = con.getResponseCode();
                System.out.println("httpResponse: " + httpResponse);

                if (  httpResponse >= HttpURLConnection.HTTP_OK
                        && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
                    Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
                    jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                    scanner.close();
                }
                else {
                    Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
                    jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                    scanner.close();
                }
                System.out.println("jsonResponse:\n" + jsonResponse);

            } catch(Throwable t) {
                t.printStackTrace();
            }*/
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

