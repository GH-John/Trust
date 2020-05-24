package com.application.arenda.entities.recyclerView;

import androidx.recyclerview.widget.RecyclerView;

import com.application.arenda.entities.models.IModel;

public interface OnItemClick {
    void onClick(RecyclerView.ViewHolder viewHolder, IModel model);
}