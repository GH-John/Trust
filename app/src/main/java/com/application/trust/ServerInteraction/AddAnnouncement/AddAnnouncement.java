package com.application.trust.ServerInteraction.AddAnnouncement;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.application.trust.CustomComponents.Thumbnail.ThumbnailCompression;
import com.application.trust.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddAnnouncement extends AsyncTask<Void, Void, Void> {
    @SuppressLint("StaticFieldLeak")
    private Context context;
    private Bitmap bitmap;
    private StringRequest request;
    private static String URL_INSERT_PHOTO = "http://192.168.43.241/AndroidConnectWithServer/php/insert/InsertAnnouncement.php";

    public AddAnnouncement(Context context, Bitmap bitmap) {
        this.context = context;
        this.bitmap = bitmap;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        sendToServer();
        return null;
    }

    private void sendToServer() {
        request = new StringRequest(Request.Method.POST, URL_INSERT_PHOTO, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    messageOutput(response);
                    JSONObject jsonObject = new JSONObject(response);

                    String code = jsonObject.getString("code");

                    switch (code) {
                        case "0": {
                            messageOutput(context.getString(R.string.error_send_photo_to_server));
                            break;
                        }
                        case "1":
                            messageOutput(context.getString(R.string.success_announcement_added));
                            break;
                        case "101":
                            messageOutput(context.getResources().getString(R.string.error_check_internet_connect));
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

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
                                    .getString(R.string.error_registration) + " " + volleyError.networkResponse.statusCode);
                        }
                    }
                }) {
            @SuppressLint("SimpleDateFormat")
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("idUser", "1");
                params.put("idCategory", "1");
                params.put("name", "some name");
                params.put("description", "some description");
                params.put("cost", "123.0");
                params.put("statusControl", "some status");
                params.put("placementDate", new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()));
                params.put("statusRent", "false");
                params.put("rating", "0");
                params.put("profit", "0");
                params.put("encodedString", ThumbnailCompression.getEncodeBase64(bitmap));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    private void messageOutput(String str) {
        Toast.makeText(context, str, Toast.LENGTH_LONG).show();
    }
}