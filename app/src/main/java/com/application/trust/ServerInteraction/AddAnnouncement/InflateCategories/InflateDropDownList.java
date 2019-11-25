package com.application.trust.ServerInteraction.AddAnnouncement.InflateCategories;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.application.trust.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressLint("StaticFieldLeak")
public class InflateDropDownList {
    private Context context;
    private StringRequest request;
    private static String URL_LOAD_CATEGORY = "http://192.168.43.241/AndroidConnectWithServer/php/load/LoadCategory.php";
    private static String URL_LOAD_SUBCATEGORY = "http://192.168.43.241/AndroidConnectWithServer/php/load/LoadSubCategory.php";

    public InflateDropDownList(Context context){
        this.context = context;
    }

    public List<ItemContent> getListItemContent(){
        List<ItemContent> itemContents = new ArrayList<>();

        request = new StringRequest(Request.Method.POST, URL_LOAD_CATEGORY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    messageOutput(response);
                    JSONObject jsonObject = new JSONObject(response);

                    String code = jsonObject.getString("code");

                    switch (code) {
                        case "0": {
                            messageOutput("не удалось загрузить категории");
                            break;
                        }
                        case "1":
                            messageOutput("категории загружены");
                            break;
                        case "101":
                            messageOutput(context.getResources().getString(R.string.error_check_internet_connect));
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                    messageOutput(context.getResources()
                            .getString(R.string.error_adding_announcement) + response);
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if (volleyError instanceof TimeoutError) {
                            messageOutput(context.getResources()
                                    .getString(R.string.error_check_internet_connect));
                        } else {
                            messageOutput(context.getResources()
                                    .getString(R.string.error_registration) + " " + volleyError.networkResponse.statusCode);
                        }
                    }
                }) {
            @SuppressLint("SimpleDateFormat")
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);

        return itemContents;
    }

    private void messageOutput(String str) {
        Toast.makeText(context, str, Toast.LENGTH_LONG).show();
    }
}