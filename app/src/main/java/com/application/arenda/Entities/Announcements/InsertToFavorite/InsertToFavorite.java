package com.application.arenda.Entities.Announcements.InsertToFavorite;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.application.arenda.Entities.User.UserCookie;
import com.application.arenda.Entities.Utils.Utils;
import com.application.arenda.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;

public class InsertToFavorite implements IInsertToFavorite {
    @Override
    public Observable<Boolean> insertToFavorite(@NonNull Context context, @NonNull String url, long idAnnouncement) {
        return Observable.create(observableEmitter -> {
            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        String code = jsonObject.getString("code");

                        switch (code) {
                            case "0": {
                                observableEmitter.onComplete();

                                Utils.messageOutput(context, context.getResources()
                                        .getString(R.string.error_insert_to_favorite) + response);
                                break;
                            }

                            case "1": {
                                boolean isFavorite = Boolean.parseBoolean(jsonObject.getString("isFavorite"));

                                observableEmitter.onNext(isFavorite);
                                break;
                            }

                            case "2": {
                                Log.d(getClass().toString(), response);

                                observableEmitter.onComplete();
                                Utils.messageOutput(context, context.getString(R.string.error_user_not_identified));
                                break;
                            }

                            case "101":
                                Utils.messageOutput(context, context.getResources()
                                        .getString(R.string.error_server_is_temporarily_unavailable));

                                observableEmitter.onComplete();

                                Log.d(getClass().toString(), response);
                                break;
                        }
                    } catch (JSONException e) {
                        Log.d(getClass().toString(), response);
                        observableEmitter.onError(e);
                        Utils.messageOutput(context, context.getResources()
                                .getString(R.string.error_insert_to_favorite) + response);
                    } finally {
                        observableEmitter.onComplete();
                    }
                }
            },
                    volleyError -> {
                        if (volleyError instanceof TimeoutError) {
                            Utils.messageOutput(context, context.getResources()
                                    .getString(R.string.error_check_internet_connect));
                        } else {
                            Utils.messageOutput(context, context.getResources()
                                    .getString(R.string.error_insert_to_favorite) + volleyError.getMessage());
                        }

                        observableEmitter.onError(volleyError);
                    }) {
                @SuppressLint("SimpleDateFormat")
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("token", UserCookie.getToken(context));
                    params.put("idAnnouncement", String.valueOf(idAnnouncement));

                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(request);
        });
    }
}