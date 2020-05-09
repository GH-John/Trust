package com.application.arenda.UI.Components.CalendarView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.annotation.NonNull;

import com.application.arenda.UI.Components.CalendarView.DayVH.DayItemOnClickListener;

import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class DayItemsAdapter extends BaseAdapter {
    private List<ModelDayItem> modelDayItems = new ArrayList<>();
    private List<DayVH> dayVHS = new ArrayList<>();

    private ModelDayItem lastSelectedStartItem;
    private ModelDayItem lastSelectedEndItem;

    private LocalDateTime currentMonth;

    private DayItemOnClickListener listener;

    private LayoutInflater inflater;

    public DayItemsAdapter(Context context) {
        inflater = LayoutInflater.from(context);

        currentMonth = LocalDateTime.now();
    }

    public void addDayItems(List<ModelDayItem> modelDayItems, LocalDateTime currentMonth) {
        this.modelDayItems.addAll(modelDayItems);
        this.currentMonth = currentMonth;

        notifyDataSetChanged();
    }

    public void replaceDayItems(List<ModelDayItem> modelDayItems, LocalDateTime currentMonth) {
        this.modelDayItems.clear();
        this.modelDayItems.addAll(modelDayItems);
        this.currentMonth = currentMonth;

        Timber.tag("DayItemsAdapter_").d("replaceDayItems");

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
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {
        DayVH vh = null;

        if (position <= dayVHS.size() - 1)
            vh = dayVHS.get(position);

        if (vh == null) {
            view = DayVH.createView(inflater);
            vh = DayVH.create(view);
            vh.onBind(getItem(position), currentMonth);

            if (listener != null)
                vh.setDayItemClick(listener);

            dayVHS.add(vh);
        } else {
            vh = dayVHS.get(position);
            view = vh.getItemView();
        }

        return view;
    }
}