package com.application.arenda.entities.serverApi.announcement;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.application.arenda.entities.models.ModelAnnouncement;
import com.application.arenda.entities.models.ModelCategory;
import com.application.arenda.entities.models.ModelFavoriteAnnouncement;
import com.application.arenda.entities.models.ModelInsertAnnouncement;
import com.application.arenda.entities.models.ModelSubcategory;
import com.application.arenda.entities.serverApi.OnApiListener;
import com.application.arenda.entities.serverApi.client.ApiClient;
import com.application.arenda.entities.serverApi.client.ApiHandler;
import com.application.arenda.entities.serverApi.client.CodeHandler;
import com.application.arenda.entities.serverApi.client.ServerResponse;
import com.application.arenda.entities.utils.FileUtils;
import com.application.arenda.entities.utils.retrofit.RetrofitUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public final class ApiAnnouncement {
    @SuppressLint("StaticFieldLeak")
    private static ApiAnnouncement instance;
    private static IApiAnnouncement api;

    private ApiAnnouncement(Context context) {
        api = ApiClient.getApi(context).create(IApiAnnouncement.class);
    }

    public static ApiAnnouncement getInstance(Context context) {
        if (instance == null)
            instance = new ApiAnnouncement(context);
        return instance;
    }

    @NonNull
    public synchronized Single<List<ModelCategory>> loadCategories() {
        return Single.create(emitter -> api.loadCategories()
                .enqueue(new Callback<ServerResponse<List<ModelCategory>>>() {
                    @Override
                    public void onResponse(@NonNull Call<ServerResponse<List<ModelCategory>>> call,
                                           @NonNull Response<ServerResponse<List<ModelCategory>>> response) {
                        if (response.isSuccessful()) {
                            emitter.onSuccess(response.body().getResponse());
                        } else {
                            emitter.onSuccess(Collections.emptyList());
                            Timber.tag("LoadingError").e(response.code() + " - " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ServerResponse<List<ModelCategory>>> call, @NonNull Throwable t) {
                        emitter.onError(t);
                    }
                }));
    }

    @NonNull
    public synchronized Single<List<ModelSubcategory>> loadSubcategories(long idCategory) {
        return Single.create(emitter -> api.loadSubcategories(idCategory)
                .enqueue(new Callback<ServerResponse<List<ModelSubcategory>>>() {
                    @Override
                    public void onResponse(@NonNull Call<ServerResponse<List<ModelSubcategory>>> call,
                                           @NonNull Response<ServerResponse<List<ModelSubcategory>>> response) {
                        if (response.isSuccessful()) {
                            emitter.onSuccess(response.body().getResponse());
                        } else {
                            emitter.onSuccess(Collections.emptyList());
                            Timber.tag("LoadingError").e(response.code() + " - " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ServerResponse<List<ModelSubcategory>>> call, @NonNull Throwable t) {
                        emitter.onError(t);
                    }
                }));
    }


    public synchronized Single<List<ModelAnnouncement>> loadAnnouncements(String token, long lastID, int limitItemsInPage, String query, OnApiListener listener) {
        return Single.create(emitter ->
                api.loadAnnouncements(
                        token,
                        lastID,
                        limitItemsInPage,
                        query)
                        .enqueue(new Callback<ServerResponse<List<ModelAnnouncement>>>() {
                            @Override
                            public void onResponse(@NonNull Call<ServerResponse<List<ModelAnnouncement>>> call, @NonNull Response<ServerResponse<List<ModelAnnouncement>>> response) {
                                if (response.isSuccessful()) {
                                    if (listener != null)
                                        listener.onComplete(response.body().getHandler());

                                    emitter.onSuccess(response.body().getResponse());
                                } else {
                                    Timber.tag("LoadingError").e(response.code() + " - " + response.message());
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<ServerResponse<List<ModelAnnouncement>>> call, @NonNull Throwable t) {
                                if (t instanceof SocketTimeoutException || t instanceof ConnectException)
                                    if (listener != null)
                                        listener.onComplete(CodeHandler.NETWORK_ERROR);


                                if (listener != null)
                                    listener.onFailure(t);
                                emitter.onError(t);
                            }
                        }));
    }

    public synchronized Single<List<ModelAnnouncement>> loadUserAnnouncements(String token, long lastID, int limitItemsInPage, String query, OnApiListener listener) {
        return Single.create(emitter ->
                api.loadUserAnnouncements(
                        token,
                        lastID,
                        limitItemsInPage,
                        query)
                        .enqueue(new Callback<ServerResponse<List<ModelAnnouncement>>>() {
                            @Override
                            public void onResponse(@NonNull Call<ServerResponse<List<ModelAnnouncement>>> call, @NonNull Response<ServerResponse<List<ModelAnnouncement>>> response) {
                                if (response.isSuccessful()) {
                                    if (listener != null)
                                        listener.onComplete(response.body().getHandler());

                                    emitter.onSuccess(response.body().getResponse());
                                } else {
                                    Timber.tag("LoadingError").e(response.code() + " - " + response.message());
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<ServerResponse<List<ModelAnnouncement>>> call, @NonNull Throwable t) {
                                if (t instanceof SocketTimeoutException || t instanceof ConnectException)
                                    if (listener != null)
                                        listener.onComplete(CodeHandler.NETWORK_ERROR);


                                if (listener != null)
                                    listener.onFailure(t);
                                emitter.onError(t);
                            }
                        }));
    }

    public synchronized Single<List<ModelAnnouncement>> loadLandLordAnnouncements(String token, long idLandLord, long lastID, int limitItemsInPage, String query, OnApiListener listener) {
        return Single.create(emitter ->
                api.loadLandLordAnnouncements(
                        token,
                        idLandLord,
                        lastID,
                        limitItemsInPage,
                        query)
                        .enqueue(new Callback<ServerResponse<List<ModelAnnouncement>>>() {
                            @Override
                            public void onResponse(@NonNull Call<ServerResponse<List<ModelAnnouncement>>> call, @NonNull Response<ServerResponse<List<ModelAnnouncement>>> response) {
                                if (response.isSuccessful()) {
                                    if (listener != null)
                                        listener.onComplete(response.body().getHandler());

                                    emitter.onSuccess(response.body().getResponse());
                                } else {
                                    Timber.tag("LoadingError").e(response.code() + " - " + response.message());
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<ServerResponse<List<ModelAnnouncement>>> call, @NonNull Throwable t) {
                                if (t instanceof SocketTimeoutException || t instanceof ConnectException)
                                    if (listener != null)
                                        listener.onComplete(CodeHandler.NETWORK_ERROR);


                                if (listener != null)
                                    listener.onFailure(t);
                                emitter.onError(t);
                            }
                        }));
    }

    public Single<List<ModelAnnouncement>> loadSimilarAnnouncements(String userToken, long idSubcategory, long idAnnouncement, int limitItemsInPage, String query, OnApiListener listener) {
        return Single.create(emitter ->
                api.loadSimilarAnnouncements(
                        userToken,
                        idSubcategory,
                        idAnnouncement,
                        limitItemsInPage,
                        query)
                        .enqueue(new Callback<ServerResponse<List<ModelAnnouncement>>>() {
                            @Override
                            public void onResponse(@NonNull Call<ServerResponse<List<ModelAnnouncement>>> call, @NonNull Response<ServerResponse<List<ModelAnnouncement>>> response) {
                                if (response.isSuccessful()) {
                                    if (listener != null)
                                        listener.onComplete(response.body().getHandler());

                                    emitter.onSuccess(response.body().getResponse());
                                } else {
                                    Timber.tag("LoadingError").e(response.code() + " - " + response.message());
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<ServerResponse<List<ModelAnnouncement>>> call, @NonNull Throwable t) {
                                if (t instanceof SocketTimeoutException || t instanceof ConnectException)
                                    if (listener != null)
                                        listener.onComplete(CodeHandler.NETWORK_ERROR);


                                if (listener != null)
                                    listener.onFailure(t);
                                emitter.onError(t);
                            }
                        }));
    }


    @SuppressLint("CheckResult")
    public synchronized Single<ApiHandler> insertAnnouncement(Context context, String token, ModelInsertAnnouncement announcement) {
        List<Uri> uris = announcement.getUrisBitmap();
        List<MultipartBody.Part> partList = new ArrayList<>();

        for (int i = 0; i < uris.size(); i++) {
            partList.add(RetrofitUtils.createFilePart(context, "picture_" + i, uris.get(i)));
        }

        return Single.create(emitter -> api.insertAnnouncement(
                RetrofitUtils.createPartFromString(token),
                RetrofitUtils.createPartFromString(String.valueOf(announcement.getIdSubcategory())),

                RetrofitUtils.createPartFromString(announcement.getName()),
                RetrofitUtils.createPartFromString(announcement.getDescription()),

                RetrofitUtils.createPartFromString(String.valueOf(announcement.getHourlyCost())),
                RetrofitUtils.createPartFromString(announcement.getHourlyCurrency().getCurrency()),

                RetrofitUtils.createPartFromString(String.valueOf(announcement.getDailyCost())),
                RetrofitUtils.createPartFromString(announcement.getDailyCurrency().getCurrency()),

                RetrofitUtils.createPartFromString(announcement.getAddress()),

                RetrofitUtils.createPartFromString(announcement.getPhone_1()),
                RetrofitUtils.createPartFromString(announcement.getPhone_2()),
                RetrofitUtils.createPartFromString(announcement.getPhone_3()),

                RetrofitUtils.createPartFromString(String.valueOf(announcement.getMinTime())),
                RetrofitUtils.createPartFromString(String.valueOf(announcement.getMinDay())),
                RetrofitUtils.createPartFromString(String.valueOf(announcement.getMaxRentalPeriod())),

                RetrofitUtils.createPartFromString(announcement.getTimeOfIssueWith().toString()),
                RetrofitUtils.createPartFromString(announcement.getTimeOfIssueBy().toString()),

                RetrofitUtils.createPartFromString(announcement.getReturnTimeWith().toString()),
                RetrofitUtils.createPartFromString(announcement.getReturnTimeBy().toString()),

                RetrofitUtils.createPartFromString(String.valueOf(announcement.isWithSale())),

                RetrofitUtils.createPartFromString(FileUtils.getFileName(context, announcement.getMainUri())),
                partList,
                RetrofitUtils.createPartFromString(String.valueOf(partList.size()))
        ).enqueue(new Callback<ApiHandler>() {
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

    public synchronized Single<Boolean> insertToFavorite(String token, long idAnnouncement, OnApiListener listener) {
        return Single.create(emitter -> api
                .insertToFavorite(token, idAnnouncement)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                String res = response.body().string();

                                if (res != null) {
                                    JSONObject object = new JSONObject(res);

                                    int code = object.getInt("code");

                                    if (CodeHandler.SUCCESS.getCode() == code) {
                                        JSONArray array = object.getJSONArray("response");

                                        emitter.onSuccess(array.getJSONObject(0).getBoolean("isFavorite"));
                                    }

                                    if (listener != null)
                                        listener.onComplete(CodeHandler.get(code));
                                }
                            } catch (Throwable throwable) {
                                emitter.onError(throwable);
                            }
                        } else {
                            emitter.onSuccess(false);

                            if (listener != null)
                                listener.onComplete(CodeHandler.UNKNOW_ERROR);

                            Timber.tag("ErrorInsertToFavorite").e(response.message());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                        emitter.onError(t);

                        if (listener != null)
                            listener.onFailure(t);

                        Timber.e(t);
                    }
                }));
    }

    public synchronized Single<ApiHandler> insertViewer(String token, long idAnnouncement, OnApiListener listener) {
        return Single.create(emitter -> api
                .insertViewer(token, idAnnouncement)
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

    public synchronized Single<List<ModelFavoriteAnnouncement>> loadFavoriteAnnouncements(String token, long lastID, int limitItemsInPage, OnApiListener listener) {
        return Single.create(emitter ->
                api.loadFavoriteAnnouncements(
                        token,
                        lastID,
                        limitItemsInPage)
                        .enqueue(new Callback<ServerResponse<List<ModelFavoriteAnnouncement>>>() {
                            @Override
                            public void onResponse(@NonNull Call<ServerResponse<List<ModelFavoriteAnnouncement>>> call, @NonNull Response<ServerResponse<List<ModelFavoriteAnnouncement>>> response) {
                                if (response.isSuccessful()) {
                                    if (listener != null)
                                        listener.onComplete(response.body().getHandler());

                                    emitter.onSuccess(response.body().getResponse());
                                } else {
                                    Timber.tag("LoadingError").e(response.code() + " - " + response.message());
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<ServerResponse<List<ModelFavoriteAnnouncement>>> call, @NonNull Throwable t) {
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