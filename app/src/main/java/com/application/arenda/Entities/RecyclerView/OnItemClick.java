package com.application.arenda.Entities.RecyclerView;

import androidx.recyclerview.widget.RecyclerView;

import com.application.arenda.Entities.Announcements.Models.IModel;

public interface OnItemClick {
    void onClick(RecyclerView.ViewHolder viewHolder, IModel model);
}
