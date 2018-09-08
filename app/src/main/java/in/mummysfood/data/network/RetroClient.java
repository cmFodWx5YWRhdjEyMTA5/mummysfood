package in.mummysfood.data.network;


import in.mummysfood.BuildConfig;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Niel on 24/07/17.
 */

public class RetroClient {

    public static RetrofitApiService getRetroClient(){
        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(RetrofitApiService.class);
    }
}