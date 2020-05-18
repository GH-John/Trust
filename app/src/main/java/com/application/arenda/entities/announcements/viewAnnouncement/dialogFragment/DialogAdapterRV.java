package com.application.arenda.entities.announcements.viewAnnouncement.dialogFragment;

import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.application.arenda.entities.models.ModelPhoneNumber;
import com.application.arenda.entities.recyclerView.BaseAdapter;
import com.application.arenda.entities.recyclerView.OnItemClick;

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