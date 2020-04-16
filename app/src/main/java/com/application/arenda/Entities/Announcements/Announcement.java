package com.application.arenda.Entities.Announcements;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.application.arenda.BuildConfig;
import com.application.arenda.Entities.Models.ModelAllAnnouncement;
import com.application.arenda.Entities.Models.ModelCategory;
import com.application.arenda.Entities.Models.ModelInsertAnnouncement;
import com.application.arenda.Entities.Models.ModelSubcategory;
import com.application.arenda.Entities.User.UserCookie;
import com.application.arenda.Entities.Utils.FileUtils;
import com.application.arenda.Entities.Utils.Retrofit.ApiClient;
import com.application.arenda.Entities.Utils.Retrofit.RetrofitUtils;
import com.application.arenda.Entities.Utils.Utils;

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
    public Observable<List<ModelSubcategory>> getSubcategories(long idCategory, OnAnnouncementListener listener) {
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

                                for (int i = 0; i < subcategories.length(); i++) {
                                    model = subcategories.getJSONObject(i);

                                    subcategoryList.add(new ModelSubcategory(Long.parseLong(model.getString("idSubcategory")),
                                            idCategory, model.getString("name").trim()));
                                }

                                Collections.sort(subcategoryList, (o1, o2) -> o2.getName().length() - o1.getName().length());

                                observableEmitter.onNext(subcategoryList);

                                if (listener != null)
                                    listener.onComplete(ApiAnnouncement.AnnouncementCodes.valueOf(message));
                            }
                        } else {
                            if (listener != null)
                                listener.onComplete(ApiAnnouncement.AnnouncementCodes.NETWORK_ERROR);
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

                                insertPhotos(object.getString("idAnnouncement"), FileUtils.getFileName(context, announcement.getMainUri()), partList, new OnAnnouncementListener() {
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

    public Observable<List<ModelAllAnnouncement>> loadAnnouncements(Context context, long lastID, int limitItemsInPage, String query, OnAnnouncementListener listener) {
        List<ModelAllAnnouncement> listAnnouncements = new ArrayList<>();

        return Observable.create(emitter ->
                ApiClient.getApi()
                        .create(ApiAnnouncement.class).loadAnnouncements(
                        UserCookie.getToken(context),
                        lastID,
                        limitItemsInPage,
                        query)
                        .enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                                try {
                                    String res = response.body().string();

                                    if (res != null) {
                                        JSONObject object = new JSONObject(res);

                                        String message = object.getString("response");

                                        if (ApiAnnouncement.AnnouncementCodes.valueOf(message) == ApiAnnouncement.AnnouncementCodes.SUCCESS_ANNOUNCEMENTS_LOADED) {

                                            JSONArray announcements = object.getJSONArray("announcements");

                                            JSONObject model;

                                            for (int i = 0; i < announcements.length(); i++) {
                                                model = announcements.getJSONObject(i);

                                                ModelAllAnnouncement announcement = new ModelAllAnnouncement();

                                                announcement.setID(Long.parseLong(model.getString("idAnnouncement")));
                                                announcement.setIdUser(Long.parseLong(model.getString("idUser")));

                                                announcement.setName(model.getString("name"));
                                                announcement.setMainUri(Uri.parse(model.getString("photoPath")));

                                                announcement.setLogin(model.getString("login"));
                                                announcement.setUserLogoUri(Uri.parse(model.getString("userLogo")));

                                                announcement.setCostToBYN(Float.parseFloat(model.getString("costToBYN")));
                                                announcement.setCostToUSD(Float.parseFloat(model.getString("costToUSD")));
                                                announcement.setCostToEUR(Float.parseFloat(model.getString("costToEUR")));

                                                announcement.setAddress(model.getString("address"));
                                                announcement.setPlacementDate(Utils.getFormatingDate(context, model.getString("placementDate")));
                                                announcement.setCountRent(Integer.parseInt(model.getString("countRent")));
                                                announcement.setRate(Float.parseFloat(model.getString("rating")));

                                                listAnnouncements.add(announcement);
                                            }

                                            emitter.onNext(listAnnouncements);

                                            if (listener != null)
                                                listener.onComplete(ApiAnnouncement.AnnouncementCodes.valueOf(message));

                                        } else {
                                            if (listener != null)
                                                listener.onComplete(ApiAnnouncement.AnnouncementCodes.UNSUCCESS_ANNOUNCEMENTS_LOADED);
                                        }
                                    }
                                } catch (JSONException | IOException e) {
                                    Timber.e(e);
                                    if (listener != null)
                                        listener.onFailure(e);
                                    emitter.onError(e);
                                } finally {
                                    emitter.onComplete();
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                                if (listener != null)
                                    listener.onFailure(t);
                                emitter.onError(t);
                            }
                        }));
    }

    private synchronized void insertPhotos(String idAnnouncement, String mainUri, List<MultipartBody.Part> partList, OnAnnouncementListener listener) {
        ApiClient.getApi()
                .create(ApiAnnouncement.class)
                .insertPictures(RetrofitUtils.createPartFromString(idAnnouncement), RetrofitUtils.createPartFromString(mainUri), partList, partList.size())
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