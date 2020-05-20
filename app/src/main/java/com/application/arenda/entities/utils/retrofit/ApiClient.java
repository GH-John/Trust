package com.application.arenda.entities.utils.retrofit;

import android.net.Uri;

import com.application.arenda.BuildConfig;
import com.application.arenda.entities.utils.retrofit.deserializers.BooleanDeserializer;
import com.application.arenda.entities.utils.retrofit.deserializers.CodeHandlerDeserializer;
import com.application.arenda.entities.utils.retrofit.deserializers.DateDeserializer;
import com.application.arenda.entities.utils.retrofit.deserializers.DateTimeDeserializer;
import com.application.arenda.entities.utils.retrofit.deserializers.TimeDeserializer;
import com.application.arenda.entities.utils.retrofit.deserializers.UriDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static Retrofit retrofit;

    public synchronized static Retrofit getApi() {
        if (retrofit == null) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Uri.class, new UriDeserializer())
                    .registerTypeAdapter(LocalDate.class, new DateDeserializer())
                    .registerTypeAdapter(LocalTime.class, new TimeDeserializer())
                    .registerTypeAdapter(LocalDateTime.class, new DateTimeDeserializer())
                    .registerTypeAdapter(Boolean.class, new BooleanDeserializer())
                    .registerTypeAdapter(CodeHandler.class, new CodeHandlerDeserializer())
                    .create();

            OkHttpClient.Builder client = new OkHttpClient.Builder()
                    .readTimeout(5, TimeUnit.MINUTES)
                    .writeTimeout(5, TimeUnit.MINUTES)
                    .addInterceptor(chain -> {
                        Request original = chain.request();
                        Request.Builder builder = original.newBuilder()
                                .method(original.method(), original.body());

                        Request request = builder.build();

                        return chain.proceed(request);
                    })
                    .addInterceptor(interceptor);

            retrofit = new Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(client.build())
                    .build();
        }

        return retrofit;
    }
}