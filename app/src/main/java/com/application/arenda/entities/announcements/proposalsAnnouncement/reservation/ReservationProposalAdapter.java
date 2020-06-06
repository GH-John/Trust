package com.application.arenda.entities.announcements.proposalsAnnouncement.reservation;

import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.application.arenda.entities.models.ModelProposal;
import com.application.arenda.entities.recyclerView.BaseAdapter;
import com.application.arenda.entities.recyclerView.OnItemClick;

public class ReservationProposalAdapter extends BaseAdapter<ModelProposal, ReservationProposalVH> {
    private OnItemClick btnStartListener;
    private OnItemClick btnSendMessageListener;
    private OnItemClick itemUserAvatarListener;
    private OnItemClick itemViewListener;
    private OnItemClick itemRescheduleReservation;
    private OnItemClick itemCancleReservation;

    @NonNull
    @Override
    public ReservationProposalVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ReservationProposalVH.create(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationProposalVH holder, int position) {
        holder.onBind(getItem(position), position);

        holder.setBtnStartListener(btnStartListener);
        holder.setBtnSendMessageListener(btnSendMessageListener);
        holder.setItemUserAvatarListener(itemUserAvatarListener);
        holder.setItemViewListener(itemViewListener);
        holder.setBtnRescheduleReservation(itemRescheduleReservation);
        holder.setBtnCancleReservation(itemCancleReservation);
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getTypeProposal().getCode();
    }

    public void setBtnStartListener(OnItemClick btnStartListener) {
        this.btnStartListener = btnStartListener;
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

    public void setItemRescheduleReservation(OnItemClick itemRescheduleReservation) {
        this.itemRescheduleReservation = itemRescheduleReservation;
    }

    public void setItemCancleReservation(OnItemClick itemCancleReservation) {
        this.itemCancleReservation = itemCancleReservation;
    }
}