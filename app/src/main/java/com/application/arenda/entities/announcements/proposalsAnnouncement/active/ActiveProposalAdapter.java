package com.application.arenda.entities.announcements.proposalsAnnouncement.active;

import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.application.arenda.entities.models.ModelProposal;
import com.application.arenda.entities.recyclerView.BaseAdapter;
import com.application.arenda.entities.recyclerView.OnItemClick;

public class ActiveProposalAdapter extends BaseAdapter<ModelProposal, ActiveProposalVH> {
    private OnItemClick btnFinishListener;
    private OnItemClick btnSendMessageListener;
    private OnItemClick itemUserAvatarListener;
    private OnItemClick itemViewListener;

    @NonNull
    @Override
    public ActiveProposalVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ActiveProposalVH.create(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull ActiveProposalVH holder, int position) {
        holder.onBind(getItem(position), position);

        holder.setBtnFinishListener(btnFinishListener);
        holder.setBtnSendMessageListener(btnSendMessageListener);
        holder.setItemUserAvatarListener(itemUserAvatarListener);
        holder.setItemViewListener(itemViewListener);
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getTypeProposal().getCode();
    }

    public void setBtnFinishListener(OnItemClick btnFinishListener) {
        this.btnFinishListener = btnFinishListener;
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