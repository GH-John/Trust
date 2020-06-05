package com.application.arenda.entities.chat;

import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.application.arenda.entities.models.ModelMessage;
import com.application.arenda.entities.recyclerView.BaseAdapter;

public class MessageAdapter extends BaseAdapter<ModelMessage, MessageVH> {
    @NonNull
    @Override
    public MessageVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return MessageVH.create(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageVH holder, int position) {
        holder.onBind(getItem(position), position);
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getType().getCode();
    }
}