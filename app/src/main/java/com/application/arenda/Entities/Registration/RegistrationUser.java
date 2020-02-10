package com.application.arenda.Entities.Registration;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
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

public class RegistrationUser {
    private final Context context;
    private ProgressBar progressBar;
    private StringRequest request;
    private AppCompatActivity startActivity;

    public RegistrationUser(Context context, AppCompatActivity startActivity) {
        this.context = context;
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
        request = new StringRequest(Request.Method.POST, ServerUtils.URL_REGISTRATION, new Response.Listener<String>() {
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
                            String token = jsonObject.getString("token").trim();

                            UserCookie.putProfile(context, token, new UserProfile());

                            context.startActivity(new Intent(context, startActivity.getClass()));
                            progressBar.setVisibility(View.GONE);
                            messageOutput(context.getResources()
                                    .getString(R.string.success_registration) + " "
                                    + name);
                            break;
                        case "101":
                            progressBar.setVisibility(View.GONE);
                            messageOutput(context.getResources().getString(R.string.error_server_is_temporarily_unavailable));
                            Log.d(String.valueOf(this.getClass()), response);
                            break;

                        default: {
                            progressBar.setVisibility(View.GONE);
                            messageOutput(context.getResources().getString(R.string.error_check_internet_connect));
                        }
                    }
                } catch (JSONException e) {
                    Log.d(String.valueOf(this.getClass()), e.getMessage());

                    progressBar.setVisibility(View.GONE);
                    messageOutput(context.getResources()
                            .getString(R.string.error_registration) + e.toString());
                }
            }
        },
                volleyError -> {
                    if (volleyError instanceof TimeoutError) {
                        progressBar.setVisibility(View.GONE);
                        messageOutput(context.getResources()
                                .getString(R.string.error_check_internet_connect));
                    } else {
                        progressBar.setVisibility(View.GONE);
//                            messageOutput(context.getResources()
//                                    .getString(R.string.error_registration) + volleyError.toString());
                        messageOutput(context.getResources()
                                .getString(R.string.error_check_internet_connect));
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