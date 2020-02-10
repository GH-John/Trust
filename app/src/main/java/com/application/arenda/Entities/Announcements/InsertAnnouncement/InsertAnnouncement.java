package com.application.arenda.Entities.Announcements.InsertAnnouncement;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.application.arenda.Entities.Announcements.Models.ModelInsertAnnouncement;
import com.application.arenda.Entities.Utils.Network.ServerUtils;
import com.application.arenda.Entities.User.UserCookie;
import com.application.arenda.Entities.Utils.Utils;
import com.application.arenda.R;
import com.application.arenda.UI.Thumbnail.ThumbnailCompression;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class InsertAnnouncement implements IInsertAnnouncement {
    @Override
    public Observable insertAnnouncement(@NonNull Context context, @NonNull String url, ModelInsertAnnouncement announcement) {
        return Observable.create(observableEmitter -> {
            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        String code = jsonObject.getString("code");

                        switch (code) {
                            case "0": {
                                observableEmitter.onComplete();

                                Utils.messageOutput(context, context.getString(R.string.error_user_not_identified));
                                break;
                            }

                            case "1": {
                                int id = Integer.valueOf(jsonObject.getString("idAnnouncement"));


                                for (Map.Entry<Uri, Bitmap> map : announcement.getMapBitmap().entrySet()) {
                                    insertPhoto(context, id, map.getValue())
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(new Observer() {
                                                @Override
                                                public void onSubscribe(Disposable d) {

                                                }

                                                @Override
                                                public void onNext(Object o) {
                                                    observableEmitter.onNext(o);
                                                }

                                                @Override
                                                public void onError(Throwable e) {
                                                    observableEmitter.onError(e);
                                                }

                                                @Override
                                                public void onComplete() {
                                                    observableEmitter.onComplete();
                                                }
                                            });
                                }
                                break;
                            }

                            case "2": {
                                Log.d(getClass().toString(), response);

                                observableEmitter.onComplete();

                                Utils.messageOutput(context, context.getResources()
                                        .getString(R.string.error_adding_announcement) + response);
                                break;
                            }

                            case "101":
                                Utils.messageOutput(context, context.getResources()
                                        .getString(R.string.error_server_is_temporarily_unavailable));

                                observableEmitter.onComplete();

                                Log.d(getClass().toString(), response);
                                break;
                        }
                    } catch (JSONException e) {
                        Log.d(getClass().toString(), response);
                        observableEmitter.onError(e);
                        Utils.messageOutput(context, context.getResources()
                                .getString(R.string.error_adding_announcement) + response);
                    } finally {
                        observableEmitter.onComplete();
                    }
                }
            },
                    volleyError -> {
                        if (volleyError instanceof TimeoutError) {
                            Utils.messageOutput(context, context.getResources()
                                    .getString(R.string.error_check_internet_connect));
                        } else {
                            Utils.messageOutput(context, context.getResources()
                                    .getString(R.string.error_adding_announcement) + volleyError.getMessage());
                        }

                        observableEmitter.onError(volleyError);
                    }) {
                @SuppressLint("SimpleDateFormat")
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("token", UserCookie.getToken(context));
                    params.put("idSubcategory", String.valueOf(announcement.getIdSubcategory()));
                    params.put("name", announcement.getName());
                    params.put("description", announcement.getDescription());
                    params.put("costToBYN", String.valueOf(announcement.getCostToBYN()));
                    params.put("costToUSD", String.valueOf(announcement.getCostToUSD()));
                    params.put("costToEUR", String.valueOf(announcement.getCostToEUR()));

                    params.put("address", announcement.getAddress());

                    params.put("phone_1", announcement.getPhone_1());
                    params.put("isVisible_phone_1", String.valueOf(announcement.isVisiblePhone_1()));

                    params.put("phone_2", announcement.getPhone_2());
                    params.put("isVisible_phone_2", String.valueOf(announcement.isVisiblePhone_2()));

                    params.put("phone_3", announcement.getPhone_3());
                    params.put("isVisible_phone_3", String.valueOf(announcement.isVisiblePhone_3()));

                    params.put("encodedString", ThumbnailCompression.getEncodeBase64(announcement.getBitmap()));

//                params.put("placementDate", new SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.getDefault())
//                .format(Calendar.getInstance().getTime()));
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(request);
        });
    }

    @Override
    public Observable insertPhoto(@NonNull Context context, int idAnnouncement, @NonNull Bitmap bitmap) {
        return Observable.create(observableEmitter -> {
            StringRequest request = new StringRequest(Request.Method.POST, ServerUtils.URL_INSERT_PHOTO,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);

                                String code = jsonObject.getString("code");

                                switch (code) {
                                    case "0": {
                                        Utils.messageOutput(context, context.getString(R.string.error_send_photo_to_server)
                                                + response);

                                        observableEmitter.onComplete();
                                        break;
                                    }

                                    case "1": {
                                        Log.d(getClass().toString(), context.getString(R.string.success_photos_loaded));

                                        observableEmitter.onComplete();
                                        break;
                                    }

                                    case "101":
                                        Utils.messageOutput(context, context.getResources()
                                                .getString(R.string.error_server_is_temporarily_unavailable));

                                        observableEmitter.onComplete();
                                        break;
                                }
                            } catch (JSONException e) {
                                Log.d(getClass().toString(), e.getMessage());

                                observableEmitter.onError(e);

                                Utils.messageOutput(context, context.getResources()
                                        .getString(R.string.error_adding_photos) + e.getMessage());
                            }
                        }
                    },
                    volleyError -> {
                        if (volleyError instanceof TimeoutError) {
                            Utils.messageOutput(context, context.getResources()
                                    .getString(R.string.error_check_internet_connect));
                        } else {
                            Utils.messageOutput(context, context.getString(R.string.error_send_photo_to_server)
                                    + volleyError.getMessage());
                        }

                        observableEmitter.onError(volleyError);
                    }) {
                @SuppressLint("SimpleDateFormat")
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("idAnnouncement", String.valueOf(idAnnouncement));
                    params.put("encodedString", ThumbnailCompression.getEncodeBase64(bitmap));
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(request);
        });
    }
}