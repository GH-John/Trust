package com.application.arenda.UI.Components.CalendarView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.annotation.NonNull;

import com.application.arenda.UI.Components.CalendarView.CalendarDayVH.CalendarDayItemClick;

import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

public class CalendarAdapter extends BaseAdapter {
    private List<DayItem> dayItems = new ArrayList<>();

    private List<List<DayItem>> stackMonth = new ArrayList<>();

    private DayItem lastSelectedStartItem;
    private DayItem lastSelectedEndItem;

    private LocalDateTime currentMonth;

    private CalendarDayItemClick listener;

    private LayoutInflater inflater;

    public CalendarAdapter(Context context) {
        inflater = LayoutInflater.from(context);

        currentMonth = LocalDateTime.now();
    }

    public void replaceDayItems(List<DayItem> dayItems, LocalDateTime currentMonth) {
        this.dayItems.clear();
        this.dayItems.addAll(dayItems);
        this.currentMonth = currentMonth;

        notifyDataSetChanged();
    }

    public List<DayItem> getDayItems() {
        return dayItems;
    }

    public void setDayItemClickListener(CalendarDayItemClick listener) {
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return dayItems.size();
    }

    @Override
    public DayItem getItem(int position) {
        return dayItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {
        if (view == null)
            view = CalendarDayVH.createView(inflater);

        CalendarDayVH vh = CalendarDayVH.create(view);
        vh.onBind(getItem(position), currentMonth);

        if (listener != null)
            vh.setDayItemClick(listener);

        return view;
    }
}