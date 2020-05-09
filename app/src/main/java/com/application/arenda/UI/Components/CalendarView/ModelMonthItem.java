package com.application.arenda.UI.Components.CalendarView;

import androidx.annotation.NonNull;

import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ModelMonthItem implements ModelMonth, Comparable<ModelMonthItem> {
    private KeyMonth keyMonth;

    private LocalDateTime dateTime;

    private List<ModelDayItem> itemList = new ArrayList<>();

    public ModelMonthItem(KeyMonth key) {
        dateTime = LocalDateTime.now();
        this.keyMonth = key;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
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