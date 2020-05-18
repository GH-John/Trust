package com.application.arenda.entities.recyclerView;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.application.arenda.entities.models.IModel;

public abstract class BaseViewHolder extends RecyclerView.ViewHolder {
    public BaseViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public abstract int getResourceLayoutId();

    public abstract void onBind(IModel model, int position);
}
