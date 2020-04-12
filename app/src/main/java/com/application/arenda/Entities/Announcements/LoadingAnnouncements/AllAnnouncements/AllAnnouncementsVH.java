package com.application.arenda.Entities.Announcements.LoadingAnnouncements.AllAnnouncements;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.application.arenda.Entities.Models.IModel;
import com.application.arenda.Entities.Models.ModelAllAnnouncement;
import com.application.arenda.Entities.RecyclerView.BaseViewHolder;
import com.application.arenda.Entities.RecyclerView.OnItemClick;
import com.application.arenda.Entities.Utils.Glide.GlideUtils;
import com.application.arenda.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AllAnnouncementsVH extends BaseViewHolder {
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

    private ModelAllAnnouncement model;
    private int position;

    public AllAnnouncementsVH(@NonNull View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }

    @Override
    public int getResourceLayoutId() {
        return R.layout.vh_announcement;
    }

    public static AllAnnouncementsVH create(ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.vh_announcement, parent, false);
        return new AllAnnouncementsVH(view);
    }

    @Override
    public void onBind(IModel model, int position) {
        this.model = (ModelAllAnnouncement) model;
        this.position = position;

        bind();
    }

    @SuppressLint({"SetTextI18n"})
    private void bind() {
        GlideUtils.loadImage(itemView.getContext(), model.getMainUriBitmap(), imgProduct);

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
            imgHeart.setImageDrawable(itemView.getContext().getDrawable(R.drawable.ic_heart_selected));
        else
            imgHeart.setImageDrawable(itemView.getContext().getDrawable(R.drawable.ic_heart_not_selected));
    }
}