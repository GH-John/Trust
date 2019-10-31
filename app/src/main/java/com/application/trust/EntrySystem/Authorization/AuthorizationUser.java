package com.application.trust.EntrySystem.Authorization;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.application.trust.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AuthorizationUser {
    private final Context context;
    private final String URL_AUTHORIZATION;

    public AuthorizationUser(Context context, String URL_AUTHORIZATION) {
        this.context = context;
        this.URL_AUTHORIZATION = URL_AUTHORIZATION;
    }

    @SuppressLint("ShowToast")
    public boolean authorization(final String email, final String password) {
        final boolean[] statusAuth = new boolean[1];

        StringRequest stringRequest = new StringRequest(StringRequest.Method.POST, URL_AUTHORIZATION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("login");

                    if (success.equals("1")) {
                        statusAuth[0] = true;

                        JSONObject object;
                        for (int i = 0; i < jsonArray.length(); i++) {
                            object = jsonArray.getJSONObject(i);

                            String name = object.getString("name").trim();

                            Toast.makeText(context, context.getResources()
                                    .getString(R.string.success_authorization) + " " + name, Toast.LENGTH_LONG).show();
                        }
                    }

                } catch (JSONException e) {
                    statusAuth[0] = false;
                    Toast.makeText(context, context.getResources()
                                    .getString(R.string.unsuccess_authorization) + " "
                                    + e.toString() + " "
                                    + context.getResources().getString(R.string.check_internet_connect),
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        statusAuth[0] = false;
                        Toast.makeText(context, context.getResources()
                                        .getString(R.string.unsuccess_authorization) + " "
                                        + volleyError.toString() + " "
                                        + context.getResources().getString(R.string.check_internet_connect),
                                Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

        return statusAuth[0];
    }
}