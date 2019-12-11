package com.application.arenda.ServerInteraction.InsertAnnouncement;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.application.arenda.Cookies.UserCookie;
import com.application.arenda.CustomComponents.Thumbnail.ThumbnailCompression;
import com.application.arenda.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class InsertAnnouncement extends AsyncTask<Void, Integer, Void> {
    private static String URL_INSERT_ANNOUNCEMENT = "http://192.168.43.241/AndroidConnectWithServer/php/insert/InsertAnnouncement.php";
    @SuppressLint("StaticFieldLeak")
    private Context context;
    private StringRequest request;
    private ModelAnnouncement modelAnnouncement;

    public InsertAnnouncement(Context context, ModelAnnouncement modelAnnouncement) {
        this.context = context;
        this.modelAnnouncement = modelAnnouncement;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        sendToServer();
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);

    }

    private void sendToServer() {
        request = new StringRequest(Request.Method.POST, URL_INSERT_ANNOUNCEMENT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String code = jsonObject.getString("code");

                    switch (code) {
                        case "0": {
                            messageOutput(context.getString(R.string.error_user_not_identified));
                            break;
                        }

                        case "1": {
                            messageOutput(context.getString(R.string.success_announcement_added));
                            int id = Integer.valueOf(jsonObject.getString("idAnnouncement"));
                            new InsertPhoto(context, id, modelAnnouncement.getMapBitmap()).execute();

                            publishProgress(id);
                            break;
                        }

                        case "2": {
                            Log.d(getClass().toString(), response);

                            messageOutput(context.getResources()
                                    .getString(R.string.error_adding_announcement) + response);
                            break;
                        }

                        case "101":
                            messageOutput(context.getResources().getString(R.string.error_server_is_temporarily_unavailable));
                            Log.d(getClass().toString(), response);
                            break;
                    }
                } catch (JSONException e) {
                    Log.d(getClass().toString(), response);

                    messageOutput(context.getResources()
                            .getString(R.string.error_adding_announcement) + response);
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
                            messageOutput(context.getResources()
                                    .getString(R.string.error_adding_announcement) + volleyError.getMessage());
                        }
                    }
                }) {
            @SuppressLint("SimpleDateFormat")
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token", UserCookie.getToken(context));
                params.put("idSubcategory", String.valueOf(modelAnnouncement.getIdSubcategory()));
                params.put("name", modelAnnouncement.getName());
                params.put("description", modelAnnouncement.getDescription());
                params.put("costToBYN", String.valueOf(modelAnnouncement.getCostToBYN()));
                params.put("costToUSD", String.valueOf(modelAnnouncement.getCostToUSD()));
                params.put("costToEUR", String.valueOf(modelAnnouncement.getCostToEUR()));

                params.put("address", modelAnnouncement.getLocation());

                params.put("phone_1", modelAnnouncement.getPhone_1());
                params.put("isVisible_phone_1", String.valueOf(modelAnnouncement.getVisiblePhone_1()));

                params.put("phone_2", modelAnnouncement.getPhone_2());
                params.put("isVisible_phone_2", String.valueOf(modelAnnouncement.getVisiblePhone_2()));

                params.put("phone_3", modelAnnouncement.getPhone_3());
                params.put("isVisible_phone_3", String.valueOf(modelAnnouncement.getVisiblePhone_3()));

                params.put("encodedString", ThumbnailCompression.getEncodeBase64(modelAnnouncement.getMainBitmap()));

//                params.put("placementDate", new SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.getDefault()).format(Calendar.getInstance().getTime()));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    public void cancelRequest() {
        request.cancel();
    }

    private void messageOutput(String str) {
        Toast.makeText(context, str, Toast.LENGTH_LONG).show();
    }
}