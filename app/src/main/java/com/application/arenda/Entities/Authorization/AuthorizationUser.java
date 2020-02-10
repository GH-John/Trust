package com.application.arenda.Entities.Authorization;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.application.arenda.Entities.Utils.Network.ServerUtils;
import com.application.arenda.Entities.User.UserCookie;
import com.application.arenda.Entities.User.UserProfile;
import com.application.arenda.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AuthorizationUser {
    private final Context context;

    private StringRequest request;
    private ProgressBar progressBar;
    private AppCompatActivity startActivity;

    public AuthorizationUser(Context context, AppCompatActivity startActivity) {
        this.context = context;
        this.startActivity = startActivity;
        this.progressBar = new ProgressBar(context);
    }

    @SuppressLint("ShowToast")
    public void authorization(final String email, final String password) {
        progressBar.setVisibility(View.VISIBLE);
        request = new StringRequest(StringRequest.Method.POST, ServerUtils.URL_AUTHORIZATION, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);

                String code = jsonObject.getString("code");

                switch (code) {
                    case "1": {
                        UserCookie.putProfile(context, jsonObject.getString("token"), new UserProfile());

                        progressBar.setVisibility(View.GONE);
                        context.startActivity(new Intent(context, startActivity.getClass()));
                        messageOutput(context.getResources()
                                .getString(R.string.success_authorization) + " " + jsonObject.getString("name"));
                        break;
                    }

                    case "2": {
                        progressBar.setVisibility(View.GONE);
                        messageOutput(context.getResources()
                                .getString(R.string.unsuccess_authorization_password));
                        break;
                    }

                    case "3": {
                        progressBar.setVisibility(View.GONE);
                        messageOutput(context.getResources()
                                .getString(R.string.unsuccess_authorization_login));
                        break;
                    }

                    case "101": {
                        progressBar.setVisibility(View.GONE);
                        messageOutput(context.getResources().getString(R.string.error_server_is_temporarily_unavailable));
                        break;
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();

                progressBar.setVisibility(View.GONE);
                messageOutput(context.getResources()
                        .getString(R.string.error_authorization) + e.toString());
            }
        },
                volleyError -> {
                    if (volleyError instanceof TimeoutError) {
                        progressBar.setVisibility(View.GONE);
                        messageOutput(context.getResources()
                                .getString(R.string.error_check_internet_connect));
                    } else {
                        progressBar.setVisibility(View.GONE);
                        messageOutput(context.getResources()
                                .getString(R.string.error_authorization) + volleyError.toString());
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

    public void requestCancel() {
        if (request != null)
            request.cancel();
    }

    public ProgressBar getProgressBar() {
        return this.progressBar;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    private void messageOutput(String str) {
        Toast.makeText(context, str, Toast.LENGTH_LONG).show();
    }
}