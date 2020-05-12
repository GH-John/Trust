package com.application.arenda.UI.Components.CalendarView;

public interface DayController {
    void selectAsCurrentDay();

    void unselectCurrentDay();

    void selectDay();

    void unselectDay();

    void setBackgroudIfIncludePeriodStartToEnd();

    void setBackgroudIfDontIncludePeriodStartToEnd();

    void setBackgroundIfSelectDateStartEqualDateEnd();
}