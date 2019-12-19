package com.application.arenda.ServerInteraction.LoadingAnnouncements;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoadingAnnouncements extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    @SuppressLint("StaticFieldLeak")
    private final SwipeRefreshLayout refreshLayout;

    @SuppressLint("StaticFieldLeak")
    private final RecyclerView recyclerView;

    private final Context context;

    private OnLoadMoreListener onLoadMoreListener;
    private OnItemClickListener onItemClickListener;

    private boolean isLoading = false;
    private LinearLayoutManager layoutManager;
    private List<ModelAnnouncement> collection;
    private int idLayoutItem,
            lastLoadedAnnouncement = 0,
            idLayoutProgress,
            ITEM_VIEW = 0,
            ITEM_LOADING = 1,
            lastVisibleItem,
            totalItemCount,
            visibleThreashold = 10;

    @SuppressLint("StaticFieldLeak")
    private String URL_INSERT_ANNOUNCEMENT = "http://192.168.43.241/AndroidConnectWithServer/php/load/LoadingAnnouncements.php";

    public LoadingAnnouncements(final Context context, final int idLayoutItem, final int idLayoutProgress,
                                final RecyclerView recyclerView, final SwipeRefreshLayout refreshLayout) {
        this.context = context;
        this.recyclerView = recyclerView;
        this.refreshLayout = refreshLayout;
        this.idLayoutItem = idLayoutItem;
        this.idLayoutProgress = idLayoutProgress;

        initializationComponents();
        initializationListeners();
    }

    private void initializationComponents() {
        collection = new ArrayList<>();

        refreshLayout.setColorSchemeResources(R.color.colorAccent,
                R.color.colorBlue,
                R.color.colorRed);

        loadingAnnouncements(context, 0, "");

        setHasStableIds(true);

        layoutManager = new LinearLayoutManager(context);

        recyclerView.setAdapter(LoadingAnnouncements.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setItemViewCacheSize(30);
        recyclerView.setHasFixedSize(true);
    }

    private void initializationListeners() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        collection.clear();
                        loadingAnnouncements(context, 0, "");
                    }
                });
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();

                lastVisibleItem = manager.findLastVisibleItemPosition();
                totalItemCount = manager.getItemCount();

                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreashold)) {

                    isLoading = true;
                }
            }
        });
    }

    private void addToCollection(ModelAnnouncement model) {
        this.collection.add(model);

        this.notifyDataSetChanged();
    }

    public void setOnLoadMore(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public void setIsLoading(boolean loading) {
        this.isLoading = loading;
    }

    public void onStop() {
        isLoading = false;
        refreshLayout.setRefreshing(false);
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == ITEM_VIEW) {
            view = LayoutInflater.from(parent.getContext()).inflate(this.idLayoutItem, parent, false);
        } else if (viewType == ITEM_LOADING) {
            view = LayoutInflater.from(parent.getContext()).inflate(this.idLayoutProgress, parent, false);
        }

        return new ItemViewHolder(view, onItemClickListener);
    }

    @SuppressLint({"SetTextI18n", "CheckResult"})
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        final int getViewType = holder.getItemViewType();

        if (getViewType == ITEM_VIEW) {
            final ItemViewHolder viewHolder = (ItemViewHolder) holder;

            final ModelAnnouncement model = this.collection.get(position);

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
                            viewHolder.imgProgress.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model,
                                                       Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            viewHolder.imgProgress.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(viewHolder.imgProduct);

            viewHolder.textPlacementDate.setText(model.getPlacementDate());
            viewHolder.textRatingAnnouncement.setText(String.valueOf(model.getRating()));

            viewHolder.textNameProduct.setText(model.getName());

            //в зависимости от предпочтения пользователя будет браться стоимость
            viewHolder.textCostProduct.setText(model.getCostToBYN() + " руб./ч.");

            viewHolder.textCountRent.setText(String.valueOf(model.getCountRent()));
            viewHolder.textLocation.setText(model.getLocation());
        } else if (getViewType == ITEM_LOADING) {
            final ProgressViewHolder progressViewHolder = (ProgressViewHolder) holder;

            progressViewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return collection.get(position) != null ? ITEM_VIEW : ITEM_LOADING;
    }

    @Override
    public long getItemId(int position) {
        return collection.get(position).hashCode();
    }

    @Override
    public int getItemCount() {
        return collection != null ? collection.size() : 0;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void search(String search) {
        collection.clear();
        loadingAnnouncements(context, 0, search);
    }

    private void loadingAnnouncements(final Context context, final int id, final String search) {
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

                            onStop();
                            break;
                        }
                        case "1":
                            JSONObject object;

                            for (int i = 0; i < announcements.length(); i++) {
                                object = announcements.getJSONObject(i);

                                ModelAnnouncement model = new ModelAnnouncement();

                                lastLoadedAnnouncement = Integer.parseInt(object.getString("idAnnouncement"));
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

                                addToCollection(model);
                            }

                            onStop();
                            break;

                        case "2": {
                            Log.d(getClass().toString(), response);
                            messageOutput(context, context.getResources()
                                    .getString(R.string.error_fail_loading_announcements) + response);

                            onStop();
                            break;
                        }
                        case "101":
                            Log.d(getClass().toString(), response);
                            messageOutput(context, context.getResources().getString(R.string.error_server_is_temporarily_unavailable));

                            onStop();
                            break;

                        default: {
                            messageOutput(context, context.getResources().getString(R.string.error_server_is_temporarily_unavailable));

                            onStop();
                        }
                    }
                } catch (JSONException e) {
                    Log.d(getClass().toString(), response);
                    messageOutput(context, context.getResources()
                            .getString(R.string.error_fail_loading_announcements) + e.getMessage());

                    onStop();
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

                        onStop();
                    }
                }) {
            @SuppressLint("SimpleDateFormat")
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("idAnnouncement", String.valueOf(id));

                if (search.length() > 0)
                    params.put("search", search);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    private void messageOutput(final Context context, String str) {
        Toast.makeText(context, str, Toast.LENGTH_LONG).show();
    }

    interface OnItemClickListener {
        void ItemClick(View view, int position);
    }

    interface OnLoadMoreListener {
        void onLoadMore();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imgProduct, imgHeart;

        private TextView textNameProduct,
                textLocation,
                textCountRent,
                textCostProduct,
                textPlacementDate,
                textRatingAnnouncement;

        private ProgressBar imgProgress;

        private OnItemClickListener onItemClickListener;

        public ItemViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
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
//            this.onItemClickListener.ItemClick(v, getAdapterPosition());
        }
    }

    public class ProgressViewHolder extends RecyclerView.ViewHolder {

        private ProgressBar progressBar;

        public ProgressViewHolder(@NonNull View itemView) {
            super(itemView);

            progressBar = itemView.findViewById(R.id.progressBarLoadMore);
        }
    }
}