package com.application.arenda.entities.serverApi.proposal;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;

import com.application.arenda.entities.models.ModelPeriodRent;
import com.application.arenda.entities.models.ModelProposal;
import com.application.arenda.entities.serverApi.OnApiListener;
import com.application.arenda.entities.serverApi.client.ApiClient;
import com.application.arenda.entities.serverApi.client.ApiHandler;
import com.application.arenda.entities.serverApi.client.CodeHandler;
import com.application.arenda.entities.serverApi.client.ServerResponse;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.List;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public final class ApiProposal {
    @SuppressLint("StaticFieldLeak")
    private static ApiProposal instance;
    private static IApiProposal api;

    private ApiProposal(Context context) {
        api = ApiClient.getApi(context).create(IApiProposal.class);
    }

    public static ApiProposal getInstance(Context context) {
        if (instance == null)
            instance = new ApiProposal(context);
        return instance;
    }

    public synchronized Single<List<ModelPeriodRent>> loadPeriodRentAnnouncement(long idAnnouncement, OnApiListener listener) {
        return Single.create(emitter ->
                api.loadPeriodRentAnnouncement(
                        idAnnouncement)
                        .enqueue(new Callback<ServerResponse<List<ModelPeriodRent>>>() {
                            @Override
                            public void onResponse(@NonNull Call<ServerResponse<List<ModelPeriodRent>>> call, @NonNull Response<ServerResponse<List<ModelPeriodRent>>> response) {
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
                            public void onFailure(@NonNull Call<ServerResponse<List<ModelPeriodRent>>> call, @NonNull Throwable t) {
                                if (t instanceof SocketTimeoutException || t instanceof ConnectException)
                                    if (listener != null)
                                        listener.onComplete(CodeHandler.NETWORK_ERROR);


                                if (listener != null)
                                    listener.onFailure(t);
                                emitter.onError(t);
                            }
                        }));
    }

    public synchronized Single<List<ModelProposal>> loadIncomingProposal(String userToken, long idRent, int limitItemsInPage, OnApiListener listener) {
        return Single.create(emitter ->
                api.loadIncomingProposals(
                        userToken,
                        idRent,
                        limitItemsInPage)
                        .enqueue(new Callback<ServerResponse<List<ModelProposal>>>() {
                            @Override
                            public void onResponse(@NonNull Call<ServerResponse<List<ModelProposal>>> call, @NonNull Response<ServerResponse<List<ModelProposal>>> response) {
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
                            public void onFailure(@NonNull Call<ServerResponse<List<ModelProposal>>> call, @NonNull Throwable t) {
                                if (t instanceof SocketTimeoutException || t instanceof ConnectException)
                                    if (listener != null)
                                        listener.onComplete(CodeHandler.NETWORK_ERROR);

                                if (listener != null)
                                    listener.onFailure(t);
                                emitter.onError(t);
                            }
                        }));
    }

    public synchronized Single<List<ModelProposal>> loadOutgoingProposal(String userToken, long idRent, int limitItemsInPage, OnApiListener listener) {
        return Single.create(emitter ->
                api.loadOutgoingProposals(
                        userToken,
                        idRent,
                        limitItemsInPage)
                        .enqueue(new Callback<ServerResponse<List<ModelProposal>>>() {
                            @Override
                            public void onResponse(@NonNull Call<ServerResponse<List<ModelProposal>>> call, @NonNull Response<ServerResponse<List<ModelProposal>>> response) {
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
                            public void onFailure(@NonNull Call<ServerResponse<List<ModelProposal>>> call, @NonNull Throwable t) {
                                if (t instanceof SocketTimeoutException || t instanceof ConnectException)
                                    if (listener != null)
                                        listener.onComplete(CodeHandler.NETWORK_ERROR);


                                if (listener != null)
                                    listener.onFailure(t);
                                emitter.onError(t);
                            }
                        }));
    }

    public synchronized Single<List<ModelProposal>> loadReservationProposal(String userToken, long idRent, int limitItemsInPage, OnApiListener listener) {
        return Single.create(emitter ->
                api.loadReservationProposal(
                        userToken,
                        idRent,
                        limitItemsInPage)
                        .enqueue(new Callback<ServerResponse<List<ModelProposal>>>() {
                            @Override
                            public void onResponse(@NonNull Call<ServerResponse<List<ModelProposal>>> call, @NonNull Response<ServerResponse<List<ModelProposal>>> response) {
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
                            public void onFailure(@NonNull Call<ServerResponse<List<ModelProposal>>> call, @NonNull Throwable t) {
                                if (t instanceof SocketTimeoutException || t instanceof ConnectException)
                                    if (listener != null)
                                        listener.onComplete(CodeHandler.NETWORK_ERROR);


                                if (listener != null)
                                    listener.onFailure(t);
                                emitter.onError(t);
                            }
                        }));
    }

    public synchronized Single<List<ModelProposal>> loadActiveProposal(String userToken, long idRent, int limitItemsInPage, OnApiListener listener) {
        return Single.create(emitter ->
                api.loadActiveProposals(
                        userToken,
                        idRent,
                        limitItemsInPage)
                        .enqueue(new Callback<ServerResponse<List<ModelProposal>>>() {
                            @Override
                            public void onResponse(@NonNull Call<ServerResponse<List<ModelProposal>>> call, @NonNull Response<ServerResponse<List<ModelProposal>>> response) {
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
                            public void onFailure(@NonNull Call<ServerResponse<List<ModelProposal>>> call, @NonNull Throwable t) {
                                if (t instanceof SocketTimeoutException || t instanceof ConnectException)
                                    if (listener != null)
                                        listener.onComplete(CodeHandler.NETWORK_ERROR);


                                if (listener != null)
                                    listener.onFailure(t);
                                emitter.onError(t);
                            }
                        }));
    }

    public synchronized Single<List<ModelProposal>> loadHistoryProposal(String userToken, long idRent, int limitItemsInPage, OnApiListener listener) {
        return Single.create(emitter ->
                api.loadHistoryProposals(
                        userToken,
                        idRent,
                        limitItemsInPage)
                        .enqueue(new Callback<ServerResponse<List<ModelProposal>>>() {
                            @Override
                            public void onResponse(@NonNull Call<ServerResponse<List<ModelProposal>>> call, @NonNull Response<ServerResponse<List<ModelProposal>>> response) {
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
                            public void onFailure(@NonNull Call<ServerResponse<List<ModelProposal>>> call, @NonNull Throwable t) {
                                if (t instanceof SocketTimeoutException || t instanceof ConnectException)
                                    if (listener != null)
                                        listener.onComplete(CodeHandler.NETWORK_ERROR);


                                if (listener != null)
                                    listener.onFailure(t);
                                emitter.onError(t);
                            }
                        }));
    }


    public synchronized Single<ApiHandler> insertProposal(String token, long idAnnouncement, String rentalStart, String rentalEnd) {
        return Single.create(emitter -> api.insertProposal(token, idAnnouncement, rentalStart, rentalEnd)
                .enqueue(new Callback<ApiHandler>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiHandler> call, @NonNull Response<ApiHandler> response) {
                        if (response.isSuccessful()) {
                            emitter.onSuccess(response.body());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiHandler> call, @NonNull Throwable t) {
                        emitter.onError(t);
                    }
                }));
    }


    public synchronized Single<ApiHandler> acceptProposal(String token, long idRent) {
        return Single.create(emitter -> api.acceptProposal(token, idRent)
                .enqueue(new Callback<ApiHandler>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiHandler> call, @NonNull Response<ApiHandler> response) {
                        if (response.isSuccessful()) {
                            emitter.onSuccess(response.body());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiHandler> call, @NonNull Throwable t) {
                        emitter.onError(t);
                    }
                }));
    }

    public synchronized Single<ApiHandler> finishProposal(String token, long idRent) {
        return Single.create(emitter -> api.finishProposal(token, idRent)
                .enqueue(new Callback<ApiHandler>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiHandler> call, @NonNull Response<ApiHandler> response) {
                        if (response.isSuccessful()) {
                            emitter.onSuccess(response.body());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiHandler> call, @NonNull Throwable t) {
                        emitter.onError(t);
                    }
                }));
    }

    public synchronized Single<ApiHandler> startProposal(String token, long idRent) {
        return Single.create(emitter -> api.startProposal(token, idRent)
                .enqueue(new Callback<ApiHandler>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiHandler> call, @NonNull Response<ApiHandler> response) {
                        if (response.isSuccessful()) {
                            emitter.onSuccess(response.body());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiHandler> call, @NonNull Throwable t) {
                        emitter.onError(t);
                    }
                }));
    }

    public synchronized Single<ApiHandler> rescheduleReservationProposal(String token, long idRent) {
        return Single.create(emitter -> api.rescheduleReservationProposal(token, idRent)
                .enqueue(new Callback<ApiHandler>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiHandler> call, @NonNull Response<ApiHandler> response) {
                        if (response.isSuccessful()) {
                            emitter.onSuccess(response.body());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiHandler> call, @NonNull Throwable t) {
                        emitter.onError(t);
                    }
                }));
    }

    public synchronized Single<ApiHandler> cancleReservationProposal(String token, long idRent) {
        return Single.create(emitter -> api.cancleReservationProposal(token, idRent)
                .enqueue(new Callback<ApiHandler>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiHandler> call, @NonNull Response<ApiHandler> response) {
                        if (response.isSuccessful()) {
                            emitter.onSuccess(response.body());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiHandler> call, @NonNull Throwable t) {
                        emitter.onError(t);
                    }
                }));
    }

    public synchronized Single<ApiHandler> rejectIncomingProposal(String token, long idRent) {
        return Single.create(emitter -> api.rejectIncomingProposal(token, idRent)
                .enqueue(new Callback<ApiHandler>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiHandler> call, @NonNull Response<ApiHandler> response) {
                        if (response.isSuccessful()) {
                            emitter.onSuccess(response.body());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiHandler> call, @NonNull Throwable t) {
                        emitter.onError(t);
                    }
                }));
    }

    public synchronized Single<ApiHandler> rejectOutgoingProposal(String token, long idRent) {
        return Single.create(emitter -> api.rejectOutgoingProposal(token, idRent)
                .enqueue(new Callback<ApiHandler>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiHandler> call, @NonNull Response<ApiHandler> response) {
                        if (response.isSuccessful()) {
                            emitter.onSuccess(response.body());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiHandler> call, @NonNull Throwable t) {
                        emitter.onError(t);
                    }
                }));
    }
}