package com.application.arenda.UI.Components.CalendarView;

import androidx.annotation.DrawableRes;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ModelDayItem {
    private LocalDateTime date;

    private List<String> events = new ArrayList<>();

    @DrawableRes
    private int eventStateDrawable;

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
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