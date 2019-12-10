package com.application.arenda.ServerInteraction.LoadingAnnouncements;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.application.arenda.CustomComponents.ComponentBackground;
import com.application.arenda.R;
import com.application.arenda.ServerInteraction.InsertAnnouncement.ModelAnnouncement;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoadingAnnouncements extends RecyclerView.Adapter<LoadingAnnouncements.ViewHolder> {
    @SuppressLint("StaticFieldLeak")
    private final SwipeRefreshLayout refreshLayout;

    @SuppressLint("StaticFieldLeak")
    private final RecyclerView recyclerView;

    private final Context context;

    private RequestOptions requestOptions;
    private LinearLayoutManager layoutManager;
    private AsyncLoadingAnnouncements asyncTask;
    private Collection<ModelAnnouncement> collection;
    private int idPatternLayout, lastVisibleItem, lastLoadedAnnouncement;

    public LoadingAnnouncements(final Context context, final int idPatternLayout, final RecyclerView recyclerView, final SwipeRefreshLayout refreshLayout) {
        this.context = context;
        this.refreshLayout = refreshLayout;
        this.recyclerView = recyclerView;
        this.idPatternLayout = idPatternLayout;
        this.requestOptions = new RequestOptions().centerCrop().placeholder(R.color.colorNotFoundPicture).error(R.color.colorRed);

        initializationComponents();
        initializationListeners();
    }

    private void initializationComponents() {
        collection = new ArrayList<>();
        asyncTask = new AsyncLoadingAnnouncements(context);

        asyncTask.execute(0);

        layoutManager = new LinearLayoutManager(context);

        refreshLayout.setColorSchemeResources(R.color.colorAccent,
                R.color.colorBlue,
                R.color.colorRed);

        recyclerView.setAdapter(LoadingAnnouncements.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
    }

    private void initializationListeners() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                asyncTask.cancel(true);
                if (asyncTask.isCancelled()) {
                    asyncTask = new AsyncLoadingAnnouncements(context);
                    asyncTask.execute(0);
                }
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager())
                        .findLastVisibleItemPosition();

                if (lastVisibleItem == collection.size()) {
                    asyncTask.execute(lastLoadedAnnouncement);
                }
            }
        });
    }

    private void addToCollection(ModelAnnouncement model) {
        this.collection.add(model);
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(this.idPatternLayout, parent, false);
        return new LoadingAnnouncements.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final ModelAnnouncement model = ((List<ModelAnnouncement>) this.collection).get(position);
        holder.textPlacementDate.setText(model.getPlacementDate());
        holder.textRatingAnnouncement.setText(String.valueOf(model.getRating()));

        holder.textNameProduct.setText(model.getName());

        //в зависимости от предпочтения пользователя будет браться стоимость
        holder.textCostProduct.setText(String.valueOf(model.getCostToBYN()));

        holder.textCountRent.setText(String.valueOf(model.getCountRent()));
        holder.textLocation.setText(model.getLocation());

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(context).load(model.getPictureUrl()).apply(requestOptions).into(holder.imgProduct);
            }
        });
    }

    @Override
    public int getItemCount() {
        return collection.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ViewGroup layout;
        private ImageView imgProduct,
                backgroundPlacementDate,
                backgroundImgHeart,
                backgroundRateAnnouncement,
                backgroundAnnouncement;
        private TextView textNameProduct,
                textLocation,
                textCostProduct,
                textCountRent,
                textPlacementDate,
                textRatingAnnouncement;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.layoutTemplateAnnouncement);

            imgProduct = itemView.findViewById(R.id.imgProduct);

            backgroundAnnouncement = itemView.findViewById(R.id.backgroundAnnouncement);
            backgroundPlacementDate = itemView.findViewById(R.id.backgroundPlacementDate);
            backgroundImgHeart = itemView.findViewById(R.id.backgroundImgHeart);
            backgroundRateAnnouncement = itemView.findViewById(R.id.backgroundRateAnnouncement);

            imgProduct.setImageDrawable(new ComponentBackground(context, R.color.colorWhite,
                    20f, 20f, 0f, 0f));

            backgroundAnnouncement.setImageDrawable(new ComponentBackground(context, R.color.colorWhite,
                    R.color.shadowColor, 6f, 0f, 3f, 20f));

            backgroundPlacementDate.setImageDrawable(new ComponentBackground(context, R.color.colorWhite,
                    20f, 0f, 20f, 0f));

            backgroundImgHeart.setImageDrawable(new ComponentBackground(context, R.color.colorWhite,
                    0f, 20f, 0f, 20f));

            backgroundRateAnnouncement.setImageDrawable(new ComponentBackground(context, R.color.colorWhite,
                    0f, 20f, 0f, 0f));

            textNameProduct = itemView.findViewById(R.id.textNameProduct);
            textLocation = itemView.findViewById(R.id.textLocation);
            textCostProduct = itemView.findViewById(R.id.textCostProduct);
            textCountRent = itemView.findViewById(R.id.textCountRent);
            textPlacementDate = itemView.findViewById(R.id.textPlacementDate);
            textRatingAnnouncement = itemView.findViewById(R.id.textRatingAnnouncement);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class AsyncLoadingAnnouncements extends AsyncTask<Integer, ModelAnnouncement, Void> {
        private final Context context;
        private String URL_INSERT_ANNOUNCEMENT = "http://192.168.43.241/AndroidConnectWithServer/php/load/LoadingAnnouncements.php";

        public AsyncLoadingAnnouncements(final Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Integer... params) {
            if (params != null) {
                for (int id : params) {
                    loadingAnnouncements(context, id);
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(ModelAnnouncement... models) {
            super.onProgressUpdate(models);
            if (models != null) {
                for (ModelAnnouncement model : models) {
                    LoadingAnnouncements.this.addToCollection(model);
                }

                Log.d("TESTS", String.valueOf(collection.size()));
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            refreshLayout.setRefreshing(false);
            this.cancel(true);
        }

        private void loadingAnnouncements(final Context context, final int id) {
            StringRequest request;

            request = new StringRequest(Request.Method.POST, URL_INSERT_ANNOUNCEMENT, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
//                        messageOutput(context, response);
                        JSONObject jsonObject = new JSONObject(response);

                        JSONArray announcements = jsonObject.getJSONArray("announcements");

                        String code = jsonObject.getString("code");

                        switch (code) {
                            case "0": {
                                Log.d(getClass().toString(), response);

                                AsyncLoadingAnnouncements.this.cancel(true);
                                break;
                            }
                            case "1":
                                JSONObject object;

                                for (int i = 0; i < announcements.length(); i++) {
                                    object = announcements.getJSONObject(i);

                                    ModelAnnouncement model = new ModelAnnouncement();

                                    lastLoadedAnnouncement = Integer.valueOf(object.getString("idAnnouncement"));

                                    model.setIdAnnouncement(lastLoadedAnnouncement);
                                    model.setIdUser(Integer.valueOf(object.getString("idUser")));

                                    model.setName(object.getString("name"));
                                    model.setDescription(object.getString("description"));

                                    model.setCostToBYN(Float.valueOf(object.getString("costToBYN")));
                                    model.setCostToBYN(Float.valueOf(object.getString("costToUSD")));
                                    model.setCostToBYN(Float.valueOf(object.getString("costToEUR")));
                                    model.setLocation(object.getString("address"));

                                    model.setPhone_1(object.getString("phone_1"));
                                    model.setVisiblePhone_1(Boolean.parseBoolean(object.getString("isVisible_phone_1")));
                                    model.setPhone_1(object.getString("phone_2"));
                                    model.setVisiblePhone_1(Boolean.parseBoolean(object.getString("isVisible_phone_2")));
                                    model.setPhone_1(object.getString("phone_3"));
                                    model.setVisiblePhone_1(Boolean.parseBoolean(object.getString("isVisible_phone_3")));

                                    model.setPlacementDate(object.getString("placementDate"));
                                    model.setStatusRent(Boolean.parseBoolean(object.getString("statusRent")));
                                    model.setRating(Integer.parseInt(object.getString("rating")));
                                    model.setCountRent(Integer.parseInt(object.getString("countRent")));

                                    onProgressUpdate(model);
                                }

                                cancel(true);
                                break;
                            case "101":
                                messageOutput(context, context.getResources().getString(R.string.error_server_is_temporarily_unavailable));
                                Log.d(getClass().toString(), response);

                                AsyncLoadingAnnouncements.this.cancel(true);
                                break;

                            default: {
                                messageOutput(context, context.getResources().getString(R.string.error_server_is_temporarily_unavailable));

                                AsyncLoadingAnnouncements.this.cancel(true);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d(getClass().toString(), response);
                        messageOutput(context, context.getResources()
                                .getString(R.string.error_fail_loading_announcements));

                        AsyncLoadingAnnouncements.this.cancel(true);
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            if (volleyError instanceof TimeoutError) {
                                messageOutput(context, context.getResources()
                                        .getString(R.string.error_check_internet_connect));
                            } else {
                                Log.d(getClass().toString(), String.valueOf(volleyError.getMessage()));
                                messageOutput(context, context.getResources()
                                        .getString(R.string.error_fail_loading_announcements));
                            }

                            AsyncLoadingAnnouncements.this.cancel(true);
                        }
                    }) {
                @SuppressLint("SimpleDateFormat")
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("idAnnouncement", String.valueOf(id));

                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(request);
        }

        private void messageOutput(final Context context, String str) {
            Toast.makeText(context, str, Toast.LENGTH_LONG).show();
        }
    }
}