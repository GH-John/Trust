package com.application.trust.EntrySystem.Registration;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.application.trust.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegistrationUser {
    private final Context context;
    private final String URL_REGISTRATION;

    public RegistrationUser(Context context, String URL_REGISTRATION) {
        this.context = context;
        this.URL_REGISTRATION = URL_REGISTRATION;
    }

    @SuppressLint("ShowToast")
    public boolean registration(final String name,
                                final String lastName,
                                final String email,
                                final String password,
                                final String phone,
                                final String accountType) {
        final boolean[] statusReg = new boolean[1];

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGISTRATION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String success = jsonObject.getString("success");

                    if (success.equals("1")) {
                        statusReg[0] = true;
                        Toast.makeText(context, context.getResources()
                                .getString(R.string.success_registration) + " " + name, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    statusReg[0] = false;
                    Toast.makeText(context, context.getResources()
                                    .getString(R.string.unsuccess_registration) + " "
                                    + e.toString() + " "
                                    + context.getResources().getString(R.string.check_internet_connect),
                            Toast.LENGTH_LONG).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        statusReg[0] = false;
                        Toast.makeText(context, context.getResources()
                                        .getString(R.string.unsuccess_registration) + " "
                                        + volleyError.toString() + " "
                                        + context.getResources().getString(R.string.check_internet_connect),
                                Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("lastName", lastName);
                params.put("email", email);
                params.put("password", password);
                params.put("phone", phone);
                params.put("accountType", accountType);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

        return statusReg[0];
    }
}