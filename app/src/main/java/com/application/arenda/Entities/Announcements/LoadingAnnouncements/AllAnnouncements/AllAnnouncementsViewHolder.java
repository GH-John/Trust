package com.application.arenda.Entities.Announcements.LoadingAnnouncements.AllAnnouncements;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.application.arenda.Entities.Announcements.InsertToFavorite.InsertToFavorite;
import com.application.arenda.Entities.Announcements.Models.ModelAllAnnouncement;
import com.application.arenda.Entities.RecyclerView.OnItemClick;
import com.application.arenda.Entities.Utils.Glide.GlideUtils;
import com.application.arenda.Entities.Utils.Network.ServerUtils;
import com.application.arenda.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AllAnnouncementsViewHolder extends RecyclerView.ViewHolder {
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

    private Context context;
    private ModelAllAnnouncement model;

    private InsertToFavorite insertToFavorite = new InsertToFavorite();

    public AllAnnouncementsViewHolder(@NonNull View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);

        imgHeart.setOnClickListener(v -> onHeartClick());
    }

    public static int getLayouId() {
        return R.layout.template_1_announcement;
    }

    @SuppressLint({"SetTextI18n"})
    public void onBind(Context context, ModelAllAnnouncement model, int position) {
        this.context = context;
        this.model = model;

        GlideUtils.loadImage(context, model.getMainUriBitmap(), imgProduct);

        textPlacementDate.setText(model.getPlacementDate());
        textRatingAnnouncement.setText(String.valueOf(model.getRate()));

        textNameProduct.setText(model.getName());

        //в зависимости от предпочтения пользователя будет браться стоимость
        textCostProduct.setText(model.getCostToBYN() + " руб./ч.");

        textCountRent.setText(String.valueOf(model.getCountRent()));
        textLocation.setText(model.getAddress());

        setHeart(model.isFavorite());
    }

    public void onItemViewClick(OnItemClick itemClick) {
        if (model != null) {
            itemView.setOnClickListener(v -> itemClick.onClick(model));
        } else {
            throw new NullPointerException("Model not initialized");
        }
    }

    private void onHeartClick() {
        insertToFavorite.insertToFavorite(context, ServerUtils.URL_INSERT_TO_FAVORITE,
                model.getIdAnnouncement())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean isFavorite) {
                        setHeart(isFavorite);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    private void setHeart(boolean b) {
        if (b)
            imgHeart.setImageDrawable(context.getDrawable(R.drawable.ic_heart_selected));
        else
            imgHeart.setImageDrawable(context.getDrawable(R.drawable.ic_heart_not_selected));
    }
}