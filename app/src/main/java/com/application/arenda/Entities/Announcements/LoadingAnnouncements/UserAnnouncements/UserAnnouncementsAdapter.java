package com.application.arenda.Entities.Announcements.LoadingAnnouncements.UserAnnouncements;

import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.application.arenda.Entities.Models.ModelAnnouncement;
import com.application.arenda.Entities.RecyclerView.BaseAdapter;
import com.application.arenda.Entities.RecyclerView.OnItemClick;

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