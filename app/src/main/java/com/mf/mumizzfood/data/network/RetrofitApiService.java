package com.mf.mumizzfood.data.network;



import com.mf.mumizzfood.location.LocationModel;
import com.mf.mumizzfood.data.network.model.LoginRequest;
import com.mf.mumizzfood.data.network.model.LogoutResponse;
import com.mf.mumizzfood.data.network.model.UploadMedia;
import com.mf.mumizzfood.models.AddressModel;
import com.mf.mumizzfood.models.DashBoardModel;
import com.mf.mumizzfood.models.HomeFeed;
import com.mf.mumizzfood.models.OrderModel;
import com.mf.mumizzfood.models.ProfileModel;
import com.mf.mumizzfood.models.SubscribtionModel;
import com.mf.mumizzfood.models.UserProfileModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Url;

/**
 * Created by Niel on 24/07/17.
 */

public interface RetrofitApiService {


    public static String BASEURL = "http://mummysfood.in/";

    OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .readTimeout(2, TimeUnit.MINUTES)
            .connectTimeout(2, TimeUnit.MINUTES)
            .build();

    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
            .setLenient()
            .create();

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://mummysfood.in/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build();

    @POST("user")
    Call<ResponseBody>saveUserInfo(@Body LoginRequest request);

    @PUT("user/{id}")
    Call<ResponseBody>updateUserInfo(@Body LoginRequest request,@Path("id")int id);


    @PUT("user/{id}")
    Call<ResponseBody>updateUserImage(@Path("id") int id, @Field("profile_image")String profile_image);



    @GET("user")
    Call<LogoutResponse> getUserInfo();

    @GET()
    Call<DashBoardModel>getChefData(@Url String url);

    @GET()
    Call<HomeFeed>getChefDataP(@Url String url);

    @GET("user/{id}")
    Call<ProfileModel>getProfileUserData(@Path("id") int id);

    @GET
    Call<UserProfileModel>getProfileUserDataForOrder(@Url String url);

//        @PUT("order")
//        Call<UserProfileModel>getProfileUserDataForOrder(@Url String url);


    @GET("user/{id}")
    Call<UserProfileModel> getProfileUserDataForOrder(@Path("id") int id);

    @POST("address")
    Call<AddressModel>postAddress(@Body AddressModel.Data addressModel);

    @PUT("address/{id}")
    Call<AddressModel>postAddressUpdate(@Path("id") int id,@Body AddressModel.Data addressModel);

    @DELETE("address/{id}")
    Call<ResponseBody>postAddressDelete(@Path("id") int id);

    @GET
    Call<SubscribtionModel>getSubscrbtionOrder(@Url String url);

    @POST("order")
    Call<OrderModel.Data> orderPlace(@Body OrderModel.Data orderModel);

    @POST("subscribe")
    Call<OrderModel.Data> subscribeOrder(@Body OrderModel.Data orderModel);

    @PUT("subscribe/{id}")
    Call<SubscribtionModel> updateSubscribeOrder(@Path("id") int id,@Body SubscribtionModel.Data model);

    @GET
    Call<SubscribtionModel> subscribeOrderById(@Url String url);

    @GET
    Call<LocationModel> getTopRatedMovies(@Url String url);

    @Multipart
    @POST("upload")
    Call<UploadMedia> uploadImage(@Part MultipartBody.Part image, @Part("user_id") Integer desc, @Part("entity_id") Integer entityId, @Part("entity_type") RequestBody type);



}