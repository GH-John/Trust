package com.application.arenda.UI.Components.CalendarView.models;

import androidx.annotation.DrawableRes;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.List;

public class ModelDayItem {
    private LocalDate date;

    private List<String> events = new ArrayList<>();

    @DrawableRes
    private int eventStateDrawable;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<String> getEvents() {
        return events;
    }

    public void setEvents(List<String> events) {
        this.events = events;
    }

    public int getEventStateDrawable() {
        return eventStateDrawable;
    }

    public void setEventStateDrawable(int eventStateDrawable) {
        this.eventStateDrawable = eventStateDrawable;
    }
}