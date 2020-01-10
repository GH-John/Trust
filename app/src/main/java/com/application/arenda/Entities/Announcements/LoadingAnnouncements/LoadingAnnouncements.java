package com.application.arenda.Entities.Announcements.LoadingAnnouncements;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.application.arenda.Entities.Announcements.Models.ModelAllAnnouncement;
import com.application.arenda.Entities.Announcements.Models.ModelUserAnnouncement;
import com.application.arenda.Entities.Announcements.Models.ModelViewAnnouncement;
import com.application.arenda.Entities.Utils.UserCookie;
import com.application.arenda.Entities.Utils.Utils;
import com.application.arenda.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

public class LoadingAnnouncements implements ILoadingAnnouncements {

    @Override
    public Observable<List<ModelAllAnnouncement>> loadAllAnnouncements(final Context context, final String url) {
        return Observable.create(observableEmitter -> {
            List<ModelAllAnnouncement> models = new ArrayList<>();

            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @SuppressLint("SimpleDateFormat")
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        JSONArray announcements = jsonObject.getJSONArray("announcements");

                        String code = jsonObject.getString("code");

                        switch (code) {
                            case "1": {
                                JSONObject object;

                                for (int i = 0; i < announcements.length(); i++) {
                                    object = announcements.getJSONObject(i);

                                    ModelAllAnnouncement model = new ModelAllAnnouncement();

                                    model.setIdAnnouncement(Integer.parseInt(object.getString("idAnnouncement")));

                                    model.setIdUser(Integer.valueOf(object.getString("idUser")));

                                    model.setName(object.getString("name"));

                                    model.setCostToBYN(Float.parseFloat(object.getString("costToBYN")));
                                    model.setCostToUSD(Float.parseFloat(object.getString("costToUSD")));
                                    model.setCostToEUR(Float.parseFloat(object.getString("costToEUR")));

                                    model.setAddress(object.getString("address"));

                                    model.setPlacementDate(Utils.getFormatingDate(context, object.getString("placementDate")));
                                    model.setRate(Float.parseFloat(object.getString("rating")));
                                    model.setCountRent(Integer.parseInt(object.getString("countRent")));
                                    model.setMainUriBitmap(Uri.parse(object.getString("photoPath")));

                                    if(object.getString("isFavorite").equals("1"))
                                        model.setFavorite(true);
                                    else
                                        model.setFavorite(false);

                                    models.add(model);
                                }

                                observableEmitter.onNext(models);
                                break;
                            }

                            case "2": {
                                Log.d(getClass().toString(), response);
                                Utils.messageOutput(context, context.getResources()
                                        .getString(R.string.error_fail_loading_announcements) + response);

                                observableEmitter.onComplete();
                                break;
                            }

                            case "3": {
                                Log.d(getClass().toString(), response);

                                observableEmitter.onComplete();
                                break;
                            }

                            case "101": {
                                Log.d(getClass().toString(), response);
                                Utils.messageOutput(context, context.getResources().getString(R.string.error_server_is_temporarily_unavailable));

                                observableEmitter.onComplete();
                                break;
                            }
                            default: {
                                Log.d(getClass().toString(), response);
                                Utils.messageOutput(context, context.getResources().getString(R.string.error_server_is_temporarily_unavailable));

                                observableEmitter.onComplete();
                            }
                        }
                    } catch (JSONException e) {
                        Log.d(getClass().toString(), response);
                        Utils.messageOutput(context, context.getResources()
                                .getString(R.string.error_fail_loading_announcements) + e.getMessage());

                        observableEmitter.onError(e);
                    } finally {
                        observableEmitter.onComplete();
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            if (volleyError instanceof TimeoutError) {
                                Utils.messageOutput(context, context.getResources()
                                        .getString(R.string.error_check_internet_connect));
                            } else {
                                Log.d(getClass().toString(), String.valueOf(volleyError.getMessage()));
                                Utils.messageOutput(context, context.getResources()
                                        .getString(R.string.error_fail_loading_announcements));
                            }

                            observableEmitter.onError(volleyError);
                        }
                    }) {
                @SuppressLint("SimpleDateFormat")
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("token", UserCookie.getToken(context));
                    params.put("idAnnouncement", String.valueOf(0));

                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(request);
        });
    }

    @Override
    public Observable<List<ModelAllAnnouncement>> searchToAllAnnouncements(Context context, String url, int lastID, String search) {
        return Observable.create(observableEmitter -> {
            StringRequest request;
            List<ModelAllAnnouncement> models = new ArrayList<>();

            request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @SuppressLint("SimpleDateFormat")
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        JSONArray announcements = jsonObject.getJSONArray("announcements");

                        String code = jsonObject.getString("code");

                        switch (code) {
                            case "1": {
                                JSONObject object;

                                for (int i = 0; i < announcements.length(); i++) {
                                    object = announcements.getJSONObject(i);

                                    ModelAllAnnouncement model = new ModelAllAnnouncement();

                                    model.setIdAnnouncement(Integer.parseInt(object.getString("idAnnouncement")));

                                    model.setIdUser(Integer.valueOf(object.getString("idUser")));

                                    model.setName(object.getString("name"));

                                    model.setCostToBYN(Float.parseFloat(object.getString("costToBYN")));
                                    model.setCostToUSD(Float.parseFloat(object.getString("costToUSD")));
                                    model.setCostToEUR(Float.parseFloat(object.getString("costToEUR")));

                                    model.setAddress(object.getString("address"));

                                    model.setPlacementDate(Utils.getFormatingDate(context, object.getString("placementDate")));
                                    model.setRate(Float.parseFloat(object.getString("rating")));
                                    model.setCountRent(Integer.parseInt(object.getString("countRent")));
                                    model.setMainUriBitmap(Uri.parse(object.getString("photoPath")));

                                    if(object.getString("isFavorite").equals("1"))
                                        model.setFavorite(true);
                                    else
                                        model.setFavorite(false);

                                    models.add(model);
                                }

                                observableEmitter.onNext(models);
                                break;
                            }

                            case "2": {
                                Log.d(getClass().toString(), response);
                                Utils.messageOutput(context, context.getResources()
                                        .getString(R.string.error_fail_loading_announcements) + response);

                                break;
                            }

                            case "3": {
                                Log.d(getClass().toString(), response);

                                break;
                            }

                            case "101": {
                                Log.d(getClass().toString(), response);
                                Utils.messageOutput(context, context.getResources().getString(R.string.error_server_is_temporarily_unavailable));

                                break;
                            }
                            default: {
                                Log.d(getClass().toString(), response);
                                Utils.messageOutput(context, context.getResources().getString(R.string.error_server_is_temporarily_unavailable));
                            }
                        }
                    } catch (JSONException e) {
                        Log.d(getClass().toString(), response);
                        Utils.messageOutput(context, context.getResources()
                                .getString(R.string.error_fail_loading_announcements) + e.getMessage());

                        observableEmitter.onError(e);
                    } finally {
                        observableEmitter.onComplete();
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            if (volleyError instanceof TimeoutError) {
                                Utils.messageOutput(context, context.getResources()
                                        .getString(R.string.error_check_internet_connect));
                            } else {
                                Log.d(getClass().toString(), String.valueOf(volleyError.getMessage()));
                                Utils.messageOutput(context, context.getResources()
                                        .getString(R.string.error_fail_loading_announcements));
                            }
                        }
                    }) {
                @SuppressLint("SimpleDateFormat")
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("token", UserCookie.getToken(context));
                    params.put("idAnnouncement", String.valueOf(lastID));

                    if (search.length() > 0 && search != null)
                        params.put("search", search);

                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(request);
        });
    }

    @Override
    public Observable<List<ModelUserAnnouncement>> loadUserAnnouncements(final Context context, final String url) {
        return Observable.create(observableEmitter -> {
            List<ModelUserAnnouncement> models = new ArrayList<>();

            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @SuppressLint("SimpleDateFormat")
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        String code = jsonObject.getString("code");

                        JSONArray announcements = jsonObject.getJSONArray("announcements");

                        switch (code) {
                            case "1": {
                                JSONObject object;

                                for (int i = 0; i < announcements.length(); i++) {
                                    object = announcements.getJSONObject(i);

                                    ModelUserAnnouncement model = new ModelUserAnnouncement();

                                    model.setIdAnnouncement(Integer.parseInt(object.getString("idAnnouncement")));

                                    model.setIdUser(Integer.valueOf(object.getString("idUser")));

                                    model.setName(object.getString("name"));

                                    model.setCountRent(Integer.parseInt(object.getString("countRent")));
                                    model.setCountViewers(Integer.parseInt(object.getString("countViewers")));
                                    model.setCountFavorites(Integer.parseInt(object.getString("countFavorites")));

                                    model.setCostToBYN(Float.parseFloat(object.getString("costToBYN")));
                                    model.setCostToUSD(Float.parseFloat(object.getString("costToUSD")));
                                    model.setCostToEUR(Float.parseFloat(object.getString("costToEUR")));

                                    model.setAddress(object.getString("address"));

                                    model.setPlacementDate(Utils.getFormatingDate(context, object.getString("placementDate")));
                                    model.setRate(Float.parseFloat(object.getString("rating")));
                                    model.setMainUriBitmap(Uri.parse(object.getString("photoPath")));

                                    models.add(model);
                                }

                                observableEmitter.onNext(models);
                                break;
                            }

                            case "2": {
                                Log.d(getClass().toString(), response);
                                Utils.messageOutput(context, context.getResources()
                                        .getString(R.string.error_fail_loading_announcements) + response);

                                break;
                            }

                            case "3": {
                                Log.d(getClass().toString(), response);

                                break;
                            }

                            case "101": {
                                Log.d(getClass().toString(), response);
                                Utils.messageOutput(context, context.getResources().getString(R.string.error_server_is_temporarily_unavailable));

                                break;
                            }
                            default: {
                                Log.d(getClass().toString(), response);
                                Utils.messageOutput(context, context.getResources().getString(R.string.error_server_is_temporarily_unavailable));
                            }
                        }
                    } catch (JSONException e) {
                        Log.d(getClass().toString(), response);
                        Utils.messageOutput(context, context.getResources()
                                .getString(R.string.error_fail_loading_announcements) + response);

                        observableEmitter.onError(e);
                    } finally {
                        observableEmitter.onComplete();
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            if (volleyError instanceof TimeoutError) {
                                Utils.messageOutput(context, context.getResources()
                                        .getString(R.string.error_check_internet_connect));
                            } else {
                                Log.d(getClass().toString(), String.valueOf(volleyError.getMessage()));
                                Utils.messageOutput(context, context.getResources()
                                        .getString(R.string.error_fail_loading_announcements) + volleyError.getMessage());
                            }
                        }
                    }) {
                @SuppressLint("SimpleDateFormat")
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("token", UserCookie.getToken(context));
                    params.put("idAnnouncement", "0");

                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(request);
        });
    }

    @Override
    public Observable<List<ModelUserAnnouncement>> searchToUserAnnouncements(Context context, String url, int lastID, String search) {
        return Observable.create(observableEmitter -> {
            StringRequest request;
            List<ModelUserAnnouncement> models = new ArrayList<>();

            request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @SuppressLint("SimpleDateFormat")
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        JSONArray announcements = jsonObject.getJSONArray("announcements");

                        String code = jsonObject.getString("code");

                        switch (code) {
                            case "1": {
                                JSONObject object;

                                for (int i = 0; i < announcements.length(); i++) {
                                    object = announcements.getJSONObject(i);

                                    ModelUserAnnouncement model = new ModelUserAnnouncement();

                                    model.setIdAnnouncement(Integer.parseInt(object.getString("idAnnouncement")));

                                    model.setIdUser(Integer.valueOf(object.getString("idUser")));

                                    model.setName(object.getString("name"));

                                    model.setCountRent(Integer.parseInt(object.getString("countRent")));
                                    model.setCountViewers(Integer.parseInt(object.getString("countViewers")));
                                    model.setCountFavorites(Integer.parseInt(object.getString("countFavorites")));

                                    model.setCostToBYN(Float.parseFloat(object.getString("costToBYN")));
                                    model.setCostToUSD(Float.parseFloat(object.getString("costToUSD")));
                                    model.setCostToEUR(Float.parseFloat(object.getString("costToEUR")));

                                    model.setAddress(object.getString("address"));

                                    model.setPlacementDate(Utils.getFormatingDate(context, object.getString("placementDate")));
                                    model.setRate(Float.parseFloat(object.getString("rating")));
                                    model.setMainUriBitmap(Uri.parse(object.getString("photoPath")));

                                    models.add(model);
                                }

                                observableEmitter.onNext(models);
                                break;
                            }

                            case "2": {
                                Log.d(getClass().toString(), response);
                                Utils.messageOutput(context, context.getResources()
                                        .getString(R.string.error_fail_loading_announcements) + response);

                                break;
                            }

                            case "3": {
                                Log.d(getClass().toString(), response);

                                break;
                            }

                            case "101": {
                                Log.d(getClass().toString(), response);
                                Utils.messageOutput(context, context.getResources().getString(R.string.error_server_is_temporarily_unavailable));

                                break;
                            }
                            default: {
                                Log.d(getClass().toString(), response);
                                Utils.messageOutput(context, context.getResources().getString(R.string.error_server_is_temporarily_unavailable));
                            }
                        }
                    } catch (JSONException e) {
                        Log.d(getClass().toString(), response);
                        Utils.messageOutput(context, context.getResources()
                                .getString(R.string.error_fail_loading_announcements) + e.getMessage());

                        observableEmitter.onError(e);
                    } finally {
                        observableEmitter.onComplete();
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            if (volleyError instanceof TimeoutError) {
                                Utils.messageOutput(context, context.getResources()
                                        .getString(R.string.error_check_internet_connect));
                            } else {
                                Log.d(getClass().toString(), String.valueOf(volleyError.getMessage()));
                                Utils.messageOutput(context, context.getResources()
                                        .getString(R.string.error_fail_loading_announcements));
                            }
                        }
                    }) {
                @SuppressLint("SimpleDateFormat")
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("token", UserCookie.getToken(context));
                    params.put("idAnnouncement", String.valueOf(lastID));

                    if (search.length() > 0 && search != null)
                        params.put("search", search);

                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(request);
        });
    }

    @Override
    public Observable<List<ModelViewAnnouncement>> loadViewAnnouncement(Context context, String url) {
        return Observable.create(observableEmitter -> {

        });
    }
}