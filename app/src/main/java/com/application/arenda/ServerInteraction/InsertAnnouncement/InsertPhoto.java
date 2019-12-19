package com.application.arenda.ServerInteraction.InsertAnnouncement;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
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
import com.application.arenda.UI.Thumbnail.ThumbnailCompression;
import com.application.arenda.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class InsertPhoto extends AsyncTask<Void, Void, Void> {
    private static String URL_INSERT_PHOTO = "http://192.168.43.241/AndroidConnectWithServer/php/insert/InsertPhoto.php";

    @SuppressLint("StaticFieldLeak")
    private Context context;

    private Map<Uri, Bitmap> mapBitmap;
    private int idAnnouncement;
    private StringRequest request;

    public InsertPhoto(Context context, int idAnnouncement, Map<Uri, Bitmap> mapBitmap) {
        this.context = context;
        this.mapBitmap = mapBitmap;
        this.idAnnouncement = idAnnouncement;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        for (Map.Entry<Uri, Bitmap> map : mapBitmap.entrySet()) {
            sendToServer(map.getValue());
        }
        return null;
    }

    private void sendToServer(final Bitmap bitmap) {
        request = new StringRequest(Request.Method.POST, URL_INSERT_PHOTO, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String code = jsonObject.getString("code");

                    switch (code) {
                        case "0": {
                            messageOutput(context.getString(R.string.error_send_photo_to_server) + response);
                            break;
                        }

                        case "1": {
                            Log.d(getClass().toString(), context.getString(R.string.success_photos_loaded));
                            break;
                        }
                        
                        case "101":
                            messageOutput(context.getResources().getString(R.string.error_server_is_temporarily_unavailable));
                            break;
                    }
                } catch (JSONException e) {
                    Log.d(getClass().toString(), e.getMessage());

                    messageOutput(context.getResources()
                            .getString(R.string.error_adding_photos) + e.getMessage());
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
                            messageOutput(context.getString(R.string.error_send_photo_to_server) + volleyError.getMessage());
                        }
                    }
                }) {
            @SuppressLint("SimpleDateFormat")
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("idAnnouncement", String.valueOf(idAnnouncement));
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
