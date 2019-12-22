package com.application.arenda.Entities.Announcements.LoadingAnnouncements;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.application.arenda.Entities.Announcements.Models.ViewModelAllAnnouncement;
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
    public Observable<List<ViewModelAllAnnouncement>> loadAllAnnouncemets(final Context context, final String url) {
        return Observable.create(observableEmitter -> {
            StringRequest request;
            List<ViewModelAllAnnouncement> models = new ArrayList<>();

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

                                    ViewModelAllAnnouncement model = new ViewModelAllAnnouncement();

                                    model.setIdAnnouncement(Integer.parseInt(object.getString("idAnnouncement")));

                                    model.setIdUser(Integer.valueOf(object.getString("idUser")));

                                    model.setName(object.getString("name"));

                                    model.setCostToBYN(Float.parseFloat(object.getString("costToBYN")));
                                    model.setCostToUSD(Float.parseFloat(object.getString("costToUSD")));
                                    model.setCostToEUR(Float.parseFloat(object.getString("costToEUR")));

                                    model.setLocation(object.getString("address"));

                                    model.setPlacementDate(object.getString("placementDate"));
                                    model.setRating(Integer.parseInt(object.getString("rating")));
                                    model.setCountRent(Integer.parseInt(object.getString("countRent")));
                                    model.setMainUriBitmap(Uri.parse(object.getString("photoPath")));

                                    models.add(model);
                                }

                                observableEmitter.onNext(models);
                                break;
                            }

                            case "2": {
                                Log.d(getClass().toString(), response);
                                messageOutput(context, context.getResources()
                                        .getString(R.string.error_fail_loading_announcements) + response);

                                break;
                            }

                            case "3": {
                                Log.d(getClass().toString(), response);

                                break;
                            }

                            case "101": {
                                Log.d(getClass().toString(), response);
                                messageOutput(context, context.getResources().getString(R.string.error_server_is_temporarily_unavailable));

                                break;
                            }
                            default: {
                                Log.d(getClass().toString(), response);
                                messageOutput(context, context.getResources().getString(R.string.error_server_is_temporarily_unavailable));
                            }
                        }
                    } catch (JSONException e) {
                        Log.d(getClass().toString(), response);
                        messageOutput(context, context.getResources()
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
                                messageOutput(context, context.getResources()
                                        .getString(R.string.error_check_internet_connect));
                            } else {
                                Log.d(getClass().toString(), String.valueOf(volleyError.getMessage()));
                                messageOutput(context, context.getResources()
                                        .getString(R.string.error_fail_loading_announcements));
                            }
                        }
                    }) {
                @SuppressLint("SimpleDateFormat")
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("idAnnouncement", String.valueOf(0));

                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(request);
        });
    }

    @Override
    public Observable<List<ViewModelAllAnnouncement>> searchToAllAnnouncemets(Context context, String url, int lastID, String search) {
        return Observable.create(observableEmitter -> {
            StringRequest request;
            List<ViewModelAllAnnouncement> models = new ArrayList<>();

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

                                    ViewModelAllAnnouncement model = new ViewModelAllAnnouncement();

                                    model.setIdAnnouncement(Integer.parseInt(object.getString("idAnnouncement")));

                                    model.setIdUser(Integer.valueOf(object.getString("idUser")));

                                    model.setName(object.getString("name"));

                                    model.setCostToBYN(Float.parseFloat(object.getString("costToBYN")));
                                    model.setCostToUSD(Float.parseFloat(object.getString("costToUSD")));
                                    model.setCostToEUR(Float.parseFloat(object.getString("costToEUR")));

                                    model.setLocation(object.getString("address"));

                                    model.setPlacementDate(object.getString("placementDate"));
                                    model.setRating(Integer.parseInt(object.getString("rating")));
                                    model.setCountRent(Integer.parseInt(object.getString("countRent")));
                                    model.setMainUriBitmap(Uri.parse(object.getString("photoPath")));

                                    models.add(model);
                                }

                                observableEmitter.onNext(models);
                                break;
                            }

                            case "2": {
                                Log.d(getClass().toString(), response);
                                messageOutput(context, context.getResources()
                                        .getString(R.string.error_fail_loading_announcements) + response);

                                break;
                            }

                            case "3": {
                                Log.d(getClass().toString(), response);

                                break;
                            }

                            case "101": {
                                Log.d(getClass().toString(), response);
                                messageOutput(context, context.getResources().getString(R.string.error_server_is_temporarily_unavailable));

                                break;
                            }
                            default: {
                                Log.d(getClass().toString(), response);
                                messageOutput(context, context.getResources().getString(R.string.error_server_is_temporarily_unavailable));
                            }
                        }
                    } catch (JSONException e) {
                        Log.d(getClass().toString(), response);
                        messageOutput(context, context.getResources()
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
                                messageOutput(context, context.getResources()
                                        .getString(R.string.error_check_internet_connect));
                            } else {
                                Log.d(getClass().toString(), String.valueOf(volleyError.getMessage()));
                                messageOutput(context, context.getResources()
                                        .getString(R.string.error_fail_loading_announcements));
                            }
                        }
                    }) {
                @SuppressLint("SimpleDateFormat")
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<>();
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
    public Observable<List<ViewModelAllAnnouncement>> loadUserAnnouncemets(final Context context, final String url) {
        return Observable.create(observableEmitter -> {
            StringRequest request;
            List<ViewModelAllAnnouncement> models = new ArrayList<>();

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

                                    ViewModelAllAnnouncement model = new ViewModelAllAnnouncement();

                                    model.setIdAnnouncement(Integer.parseInt(object.getString("idAnnouncement")));

                                    model.setIdUser(Integer.valueOf(object.getString("idUser")));

                                    model.setName(object.getString("name"));

                                    model.setCostToBYN(Float.parseFloat(object.getString("costToBYN")));
                                    model.setCostToUSD(Float.parseFloat(object.getString("costToUSD")));
                                    model.setCostToEUR(Float.parseFloat(object.getString("costToEUR")));

                                    model.setLocation(object.getString("address"));

                                    model.setPlacementDate(object.getString("placementDate"));
                                    model.setRating(Integer.parseInt(object.getString("rating")));
                                    model.setCountRent(Integer.parseInt(object.getString("countRent")));
                                    model.setMainUriBitmap(Uri.parse(object.getString("photoPath")));

                                    models.add(model);
                                }

                                observableEmitter.onNext(models);
                                break;
                            }

                            case "2": {
                                Log.d(getClass().toString(), response);
                                messageOutput(context, context.getResources()
                                        .getString(R.string.error_fail_loading_announcements) + response);

                                break;
                            }

                            case "3": {
                                Log.d(getClass().toString(), response);

                                break;
                            }

                            case "101": {
                                Log.d(getClass().toString(), response);
                                messageOutput(context, context.getResources().getString(R.string.error_server_is_temporarily_unavailable));

                                break;
                            }
                            default: {
                                Log.d(getClass().toString(), response);
                                messageOutput(context, context.getResources().getString(R.string.error_server_is_temporarily_unavailable));
                            }
                        }
                    } catch (JSONException e) {
                        Log.d(getClass().toString(), response);
                        messageOutput(context, context.getResources()
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
                                messageOutput(context, context.getResources()
                                        .getString(R.string.error_check_internet_connect));
                            } else {
                                Log.d(getClass().toString(), String.valueOf(volleyError.getMessage()));
                                messageOutput(context, context.getResources()
                                        .getString(R.string.error_fail_loading_announcements));
                            }
                        }
                    }) {
                @SuppressLint("SimpleDateFormat")
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("idAnnouncement", String.valueOf(0));

                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(request);
        });
    }

    @Override
    public Observable<List<ViewModelAllAnnouncement>> searchToUserAnnouncemets(Context context, String url, int lastID, String search) {
        return Observable.create(observableEmitter -> {
            StringRequest request;
            List<ViewModelAllAnnouncement> models = new ArrayList<>();

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

                                    ViewModelAllAnnouncement model = new ViewModelAllAnnouncement();

                                    model.setIdAnnouncement(Integer.parseInt(object.getString("idAnnouncement")));

                                    model.setIdUser(Integer.valueOf(object.getString("idUser")));

                                    model.setName(object.getString("name"));

                                    model.setCostToBYN(Float.parseFloat(object.getString("costToBYN")));
                                    model.setCostToUSD(Float.parseFloat(object.getString("costToUSD")));
                                    model.setCostToEUR(Float.parseFloat(object.getString("costToEUR")));

                                    model.setLocation(object.getString("address"));

                                    model.setPlacementDate(object.getString("placementDate"));
                                    model.setRating(Integer.parseInt(object.getString("rating")));
                                    model.setCountRent(Integer.parseInt(object.getString("countRent")));
                                    model.setMainUriBitmap(Uri.parse(object.getString("photoPath")));

                                    models.add(model);
                                }

                                observableEmitter.onNext(models);
                                break;
                            }

                            case "2": {
                                Log.d(getClass().toString(), response);
                                messageOutput(context, context.getResources()
                                        .getString(R.string.error_fail_loading_announcements) + response);

                                break;
                            }

                            case "3": {
                                Log.d(getClass().toString(), response);

                                break;
                            }

                            case "101": {
                                Log.d(getClass().toString(), response);
                                messageOutput(context, context.getResources().getString(R.string.error_server_is_temporarily_unavailable));

                                break;
                            }
                            default: {
                                Log.d(getClass().toString(), response);
                                messageOutput(context, context.getResources().getString(R.string.error_server_is_temporarily_unavailable));
                            }
                        }
                    } catch (JSONException e) {
                        Log.d(getClass().toString(), response);
                        messageOutput(context, context.getResources()
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
                                messageOutput(context, context.getResources()
                                        .getString(R.string.error_check_internet_connect));
                            } else {
                                Log.d(getClass().toString(), String.valueOf(volleyError.getMessage()));
                                messageOutput(context, context.getResources()
                                        .getString(R.string.error_fail_loading_announcements));
                            }
                        }
                    }) {
                @SuppressLint("SimpleDateFormat")
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<>();
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



    private void messageOutput(final Context context, String str) {
        Toast.makeText(context, str, Toast.LENGTH_LONG).show();
    }
}