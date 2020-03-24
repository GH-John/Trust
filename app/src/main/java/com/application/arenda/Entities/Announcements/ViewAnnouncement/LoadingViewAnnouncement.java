package com.application.arenda.Entities.Announcements.ViewAnnouncement;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.application.arenda.Entities.Announcements.Models.ModelViewAnnouncement;
import com.application.arenda.Entities.Announcements.ViewAnnouncement.DialogFragment.ModelPhoneNumber;
import com.application.arenda.Entities.User.UserCookie;
import com.application.arenda.Entities.Utils.Utils;
import com.application.arenda.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

public class LoadingViewAnnouncement implements ILoadingViewAnnouncement {
    @Override
    public Observable<ModelViewAnnouncement> loadAnnouncement(Context context, String url, long idAnnouncement) {
        return Observable.create(observableEmitter -> {
            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @SuppressLint("SimpleDateFormat")
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        JSONArray announcement = jsonObject.getJSONArray("announcement");
                        JSONArray uris = jsonObject.getJSONArray("uris");

                        String code = jsonObject.getString("code");

                        switch (code) {
                            case "1": {
                                JSONObject object;
                                ModelViewAnnouncement model = new ModelViewAnnouncement();
                                List<Uri> uriList = new ArrayList<>();
                                List<ModelPhoneNumber> phoneNumbers = new ArrayList<>();

                                for (int i = 0; i < announcement.length(); i++) {
                                    object = announcement.getJSONObject(i);

                                    model.setID(Integer.parseInt(object.getString("idAnnouncement")));

                                    model.setIdUser(Integer.parseInt(object.getString("idUser")));

                                    model.setName(object.getString("name"));
                                    model.setDescription(object.getString("description"));

                                    model.setCostToBYN(Float.parseFloat(object.getString("costToBYN")));
                                    model.setCostToUSD(Float.parseFloat(object.getString("costToUSD")));
                                    model.setCostToEUR(Float.parseFloat(object.getString("costToEUR")));

                                    model.setAddress(object.getString("address"));

                                    model.setPlacementDate(Utils.getFormatingDate(context, object.getString("placementDate")));
                                    model.setRate(Float.parseFloat(object.getString("rating")));
                                    model.setCountRent(Integer.parseInt(object.getString("countRent")));

                                    if (object.getString("isVisible_phone_1").equals("1"))
                                        phoneNumbers.add(new ModelPhoneNumber(1, object.getString("phone_1")));
                                    if (object.getString("isVisible_phone_2").equals("1"))
                                        phoneNumbers.add(new ModelPhoneNumber(2, object.getString("phone_2")));
                                    if (object.getString("isVisible_phone_3").equals("1"))
                                        phoneNumbers.add(new ModelPhoneNumber(3, object.getString("phone_3")));

                                    model.setPhoneNumbers(phoneNumbers);

                                    model.setFavorite(object.getString("isFavorite").equals("1"));
                                }

                                for (int i = 0; i < uris.length(); i++) {
                                    object = uris.getJSONObject(i);

                                    uriList.add(Uri.parse(object.getString("photoPath")));
                                }

                                model.setUriCollection(uriList);

                                observableEmitter.onNext(model);
                                break;
                            }

                            case "2": {
                                Log.d(getClass().toString(), response);
                                Utils.messageOutput(context, context.getResources()
                                        .getString(R.string.error_fail_loading_announcement) + response);

                                observableEmitter.onComplete();
                                break;
                            }

                            case "101": {
                                Log.d(getClass().toString(), response);
                                Utils.messageOutput(context, context.getResources().getString(R.string.error_server_is_temporarily_unavailable));

                                observableEmitter.onComplete();
                                break;
                            }
                            default: {
                                Log.d(getClass().toString(), response);
                                Utils.messageOutput(context, context.getResources().getString(R.string.error_server_is_temporarily_unavailable));

                                observableEmitter.onComplete();
                            }
                        }
                    } catch (JSONException e) {
                        Log.d(getClass().toString(), response);
                        Utils.messageOutput(context, context.getResources()
                                .getString(R.string.error_fail_loading_announcement) + e.getMessage());

                        observableEmitter.onError(e);
                    } finally {
                        observableEmitter.onComplete();
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            if (volleyError instanceof TimeoutError) {
                                Utils.messageOutput(context, context.getResources()
                                        .getString(R.string.error_check_internet_connect));
                            } else {
                                Log.d(getClass().toString(), String.valueOf(volleyError.getMessage()));
                                Utils.messageOutput(context, context.getResources()
                                        .getString(R.string.error_fail_loading_announcement));
                            }

                            observableEmitter.onError(volleyError);
                        }
                    }) {
                @SuppressLint("SimpleDateFormat")
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<>();
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