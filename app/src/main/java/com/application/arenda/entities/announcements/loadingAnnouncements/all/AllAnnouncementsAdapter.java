package com.application.arenda.entities.announcements.loadingAnnouncements.all;

import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.application.arenda.entities.models.ModelAnnouncement;
import com.application.arenda.entities.recyclerView.BaseAdapter;
import com.application.arenda.entities.recyclerView.OnItemClick;

public class AllAnnouncementsAdapter extends BaseAdapter<ModelAnnouncement, AllAnnouncementsVH> {

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
    public AllAnnouncementsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return AllAnnouncementsVH.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull AllAnnouncementsVH holder, int position) {
        holder.onBind(getItem(position), position);

        if (itemViewClick != null)
            holder.setOnItemViewClick(itemViewClick);

        if (itemHeartClick != null)
            holder.setOnItemHeartClick(itemHeartClick);

        if (itemUserAvatarClick != null)
            holder.setItemUserAvatarClick(itemUserAvatarClick);
    }
}