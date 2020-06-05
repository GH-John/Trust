package com.application.arenda.entities.announcements.proposalsAnnouncement.incoming;

import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.application.arenda.entities.models.ModelProposal;
import com.application.arenda.entities.recyclerView.BaseAdapter;
import com.application.arenda.entities.recyclerView.OnItemClick;

public class InProposalAdapter extends BaseAdapter<ModelProposal, InProposalVH> {
    private OnItemClick btnAcceptListener;
    private OnItemClick btnSendMessageListener;
    private OnItemClick itemUserAvatarListener;
    private OnItemClick btnRejectListener;

    @NonNull
    @Override
    public InProposalVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return InProposalVH.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull InProposalVH holder, int position) {
        holder.onBind(getItem(position), position);

        holder.setBtnAcceptListener(btnAcceptListener);
        holder.setBtnSendMessageListener(btnSendMessageListener);
        holder.setItemUserAvatarListener(itemUserAvatarListener);
        holder.setBtnRejectListener(btnRejectListener);
    }

    public void setBtnAcceptListener(OnItemClick btnAcceptListener) {
        this.btnAcceptListener = btnAcceptListener;
    }

    public void setBtnSendMessageListener(OnItemClick btnSendMessageListener) {
        this.btnSendMessageListener = btnSendMessageListener;
    }

    public void setItemUserAvatarListener(OnItemClick itemUserAvatarListener) {
        this.itemUserAvatarListener = itemUserAvatarListener;
    }

    public void setBtnRejectListener(OnItemClick btnRejectListener) {
        this.btnRejectListener = btnRejectListener;
    }
}