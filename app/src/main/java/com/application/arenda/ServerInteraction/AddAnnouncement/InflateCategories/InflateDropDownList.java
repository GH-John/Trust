package com.application.arenda.ServerInteraction.AddAnnouncement.InflateCategories;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.application.arenda.Patterns.Observer;
import com.application.arenda.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressLint("StaticFieldLeak")
public class InflateDropDownList {
    private static String URL_LOAD_CATEGORY = "http://192.168.43.241/AndroidConnectWithServer/php/load/LoadCategory.php";
    private static String URL_LOAD_SUBCATEGORY = "http://192.168.43.241/AndroidConnectWithServer/php/load/LoadSubcategory.php";

    public static Collection getCollectionCategories(final Context context, final View progressBar, final Observer observer) {
        progressBar.setVisibility(View.VISIBLE);
        final Collection collection = new ArrayList<>();
        StringRequest request;
        request = new StringRequest(Request.Method.POST, URL_LOAD_CATEGORY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray categories = jsonObject.getJSONArray("categories");

                    String code = jsonObject.getString("code");

                    switch (code) {
                        case "0": {
                            progressBar.setVisibility(View.GONE);
                            Log.d("InflateDropDownList", response);
                            break;
                        }
                        case "1":
                            JSONObject object;

                            for (int i = 0; i < categories.length(); i++) {
                                object = categories.getJSONObject(i);

                                collection.add(new ItemContent(Integer.valueOf(object.getString("idCategory")),
                                        object.getString("name").trim()));

                            }
                            Collections.sort((List) collection, new Comparator<ItemContent>() {
                                @Override
                                public int compare(ItemContent o1, ItemContent o2) {
                                    return o2.getName().length() - o1.getName().length();
                                }
                            });
                            progressBar.setVisibility(View.GONE);
                            observer.update(collection);
                            break;
                        case "101":
                            progressBar.setVisibility(View.GONE);
                            Log.d("InflateDropDownList", response);
                            break;

                        default:
                            progressBar.setVisibility(View.GONE);
                            observer.update(collection);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("InflateDropDownList", response);
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progressBar.setVisibility(View.GONE);
                        if (volleyError instanceof TimeoutError) {
                            messageOutput(context, context.getResources()
                                    .getString(R.string.error_check_internet_connect));
                        } else {
                            Log.d("InflateDropDownList", String.valueOf(volleyError.networkResponse.statusCode));
                        }
                    }
                }) {
            @SuppressLint("SimpleDateFormat")
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return new HashMap<>();
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);

        return collection;
    }

    public static Collection getCollectionSubcategories(final Context context, final int idCategories, final View progressBar, final Observer observer) {
        progressBar.setVisibility(View.VISIBLE);
        final Collection collection = new ArrayList<>();
        StringRequest request;

        request = new StringRequest(Request.Method.POST, URL_LOAD_SUBCATEGORY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray subCategories = jsonObject.getJSONArray("subCategories");

                    String code = jsonObject.getString("code");

                    switch (code) {
                        case "0": {
                            progressBar.setVisibility(View.GONE);
                            Log.d("InflateDropDownList", response);
                            break;
                        }
                        case "1":
                            JSONObject object;

                            for (int i = 0; i < subCategories.length(); i++) {
                                object = subCategories.getJSONObject(i);

                                collection.add(new ItemContent(Integer.valueOf(object.getString("subCategories")),
                                        object.getString("name").trim()));
                            }
                            Collections.sort((List) collection, new Comparator<ItemContent>() {
                                @Override
                                public int compare(ItemContent o1, ItemContent o2) {
                                    return o2.getName().length() - o1.getName().length();
                                }
                            });
                            progressBar.setVisibility(View.GONE);
                            observer.update(collection);
                            break;
                        case "101":
                            progressBar.setVisibility(View.GONE);
                            Log.d("InflateDropDownList", response);
                            break;

                        default:
                            progressBar.setVisibility(View.GONE);
                            observer.update(collection);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("InflateDropDownList", response);
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progressBar.setVisibility(View.GONE);
                        if (volleyError instanceof TimeoutError) {
                            messageOutput(context, context.getResources()
                                    .getString(R.string.error_check_internet_connect));
                        } else {
                            Log.d("InflateDropDownList", String.valueOf(volleyError.networkResponse.statusCode));
                        }
                    }
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

        return collection;
    }

    private static void messageOutput(final Context context, String str) {
        Toast.makeText(context, str, Toast.LENGTH_LONG).show();
    }
}