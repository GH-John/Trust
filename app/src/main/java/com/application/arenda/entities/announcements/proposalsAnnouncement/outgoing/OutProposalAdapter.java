package com.application.arenda.entities.announcements.proposalsAnnouncement.outgoing;

import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.application.arenda.entities.models.ModelProposal;
import com.application.arenda.entities.recyclerView.BaseAdapter;
import com.application.arenda.entities.recyclerView.OnItemClick;

public class OutProposalAdapter extends BaseAdapter<ModelProposal, OutProposalVH> {
    private OnItemClick btnRejectListener;
    private OnItemClick btnSendMessageListener;
    private OnItemClick itemUserAvatarListener;

    @NonNull
    @Override
    public OutProposalVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return OutProposalVH.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull OutProposalVH holder, int position) {
        holder.onBind(getItem(position), position);

        holder.setBtnRejectListener(btnRejectListener);
        holder.setBtnSendMessageListener(btnSendMessageListener);
        holder.setItemUserAvatarListener(itemUserAvatarListener);
    }

    public void setBtnRejectListener(OnItemClick btnRejectListener) {
        this.btnRejectListener = btnRejectListener;
    }

    public void setBtnSendMessageListener(OnItemClick btnSendMessageListener) {
        this.btnSendMessageListener = btnSendMessageListener;
    }

    public void setItemUserAvatarListener(OnItemClick itemUserAvatarListener) {
        this.itemUserAvatarListener = itemUserAvatarListener;
    }
}