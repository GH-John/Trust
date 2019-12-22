package com.application.arenda.Entities.Announcements.InsertAnnouncement.InflateDropDownList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.application.arenda.Entities.Cookies.ServerUtils;
import com.application.arenda.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

public class LoadingCategories implements ILoadingCategories {

    private void messageOutput(final Context context, String str) {
        Toast.makeText(context, str, Toast.LENGTH_LONG).show();
    }

    @Override
    public Observable<List<ModelItemContent>> loadingCategories(final Context context) {
        return Observable.create(observableEmitter -> {
            List<ModelItemContent> models = new ArrayList<>();
            StringRequest request;
            request = new StringRequest(Request.Method.POST, ServerUtils.URL_LOAD_CATEGORY, response -> {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray categories = jsonObject.getJSONArray("categories");

                    String code = jsonObject.getString("code");

                    switch (code) {
                        case "0": {
                            Log.d("LoadingCategories", response);
                            break;
                        }
                        case "1":
                            JSONObject object;

                            for (int i = 0; i < categories.length(); i++) {
                                object = categories.getJSONObject(i);

                                models.add(new ModelItemContent(Integer.valueOf(object.getString("idCategory")),
                                        object.getString("name").trim()));

                            }
                            Collections.sort(models, (Comparator<ModelItemContent>) (o1, o2) -> o2.getName().length() - o1.getName().length());

                            observableEmitter.onNext(models);
                            break;
                        case "101":
                            messageOutput(context, context.getResources().getString(R.string.error_server_is_temporarily_unavailable));
                            Log.d("LoadingCategories", response);
                            break;
                    }
                } catch (JSONException e) {
                    observableEmitter.onError(e);
                    Log.d("LoadingCategories", response);
                } finally {
                    observableEmitter.onComplete();
                }
            },
                    volleyError -> {
                        if (volleyError instanceof TimeoutError) {
                            messageOutput(context, context.getResources()
                                    .getString(R.string.error_check_internet_connect));
                        } else {
                            Log.d("LoadingCategories", String.valueOf(volleyError.getMessage()));
                            messageOutput(context, context.getResources()
                                    .getString(R.string.error_fail_load_categories));
                        }

                        observableEmitter.onError(volleyError);
                    }) {
                @SuppressLint("SimpleDateFormat")
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    return new HashMap<>();
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(request);
        });
    }

    @Override
    public Observable<List<ModelItemContent>> loadingSubcategories(final Context context, final int idCategories) {
        return Observable.create(observableEmitter -> {
            List<ModelItemContent> models = new ArrayList<>();
            StringRequest request;

            request = new StringRequest(Request.Method.POST, ServerUtils.URL_LOAD_SUBCATEGORY, response -> {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray subCategories = jsonObject.getJSONArray("subcategories");

                    String code = jsonObject.getString("code");

                    switch (code) {
                        case "0": {
                            Log.d("LoadingCategories", response);
                            break;
                        }
                        case "1":
                            JSONObject object;

                            for (int i = 0; i < subCategories.length(); i++) {
                                object = subCategories.getJSONObject(i);

                                models.add(new ModelItemContent(Integer.valueOf(object.getString("idSubcategory")),
                                        object.getString("name").trim()));
                            }
                            Collections.sort(models, (Comparator<ModelItemContent>) (o1, o2) -> o2.getName().length() - o1.getName().length());

                            observableEmitter.onNext(models);
                            break;
                        case "101":
                            messageOutput(context, context.getResources().getString(R.string.error_server_is_temporarily_unavailable));
                            Log.d("LoadingCategories", response);
                            break;

                    }
                } catch (JSONException e) {
                    observableEmitter.onError(e);
                    Log.d("LoadingCategories", response);
                } finally {
                    observableEmitter.onComplete();
                }
            },
                    volleyError -> {
                        if (volleyError instanceof TimeoutError) {
                            messageOutput(context, context.getResources()
                                    .getString(R.string.error_check_internet_connect));
                        } else {
                            Log.d("LoadingCategories", String.valueOf(volleyError.getMessage()));
                            messageOutput(context, context.getResources()
                                    .getString(R.string.error_fail_load_categories));
                        }

                        observableEmitter.onError(volleyError);
                    }) {
                @SuppressLint("SimpleDateFormat")
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("idCategory", String.valueOf(idCategories));
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(request);
        });
    }
}