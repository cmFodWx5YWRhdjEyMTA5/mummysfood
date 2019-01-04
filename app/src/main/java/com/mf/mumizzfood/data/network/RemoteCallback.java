package com.mf.mumizzfood.data.network;

import com.mf.mumizzfood.utils.AppLogger;

import retrofit2.Call;
import retrofit2.Callback;
import java.net.HttpURLConnection;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Response;


public abstract class RemoteCallback<T> implements Callback<T> {
    @Override
    public final void onResponse(Call<T> call, Response<T> response) {
        switch (response.code()) {
            case HttpsURLConnection.HTTP_OK:
            case HttpsURLConnection.HTTP_CREATED:
            case HttpsURLConnection.HTTP_ACCEPTED:
            case HttpsURLConnection.HTTP_NOT_AUTHORITATIVE:
                if (response.body() != null) {
                    AppLogger.d(response.body().toString());
                    onSuccess(response.body());
                }
                break;

            case HttpURLConnection.HTTP_UNAUTHORIZED:
                break;

            default:
                onFailed(new Throwable("Default " + response.code() + " " + response.message()));
        }
    }

    @Override
    public final void onFailure(Call<T> call, Throwable t) {
        AppLogger.d(t.getMessage());
        onFailed(t);
    }

    public abstract void onSuccess(T response);

    public abstract void onFailed(Throwable throwable);
}