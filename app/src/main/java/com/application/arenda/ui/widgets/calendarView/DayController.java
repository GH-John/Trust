package com.application.arenda.ui.widgets.calendarView;

public interface DayController {
    void selectAsCurrentDay();

    void unselectCurrentDay();

    void selectDay();

    void unselectDay();

    void setBackgroudIfIncludePeriodStartToEnd();

    void setBackgroudIfDontIncludePeriodStartToEnd();

    void setBackgroundIfSelectDateStartEqualDateEnd();
}