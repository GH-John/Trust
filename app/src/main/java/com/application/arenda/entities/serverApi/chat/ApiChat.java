package com.application.arenda.entities.serverApi.chat;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;

import com.application.arenda.entities.models.ModelChat;
import com.application.arenda.entities.models.ModelMessage;
import com.application.arenda.entities.serverApi.OnApiListener;
import com.application.arenda.entities.serverApi.client.ApiClient;
import com.application.arenda.entities.serverApi.client.CodeHandler;
import com.application.arenda.entities.serverApi.client.ServerResponse;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.List;

import io.reactivex.Single;
import io.socket.client.Socket;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public final class ApiChat {
    @SuppressLint("StaticFieldLeak")
    private static ApiChat instance;
    private static IApiChat api;
    private static Socket socket;

    private ApiChat(Context context) {
        api = ApiClient.getApi(context).create(IApiChat.class);
    }

    public static ApiChat getInstance(Context context) {
        if (instance == null)
            instance = new ApiChat(context);
        return instance;
    }

    public static Socket getSocket() {
        return socket;
    }

    public synchronized Single<List<ModelChat>> loadChats(String token, long lastID, int limitItemsInPage, OnApiListener listener) {
        return Single.create(emitter ->
                api.loadChats(
                        token,
                        lastID,
                        limitItemsInPage)
                        .enqueue(new Callback<ServerResponse<List<ModelChat>>>() {
                            @Override
                            public void onResponse(@NonNull Call<ServerResponse<List<ModelChat>>> call, @NonNull Response<ServerResponse<List<ModelChat>>> response) {
                                if (response.isSuccessful()) {
                                    if (listener != null)
                                        listener.onComplete(response.body().getHandler());

                                    if (response.body().getHandler() == CodeHandler.SUCCESS ||
                                            response.body().getHandler() == CodeHandler.NONE_REZULT)
                                        emitter.onSuccess(response.body().getResponse());
                                } else {
                                    if (listener != null)
                                        listener.onComplete(CodeHandler.get(response.code()));
                                    Timber.tag("LoadingError").e(response.code() + " - " + response.message());
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<ServerResponse<List<ModelChat>>> call, @NonNull Throwable t) {
                                if (t instanceof SocketTimeoutException || t instanceof ConnectException)
                                    if (listener != null)
                                        listener.onComplete(CodeHandler.NETWORK_ERROR);


                                if (listener != null)
                                    listener.onFailure(t);
                                emitter.onError(t);
                            }
                        }));
    }

    public synchronized Single<List<ModelMessage>> loadChat(String token, long idUser_To, long loadAfterIdMessage, long loadBeforeIdMessage, int limitItemsInPage, OnApiListener listener) {
        return Single.create(emitter ->
                api.loadChat(
                        token,
                        idUser_To,
                        loadAfterIdMessage,
                        loadBeforeIdMessage,
                        limitItemsInPage)
                        .enqueue(new Callback<ServerResponse<List<ModelMessage>>>() {
                            @Override
                            public void onResponse(@NonNull Call<ServerResponse<List<ModelMessage>>> call, @NonNull Response<ServerResponse<List<ModelMessage>>> response) {
                                if (response.isSuccessful()) {
                                    if (listener != null)
                                        listener.onComplete(response.body().getHandler());

                                    if (response.body().getHandler() == CodeHandler.SUCCESS ||
                                            response.body().getHandler() == CodeHandler.NONE_REZULT)
                                        emitter.onSuccess(response.body().getResponse());
                                } else {
                                    if (listener != null)
                                        listener.onComplete(CodeHandler.get(response.code()));
                                    Timber.tag("LoadingError").e(response.code() + " - " + response.message());
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<ServerResponse<List<ModelMessage>>> call, @NonNull Throwable t) {
                                if (t instanceof SocketTimeoutException || t instanceof ConnectException)
                                    if (listener != null)
                                        listener.onComplete(CodeHandler.NETWORK_ERROR);


                                if (listener != null)
                                    listener.onFailure(t);
                                emitter.onError(t);
                            }
                        }));
    }
}