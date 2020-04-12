package com.application.arenda.Entities.Announcements;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.application.arenda.BuildConfig;
import com.application.arenda.Entities.Models.ModelCategory;
import com.application.arenda.Entities.Models.ModelInsertAnnouncement;
import com.application.arenda.Entities.Models.ModelSubcategory;
import com.application.arenda.Entities.User.UserCookie;
import com.application.arenda.Entities.Utils.Retrofit.ApiClient;
import com.application.arenda.Entities.Utils.Retrofit.RetrofitUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class Announcement {
    @SuppressLint("StaticFieldLeak")
    private static Announcement announcement;

    private Announcement() {
    }

    public static Announcement getInstance() {
        if (announcement == null)
            announcement = new Announcement();

        return announcement;
    }

    @NonNull
    public Observable<List<ModelCategory>> getCategories(OnAnnouncementListener announcementListener) {
        return Observable.create(observableEmitter -> {
            List<ModelCategory> categoryList = new ArrayList<>();

            ApiClient.getApi()
                    .create(ApiAnnouncement.class).getCategories()
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(@NonNull Call<ResponseBody> call,
                                               @NonNull Response<ResponseBody> response) {
                            try {
                                String res = response.body().string();

                                if (res != null) {
                                    JSONObject object = new JSONObject(res);

                                    String message = object.getString("response");

                                    if (ApiAnnouncement.AnnouncementCodes.valueOf(message) == ApiAnnouncement.AnnouncementCodes.SUCCESS_CATEGORIES_LOADED) {

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

                                        if (announcementListener != null)
                                            announcementListener.onComplete(ApiAnnouncement.AnnouncementCodes.valueOf(message));
                                    }
                                } else {
                                    if (announcementListener != null)
                                        announcementListener.onComplete(ApiAnnouncement.AnnouncementCodes.NETWORK_ERROR);
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
    public Observable<List<ModelSubcategory>> getSubcategories(long idCategory, OnAnnouncementListener announcementListener) {
        return Observable.create(observableEmitter -> {
            List<ModelSubcategory> subcategoryList = new ArrayList<>();

            ApiClient.getApi()
                    .create(ApiAnnouncement.class).getSubcategories(idCategory).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call,
                                       @NonNull Response<ResponseBody> response) {
                    try {
                        String res = response.body().string();

                        if (res != null) {
                            JSONObject object = new JSONObject(res);

                            String message = object.getString("response");

                            if (ApiAnnouncement.AnnouncementCodes.valueOf(message) == ApiAnnouncement.AnnouncementCodes.SUCCESS_SUBCATEGORIES_LOADED) {

                                JSONArray subcategories = object.getJSONArray("subcategories");

                                JSONObject model;

                                Timber.tag("SUB_SIZE").i(String.valueOf(subcategories.length()));

                                for (int i = 0; i < subcategories.length(); i++) {
                                    model = subcategories.getJSONObject(i);

                                    subcategoryList.add(new ModelSubcategory(Long.parseLong(model.getString("idSubcategory")),
                                            idCategory, model.getString("name").trim()));
                                }

                                Collections.sort(subcategoryList, (o1, o2) -> o2.getName().length() - o1.getName().length());

                                observableEmitter.onNext(subcategoryList);

                                if (announcementListener != null)
                                    announcementListener.onComplete(ApiAnnouncement.AnnouncementCodes.valueOf(message));
                            }
                        } else {
                            if (announcementListener != null)
                                announcementListener.onComplete(ApiAnnouncement.AnnouncementCodes.NETWORK_ERROR);
                        }
                    } catch (JSONException | IOException e) {
                        Timber.e(e);
                        if (announcementListener != null)
                            announcementListener.onFailure(e);

                        observableEmitter.onError(e);

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

    public Observable<ApiAnnouncement.AnnouncementCodes> insertAnnouncement(Context context, ModelInsertAnnouncement announcement) {
        return Observable.create(emitter -> {
            List<Uri> uris = announcement.getUrisBitmap();
            List<MultipartBody.Part> partList = new ArrayList<>();

            for (int i = 0; i < uris.size(); i++) {
                partList.add(RetrofitUtils.createFilePart(context, "picture_" + i, uris.get(i)));
            }

            ApiClient.getApi()
                    .create(ApiAnnouncement.class)
                    .insertAnnouncement(
                            UserCookie.getToken(context),
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
                    try {
                        String res = response.body().string();

                        if (res != null) {
                            JSONObject object = new JSONObject(res);

                            String message = object.getString("response");

                            if (ApiAnnouncement.AnnouncementCodes.valueOf(message) == ApiAnnouncement.AnnouncementCodes.SUCCESS_ANNOUNCEMENT_ADDED) {

                                emitter.onNext(ApiAnnouncement.AnnouncementCodes.SUCCESS_ANNOUNCEMENT_ADDED);

                                insertPhotos(object.getString("idAnnouncement"), partList, new OnAnnouncementListener() {
                                    @Override
                                    public void onComplete(@NonNull ApiAnnouncement.AnnouncementCodes code) {
                                        emitter.onNext(code);
                                    }

                                    @Override
                                    public void onFailure(@NonNull Throwable t) {
                                        emitter.onError(t);
                                    }
                                });
                            } else {
                                emitter.onNext(ApiAnnouncement.AnnouncementCodes.UNKNOW_ERROR);
                            }
                        } else {
                            emitter.onNext(ApiAnnouncement.AnnouncementCodes.UNKNOW_ERROR);
                        }
                    } catch (JSONException | IOException e) {
                        Timber.e(e);
                        emitter.onError(e);
                    } finally {
                        emitter.onComplete();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                    emitter.onError(t);
                }
            });
        });
    }

    private synchronized void insertPhotos(String idAnnouncement, List<MultipartBody.Part> partList, OnAnnouncementListener listener) {
        ApiClient.getApi()
                .create(ApiAnnouncement.class)
                .insertPictures(RetrofitUtils.createPartFromString(idAnnouncement), partList, partList.size())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        try {
                            String res = response.body().string();

                            if (res != null) {
                                JSONObject object = new JSONObject(res);

                                String message = object.getString("response");

                                if (ApiAnnouncement.AnnouncementCodes.valueOf(message) == ApiAnnouncement.AnnouncementCodes.SUCCESS_PICTURES_ADDED) {
                                    listener.onComplete(ApiAnnouncement.AnnouncementCodes.SUCCESS_PICTURES_ADDED);
                                } else {
                                    listener.onComplete(ApiAnnouncement.AnnouncementCodes.UNKNOW_ERROR);
                                }
                            } else {
                                listener.onComplete(ApiAnnouncement.AnnouncementCodes.UNKNOW_ERROR);
                            }
                        } catch (JSONException | IOException e) {
                            Timber.e(e);
                            listener.onComplete(ApiAnnouncement.AnnouncementCodes.UNKNOW_ERROR);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                        Timber.e(t);
                        listener.onComplete(ApiAnnouncement.AnnouncementCodes.UNKNOW_ERROR);
                    }
                });
    }
}