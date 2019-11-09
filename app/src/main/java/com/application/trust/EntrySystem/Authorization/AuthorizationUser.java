package com.application.trust.EntrySystem.Authorization;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
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

    private StringRequest request;
    private ProgressBar progressBar;
    private AppCompatActivity startActivity;

    public AuthorizationUser(Context context, String URL_AUTHORIZATION, AppCompatActivity startActivity) {
        this.context = context;
        this.URL_AUTHORIZATION = URL_AUTHORIZATION;
        this.startActivity = startActivity;
        this.progressBar = new ProgressBar(context);
    }

    @SuppressLint("ShowToast")
    public void authorization(final String email, final String password) {
        progressBar.setVisibility(View.VISIBLE);
        request = new StringRequest(StringRequest.Method.POST, URL_AUTHORIZATION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("authorization");

                    String code = jsonObject.getString("code");

                    switch(code){
                        case "1":{
                            JSONObject object;
                            String name = "";
                            for (int i = 0; i < jsonArray.length(); i++) {
                                object = jsonArray.getJSONObject(i);

                                name = object.getString("name").trim();
                            }
                            progressBar.setVisibility(View.GONE);
                            context.startActivity(new Intent(context, startActivity.getClass()));
                            messageOutput(context.getResources()
                                    .getString(R.string.success_authorization) + " " + name);
                            break;
                        }

                        case "2":{
                            progressBar.setVisibility(View.GONE);
                            messageOutput(context.getResources()
                                    .getString(R.string.unsuccess_authorization_password));
                            break;
                        }

                        case "3":{
                            progressBar.setVisibility(View.GONE);
                            messageOutput(context.getResources()
                                    .getString(R.string.unsuccess_authorization_login));
                            break;
                        }

                        case "101": {
                            progressBar.setVisibility(View.GONE);
                            messageOutput(context.getResources().getString(R.string.error_server_connect));
                            break;
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                    progressBar.setVisibility(View.GONE);
                    messageOutput(context.getResources()
                            .getString(R.string.error_unsuccess_authorization) + " "
                            + e.toString());
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
                                    .getString(R.string.error_unsuccess_authorization) + " "
                                    + volleyError.toString());
                        }
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