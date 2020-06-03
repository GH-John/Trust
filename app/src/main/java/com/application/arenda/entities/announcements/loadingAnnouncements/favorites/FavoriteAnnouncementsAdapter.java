package com.application.arenda.entities.announcements.loadingAnnouncements.favorites;

import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.application.arenda.entities.models.ModelFavoriteAnnouncement;
import com.application.arenda.entities.recyclerView.BaseAdapter;
import com.application.arenda.entities.recyclerView.OnItemClick;

public class FavoriteAnnouncementsAdapter extends BaseAdapter<ModelFavoriteAnnouncement, FavoriteAnnouncementVH> {

    private OnItemClick itemViewClick;
    private OnItemClick itemHeartClick;
    private OnItemClick itemUserAvatarClick;

    public void setItemViewClick(OnItemClick itemClick) {
        this.itemViewClick = itemClick;
    }

    public void setItemHeartClick(OnItemClick itemHeartClick) {
        this.itemHeartClick = itemHeartClick;
    }

    public void setItemUserAvatarClick(OnItemClick itemUserAvatarClick) {
        this.itemUserAvatarClick = itemUserAvatarClick;
    }

    @NonNull
    @Override
    public FavoriteAnnouncementVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return FavoriteAnnouncementVH.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteAnnouncementVH holder, int position) {
        holder.onBind(getItem(position), position);

        if (itemViewClick != null)
            holder.setOnItemViewClick(itemViewClick);

        if (itemHeartClick != null)
            holder.setOnItemHeartClick(itemHeartClick);

        if (itemUserAvatarClick != null)
            holder.setItemUserAvatarClick(itemUserAvatarClick);
    }
}