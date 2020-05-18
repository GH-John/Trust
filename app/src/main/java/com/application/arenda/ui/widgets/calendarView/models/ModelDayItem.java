package com.application.arenda.ui.widgets.calendarView.models;

import androidx.annotation.DrawableRes;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.List;

public class ModelDayItem {
    private LocalDate date;

    private List<ModelEvent> events = new ArrayList<>();

    @DrawableRes
    private int eventStateDrawable;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<ModelEvent> getEvents() {
        return events;
    }

    public void setEvents(List<ModelEvent> events) {
        this.events = events;
    }

    public int getEventStateDrawable() {
        return eventStateDrawable;
    }

    public void setEventStateDrawable(int eventStateDrawable) {
        this.eventStateDrawable = eventStateDrawable;
    }
}