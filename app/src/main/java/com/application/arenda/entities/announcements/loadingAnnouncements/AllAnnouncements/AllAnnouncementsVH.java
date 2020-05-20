package com.application.arenda.entities.announcements.loadingAnnouncements.AllAnnouncements;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.application.arenda.R;
import com.application.arenda.entities.models.IModel;
import com.application.arenda.entities.models.ModelAnnouncement;
import com.application.arenda.entities.recyclerView.BaseViewHolder;
import com.application.arenda.entities.recyclerView.OnItemClick;
import com.application.arenda.entities.utils.Utils;
import com.application.arenda.entities.utils.glide.GlideUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AllAnnouncementsVH extends BaseViewHolder {

    @Nullable
    @BindView(R.id.itemUserLogo)
    ImageView itemUserLogo;

    @Nullable
    @BindView(R.id.textUserLogin)
    TextView textUserLogin;

    @Nullable
    @BindView(R.id.itemAnnouncementMore)
    ImageButton itemAnnouncementMore;

    @Nullable
    @BindView(R.id.itemViewReviews)
    TextView itemViewReviews;

    @Nullable
    @BindView(R.id.itemImgProduct)
    ImageView itemImgProduct;

    @Nullable
    @BindView(R.id.imgHeart)
    ImageView imgHeart;

    @Nullable
    @BindView(R.id.textNameProduct)
    TextView textNameProduct;

    @Nullable
    @BindView(R.id.itemLocation)
    TextView itemLocation;

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

    private ModelAnnouncement model;
    private int position;

    public AllAnnouncementsVH(@NonNull View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }

    public static AllAnnouncementsVH create(ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.vh_announcement, parent, false);
        return new AllAnnouncementsVH(view);
    }

    @Override
    public int getResourceLayoutId() {
        return R.layout.vh_announcement;
    }

    @Override
    public void onBind(IModel model, int position) {
        this.model = (ModelAnnouncement) model;
        this.position = position;

        bind();
    }

    @SuppressLint({"SetTextI18n"})
    private void bind() {
        GlideUtils.loadImage(itemView.getContext(), model.getPictures().get(0).getUri(), itemImgProduct);

        GlideUtils.loadAvatar(itemView.getContext(), model.getUserAvatar(), itemUserLogo);

        if (model.getCountReviews() > 0) {
            itemViewReviews.setText(itemViewReviews.getText().toString() + " (" + model.getCountReviews() + ")");
            itemViewReviews.setVisibility(View.VISIBLE);
        }

        textUserLogin.setText(model.getLogin());

        textPlacementDate.setText(Utils.getFormatingDate(itemView.getContext(), model.getAnnouncementCreated()));
        textRatingAnnouncement.setText(String.valueOf(model.getAnnouncementRating()));

        textNameProduct.setText(model.getName());

        textCostProduct.setText(model.getCostToUSD() + " " + itemView.getContext().getResources().getString(R.string.text_cost_usd_in_hour));

        textCountRent.setText(String.valueOf(model.getCountRent()));
        itemLocation.setText(model.getAddress());

        setActiveHeart(model.isFavorite());
    }

    public void setItemLocationClick(OnItemClick itemClick) {
        if (model != null && itemClick != null) {
            itemLocation.setOnClickListener(v -> itemClick.onClick(this, model));
        } else {
            throw new NullPointerException("Model not initialized");
        }
    }

    public void setItemUserLogoClick(OnItemClick itemClick) {
        if (model != null && itemClick != null) {
            itemUserLogo.setOnClickListener(v -> itemClick.onClick(this, model));
        } else {
            throw new NullPointerException("Model not initialized");
        }
    }

    public void setItemAnnouncementMoreClick(OnItemClick itemClick) {
        if (model != null && itemClick != null) {
            itemAnnouncementMore.setOnClickListener(v -> itemClick.onClick(this, model));
        } else {
            throw new NullPointerException("Model not initialized");
        }
    }

    public void setItemViewReviewsClick(OnItemClick itemClick) {
        if (model != null && itemClick != null) {
            itemViewReviews.setOnClickListener(v -> itemClick.onClick(this, model));
        } else {
            throw new NullPointerException("Model not initialized");
        }
    }

    public void setOnItemViewClick(OnItemClick itemClick) {
        if (model != null && itemClick != null) {
            itemImgProduct.setOnClickListener(v -> itemClick.onClick(this, model));
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

        model.setFavorite(b);
    }
}