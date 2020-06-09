package com.application.arenda.entities.announcements.loadingAnnouncements.view;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.application.arenda.R;
import com.application.arenda.entities.models.IModel;
import com.application.arenda.entities.models.ModelAnnouncement;
import com.application.arenda.entities.recyclerView.BaseViewHolder;
import com.application.arenda.entities.recyclerView.OnItemClick;
import com.application.arenda.entities.utils.glide.GlideUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LandLord_Similar_AnnouncementsVH extends BaseViewHolder {

    @Nullable
    @BindView(R.id.itemImgProduct)
    ImageView itemImgProduct;

    @Nullable
    @BindView(R.id.btnInsertToFavorite)
    ImageButton btnInsertToFavorite;

    @Nullable
    @BindView(R.id.textNameProduct)
    TextView textNameProduct;

    @Nullable
    @BindView(R.id.textCountRent)
    TextView textCountRent;

    @Nullable
    @BindView(R.id.textCostProduct)
    TextView textCostProduct;

    private ModelAnnouncement model;
    private int position;

    public LandLord_Similar_AnnouncementsVH(@NonNull View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }

    public static LandLord_Similar_AnnouncementsVH create(ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.vh_small_announcement, parent, false);
        return new LandLord_Similar_AnnouncementsVH(view);
    }

    @Override
    @LayoutRes
    public int getResourceLayoutId() {
        return R.layout.vh_small_announcement;
    }

    @Override
    public void onBind(IModel model, int position) {
        this.model = (ModelAnnouncement) model;
        this.position = position;

        bind();
    }

    @SuppressLint({"CheckResult", "SetTextI18n"})
    private void bind() {
//        ModelPicture.getMainPicture(model.getPictures())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(uri -> GlideUtils.loadImage(itemView.getContext(), uri, itemImgProduct));

        GlideUtils.loadImage(itemView.getContext(), model.getPictures().get(0).getUri(), itemImgProduct);

        textNameProduct.setText(model.getName());
        textCountRent.setText(String.valueOf(model.getCountRent()));

        textCostProduct.setText(model.getHourlyCost() + " " + itemView.getContext().getResources().getString(R.string.text_cost_usd_in_hour));

        setActiveHeart(model.isFavorite());
    }

    public void setOnItemViewClick(OnItemClick itemClick) {
        if (model != null && itemClick != null) {
            itemView.setOnClickListener(v -> itemClick.onClick(this, model, position));
        } else {
            throw new NullPointerException("Model not initialized");
        }
    }

    public void setOnItemHeartClick(OnItemClick itemClick) {
        if (model != null && itemClick != null) {
            btnInsertToFavorite.setOnClickListener(v -> itemClick.onClick(this, model, position));
        } else {
            throw new NullPointerException("Model not initialized");
        }
    }

    public void setActiveHeart(boolean b) {
        if (b)
            btnInsertToFavorite.setImageDrawable(itemView.getContext().getDrawable(R.drawable.ic_heart_selected));
        else
            btnInsertToFavorite.setImageDrawable(itemView.getContext().getDrawable(R.drawable.ic_heart_not_selected));
    }
}