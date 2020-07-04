package com.application.arenda.entities.announcements.loadingAnnouncements.favorites;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.application.arenda.R;
import com.application.arenda.entities.models.IModel;
import com.application.arenda.entities.models.ModelFavoriteAnnouncement;
import com.application.arenda.entities.recyclerView.BaseViewHolder;
import com.application.arenda.entities.recyclerView.OnItemClick;
import com.application.arenda.entities.utils.Utils;
import com.application.arenda.entities.utils.glide.GlideUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoriteAnnouncementVH extends BaseViewHolder {
    @BindView(R.id.itemImgProduct)
    ImageView itemImgProduct;

    @BindView(R.id.itemUserAvatar)
    ImageView itemUserAvatar;

    @BindView(R.id.userLogin)
    TextView userLogin;

    @BindView(R.id.nameProduct)
    TextView nameProduct;

    @BindView(R.id.costProduct)
    TextView costProduct;

    @BindView(R.id.userAddress)
    TextView userAddress;

    @BindView(R.id.placementDate)
    TextView placementDate;

    @BindView(R.id.imgHeart)
    ImageView imgHeart;

    private ModelFavoriteAnnouncement model;
    private int position;

    public FavoriteAnnouncementVH(@NonNull View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }

    public static FavoriteAnnouncementVH create(ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.vh_favorite_announcement, parent, false);
        return new FavoriteAnnouncementVH(view);
    }

    @Override
    public int getResourceLayoutId() {
        return R.layout.vh_favorite_announcement;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBind(IModel model, int position) {
        this.position = position;
        if (model == null)
            return;

        this.model = (ModelFavoriteAnnouncement) model;

        GlideUtils.loadAvatar(itemView.getContext(), this.model.getUserAvatar(), itemUserAvatar);
        GlideUtils.loadImage(itemView.getContext(), this.model.getPicture(), itemImgProduct);

        userLogin.setText(this.model.getLogin());
        if (this.model.getAddress() == null || this.model.getAddress().isEmpty())
            userAddress.setVisibility(View.GONE);
        else {
            userAddress.setVisibility(View.VISIBLE);
            userAddress.setText(this.model.getAddress());
        }

        placementDate.setText(Utils.getFormatingDate(itemView.getContext(), this.model.getCreated()));
        nameProduct.setText(this.model.getName());
        costProduct.setText(this.model.getHourlyCost() + " " + itemView.getContext().getResources().getString(R.string.text_cost_usd_in_hourly));
    }

    public void setItemUserAvatarClick(OnItemClick itemClick) {
        if (model != null && itemClick != null) {
            itemUserAvatar.setOnClickListener(v -> itemClick.onClick(this, model, position));
        } else {
            throw new NullPointerException("Model not initialized");
        }
    }

    public void setOnItemViewClick(OnItemClick itemClick) {
        if (model != null && itemClick != null) {
            itemImgProduct.setOnClickListener(v -> itemClick.onClick(this, model, position));
        } else {
            throw new NullPointerException("Model not initialized");
        }
    }

    public void setOnItemHeartClick(OnItemClick itemClick) {
        if (model != null && itemClick != null) {
            imgHeart.setOnClickListener(v -> itemClick.onClick(this, model, position));
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