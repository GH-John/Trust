package com.application.arenda.Entities.Authentication;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;

import com.application.arenda.Entities.User.AccountType;
import com.application.arenda.Entities.User.UserCookie;
import com.application.arenda.Entities.User.UserProfile;
import com.application.arenda.Entities.Utils.Retrofit.ApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public final class Authentication {
    @SuppressLint("StaticFieldLeak")
    private static Authentication instance;

    private OnAuthenticationListener authenticationListener;

    private ApiAuthentication authentication;

    private Authentication() {
        authentication = ApiClient.getApi().create(ApiAuthentication.class);
    }

    public static Authentication getInstance() {
        if (instance == null)
            instance = new Authentication();

        return instance;
    }

    private void authentication(@NonNull Context context, @NonNull Response<ResponseBody> response) {
        String res = null;
        try {
            res = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (res != null) {
            try {
                JSONObject object = new JSONObject(res);

                String token = object.getString("token");
                String message = object.getString("response");

                if (!token.equals("-1"))
                    UserCookie.putProfile(context, token, new UserProfile());

                if (message != null)
                    authenticationListener.onComplete(ApiAuthentication.AuthenticationCodes.valueOf(message));
                else
                    authenticationListener.onComplete(ApiAuthentication.AuthenticationCodes.NETWORK_ERROR);

            } catch (JSONException e) {
                Timber.e(e);
            }
        }
    }

    public void registration(@NonNull Context context,
                             @NonNull String name,
                             @NonNull String lastName,
                             @NonNull String login,
                             @NonNull String email,
                             @NonNull String password,
                             @NonNull String phone,
                             @NonNull AccountType accountType) {


        Call<ResponseBody> registrationCall = authentication
                .registration(name, lastName, login, email, password, phone, accountType.getType());

        registrationCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                authentication(context, response);
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                authenticationListener.onFailure(t);
            }
        });
    }

    public void authorization(@NonNull Context context,
                              @NonNull String email,
                              @NonNull String password) {
        Call<ResponseBody> authenticationCall = authentication
                .authorization(email, password);

        authenticationCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                authentication(context, response);
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                authenticationListener.onFailure(t);
            }
        });
    }

    public void authorizationUseToken(Context context) {
        String token = UserCookie.getToken(context);
        if (token != null) {
            Call<ResponseBody> authenticationCall = authentication
                    .authorizationUseToken(token);

            authenticationCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    String res = null;
                    try {
                        res = response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (res != null) {
                        try {
                            JSONObject object = new JSONObject(res);

                            String message = object.getString("response");

                            if (message != null)
                                authenticationListener.onComplete(ApiAuthentication.AuthenticationCodes.valueOf(message));
                            else
                                authenticationListener.onComplete(ApiAuthentication.AuthenticationCodes.NETWORK_ERROR);

                        } catch (JSONException e) {
                            Timber.e(e);
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                    authenticationListener.onFailure(t);
                }
            });
        } else {
            authenticationListener.onComplete(ApiAuthentication.AuthenticationCodes.WRONG_TOKEN);
        }
    }

    public void setOnAuthenticationListener(OnAuthenticationListener listener) {
        authenticationListener = listener;
    }
}