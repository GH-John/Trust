package com.application.arenda.Entities.Utils.RestApi;

import android.content.Context;

import com.application.arenda.BuildConfig;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestApiFactory {
    private static RestApi restApi;
    private static OkHttpClient.Builder okHttpBuilder;
    private static HttpLoggingInterceptor httpLoggingInterceptor;

    public static RestApi getRestApi(Context context) {
        if (restApi == null)
            restApi = new Retrofit.Builder().baseUrl(BuildConfig.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(getOkHttpClientBuild(context))
                    .build()
                    .create(RestApi.class);

        return restApi;
    }

    public static OkHttpClient getOkHttpClientBuild(Context context) {
        if (okHttpBuilder == null || httpLoggingInterceptor == null) {
            okHttpBuilder = new OkHttpClient().newBuilder();
            httpLoggingInterceptor = new HttpLoggingInterceptor();

            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            okHttpBuilder.addInterceptor(httpLoggingInterceptor);
        }

        return okHttpBuilder.build();
    }
}