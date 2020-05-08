package com.application.arenda.UI.Components.CalendarView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;

import com.application.arenda.Entities.Models.IModel;
import com.application.arenda.Entities.RecyclerView.BaseViewHolder;
import com.application.arenda.R;
import com.application.arenda.UI.Components.CalendarView.DayVH.DayItemOnClickListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MonthVH extends BaseViewHolder {
    @BindView(R.id.month_grid_container)
    GridView month;

    private ModelMonthItem monthItem;

    public MonthVH(@NonNull View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);

        month.setAdapter(new DayItemsAdapter(itemView.getContext()));
    }

    public static MonthVH create(ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.calendar_vh_month, parent, false);

        return new MonthVH(view);
    }

    @Override
    public int getResourceLayoutId() {
        return R.layout.calendar_vh_month;
    }

    @Override
    public void onBind(IModel model, int position) {
        monthItem = (ModelMonthItem) model;

        ((DayItemsAdapter) month.getAdapter()).addDayItems(monthItem.getItemList(), monthItem.getDateTime());
    }

    public void setDayItemClick(DayItemOnClickListener listener) {
        if (listener == null)
            return;

        ((DayItemsAdapter) month.getAdapter()).setDayItemClickListener(listener);
    }
}