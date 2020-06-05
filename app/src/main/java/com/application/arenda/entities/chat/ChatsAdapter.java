package com.application.arenda.entities.chat;

import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.application.arenda.entities.models.ModelChat;
import com.application.arenda.entities.recyclerView.BaseAdapter;
import com.application.arenda.entities.recyclerView.OnItemClick;

public class ChatsAdapter  extends BaseAdapter<ModelChat, ChatsVH> {
    private OnItemClick itemViewClick;

    public void setItemViewClick(OnItemClick itemClick) {
        itemViewClick = itemClick;
    }

    @NonNull
    @Override
    public ChatsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ChatsVH.create(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatsVH holder, int position) {
        holder.onBind(getItem(position), position);

        if (itemViewClick != null)
            holder.setOnItemViewClick(itemViewClick);
    }
}