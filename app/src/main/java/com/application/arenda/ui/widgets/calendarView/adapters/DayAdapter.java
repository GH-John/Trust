package com.application.arenda.ui.widgets.calendarView.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.annotation.NonNull;

import com.application.arenda.ui.widgets.calendarView.models.ModelDayItem;
import com.application.arenda.ui.widgets.calendarView.models.ModelVisibleMonths;
import com.application.arenda.ui.widgets.calendarView.vh.DayVH;
import com.application.arenda.ui.widgets.calendarView.vh.DayVH.DayItemOnClickListener;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DayAdapter extends BaseAdapter {
    private List<ModelDayItem> modelDayItems = new ArrayList<>();

    private ModelVisibleMonths months;

    private LocalDate startDate, endDate;

    private DayItemOnClickListener listener;

    public void onBindAdapter(List<ModelDayItem> modelDayItems, ModelVisibleMonths months) {
        this.modelDayItems.clear();
        this.modelDayItems.addAll(modelDayItems);
        this.months = months;

        notifyDataSetChanged();
    }

    public void updateAdapter(LocalDate dateStart, LocalDate dateEnd) {
        this.startDate = dateStart;
        this.endDate = dateEnd;

        notifyDataSetChanged();
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
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

        vh.onBind(getItem(position), months, startDate, endDate);

        if (listener != null)
            vh.setDayItemClick(listener);

        return vh;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DayAdapter that = (DayAdapter) o;
        return Objects.equals(modelDayItems, that.modelDayItems) &&
                startDate.equals(that.startDate) &&
                endDate.equals(that.endDate) &&
                Objects.equals(listener, that.listener);
    }

    @Override
    public int hashCode() {
        return Objects.hash(modelDayItems, startDate, endDate, listener);
    }
}