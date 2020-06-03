package com.application.arenda.entities.announcements.loadingAnnouncements.user;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class UserAnnouncementsVH extends BaseViewHolder {
    @Nullable
    @BindView(R.id.itemImgProduct)
    ImageView imgProduct;

    @Nullable
    @BindView(R.id.textNameProduct)
    TextView textNameProduct;

    @Nullable
    @BindView(R.id.itemLocation)
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

    private ModelAnnouncement model;
    private int position;

    public UserAnnouncementsVH(@NonNull View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }

    public static UserAnnouncementsVH create(ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.vh_user_announcement, parent, false);
        return new UserAnnouncementsVH(view);
    }

    @Override
    public int getResourceLayoutId() {
        return R.layout.vh_user_announcement;
    }

    @Override
    public void onBind(IModel model, int position) {
        this.model = (ModelAnnouncement) model;
        this.position = position;

        bind();
    }

    @SuppressLint("SetTextI18n")
    private void bind() {
        GlideUtils.loadImage(itemView.getContext(), model.getPictures().get(0).getUri(), imgProduct);

        textPlacementDate.setText(Utils.getFormatingDate(itemView.getContext(), model.getAnnouncementCreated()));
        textRatingAnnouncement.setText(String.valueOf(model.getAnnouncementRating()));

        textCountViewers.setText(String.valueOf(model.getCountViewers()));
        textCountFavorites.setText(String.valueOf(model.getCountFavorites()));

        textNameProduct.setText(model.getName());

        //в зависимости от предпочтения пользователя будет браться стоимость
        textCostProduct.setText(model.getHourlyCost() + " " + itemView.getContext().getResources().getString(R.string.text_cost_usd_in_hour));

        textCountRent.setText(String.valueOf(model.getCountRent()));
        textLocation.setText(model.getAddress());
    }

    public void setOnItemViewClick(OnItemClick itemClick) {
        if (model != null && itemClick != null) {
            itemView.setOnClickListener(v -> itemClick.onClick(this, model, position));
        } else {
            throw new NullPointerException("Model not initialized");
        }
    }
}