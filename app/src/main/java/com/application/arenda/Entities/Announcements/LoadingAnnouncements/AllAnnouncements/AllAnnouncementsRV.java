package com.application.arenda.Entities.Announcements.LoadingAnnouncements.AllAnnouncements;

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

import com.application.arenda.Entities.Announcements.InsertToFavorite.InsertToFavorite;
import com.application.arenda.Entities.Announcements.Models.ModelAllAnnouncement;
import com.application.arenda.Entities.Utils.ServerUtils;
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
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AllAnnouncementsRV extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    private OnClickItem onClickItem;
    private InsertToFavorite insertToFavorite = new InsertToFavorite();

    private List<ModelAllAnnouncement> collection = new ArrayList<>();
    private int idLayoutItem,
            idLayoutProgress,
            ITEM_VIEW = 0,
            ITEM_LOADING = 1;

    public AllAnnouncementsRV(final Context context, final int idLayoutItem, List<ModelAllAnnouncement> collection) {
        this.context = context;
        this.idLayoutItem = idLayoutItem;
        this.collection = collection;
        this.onClickItem = model -> {};
        setHasStableIds(true);
    }

    public void rewriteCollection(List<ModelAllAnnouncement> collection){
        this.collection.clear();
        this.collection.addAll(collection);

        this.notifyDataSetChanged();
    }

    public void onItemClick(OnClickItem onClickItem){
        this.onClickItem = onClickItem;
    }

    @NonNull
    @Override
    public AllAnnouncementsRV.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == ITEM_VIEW) {
            view = LayoutInflater.from(parent.getContext()).inflate(this.idLayoutItem, parent, false);
        } else if (viewType == ITEM_LOADING) {
            view = LayoutInflater.from(parent.getContext()).inflate(this.idLayoutProgress, parent, false);
        }

        return new AllAnnouncementsRV.ItemViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "CheckResult"})
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        final int getViewType = holder.getItemViewType();

        if (getViewType == ITEM_VIEW) {
            final AllAnnouncementsRV.ItemViewHolder viewHolder = (AllAnnouncementsRV.ItemViewHolder) holder;

            final ModelAllAnnouncement model = this.collection.get(position);

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

            viewHolder.textNameProduct.setText(model.getName());

            //в зависимости от предпочтения пользователя будет браться стоимость
            viewHolder.textCostProduct.setText(model.getCostToBYN() + " руб./ч.");

            viewHolder.textCountRent.setText(String.valueOf(model.getCountRent()));
            viewHolder.textLocation.setText(model.getAddress());

            viewHolder.selectHeart(model.isFavorite());
        } else if (getViewType == ITEM_LOADING) {
            final AllAnnouncementsRV.ProgressViewHolder progressViewHolder = (AllAnnouncementsRV.ProgressViewHolder) holder;

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

    public interface OnClickItem {
        void onClick(ModelAllAnnouncement model);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        @Nullable
        @BindView(R.id.imgProduct)
        ImageView imgProduct;

        @Nullable
        @BindView(R.id.imgHeart)
        ImageView imgHeart;

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
        @BindView(R.id.imgProgress)
        ProgressBar imgProgress;


        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(v -> onClickItem.onClick(collection.get(getLayoutPosition())));
        }

        @OnClick(R.id.imgHeart)
        public void onHeartClick() {
            insertToFavorite.insertToFavorite(context, ServerUtils.URL_INSERT_TO_FAVORITE,
                    collection.get(getLayoutPosition()).getIdAnnouncement())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Boolean>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Boolean isFavorite) {
                            selectHeart(isFavorite);
                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }

        public void selectHeart(boolean b){
            if(b)
                imgHeart.setImageDrawable(context.getDrawable(R.drawable.ic_heart_selected));
            else
                imgHeart.setImageDrawable(context.getDrawable(R.drawable.ic_heart_not_selected));
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