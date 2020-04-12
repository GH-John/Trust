package com.application.arenda.Entities.RecyclerView;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.application.arenda.Entities.Models.BackendlessTable;

public abstract class BaseViewHolder extends RecyclerView.ViewHolder {
    public BaseViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public abstract int getResourceLayoutId();

    public abstract void onBind(BackendlessTable model, int position);
}
