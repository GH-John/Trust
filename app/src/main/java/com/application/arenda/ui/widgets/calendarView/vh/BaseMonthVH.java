package com.application.arenda.ui.widgets.calendarView.vh;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.application.arenda.ui.widgets.calendarView.models.ModelMonth;

import org.threeten.bp.LocalDate;

public abstract class BaseMonthVH extends RecyclerView.ViewHolder {
    public BaseMonthVH(@NonNull View itemView) {
        super(itemView);
    }

    public abstract int getResourceLayoutId();

    public abstract void onBind(ModelMonth model, LocalDate selectedDayStart, LocalDate selectedDayEnd);
}
