package com.application.arenda.Entities.Announcements.LoadingAnnouncements.AllAnnouncements;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.application.arenda.Entities.Announcements.Models.ModelAllAnnouncement;
import com.application.arenda.Entities.Utils.Glide.GlideUtils;
import com.application.arenda.R;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    public AllAnnouncementsViewHolder(@NonNull View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }

    public static AllAnnouncementsViewHolder create(ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.vh_network_state, parent, false);
        return new AllAnnouncementsViewHolder(view);
    }

    public static int getLayouId() {
        return R.layout.vh_announcement;
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

        setActiveHeart(model.isFavorite());
    }

    public void setOnItemViewClick(OnItemClick itemClick) {
        if (model != null && itemClick != null) {
            itemView.setOnClickListener(v -> itemClick.onClick(this, model));
        } else {
            throw new NullPointerException("Model not initialized");
        }
    }

    public void setOnItemHeartClick(OnItemClick itemClick) {
        if (model != null && itemClick != null) {
            imgHeart.setOnClickListener(v -> itemClick.onClick(this, model));
        } else {
            throw new NullPointerException("Model not initialized");
        }
    }

    public void setActiveHeart(boolean b) {
        if (b)
            imgHeart.setImageDrawable(context.getDrawable(R.drawable.ic_heart_selected));
        else
            imgHeart.setImageDrawable(context.getDrawable(R.drawable.ic_heart_not_selected));
    }

    public interface OnItemClick {
        void onClick(RecyclerView.ViewHolder viewHolder, ModelAllAnnouncement model);
    }
}