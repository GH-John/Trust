package com.application.arenda.UI.Components.CalendarView.models;

import androidx.annotation.NonNull;

import com.application.arenda.UI.Components.CalendarView.adapters.MonthAdapter;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import timber.log.Timber;

public class ModelVisibleMonths implements ModelMonth, Comparable<ModelVisibleMonths> {
    private KeyMonth keyMainMonth, keyFirstVisibleMonth, keyLastVisibleMonth;

    private LocalDate mainDate;

    private MonthAdapter monthAdapter;

    private List<ModelDayItem> itemList = new ArrayList<>();

    private List<ModelEvent> allEventsMonth = new ArrayList<>();

    public ModelVisibleMonths(KeyMonth key) {
        mainDate = LocalDate.now();
        this.keyMainMonth = key;
        monthAdapter = new MonthAdapter();
    }

    public LocalDate getMainDate() {
        return mainDate;
    }

    public void setMainDate(LocalDate dateTime) {
        this.mainDate = dateTime;
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

    public List<ModelEvent> getAllEventsMonth() {
        return allEventsMonth;
    }

    public void setAllEventsMonth(List<ModelEvent> allEventsMonth) {
        this.allEventsMonth = allEventsMonth;
    }

    public void updateAllEventsVisibleMonths(Map<KeyMonth, List<ModelEvent>> groupedEvents) {
        allEventsMonth.clear();

        if (groupedEvents.containsKey(keyMainMonth))
            allEventsMonth.addAll(groupedEvents.get(keyMainMonth));

        if (!keyMainMonth.equals(keyFirstVisibleMonth)) {
            if (groupedEvents.containsKey(keyFirstVisibleMonth))
                allEventsMonth.addAll(groupedEvents.get(keyFirstVisibleMonth));
        }

        if (!keyMainMonth.equals(keyLastVisibleMonth)) {
            if (groupedEvents.containsKey(keyLastVisibleMonth))
                allEventsMonth.addAll(groupedEvents.get(keyLastVisibleMonth));
        }

        Timber.d("<---------------------------------------->");

        Timber.tag("KeyMainMonth").d(keyMainMonth.getKey());
        for (ModelEvent event : allEventsMonth) {
            Timber.tag("ModelEvent").d(
                    " Start date - " + event.getDateStart().toString() +
                            " Start time - " + event.getTimeStart().toString() +
                            " End date - " + event.getDateEnd().toString() +
                            " End time - " + event.getTimeEnd().toString()
            );
        }

        Timber.d("<---------------------------------------->");

        getMonthAdapter().updateAdapter();
    }

    @Override
    public KeyMonth getKeyFirstVisibleMonth() {
        return keyFirstVisibleMonth;
    }

    @Override
    public void setKeyFirstVisibleMonth(KeyMonth keyFirstVisibleMonth) {
        this.keyFirstVisibleMonth = keyFirstVisibleMonth;
    }

    @Override
    public KeyMonth getKeyLastVisibleMonth() {
        return keyLastVisibleMonth;
    }

    @Override
    public void setKeyLastVisibleMonth(KeyMonth keyLastVisibleMonth) {
        this.keyLastVisibleMonth = keyLastVisibleMonth;
    }

    @Override
    public KeyMonth getKeyMainMonth() {
        return keyMainMonth;
    }

    @Override
    public void setKeyMainMonth(KeyMonth key) {
        this.keyMainMonth = key;
    }

    @Override
    public int compareTo(@NonNull ModelVisibleMonths o) {
        return keyMainMonth.compareTo(o.getKeyMainMonth());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModelVisibleMonths item = (ModelVisibleMonths) o;
        return keyMainMonth.equals(item.keyMainMonth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(keyMainMonth);
    }
}