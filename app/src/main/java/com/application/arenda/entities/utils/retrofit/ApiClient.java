package com.application.arenda.entities.utils.retrofit;

import android.content.Context;
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
    private static long CACHE_SIZE = 10 * 1024 * 1024;

    public synchronized static Retrofit getApi(Context context) {
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
//                    .cache(new Cache(context.getCacheDir(), CACHE_SIZE))
                    .addInterceptor(chain -> {
//                        Request request = chain.request();
//
//                        if (NetworkCheck.Companion.isOnline(context))
//                            /*
//                             *  If there is Internet, get the cache that was stored 5 seconds ago.
//                             *  If the cache is older than 5 seconds, then discard it,
//                             *  and indicate an error in fetching the response.
//                             *  The 'max-age' attribute is responsible for this behavior.
//                             */
//                            request.newBuilder().header("Cache-Control", "public, max-age=" + 5).build();
//                        else
//                            /*
//                             *  If there is no Internet, get the cache that was stored 7 days ago.
//                             *  If the cache is older than 7 days, then discard it,
//                             *  and indicate an error in fetching the response.
//                             *  The 'max-stale' attribute is responsible for this behavior.
//                             *  The 'only-if-cached' attribute indicates to not retrieve new data; fetch the cache only instead.
//                             */
//                            request.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7).build();
//
//
//                        return chain.proceed(request);

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