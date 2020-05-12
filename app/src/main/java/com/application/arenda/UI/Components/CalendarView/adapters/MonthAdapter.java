package com.application.arenda.UI.Components.CalendarView.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.annotation.NonNull;

import com.application.arenda.UI.Components.CalendarView.models.ModelDayItem;
import com.application.arenda.UI.Components.CalendarView.vh.DayVH;
import com.application.arenda.UI.Components.CalendarView.vh.DayVH.DayItemOnClickListener;
import com.application.arenda.UI.Components.CalendarView.vh.VisibleMonthVH;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.List;

public class MonthAdapter extends BaseAdapter {
    private List<ModelDayItem> modelDayItems = new ArrayList<>();

    private VisibleMonthVH monthVH;

    private LocalDate startDate, endDate;

    private DayItemOnClickListener listener;

    public void onBindAdapter(List<ModelDayItem> modelDayItems, VisibleMonthVH monthVH) {
        this.modelDayItems.clear();
        this.modelDayItems.addAll(modelDayItems);
        this.monthVH = monthVH;

        notifyDataSetChanged();
    }

    public void updateAdapter(LocalDate dateStart, LocalDate dateEnd) {
        if (startDate != null && endDate != null && dateStart != null && dateEnd != null)
            if (startDate.isEqual(dateStart) && endDate.isEqual(dateEnd))
                return;

        this.startDate = dateStart;
        this.endDate = dateEnd;

        notifyDataSetChanged();
    }

    public void updateAdapter() {
        notifyDataSetChanged();
    }

    public List<ModelDayItem> getModelDayItems() {
        return modelDayItems;
    }

    public void setDayItemClickListener(DayItemOnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return modelDayItems.size();
    }

    @Override
    public ModelDayItem getItem(int position) {
        return modelDayItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).hashCode();
    }

    @NonNull
    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {
        DayVH vh;
        if (view == null) {
            vh = DayVH.create(parent.getContext());
        } else {
            vh = (DayVH) view;
        }

        vh.onBind(getItem(position), monthVH, startDate, endDate);

        if (listener != null)
            vh.setDayItemClick(listener);

        return vh;
    }
}