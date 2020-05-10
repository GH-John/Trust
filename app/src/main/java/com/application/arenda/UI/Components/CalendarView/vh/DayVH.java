package com.application.arenda.UI.Components.CalendarView.vh;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.application.arenda.R;
import com.application.arenda.UI.Components.CalendarView.models.ModelDayItem;
import com.application.arenda.UI.Components.CalendarView.models.ModelMonthItem;

import org.threeten.bp.LocalDate;
import org.threeten.bp.Month;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DayVH extends FrameLayout {
    @BindView(R.id.calendarDay)
    TextView calendarDay;

    @BindView(R.id.calendarEventState)
    ImageView imageEventState;

    private LocalDate currentMonth, selectedDayStart, selectedDayEnd, date, toDay;

    private MonthVH monthVH;
    private boolean isSelected;
    private boolean isDayIncludeToCurrentMonth;
    private boolean isCurrentDayOfMonth;

    public DayVH(Context context) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.calendar_vh_day, this);

        toDay = LocalDate.now();

        ButterKnife.bind(this, view);
    }

    public static DayVH create(Context context) {
        return new DayVH(context);
    }

    public void onBind(ModelDayItem modelDayItem, MonthVH monthVH, LocalDate selectedDayStart, LocalDate selectedDayEnd) {
        date = modelDayItem.getDate();

        this.monthVH = monthVH;
        this.selectedDayStart = selectedDayStart;
        this.selectedDayEnd = selectedDayEnd;

        currentMonth = monthVH.getMonthItem().getDate();

        if (modelDayItem.getEvents().size() > 0) {
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

        if (selectedDayStart != null) {

            if (date.isEqual(selectedDayStart) || (selectedDayEnd != null && date.isEqual(selectedDayEnd)))
                selectDay();
            else if (selectedDayEnd != null) {
                if (date.isAfter(selectedDayStart) && date.isBefore(selectedDayEnd)) {
                    setBackgroudIfIncludePeriodStartToEnd();
                } else if(date.isAfter(selectedDayEnd) && date.isBefore(selectedDayStart)){
                    setBackgroudIfDontIncludePeriodStartToEnd();
                }
            } else {
                if (isDayIncludeToCurrentMonth) {
                    setTypefaceNormalBlack();
                    unselectDay();
                } else {
                    setTypefaceNormalGray();
                    unselectDay();
                }
                if (isCurrentDayOfMonth)
                    selectAsCurrentDay();
            }
        }

        if (selectedDayEnd != null) {
            if (date.isEqual(selectedDayEnd) || (selectedDayStart != null && date.isEqual(selectedDayStart)))
                selectDay();
            else if (selectedDayStart != null) {
                if (date.isAfter(selectedDayStart) && date.isBefore(selectedDayEnd)) {
                    setBackgroudIfIncludePeriodStartToEnd();
                } else if(date.isAfter(selectedDayEnd) && date.isBefore(selectedDayStart)){
                    setBackgroudIfDontIncludePeriodStartToEnd();
                }
            } else {
                if (isDayIncludeToCurrentMonth) {
                    setTypefaceNormalBlack();
                    unselectDay();
                } else {
                    setTypefaceNormalGray();
                    unselectDay();
                }
                if (isCurrentDayOfMonth)
                    selectAsCurrentDay();
            }
        }

        if (selectedDayStart != null && selectedDayEnd != null) {
            if (selectedDayStart.isEqual(selectedDayEnd) && date.isEqual(selectedDayStart))
                setBackgroubdIfSelectDateStartEqualDateEnd();
        }

        calendarDay.setText(String.valueOf(date.getDayOfMonth()));
    }

    public MonthVH getMonthVH() {
        return monthVH;
    }

    public boolean isCurrentDayOfMonth() {
        isCurrentDayOfMonth = date.getDayOfMonth() == toDay.getDayOfMonth() && date.getMonth().equals(toDay.getMonth()) && date.getYear() == toDay.getYear();

        return isCurrentDayOfMonth;
    }

    public boolean isDayIncludeToCurrentMonth(Month month, int year) {
        isDayIncludeToCurrentMonth = date.getMonth().equals(month) && date.getYear() == year;

        return isDayIncludeToCurrentMonth;
    }

    public LocalDate getDate() {
        return date;
    }

    private void setTypefaceNormalGray() {
        calendarDay.setTypeface(null, Typeface.NORMAL);
        calendarDay.setTextColor(getContext().getResources().getColor(R.color.colorHint));
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

        setBackgroundResource(R.drawable.calendar_current_day_unselected);

        isSelected = false;
    }

    public void selectAsCurrentDay() {
        setTypefaceBoldBlack();

        setBackgroundResource(R.drawable.calendar_current_day_unselected);
    }

    public void selectDay() {
        setTypefaceBoldWhite();
        setBackgroundResource(R.drawable.calendar_day_selected);

        isSelected = true;
    }

    public void unselectDay() {
        if (isDayIncludeToCurrentMonth)
            setTypefaceNormalBlack();
        else
            setTypefaceNormalGray();

        setBackgroundResource(R.drawable.calendar_day_unselected);

        isSelected = false;
    }

    private void setBackgroudIfIncludePeriodStartToEnd() {
        setTypefaceBoldWhite();
        setBackgroundResource(R.drawable.calendar_day_include_in_period_start_end);
    }

    private void setBackgroudIfDontIncludePeriodStartToEnd() {
        setTypefaceBoldWhite();
        setBackgroundResource(R.drawable.calendar_day_dont_include_in_period_start_end);
    }

    private void setBackgroubdIfSelectDateStartEqualDateEnd() {
        setTypefaceBoldWhite();
        setBackgroundResource(R.drawable.calendar_day_selected_start_and_end);
    }

    public void setDayItemClick(DayItemOnClickListener listener) {
        if (listener == null)
            return;

        setOnClickListener(v -> listener.onClick(date, monthVH.getMonthItem()));
    }

    public boolean isSelected() {
        return isSelected;
    }

    public interface DayItemOnClickListener {
        void onClick(LocalDate dateTime, ModelMonthItem monthItem);
    }
}