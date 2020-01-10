package com.application.arenda.Entities.Authorization;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.application.arenda.Entities.Utils.ServerUtils;
import com.application.arenda.R;
import com.application.arenda.UI.Components.ComponentManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AuthorizationUseToken {
    private final Context context;
    private StringRequest request;

    public AuthorizationUseToken(Context context) {
        this.context = context;
    }

    @SuppressLint("ShowToast")
    public void authorization(final String token, final ComponentManager.Observer observer) {
        request = new StringRequest(StringRequest.Method.POST, ServerUtils.URL_AUTHORIZATION_USE_TOKEN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String code = jsonObject.getString("code");

                    switch (code) {
                        case "1": {
                            observer.update(true);
                            break;
                        }

                        case "2": {
                            observer.update(false);
                            break;
                        }

                        case "101": {
                            observer.update(false);
                            messageOutput(context.getResources().getString(R.string.error_server_is_temporarily_unavailable));
                            break;
                        }

                        default: {
                            observer.update(false);
                            messageOutput(context.getResources()
                                    .getString(R.string.error_check_internet_connect));
                        }
                    }

                } catch (JSONException e) {
                    Log.d(String.valueOf(this.getClass()), e.getMessage());

                    messageOutput(context.getResources()
                            .getString(R.string.error_authorization) + e.toString());
                    observer.update(false);
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
                            Log.d(String.valueOf(this.getClass()), volleyError.toString());
                            messageOutput(context.getResources()
                                    .getString(R.string.error_check_internet_connect));
                        }
                        observer.update(false);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token", token);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    public void requestCancel() {
        if (request != null)
            request.cancel();
    }

    private void messageOutput(String str) {
        Toast.makeText(context, str, Toast.LENGTH_LONG).show();
    }
}