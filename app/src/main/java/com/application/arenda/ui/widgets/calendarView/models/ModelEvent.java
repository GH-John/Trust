package com.application.arenda.ui.widgets.calendarView.models;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;

import java.util.Objects;

public abstract class ModelEvent {
    @SerializedName("dateStart")
    private LocalDate dateStart;

    @SerializedName("timeStart")
    private LocalTime timeStart;

    @SerializedName("dateEnd")
    private LocalDate dateEnd;

    @SerializedName("timeEnd")
    private LocalTime timeEnd;

    public LocalDate getDateStart() {
        return dateStart;
    }

    public void setDateStart(LocalDate dateStart) {
        this.dateStart = dateStart;
    }

    public LocalTime getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(LocalTime timeStart) {
        this.timeStart = timeStart;
    }

    public LocalDate getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(LocalDate dateEnd) {
        this.dateEnd = dateEnd;
    }

    public LocalTime getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(LocalTime timeEnd) {
        this.timeEnd = timeEnd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ModelEvent event = (ModelEvent) o;
        return dateStart.equals(event.dateStart) &&
                timeStart.equals(event.timeStart) &&
                dateEnd.equals(event.dateEnd) &&
                timeEnd.equals(event.timeEnd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dateStart, timeStart, dateEnd, timeEnd);
    }

    @NonNull
    @Override
    public String toString() {
        return "ModelEvent{" +
                "dateStart=" + dateStart +
                ", timeStart=" + timeStart +
                ", dateEnd=" + dateEnd +
                ", timeEnd=" + timeEnd +
                '}';
    }
}