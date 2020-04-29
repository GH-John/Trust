package com.application.arenda.Entities.Authentication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.application.arenda.Entities.Models.ModelUser;
import com.application.arenda.Entities.Room.LocalCacheManager;
import com.application.arenda.Entities.User.AccountType;
import com.application.arenda.Entities.Utils.Retrofit.ApiClient;
import com.application.arenda.Entities.Utils.Retrofit.CodeHandler;
import com.application.arenda.Entities.Utils.Retrofit.RetrofitUtils;
import com.application.arenda.Entities.Utils.Retrofit.ServerHandler;

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

    private ApiAuthentication() {
        api = ApiClient.getApi().create(IApiAuthentication.class);
    }

    public static ApiAuthentication getInstance() {
        if (instance == null)
            instance = new ApiAuthentication();

        return instance;
    }

    private synchronized void authentication(@NonNull Context context, @NonNull Response<ServerHandler<ModelUser>> response, SingleEmitter<CodeHandler> emitter) {
        if (response.isSuccessful() && CodeHandler.SUCCESS.getCode() == response.body().getCode()) {
            LocalCacheManager.getInstance(context)
                    .users()
                    .changeCurrentUser(response.body().getResponse())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new ResourceCompletableObserver() {
                        @Override
                        public void onComplete() {
                            emitter.onSuccess(CodeHandler.get(response.body().getCode()));
                        }

                        @Override
                        public void onError(Throwable e) {
                            emitter.onError(e);
                        }
                    });
        } else if (response.code() >= 200 && response.code() <= 300) {
            Timber.tag("AuthenticationError").e(response.body().getError());
        } else {
            Timber.tag("AuthenticationError").e(response.code() + " - " + response.message());
        }
    }

    public Single<CodeHandler> registration(@NonNull Context context,
                                            @NonNull Uri userLogo,
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
                .enqueue(new Callback<ServerHandler<ModelUser>>() {
                    @Override
                    public void onResponse(@NonNull Call<ServerHandler<ModelUser>> call, @NonNull Response<ServerHandler<ModelUser>> response) {
                        authentication(context, response, emitter);
                    }

                    @Override
                    public void onFailure(@NonNull Call<ServerHandler<ModelUser>> call, @NonNull Throwable t) {
                        emitter.onError(t);

                        Timber.e(t);
                    }
                }));
    }

    public Single<CodeHandler> authorization(@NonNull Context context,
                                             @NonNull String email,
                                             @NonNull String password) {
        return Single.create(emitter -> api.authorization(email, password)
                .enqueue(new Callback<ServerHandler<ModelUser>>() {
                    @Override
                    public void onResponse(@NonNull Call<ServerHandler<ModelUser>> call, @NonNull Response<ServerHandler<ModelUser>> response) {
                        authentication(context, response, emitter);
                    }

                    @Override
                    public void onFailure(@NonNull Call<ServerHandler<ModelUser>> call, @NonNull Throwable t) {
                        emitter.onError(t);

                        Timber.e(t);
                    }
                }));
    }
}