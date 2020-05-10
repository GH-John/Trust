package com.application.arenda.UI.Components.CalendarView.models;

import androidx.annotation.NonNull;

import com.application.arenda.UI.Components.CalendarView.adapters.MonthAdapter;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ModelMonthItem implements ModelMonth, Comparable<ModelMonthItem> {
    private KeyMonth keyMonth;

    private LocalDate dateTime;

    private MonthAdapter monthAdapter;

    private List<ModelDayItem> itemList = new ArrayList<>();

    public ModelMonthItem(KeyMonth key) {
        dateTime = LocalDate.now();
        this.keyMonth = key;
        monthAdapter = new MonthAdapter();
    }

    public LocalDate getDate() {
        return dateTime;
    }

    public void setDate(LocalDate dateTime) {
        this.dateTime = dateTime;
    }

    public MonthAdapter getMonthAdapter() {
        return monthAdapter;
    }

    public List<ModelDayItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<ModelDayItem> itemList) {
        this.itemList = itemList;
    }

    @Override
    public KeyMonth getKey() {
        return keyMonth;
    }

    @Override
    public void setKey(KeyMonth key) {
        this.keyMonth = key;
    }

    @Override
    public int compareTo(@NonNull ModelMonthItem o) {
        return keyMonth.compareTo(o.getKey());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModelMonthItem item = (ModelMonthItem) o;
        return keyMonth.equals(item.keyMonth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(keyMonth);
    }
}