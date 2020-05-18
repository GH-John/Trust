package com.application.arenda.entities.announcements.loadingAnnouncements.UserAnnouncements;

import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.application.arenda.entities.models.ModelAnnouncement;
import com.application.arenda.entities.recyclerView.BaseAdapter;
import com.application.arenda.entities.recyclerView.OnItemClick;

public class UserAnnouncementsAdapter extends BaseAdapter<ModelAnnouncement, UserAnnouncementsVH> {

    private OnItemClick itemViewClick;

    @NonNull
    @Override
    public UserAnnouncementsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return UserAnnouncementsVH.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAnnouncementsVH holder, int position) {
        holder.onBind(getItem(position), position);

        if (itemViewClick != null)
            holder.setOnItemViewClick(itemViewClick);
    }

    public void setItemViewClick(OnItemClick itemClick) {
        this.itemViewClick = itemClick;
    }
}