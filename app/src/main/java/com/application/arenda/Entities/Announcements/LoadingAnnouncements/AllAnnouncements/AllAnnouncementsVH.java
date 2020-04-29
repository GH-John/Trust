package com.application.arenda.Entities.Announcements.LoadingAnnouncements.AllAnnouncements;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.application.arenda.Entities.Models.IModel;
import com.application.arenda.Entities.Models.ModelAnnouncement;
import com.application.arenda.Entities.RecyclerView.BaseViewHolder;
import com.application.arenda.Entities.RecyclerView.OnItemClick;
import com.application.arenda.Entities.Utils.Glide.GlideUtils;
import com.application.arenda.Entities.Utils.Utils;
import com.application.arenda.R;

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
        GlideUtils.loadImage(itemView.getContext(), Uri.parse(model.getPictures().get(0).getPicture()), itemImgProduct);

        GlideUtils.loadAvatar(itemView.getContext(), Uri.parse(model.getUserLogo()), itemUserLogo);

        if (model.getCountReviews() > 0) {
            itemViewReviews.setText(itemViewReviews.getText().toString() + " (" + model.getCountReviews() + ")");
            itemViewReviews.setVisibility(View.VISIBLE);
        }

        textUserLogin.setText(model.getLogin());

        textPlacementDate.setText(Utils.getFormatingDate(itemView.getContext(), model.getAnnouncementCreated()));
        textRatingAnnouncement.setText(String.valueOf(model.getAnnouncementRating()));

        textNameProduct.setText(model.getName());

        //в зависимости от предпочтения пользователя будет браться стоимость
        textCostProduct.setText(model.getCostToBYN() + " руб./ч.");

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
    }
}