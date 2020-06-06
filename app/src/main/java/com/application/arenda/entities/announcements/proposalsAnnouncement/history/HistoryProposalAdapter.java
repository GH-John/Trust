package com.application.arenda.entities.announcements.proposalsAnnouncement.history;

import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.application.arenda.entities.models.ModelProposal;
import com.application.arenda.entities.recyclerView.BaseAdapter;
import com.application.arenda.entities.recyclerView.OnItemClick;

public class HistoryProposalAdapter extends BaseAdapter<ModelProposal, HistoryProposalVH> {
    private OnItemClick btnSendMessageListener;
    private OnItemClick itemUserAvatarListener;
    private OnItemClick itemViewListener;

    @NonNull
    @Override
    public HistoryProposalVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return HistoryProposalVH.create(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryProposalVH holder, int position) {
        holder.onBind(getItem(position), position);

        holder.setBtnSendMessageListener(btnSendMessageListener);
        holder.setItemUserAvatarListener(itemUserAvatarListener);
        holder.setItemViewListener(itemViewListener);
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getTypeProposal().getCode();
    }

    public void setBtnSendMessageListener(OnItemClick btnSendMessageListener) {
        this.btnSendMessageListener = btnSendMessageListener;
    }

    public void setItemUserAvatarListener(OnItemClick itemUserAvatarListener) {
        this.itemUserAvatarListener = itemUserAvatarListener;
    }

    public void setItemViewListener(OnItemClick itemViewListener) {
        this.itemViewListener = itemViewListener;
    }
}