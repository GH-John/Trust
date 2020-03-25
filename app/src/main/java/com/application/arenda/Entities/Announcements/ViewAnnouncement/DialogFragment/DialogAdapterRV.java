package com.application.arenda.Entities.Announcements.ViewAnnouncement.DialogFragment;

import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.application.arenda.Entities.RecyclerView.BaseAdapter;
import com.application.arenda.Entities.RecyclerView.OnItemClick;

public class DialogAdapterRV extends BaseAdapter<ModelPhoneNumber, DialogViewHolderRV> {

    private OnItemClick itemClick;

    public void setOnItemClick(OnItemClick itemClick) {
        this.itemClick = itemClick;
    }

    @NonNull
    @Override
    public DialogViewHolderRV onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return DialogViewHolderRV.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull DialogViewHolderRV holder, int position) {
        holder.onBind(getItem(position), position);

        holder.setOnItemClick(itemClick);
    }
}