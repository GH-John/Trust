package com.application.arenda.Entities.RecyclerView;

import androidx.recyclerview.widget.RecyclerView;

import com.application.arenda.Entities.Models.BackendlessTable;

public interface OnItemClick {
    void onClick(RecyclerView.ViewHolder viewHolder, BackendlessTable model);
}
