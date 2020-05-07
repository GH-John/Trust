package com.application.arenda.Entities.Announcements;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.application.arenda.BuildConfig;
import com.application.arenda.Entities.Models.ModelAnnouncement;
import com.application.arenda.Entities.Models.ModelCategory;
import com.application.arenda.Entities.Models.ModelInsertAnnouncement;
import com.application.arenda.Entities.Models.ModelSubcategory;
import com.application.arenda.Entities.Utils.FileUtils;
import com.application.arenda.Entities.Utils.Retrofit.ApiClient;
import com.application.arenda.Entities.Utils.Retrofit.CodeHandler;
import com.application.arenda.Entities.Utils.Retrofit.RetrofitUtils;
import com.application.arenda.Entities.Utils.Retrofit.ServerHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
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

    private ApiAnnouncement() {
        api = ApiClient.getApi().create(IApiAnnouncement.class);
    }

    public static ApiAnnouncement getInstance() {
        if (instance == null)
            instance = new ApiAnnouncement();
        return instance;
    }

    @NonNull
    public synchronized Observable<List<ModelCategory>> getCategories(OnApiListener announcementListener) {
        return Observable.create(observableEmitter -> {
            List<ModelCategory> categoryList = new ArrayList<>();

            api.getCategories()
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(@NonNull Call<ResponseBody> call,
                                               @NonNull Response<ResponseBody> response) {
                            try {
                                String res = response.body().string();

                                if (res != null) {
                                    JSONObject object = new JSONObject(res);

                                    String message = object.getString("response");

                                    if (IApiAnnouncement.AnnouncementCodes.valueOf(message) == IApiAnnouncement.AnnouncementCodes.SUCCESS_CATEGORIES_LOADED) {

                                        JSONArray categories = object.getJSONArray("categories");

                                        JSONObject model;

                                        for (int i = 0; i < categories.length(); i++) {
                                            model = categories.getJSONObject(i);

                                            categoryList.add(new ModelCategory(Long.parseLong(model.getString("idCategory")),

                                                    //--> delete for release version

                                                    BuildConfig.BASE_URL +

                                                            //<--

                                                            model.getString("iconUri"), model.getString("name").trim()));
                                        }

                                        Collections.sort(categoryList, (o1, o2) -> o2.getName().length() - o1.getName().length());

                                        observableEmitter.onNext(categoryList);

//                                        if (announcementListener != null)
//                                            announcementListener.onComplete(IApiAnnouncement.AnnouncementCodes.valueOf(message));
                                    }
                                } else {
//                                    if (announcementListener != null)
//                                        announcementListener.onComplete(IApiAnnouncement.AnnouncementCodes.NETWORK_ERROR);
                                }
                            } catch (JSONException | IOException e) {
                                Timber.e(e);

                                if (announcementListener != null)
                                    announcementListener.onFailure(e);

                                observableEmitter.onError(e);
                            } finally {
                                observableEmitter.onComplete();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                            if (announcementListener != null)
                                announcementListener.onFailure(t);

                            observableEmitter.onError(t);
                        }
                    });
        });
    }

    @NonNull
    public synchronized Observable<List<ModelSubcategory>> getSubcategories(long idCategory, OnApiListener listener) {
        return Observable.create(observableEmitter -> {
            List<ModelSubcategory> subcategoryList = new ArrayList<>();

            api.getSubcategories(idCategory).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call,
                                       @NonNull Response<ResponseBody> response) {
                    try {
                        String res = response.body().string();

                        if (res != null) {
                            JSONObject object = new JSONObject(res);

                            String message = object.getString("response");

                            if (IApiAnnouncement.AnnouncementCodes.valueOf(message) == IApiAnnouncement.AnnouncementCodes.SUCCESS_SUBCATEGORIES_LOADED) {

                                JSONArray subcategories = object.getJSONArray("subcategories");

                                JSONObject model;

                                for (int i = 0; i < subcategories.length(); i++) {
                                    model = subcategories.getJSONObject(i);

                                    subcategoryList.add(new ModelSubcategory(Long.parseLong(model.getString("idSubcategory")),
                                            idCategory, model.getString("name").trim()));
                                }

                                Collections.sort(subcategoryList, (o1, o2) -> o2.getName().length() - o1.getName().length());

                                observableEmitter.onNext(subcategoryList);

//                                if (listener != null)
//                                    listener.onComplete(IApiAnnouncement.AnnouncementCodes.valueOf(message));
                            }
                        } else {
//                            if (listener != null)
//                                listener.onComplete(IApiAnnouncement.AnnouncementCodes.NETWORK_ERROR);
                        }
                    } catch (JSONException | IOException e) {
                        Timber.e(e);
                        if (listener != null)
                            listener.onFailure(e);

                        observableEmitter.onError(e);

                        observableEmitter.onError(e);
                    } finally {
                        observableEmitter.onComplete();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                    if (listener != null)
                        listener.onFailure(t);

                    observableEmitter.onError(t);
                }
            });
        });
    }

    public synchronized Observable<IApiAnnouncement.AnnouncementCodes> insertAnnouncement(Context context, String token, ModelInsertAnnouncement announcement) {
        return Observable.create(emitter -> {
            List<Uri> uris = announcement.getUrisBitmap();
            List<MultipartBody.Part> partList = new ArrayList<>();

            for (int i = 0; i < uris.size(); i++) {
                partList.add(RetrofitUtils.createFilePart(context, "picture_" + i, uris.get(i)));
            }

            api.insertAnnouncement(
                    token,
                    announcement.getIdSubcategory(),

                    announcement.getName(),
                    announcement.getDescription(),

                    announcement.getCostToBYN(),
                    announcement.getCostToUSD(),
                    announcement.getCostToEUR(),

                    announcement.getAddress(),

                    announcement.getPhone_1(),

                    announcement.getPhone_2(),

                    announcement.getPhone_3()

            ).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            String res = response.body().string();

                            if (res != null) {
                                JSONObject object = new JSONObject(res);

                                String message = object.getString("response");

                                if (IApiAnnouncement.AnnouncementCodes.valueOf(message) == IApiAnnouncement.AnnouncementCodes.SUCCESS_ANNOUNCEMENT_ADDED) {

                                    emitter.onNext(IApiAnnouncement.AnnouncementCodes.SUCCESS_ANNOUNCEMENT_ADDED);

                                    insertPictures(object.getString("idAnnouncement"), FileUtils.getFileName(context, announcement.getMainUri()), partList, new OnApiListener() {
                                        @Override
                                        public void onComplete(@NonNull CodeHandler code) {
//                                        emitter.onNext(code);
                                        }

                                        @Override
                                        public void onFailure(@NonNull Throwable t) {
                                            emitter.onError(t);
                                        }
                                    });
                                } else {
                                    emitter.onNext(IApiAnnouncement.AnnouncementCodes.UNKNOW_ERROR);
                                }
                            } else {
                                emitter.onNext(IApiAnnouncement.AnnouncementCodes.UNKNOW_ERROR);
                            }
                        } catch (JSONException | IOException e) {
                            Timber.e(e);
                            emitter.onError(e);
                        } finally {
                            emitter.onComplete();
                        }
                    } else {
                        emitter.onNext(IApiAnnouncement.AnnouncementCodes.UNKNOW_ERROR);

                        Timber.tag("ErrorInsertAnnouncement").e(response.message());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                    emitter.onError(t);
                }
            });
        });
    }

    public synchronized Single<List<ModelAnnouncement>> loadAnnouncements(Context context, String token, long lastID, int limitItemsInPage, String query, OnApiListener listener) {
        return Single.create(emitter ->
                api.loadAnnouncements(
                        token,
                        lastID,
                        limitItemsInPage,
                        query)
                        .enqueue(new Callback<ServerHandler<List<ModelAnnouncement>>>() {
                            @Override
                            public void onResponse(@NonNull Call<ServerHandler<List<ModelAnnouncement>>> call, @NonNull Response<ServerHandler<List<ModelAnnouncement>>> response) {
                                if (response.isSuccessful() && CodeHandler.SUCCESS.getCode() == response.body().getCode()) {
                                    if (listener != null)
                                        listener.onComplete(CodeHandler.get(response.body().getCode()));

                                    emitter.onSuccess(response.body().getResponse());
                                } else if (response.code() >= 200 && response.code() <= 300) {
                                    if (listener != null)
                                        listener.onComplete(CodeHandler.get(response.body().getCode()));

                                    Timber.tag("LoadingError").e(response.body().getError());
                                } else {
                                    Timber.tag("LoadingError").e(response.code() + " - " + response.message());
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<ServerHandler<List<ModelAnnouncement>>> call, @NonNull Throwable t) {
                                if (t instanceof SocketTimeoutException || t instanceof ConnectException)
                                    if (listener != null)
                                        listener.onComplete(CodeHandler.NETWORK_ERROR);


                                if (listener != null)
                                    listener.onFailure(t);
                                emitter.onError(t);
                            }
                        }));
    }

    public synchronized Single<List<ModelAnnouncement>> loadUserAnnouncements(Context context, String token, long lastID, int limitItemsInPage, String query, OnApiListener listener) {
        return Single.create(emitter ->
                api.loadUserAnnouncements(
                        token,
                        lastID,
                        limitItemsInPage,
                        query)
                        .enqueue(new Callback<ServerHandler<List<ModelAnnouncement>>>() {
                            @Override
                            public void onResponse(@NonNull Call<ServerHandler<List<ModelAnnouncement>>> call, @NonNull Response<ServerHandler<List<ModelAnnouncement>>> response) {
                                if (response.isSuccessful() && CodeHandler.SUCCESS.getCode() == response.body().getCode()) {
                                    if (listener != null)
                                        listener.onComplete(CodeHandler.get(response.body().getCode()));

                                    emitter.onSuccess(response.body().getResponse());
                                } else if (response.code() >= 200 && response.code() <= 300) {
                                    if (listener != null)
                                        listener.onComplete(CodeHandler.get(response.body().getCode()));

                                    Timber.tag("LoadingError").e(response.body().getError());
                                } else {
                                    Timber.tag("LoadingError").e(response.code() + " - " + response.message());
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<ServerHandler<List<ModelAnnouncement>>> call, @NonNull Throwable t) {
                                if (t instanceof SocketTimeoutException || t instanceof ConnectException)
                                    if (listener != null)
                                        listener.onComplete(CodeHandler.NETWORK_ERROR);


                                if (listener != null)
                                    listener.onFailure(t);
                                emitter.onError(t);
                            }
                        }));
    }

    public synchronized Single<List<ModelAnnouncement>> loadLandLordAnnouncements(Context context, String token, long idLandLord, long lastID, int limitItemsInPage, String query, OnApiListener listener) {
        return Single.create(emitter ->
                api.loadLandLordAnnouncements(
                        token,
                        idLandLord,
                        lastID,
                        limitItemsInPage,
                        query)
                        .enqueue(new Callback<ServerHandler<List<ModelAnnouncement>>>() {
                            @Override
                            public void onResponse(@NonNull Call<ServerHandler<List<ModelAnnouncement>>> call, @NonNull Response<ServerHandler<List<ModelAnnouncement>>> response) {
                                if (response.isSuccessful() && CodeHandler.SUCCESS.getCode() == response.body().getCode()) {
                                    if (listener != null)
                                        listener.onComplete(CodeHandler.get(response.body().getCode()));

                                    emitter.onSuccess(response.body().getResponse());
                                } else if (response.code() >= 200 && response.code() <= 300) {
                                    if (listener != null)
                                        listener.onComplete(CodeHandler.get(response.body().getCode()));

                                    Timber.tag("LoadingError").e(response.body().getError());
                                } else {
                                    Timber.tag("LoadingError").e(response.code() + " - " + response.message());
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<ServerHandler<List<ModelAnnouncement>>> call, @NonNull Throwable t) {
                                if (t instanceof SocketTimeoutException || t instanceof ConnectException)
                                    if (listener != null)
                                        listener.onComplete(CodeHandler.NETWORK_ERROR);


                                if (listener != null)
                                    listener.onFailure(t);
                                emitter.onError(t);
                            }
                        }));
    }

    public Single<List<ModelAnnouncement>> loadSimilarAnnouncements(Context context, String userToken, long idSubcategory, long idAnnouncement, int limitItemsInPage, String query, OnApiListener listener) {
        return Single.create(emitter ->
                api.loadSimilarAnnouncements(
                        userToken,
                        idSubcategory,
                        idAnnouncement,
                        limitItemsInPage,
                        query)
                        .enqueue(new Callback<ServerHandler<List<ModelAnnouncement>>>() {
                            @Override
                            public void onResponse(@NonNull Call<ServerHandler<List<ModelAnnouncement>>> call, @NonNull Response<ServerHandler<List<ModelAnnouncement>>> response) {
                                if (response.isSuccessful() && CodeHandler.SUCCESS.getCode() == response.body().getCode()) {
                                    if (listener != null)
                                        listener.onComplete(CodeHandler.get(response.body().getCode()));

                                    emitter.onSuccess(response.body().getResponse());
                                } else if (response.code() >= 200 && response.code() <= 300) {
                                    if (listener != null)
                                        listener.onComplete(CodeHandler.get(response.body().getCode()));

                                    Timber.tag("LoadingError").e(response.body().getError());
                                } else {
                                    Timber.tag("LoadingError").e(response.code() + " - " + response.message());
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<ServerHandler<List<ModelAnnouncement>>> call, @NonNull Throwable t) {
                                if (t instanceof SocketTimeoutException || t instanceof ConnectException)
                                    if (listener != null)
                                        listener.onComplete(CodeHandler.NETWORK_ERROR);


                                if (listener != null)
                                    listener.onFailure(t);
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

    private synchronized void insertPictures(String idAnnouncement, String mainUri, List<MultipartBody.Part> partList, OnApiListener listener) {
        api.insertPictures(RetrofitUtils.createPartFromString(idAnnouncement), RetrofitUtils.createPartFromString(mainUri), partList, partList.size())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        try {
                            String res = response.body().string();

                            if (res != null) {

                                JSONObject object = new JSONObject(res);

                                String message = object.getString("code");

                                if (IApiAnnouncement.AnnouncementCodes.valueOf(message) == IApiAnnouncement.AnnouncementCodes.SUCCESS_PICTURES_ADDED) {
//                                    listener.onComplete(IApiAnnouncement.AnnouncementCodes.SUCCESS_PICTURES_ADDED);
                                } else {
//                                    listener.onComplete(IApiAnnouncement.AnnouncementCodes.UNKNOW_ERROR);
                                }
                            } else {
//                                listener.onComplete(IApiAnnouncement.AnnouncementCodes.UNKNOW_ERROR);
                            }
                        } catch (JSONException | IOException e) {
                            Timber.e(e);
//                            listener.onComplete(IApiAnnouncement.AnnouncementCodes.UNKNOW_ERROR);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                        Timber.e(t);
//                        listener.onComplete(IApiAnnouncement.AnnouncementCodes.UNKNOW_ERROR);
                    }
                });
    }

    public synchronized Completable insertViewer(String token, long idAnnouncement, OnApiListener listener) {
        return Completable.create(emitter -> api
                .insertViewer(token, idAnnouncement)
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
                                        emitter.onComplete();
                                    }

                                    if (listener != null)
                                        listener.onComplete(CodeHandler.get(code));
                                }
                            } catch (Throwable throwable) {
                                emitter.onError(throwable);
                            }
                        } else {
                            emitter.onComplete();

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
}