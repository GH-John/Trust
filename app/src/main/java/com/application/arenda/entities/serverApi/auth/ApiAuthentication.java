package com.application.arenda.entities.serverApi.auth;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.application.arenda.entities.models.ModelUser;
import com.application.arenda.entities.room.LocalCacheManager;
import com.application.arenda.entities.user.AccountType;
import com.application.arenda.entities.utils.retrofit.ApiClient;
import com.application.arenda.entities.utils.retrofit.CodeHandler;
import com.application.arenda.entities.utils.retrofit.RetrofitUtils;
import com.application.arenda.entities.utils.retrofit.ServerResponse;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.ResourceCompletableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public final class ApiAuthentication {
    @SuppressLint("StaticFieldLeak")
    private static ApiAuthentication instance;

    private IApiAuthentication api;

    private ApiAuthentication(Context context) {
        api = ApiClient.getApi(context).create(IApiAuthentication.class);
    }

    public static ApiAuthentication getInstance(Context context) {
        if (instance == null)
            instance = new ApiAuthentication(context);

        return instance;
    }

    private synchronized void authentication(@NonNull Context context, @NonNull Response<ServerResponse<ModelUser>> response, SingleEmitter<CodeHandler> emitter) {
        try {
            if (response.isSuccessful()) {
                if (response.body() != null) {
                    LocalCacheManager.getInstance(context)
                            .users()
                            .changeCurrentUser(response.body().getResponse())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new ResourceCompletableObserver() {
                                @Override
                                public void onComplete() {
                                    emitter.onSuccess(response.body().getHandler());
                                }

                                @Override
                                public void onError(Throwable e) {
                                    emitter.onError(e);
                                }
                            });
                } else {
                    emitter.onSuccess(response.body().getHandler());
                }
            } else {
                Timber.tag("AuthenticationError").e(response.code() + " - " + response.message());
            }
        } catch (Throwable throwable) {
            Timber.e(throwable);
        }
    }

    public Single<CodeHandler> registration(@NonNull Context context,
                                            Uri userLogo,
                                            @NonNull String name,
                                            @NonNull String lastName,
                                            @NonNull String login,
                                            @NonNull String email,
                                            @NonNull String password,
                                            @NonNull String phone,
                                            @NonNull AccountType accountType) {

        return Single.create(emitter -> api.registration(RetrofitUtils.createFilePart(context, "userLogo", userLogo),
                RetrofitUtils.createPartFromString(name),
                RetrofitUtils.createPartFromString(lastName),
                RetrofitUtils.createPartFromString(login),
                RetrofitUtils.createPartFromString(email),
                RetrofitUtils.createPartFromString(password),
                RetrofitUtils.createPartFromString(phone),
                RetrofitUtils.createPartFromString(accountType.getType()))
                .enqueue(new Callback<ServerResponse<ModelUser>>() {
                    @Override
                    public void onResponse(@NonNull Call<ServerResponse<ModelUser>> call, @NonNull Response<ServerResponse<ModelUser>> response) {
                        authentication(context, response, emitter);
                    }

                    @Override
                    public void onFailure(@NonNull Call<ServerResponse<ModelUser>> call, @NonNull Throwable t) {
                        emitter.onError(t);

                        Timber.e(t);
                    }
                }));
    }

    public Single<CodeHandler> authorization(@NonNull Context context,
                                             @NonNull String email,
                                             @NonNull String password) {
        return Single.create(emitter -> api.authorization(email, password)
                .enqueue(new Callback<ServerResponse<ModelUser>>() {
                    @Override
                    public void onResponse(@NonNull Call<ServerResponse<ModelUser>> call, @NonNull Response<ServerResponse<ModelUser>> response) {
                        authentication(context, response, emitter);
                    }

                    @Override
                    public void onFailure(@NonNull Call<ServerResponse<ModelUser>> call, @NonNull Throwable t) {
                        emitter.onError(t);

                        Timber.e(t);
                    }
                }));
    }
}