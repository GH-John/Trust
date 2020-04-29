package com.application.arenda.Entities.Announcements.LoadingAnnouncements.ViewAnnouncement;

import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.application.arenda.Entities.Models.ModelAnnouncement;
import com.application.arenda.Entities.RecyclerView.BaseAdapter;
import com.application.arenda.Entities.RecyclerView.OnItemClick;

public class LandLord_Similar_AnnouncementsAdapter extends BaseAdapter<ModelAnnouncement, LandLord_Similar_AnnouncementsVH> {

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
    public LandLord_Similar_AnnouncementsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return LandLord_Similar_AnnouncementsVH.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull LandLord_Similar_AnnouncementsVH holder, int position) {
        holder.onBind(getItem(position), position);

        if (itemViewClick != null)
            holder.setOnItemViewClick(itemViewClick);

        if (itemHeartClick != null)
            holder.setOnItemHeartClick(itemHeartClick);
    }
}