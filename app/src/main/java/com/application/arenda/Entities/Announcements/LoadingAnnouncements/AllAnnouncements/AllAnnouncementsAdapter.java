package com.application.arenda.Entities.Announcements.LoadingAnnouncements.AllAnnouncements;

import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.application.arenda.Entities.Announcements.Models.ModelAllAnnouncement;
import com.application.arenda.Entities.RecyclerView.BaseAdapter;
import com.application.arenda.Entities.RecyclerView.OnItemClick;

public class AllAnnouncementsAdapter extends BaseAdapter<ModelAllAnnouncement, AllAnnouncementsVH> {

    private OnItemClick itemViewClick;
    private OnItemClick itemHeartClick;

    public void setItemViewClick(OnItemClick itemClick) {
        this.itemViewClick = itemClick;
    }

    public void setItemHeartClick(OnItemClick itemHeartClick) {
        this.itemHeartClick = itemHeartClick;
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
    }
}