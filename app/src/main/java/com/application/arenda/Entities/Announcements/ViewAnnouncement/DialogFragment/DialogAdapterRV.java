package com.application.arenda.Entities.Announcements.ViewAnnouncement.DialogFragment;

import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.application.arenda.Entities.RecyclerView.BaseAdapter;

public class DialogAdapterRV extends BaseAdapter<ModelPhoneNumber, DialogViewHolderRV> {
    @NonNull
    @Override
    public DialogViewHolderRV onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return DialogViewHolderRV.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull DialogViewHolderRV holder, int position) {
        holder.onBind(getItem(position), position);
    }
}