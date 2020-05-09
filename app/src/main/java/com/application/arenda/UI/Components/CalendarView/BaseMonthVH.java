package com.application.arenda.UI.Components.CalendarView;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class BaseMonthVH extends RecyclerView.ViewHolder {
    public BaseMonthVH(@NonNull View itemView) {
        super(itemView);
    }

    public abstract int getResourceLayoutId();

    public abstract void onBind(ModelMonth model, int position);
}
