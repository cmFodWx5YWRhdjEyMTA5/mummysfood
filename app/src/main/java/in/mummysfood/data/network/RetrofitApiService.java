package in.mummysfood.data.network;



import in.mummysfood.Location.LocationModel;
import in.mummysfood.data.network.model.LoginRequest;
import in.mummysfood.data.network.model.LogoutResponse;
import in.mummysfood.models.AddressModel;
import in.mummysfood.models.DashBoardModel;
import in.mummysfood.models.OrderModel;
import in.mummysfood.models.ProfileModel;
import in.mummysfood.models.UserProfileModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Url;

/**
 * Created by Niel on 24/07/17.
 */

public interface RetrofitApiService {


        public static String BASEURL = "http://www.foodsfarmer.com/";

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(2, TimeUnit.MINUTES)
                .connectTimeout(2, TimeUnit.MINUTES)
                .build();

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.foodsfarmer.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();

        @POST("user")
        Call<LoginRequest>saveUserInfo(@Body LoginRequest request);

        @GET("user")
        Call<LogoutResponse> getUserInfo();

        @GET("geoUser?lat=22.7533&lng=75.8937")
        Call<DashBoardModel>getChefData();

        @GET("user/{user_id}")
        Call<ProfileModel>getProfileUserData(@Path("user_id") int id);

        @GET
        Call<UserProfileModel>getProfileUserDataForOrder(@Url String url);

//        @PUT("order")
//        Call<UserProfileModel>getProfileUserDataForOrder(@Url String url);


        @GET("user/{id}")
        Call<UserProfileModel> getProfileUserDataForOrder(@Path("id") int id);

        @POST("address")
        Call<AddressModel.Data>postAddress(@Body AddressModel.Data addressModel);

        @POST("order")
        Call<OrderModel.Data> orderPlace(@Body OrderModel.Data orderModel);

        @GET
        Call<LocationModel> getTopRatedMovies(@Url String url);

}