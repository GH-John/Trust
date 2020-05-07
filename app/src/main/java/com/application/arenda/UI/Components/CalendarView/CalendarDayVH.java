package com.application.arenda.UI.Components.CalendarView;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.application.arenda.R;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.Month;

public class CalendarDayVH {
    private TextView calendarDay;

    private ImageView imageEventState;

    private View itemView;

    private LocalDateTime dateTime;
    private LocalDate toDay;
    private boolean isSelected;
    private boolean isDayIncludeToCurrentMonth;
    private boolean isCurrentDayOfMonth;

    private CalendarDayVH(@NonNull View itemView) {
        this.itemView = itemView;
        dateTime = LocalDateTime.now();
        toDay = LocalDate.now();

        calendarDay = itemView.findViewById(R.id.calendarDay);
        imageEventState = itemView.findViewById(R.id.calendarEventState);
    }

    public static CalendarDayVH create(View view) {
        return new CalendarDayVH(view);
    }

    @SuppressLint("InflateParams")
    public static View createView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.calendar_vh_day, null);
    }

    public void onBind(DayItem dayItem, LocalDateTime currentMonth) {
        dateTime = dayItem.getDate();

        if (dayItem.getEvents().size() > 0) {
            imageEventState.setImageResource(R.drawable.ic_dot_selected);
        }

        unselectDay();

        if (isDayIncludeToCurrentMonth(currentMonth.getMonth(), currentMonth.getYear()))
            setTypefaceNormalBlack();
        else
            setTypefaceNormalGray();

        if (isCurrentDayOfMonth()) {
            selectAsCurrentDay();
        }

        calendarDay.setText(String.valueOf(dateTime.getDayOfMonth()));
    }

    public boolean isCurrentDayOfMonth() {
        isCurrentDayOfMonth = dateTime.getDayOfMonth() == toDay.getDayOfMonth() && dateTime.getMonth().equals(toDay.getMonth()) && dateTime.getYear() == toDay.getYear();

        return isCurrentDayOfMonth;
    }

    public boolean isDayIncludeToCurrentMonth(Month month, int year) {
        isDayIncludeToCurrentMonth = dateTime.getMonth().equals(month) && dateTime.getYear() == year;

        return isDayIncludeToCurrentMonth;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    private void setTypefaceNormalGray() {
        calendarDay.setTypeface(null, Typeface.NORMAL);
        calendarDay.setTextColor(itemView.getContext().getResources().getColor(R.color.colorHint));
    }

    private void setTypefaceNormalBlack() {
        calendarDay.setTypeface(null, Typeface.NORMAL);
        calendarDay.setTextColor(Color.BLACK);
    }

    private void setTypefaceBoldBlack() {
        calendarDay.setTypeface(null, Typeface.BOLD);
        calendarDay.setTextColor(Color.BLACK);
    }

    private void setTypefaceBoldWhite() {
        calendarDay.setTypeface(null, Typeface.BOLD);
        calendarDay.setTextColor(Color.WHITE);
    }

    public void unselectCurrentDay() {
        if (isDayIncludeToCurrentMonth)
            setTypefaceBoldBlack();
        else
            setTypefaceNormalGray();

        itemView.setBackgroundResource(R.drawable.calendar_current_day_unselected);

        isSelected = false;
    }

    public void selectAsCurrentDay() {
        setTypefaceBoldBlack();

        itemView.setBackgroundResource(R.drawable.calendar_current_day_unselected);

        isSelected = true;
    }

    public void selectDay() {
        setTypefaceBoldWhite();
        itemView.setBackgroundResource(R.drawable.calendar_day_selected);

        isSelected = true;
    }

    public void unselectDay() {
        if (isDayIncludeToCurrentMonth)
            setTypefaceNormalBlack();
        else
            setTypefaceNormalGray();

        itemView.setBackgroundResource(R.drawable.calendar_day_unselected);

        isSelected = false;
    }

    public void setDayItemClick(CalendarDayItemClick listener) {
        if (listener == null)
            return;

        itemView.setOnClickListener(v -> listener.onClick(CalendarDayVH.this));
    }

    public boolean isSelected() {
        return isSelected;
    }

    public interface CalendarDayItemClick {
        void onClick(CalendarDayVH vh);
    }
}