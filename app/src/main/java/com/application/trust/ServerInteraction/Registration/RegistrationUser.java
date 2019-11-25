package com.application.trust.ServerInteraction.Registration;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

import java.util.HashMap;
import java.util.Map;

public class RegistrationUser {
    private final Context context;
    private final String URL_REGISTRATION;

    private ProgressBar progressBar;
    private StringRequest request;
    private AppCompatActivity startActivity;

    public RegistrationUser(Context context, String URL_REGISTRATION, AppCompatActivity startActivity) {
        this.context = context;
        this.URL_REGISTRATION = URL_REGISTRATION;
        this.startActivity = startActivity;
        this.progressBar = new ProgressBar(context);
    }

    @SuppressLint("ShowToast")
    public void registration(final String name,
                                final String lastName,
                                final String email,
                                final String password,
                                final String phone,
                                final String accountType) {
        progressBar.setVisibility(View.VISIBLE);
        request = new StringRequest(Request.Method.POST, URL_REGISTRATION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String code = jsonObject.getString("code");

                    switch (code) {
                        case "0": {
                            progressBar.setVisibility(View.GONE);
                            messageOutput(context.getResources()
                                    .getString(R.string.error_user_exists));
                            break;
                        }
                        case "1":
                            progressBar.setVisibility(View.GONE);
                            context.startActivity(new Intent(context, startActivity.getClass()));
                            messageOutput(context.getResources()
                                    .getString(R.string.success_registration) + " "
                                    + name);
                            break;
                        case "101":
                            progressBar.setVisibility(View.GONE);
                            messageOutput(context.getResources().getString(R.string.error_check_internet_connect));
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                    progressBar.setVisibility(View.GONE);
                    messageOutput(context.getResources()
                            .getString(R.string.error_registration)+ e.toString());
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if(volleyError instanceof TimeoutError){
                            progressBar.setVisibility(View.GONE);
                            messageOutput(context.getResources()
                                    .getString(R.string.error_check_internet_connect));
                        }else {
                            progressBar.setVisibility(View.GONE);
                            messageOutput(context.getResources()
                                    .getString(R.string.error_registration)+ volleyError.toString());
                        }
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
        requestQueue.add(request);
    }

    public void requestCancel(){
        if(request != null)
            request.cancel();
    }

    public void setProgressBar(ProgressBar progressBar){
        this.progressBar = progressBar;
    }

    public ProgressBar getProgressBar(){
        return this.progressBar;
    }

    private void messageOutput(String str){
        Toast.makeText(context, str, Toast.LENGTH_LONG).show();
    }
}