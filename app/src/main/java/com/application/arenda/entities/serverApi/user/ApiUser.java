package com.application.arenda.entities.serverApi.user;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.application.arenda.entities.models.AccountType;
import com.application.arenda.entities.models.ModelUser;
import com.application.arenda.entities.models.ModelUserProfileToView;
import com.application.arenda.entities.room.LocalCacheManager;
import com.application.arenda.entities.serverApi.client.ApiClient;
import com.application.arenda.entities.serverApi.client.ApiHandler;
import com.application.arenda.entities.serverApi.client.CodeHandler;
import com.application.arenda.entities.serverApi.client.ServerResponse;
import com.application.arenda.entities.utils.retrofit.RetrofitUtils;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.ResourceCompletableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public final class ApiUser {
    @SuppressLint("StaticFieldLeak")
    private static ApiUser instance;

    private IApiUser api;

    private ApiUser(Context context) {
        api = ApiClient.getApi(context).create(IApiUser.class);
    }

    public static ApiUser getInstance(Context context) {
        if (instance == null)
            instance = new ApiUser(context);

        return instance;
    }

    @SuppressLint("CheckResult")
    private synchronized void authentication(@NonNull Context context, @NonNull Response<ServerResponse<ModelUser>> response, SingleEmitter<CodeHandler> emitter) {
        try {
            if (response.isSuccessful()) {
                if (response.body() != null && response.body().getHandler() == CodeHandler.SUCCESS) {
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
        } catch (Throwable t) {
            if (t instanceof SocketTimeoutException || t instanceof ConnectException)
                emitter.onSuccess(CodeHandler.NETWORK_ERROR);

            Timber.e(t);
        }
    }

    public synchronized Single<CodeHandler> registration(@NonNull Context context,
                                                         Uri userLogo,
                                                         @NonNull String name,
                                                         @NonNull String lastName,
                                                         @NonNull String login,
                                                         @NonNull String email,
                                                         @NonNull String password,
                                                         @NonNull String phone,
                                                         @NonNull String address,
                                                         @NonNull AccountType accountType) {

        return Single.create(emitter -> api.registration(
                RetrofitUtils.createFilePart(context, "userLogo", userLogo),
                RetrofitUtils.createPartFromString(name),
                RetrofitUtils.createPartFromString(lastName),
                RetrofitUtils.createPartFromString(login),
                RetrofitUtils.createPartFromString(email),
                RetrofitUtils.createPartFromString(password),
                RetrofitUtils.createPartFromString(phone),
                RetrofitUtils.createPartFromString(address),
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

    public synchronized Single<CodeHandler> authorization(@NonNull Context context,
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

    public synchronized Single<CodeHandler> followToUser(String token, long idUser, Boolean isFollow) {
        return Single.create(emitter -> api.followToUser(token, idUser, isFollow).enqueue(new Callback<ApiHandler>() {
            @Override
            public void onResponse(@NonNull Call<ApiHandler> call, @NonNull Response<ApiHandler> response) {
                if (response.isSuccessful())
                    emitter.onSuccess(response.body().getHandler());
                else {
                    emitter.onSuccess(CodeHandler.get(response.code()));

                    Timber.e(response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiHandler> call, @NonNull Throwable t) {
                emitter.onError(t);

                Timber.e(t);
            }
        }));
    }

    public synchronized Single<CodeHandler> loadOwnProfile(Context context, String token) {
        return Single.create(emitter -> api.loadOwnProfile(token).enqueue(new Callback<ServerResponse<ModelUser>>() {
            @SuppressLint("CheckResult")
            @Override
            public void onResponse(@NonNull Call<ServerResponse<ModelUser>> call, @NonNull Response<ServerResponse<ModelUser>> response) {
                if (response.isSuccessful()) {
                    if (response.body().getHandler() == CodeHandler.SUCCESS) {
                        ModelUser user = response.body().getResponse();
                        user.setCurrent(true);

                        LocalCacheManager.getInstance(context)
                                .users()
                                .updateInRoom(user)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(() -> emitter.onSuccess(response.body().getHandler()), Timber::e);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ServerResponse<ModelUser>> call, @NonNull Throwable t) {
                if (t instanceof SocketTimeoutException || t instanceof ConnectException)
                    emitter.onSuccess(CodeHandler.NETWORK_ERROR);

                emitter.onError(t);

                Timber.e(t);
            }
        }));
    }

    public synchronized Single<ServerResponse<ModelUserProfileToView>> loadProfileToView(String token, long idUser) {
        return Single.create(emitter -> api.loadProfileToView(token, idUser).enqueue(new Callback<ServerResponse<ModelUserProfileToView>>() {
            @Override
            public void onResponse(@NonNull Call<ServerResponse<ModelUserProfileToView>> call, @NonNull Response<ServerResponse<ModelUserProfileToView>> response) {
                if (response.isSuccessful())
                    emitter.onSuccess(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<ServerResponse<ModelUserProfileToView>> call, @NonNull Throwable t) {
                emitter.onError(t);

                Timber.e(t);
            }
        }));
    }

    public synchronized Single<CodeHandler> updateProfile(String token,
                                                         String address_1,
                                                         String address_2,
                                                         String address_3,
                                                         String phone_1,
                                                         String phone_2,
                                                         String phone_3) {
        return Single.create(emitter -> api.updateProfile(token,
                address_1, address_2, address_3,
                phone_1, phone_2, phone_3)
                .enqueue(new Callback<ApiHandler>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiHandler> call, @NonNull Response<ApiHandler> response) {
                        if (response.isSuccessful())
                            emitter.onSuccess(response.body().getHandler());
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiHandler> call, @NonNull Throwable t) {
                        emitter.onError(t);

                        Timber.e(t);
                    }
                }));
    }
}