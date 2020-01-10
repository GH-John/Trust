package com.application.arenda.Entities.Announcements.LoadingAnnouncements.UserAnnouncements;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.application.arenda.Entities.Announcements.Models.ModelUserAnnouncement;
import com.application.arenda.Entities.Utils.Utils;
import com.application.arenda.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserAnnouncementsRV extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;

    private List<ModelUserAnnouncement> collection = new ArrayList<>();

    private int idLayoutItem,
            idLayoutProgress,
            ITEM_VIEW = 0,
            ITEM_LOADING = 1;

    public UserAnnouncementsRV(final Context context, final int idLayoutItem, List<ModelUserAnnouncement> collection) {
        this.context = context;
        this.idLayoutItem = idLayoutItem;
        this.collection = collection;
        setHasStableIds(true);
    }

    public void rewriteCollection(List<ModelUserAnnouncement> collection) {
        this.collection.clear();
        this.collection.addAll(collection);

        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserAnnouncementsRV.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == ITEM_VIEW) {
            view = LayoutInflater.from(parent.getContext()).inflate(this.idLayoutItem, parent, false);
        } else if (viewType == ITEM_LOADING) {
            view = LayoutInflater.from(parent.getContext()).inflate(this.idLayoutProgress, parent, false);
        }

        return new UserAnnouncementsRV.ItemViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "CheckResult"})
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        final int getViewType = holder.getItemViewType();

        if (getViewType == ITEM_VIEW) {
            final UserAnnouncementsRV.ItemViewHolder viewHolder = (UserAnnouncementsRV.ItemViewHolder) holder;

            final ModelUserAnnouncement model = this.collection.get(position);

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
            viewHolder.textRatingAnnouncement.setText(String.valueOf(model.getRate()));

            viewHolder.textCountViewers.setText(String.valueOf(model.getCountViewers()));
            viewHolder.textCountFavorites.setText(String.valueOf(model.getCountFavorites()));

            viewHolder.textNameProduct.setText(model.getName());

            //в зависимости от предпочтения пользователя будет браться стоимость
            viewHolder.textCostProduct.setText(model.getCostToBYN() + " руб./ч.");

            viewHolder.textCountRent.setText(String.valueOf(model.getCountRent()));
            viewHolder.textLocation.setText(model.getAddress());
        } else if (getViewType == ITEM_LOADING) {
            final UserAnnouncementsRV.ProgressViewHolder progressViewHolder = (UserAnnouncementsRV.ProgressViewHolder) holder;

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

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Nullable
        @BindView(R.id.imgProduct)
        ImageView imgProduct;

        @Nullable
        @BindView(R.id.textNameProduct)
        TextView textNameProduct;

        @Nullable
        @BindView(R.id.textAddress)
        TextView textLocation;

        @Nullable
        @BindView(R.id.textCountRent)
        TextView textCountRent;

        @Nullable
        @BindView(R.id.textCostProduct)
        TextView textCostProduct;

        @Nullable
        @BindView(R.id.textPlacementDate)
        TextView textPlacementDate;

        @Nullable
        @BindView(R.id.textRatingAnnouncement)
        TextView textRatingAnnouncement;

        @Nullable
        @BindView(R.id.textCountViewers)
        TextView textCountViewers;

        @Nullable
        @BindView(R.id.textCountFavorites)
        TextView textCountFavorites;

        @Nullable
        @BindView(R.id.imgProgress)
        ProgressBar imgProgress;


        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Utils.messageOutput(context, "open look announcement to the future");
        }
    }

    public class ProgressViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.progressBarLoadImg)
        ProgressBar progressBar;

        public ProgressViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}