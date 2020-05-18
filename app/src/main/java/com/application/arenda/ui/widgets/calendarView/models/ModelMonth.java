package com.application.arenda.ui.widgets.calendarView.models;

public interface ModelMonth {
    KeyMonth getKeyMainMonth();

    void setKeyMainMonth(KeyMonth key);

    KeyMonth getKeyFirstVisibleMonth();

    void setKeyFirstVisibleMonth(KeyMonth keyFirstVisibleMonth);

    KeyMonth getKeyLastVisibleMonth();

    void setKeyLastVisibleMonth(KeyMonth keyLastVisibleMonth);
}
