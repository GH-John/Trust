package com.application.arenda.UI.Components.CalendarView.vh;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;

import com.application.arenda.R;
import com.application.arenda.UI.Components.CalendarView.models.KeyMonth;
import com.application.arenda.UI.Components.CalendarView.vh.DayVH.DayItemOnClickListener;
import com.application.arenda.UI.Components.CalendarView.models.ModelMonth;
import com.application.arenda.UI.Components.CalendarView.models.ModelMonthItem;

import org.threeten.bp.LocalDate;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MonthVH extends BaseMonthVH {
    @BindView(R.id.month_grid_container)
    GridView month;

    private ModelMonthItem monthItem;

    private MonthVH(@NonNull View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }

    public static MonthVH create(ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.calendar_vh_month, parent, false);

        return new MonthVH(view);
    }

    public ModelMonthItem getMonthItem() {
        return monthItem;
    }

    @Override
    public int getResourceLayoutId() {
        return R.layout.calendar_vh_month;
    }

    @Override
    public void onBind(ModelMonth model, LocalDate selectedDayStart, LocalDate selectedDayEnd) {
        monthItem = (ModelMonthItem) model;

        monthItem.getMonthAdapter().onBindAdapter(monthItem.getItemList(), this);

        month.setAdapter(monthItem.getMonthAdapter());
    }

    public KeyMonth getKeyMonth() {
        return monthItem.getKey();
    }

    public void setDayItemClick(DayItemOnClickListener listener) {
        if (listener == null)
            return;

        monthItem.getMonthAdapter().setDayItemClickListener(listener);
    }
}