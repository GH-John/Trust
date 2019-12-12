package com.application.arenda.ServerInteraction.LoadingAnnouncements;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
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
import com.application.arenda.R;
import com.application.arenda.ServerInteraction.InsertAnnouncement.ModelAnnouncement;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

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

    private OnItemClickListener onItemClickListener;

    private LinearLayoutManager layoutManager;
    private AsyncLoadingAnnouncements asyncTask;
    private Collection<ModelAnnouncement> collection;
    private int idPatternLayout, lastVisibleItem, lastLoadedAnnouncement;

    public LoadingAnnouncements(final Context context, final int idPatternLayout,
                                final RecyclerView recyclerView, final SwipeRefreshLayout refreshLayout) {
        this.context = context;
        this.recyclerView = recyclerView;
        this.refreshLayout = refreshLayout;
        this.idPatternLayout = idPatternLayout;

        initializationComponents();
        initializationListeners();
    }

    @Override
    public long getItemId(int position) {
        return ((List<ModelAnnouncement>) collection).get(position).hashCode();
    }

    private void initializationComponents() {
        collection = new ArrayList<>();
        asyncTask = new AsyncLoadingAnnouncements(context);

        refreshLayout.setColorSchemeResources(R.color.colorAccent,
                R.color.colorBlue,
                R.color.colorRed);

        asyncTask.execute(0);

        setHasStableIds(true);

        layoutManager = new LinearLayoutManager(context);

        recyclerView.setAdapter(LoadingAnnouncements.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setItemViewCacheSize(50);
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
    }

    private void addToCollection(ModelAnnouncement model) {
        this.collection.add(model);

        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(this.idPatternLayout, parent, false);
        return new LoadingAnnouncements.ViewHolder(v, onItemClickListener);
    }

    @SuppressLint({"SetTextI18n", "CheckResult"})
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final ModelAnnouncement model = ((List<ModelAnnouncement>) this.collection).get(position);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.color.colorNotFoundPicture);
        requestOptions.placeholder(R.color.colorNotFoundPicture);
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        requestOptions.centerCrop();
        requestOptions.timeout(3000);

        Glide.with(context)
                .load(model.getMainUriBitmap())
                .apply(requestOptions)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                Target<Drawable> target, boolean isFirstResource) {
                        holder.imgProgress.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model,
                                                   Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.imgProgress.setVisibility(View.GONE);
                        return false;
                    }
                })
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.imgProduct);

        holder.textPlacementDate.setText(model.getPlacementDate());
        holder.textRatingAnnouncement.setText(String.valueOf(model.getRating()));

        holder.textNameProduct.setText(model.getName());

        //в зависимости от предпочтения пользователя будет браться стоимость
        holder.textCostProduct.setText(model.getCostToBYN() + " руб./ч.");

        holder.textCountRent.setText(String.valueOf(model.getCountRent()));
        holder.textLocation.setText(model.getLocation());
    }

    @Override
    public int getItemCount() {
        return collection.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    interface OnItemClickListener {
        void ItemClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imgProduct, imgHeart;

        private TextView textNameProduct,
                textLocation,
                textCountRent,
                textCostProduct,
                textPlacementDate,
                textRatingAnnouncement;

        private ProgressBar imgProgress;

        private OnItemClickListener onItemClickListener;

        public ViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);

            this.onItemClickListener = onItemClickListener;

            imgHeart = itemView.findViewById(R.id.imgHeart);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            imgProgress = itemView.findViewById(R.id.imgProgress);

            textLocation = itemView.findViewById(R.id.textLocation);
            textCountRent = itemView.findViewById(R.id.textCountRent);
            textNameProduct = itemView.findViewById(R.id.textNameProduct);
            textCostProduct = itemView.findViewById(R.id.textCostProduct);
            textPlacementDate = itemView.findViewById(R.id.textPlacementDate);
            textRatingAnnouncement = itemView.findViewById(R.id.textRatingAnnouncement);

            initializationListeners();
        }

        private void initializationListeners() {
            itemView.setOnClickListener(this);
            imgHeart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imgHeart.setImageDrawable(context.getDrawable(R.drawable.ic_heart_selected));
                }
            });
        }

        @Override
        public void onClick(View v) {
            this.onItemClickListener.ItemClick(v, getAdapterPosition());
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
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            this.cancel(true);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            this.cancel(true);
        }

        private void loadingAnnouncements(final Context context, final int id) {
            StringRequest request;

            request = new StringRequest(Request.Method.POST, URL_INSERT_ANNOUNCEMENT, new Response.Listener<String>() {
                @SuppressLint("SimpleDateFormat")
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        JSONArray announcements = jsonObject.getJSONArray("announcements");

                        String code = jsonObject.getString("code");

                        switch (code) {
                            case "0": {
                                Log.d(getClass().toString(), response);

                                refreshLayout.setRefreshing(false);
                                onCancelled();
                                break;
                            }
                            case "1":
                                JSONObject object;

                                for (int i = 0; i < announcements.length(); i++) {
                                    object = announcements.getJSONObject(i);

                                    ModelAnnouncement model = new ModelAnnouncement();

                                    lastLoadedAnnouncement = Integer.valueOf(object.getString("idAnnouncement"));
                                    model.setIdAnnouncement(Integer.parseInt(object.getString("idAnnouncement")));

                                    model.setIdUser(Integer.valueOf(object.getString("idUser")));

                                    model.setName(object.getString("name"));
                                    model.setDescription(object.getString("description"));

                                    model.setCostToBYN(Float.parseFloat(object.getString("costToBYN")));
                                    model.setCostToUSD(Float.parseFloat(object.getString("costToUSD")));
                                    model.setCostToEUR(Float.parseFloat(object.getString("costToEUR")));

                                    model.setLocation(object.getString("address"));

                                    model.setPhone_1(object.getString("phone_1"));
                                    model.setVisiblePhone_1(Boolean.parseBoolean(object.getString("isVisible_phone_1")));
                                    model.setPhone_2(object.getString("phone_2"));
                                    model.setVisiblePhone_2(Boolean.parseBoolean(object.getString("isVisible_phone_2")));
                                    model.setPhone_3(object.getString("phone_3"));
                                    model.setVisiblePhone_3(Boolean.parseBoolean(object.getString("isVisible_phone_3")));

                                    model.setPlacementDate(object.getString("placementDate"));
                                    model.setStatusRent(Boolean.parseBoolean(object.getString("statusRent")));
                                    model.setRating(Integer.parseInt(object.getString("rating")));
                                    model.setCountRent(Integer.parseInt(object.getString("countRent")));
                                    model.setMainUriBitmap(Uri.parse(object.getString("photoPath")));

                                    onProgressUpdate(model);
                                }

                                refreshLayout.setRefreshing(false);

                                onCancelled();
                                break;

                            case "2": {
                                Log.d(getClass().toString(), response);
                                messageOutput(context, context.getResources()
                                        .getString(R.string.error_fail_loading_announcements) + response);

                                refreshLayout.setRefreshing(false);
                                onCancelled();
                                break;
                            }
                            case "101":
                                Log.d(getClass().toString(), response);
                                messageOutput(context, context.getResources().getString(R.string.error_server_is_temporarily_unavailable));

                                refreshLayout.setRefreshing(false);
                                onCancelled();
                                break;

                            default: {
                                messageOutput(context, context.getResources().getString(R.string.error_server_is_temporarily_unavailable));

                                refreshLayout.setRefreshing(false);
                                onCancelled();
                            }
                        }
                    } catch (JSONException e) {
                        Log.d(getClass().toString(), response);
                        messageOutput(context, context.getResources()
                                .getString(R.string.error_fail_loading_announcements) + e.getMessage());

                        refreshLayout.setRefreshing(false);
                        onCancelled();
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

                            refreshLayout.setRefreshing(false);
                            onCancelled();
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