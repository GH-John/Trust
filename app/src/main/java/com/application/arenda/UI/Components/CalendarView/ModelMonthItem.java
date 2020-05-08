package com.application.arenda.UI.Components.CalendarView;

import androidx.annotation.Nullable;

import com.application.arenda.Entities.Models.IModel;

import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

public class ModelMonthItem implements IModel {
    private long ID;

    private LocalDateTime dateTime;

    private List<ModelDayItem> itemList = new ArrayList<>();

    public ModelMonthItem() {
        dateTime = LocalDateTime.now();
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
    public long getID() {
        return ID;
    }

    @Override
    public void setID(long id) {
        this.ID = id;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == this)
            return true;

        if (obj == null || !(obj instanceof ModelMonthItem))
            return false;

        ModelMonthItem item = (ModelMonthItem) obj;
        LocalDateTime localDateTime = item.getDateTime();

        return dateTime.getMonth() == localDateTime.getMonth() && dateTime.getYear() == localDateTime.getYear();
    }
}