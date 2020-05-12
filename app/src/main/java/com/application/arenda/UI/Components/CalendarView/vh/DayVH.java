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
import com.application.arenda.UI.Components.CalendarView.DayController;
import com.application.arenda.UI.Components.CalendarView.models.ModelDayItem;
import com.application.arenda.UI.Components.CalendarView.models.ModelEvent;
import com.application.arenda.UI.Components.CalendarView.models.ModelVisibleMonths;

import org.threeten.bp.LocalDate;
import org.threeten.bp.Month;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DayVH extends FrameLayout implements DayController {
    @BindView(R.id.calendarDay)
    TextView calendarDay;

    @BindView(R.id.calendarEventState)
    ImageView imageEventState;

    private LocalDate currentMonth, selectedDayStart, selectedDayEnd, date, toDay;

    private VisibleMonthVH monthVH;
    private boolean isSelected;
    private boolean isDayIncludeToCurrentMonth;
    private boolean isCurrentDayOfMonth;
    private boolean isDayIncludeToPeriodStartToEnd = false;
    private DayItemOnClickListener dayItemOnClickListener;

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

    public void onBind(ModelDayItem modelDayItem, VisibleMonthVH monthVH, LocalDate selectedDayStart, LocalDate selectedDayEnd) {
        date = modelDayItem.getDate();

        this.monthVH = monthVH;
        this.selectedDayStart = selectedDayStart;
        this.selectedDayEnd = selectedDayEnd;

        currentMonth = monthVH.getMonthItem().getMainDate();

        calendarDay.setText(String.valueOf(date.getDayOfMonth()));

        imageEventState.setImageDrawable(null);

        if (dayItemOnClickListener != null)
            setOnClickListener(v -> dayItemOnClickListener.onClick(date, monthVH.getMonthItem(), DayVH.this));

        List<ModelEvent> monthEvents = monthVH.getMonthItem().getAllEventsMonth();
        List<ModelEvent> dayEvents = modelDayItem.getEvents();

        if (monthEvents != null) {
            for (ModelEvent event : monthEvents) {
                if (!dayEvents.contains(event))
                    dayEvents.add(event);
            }

            modelDayItem.setEvents(dayEvents);
        }

        if (dayEvents.size() > 0) {

            for (ModelEvent event : dayEvents) {

                if (date.isEqual(event.getDateStart()) || date.isEqual(event.getDateEnd())) {
                    imageEventState.setImageResource(R.drawable.ic_dot_selected);
                }
            }

        }

        unselectDay();

        if (isDayIncludeTo(currentMonth.getMonth(), currentMonth.getYear()))
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
                    isDayIncludeToPeriodStartToEnd = true;
                    setBackgroudIfIncludePeriodStartToEnd();
                } else if (date.isAfter(selectedDayEnd) && date.isBefore(selectedDayStart)) {
                    isDayIncludeToPeriodStartToEnd = false;
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
                    isDayIncludeToPeriodStartToEnd = true;
                    setBackgroudIfIncludePeriodStartToEnd();
                } else if (date.isAfter(selectedDayEnd) && date.isBefore(selectedDayStart)) {
                    isDayIncludeToPeriodStartToEnd = false;
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
                setBackgroundIfSelectDateStartEqualDateEnd();
        }
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

    public LocalDate getDate() {
        return date;
    }

    public VisibleMonthVH getMonthVH() {
        return monthVH;
    }

    public boolean isCurrentDayOfMonth() {
        isCurrentDayOfMonth = date.getDayOfMonth() == toDay.getDayOfMonth() && date.getMonth().equals(toDay.getMonth()) && date.getYear() == toDay.getYear();

        return isCurrentDayOfMonth;
    }

    public boolean isDayIncludeTo(Month month, int year) {
        isDayIncludeToCurrentMonth = date.getMonth().equals(month) && date.getYear() == year;

        return isDayIncludeToCurrentMonth;
    }

    @Override
    public void selectAsCurrentDay() {
        setTypefaceBoldBlack();

        setBackgroundResource(R.drawable.calendar_current_day_unselected);
    }

    @Override
    public void unselectCurrentDay() {
        if (isDayIncludeToCurrentMonth)
            setTypefaceBoldBlack();
        else
            setTypefaceNormalGray();

        setBackgroundResource(R.drawable.calendar_current_day_unselected);

        isSelected = false;
    }

    @Override
    public void selectDay() {
        setTypefaceBoldWhite();
        setBackgroundResource(R.drawable.calendar_day_selected);

        isSelected = true;
    }

    @Override
    public void unselectDay() {
        if (isDayIncludeToCurrentMonth)
            setTypefaceNormalBlack();
        else
            setTypefaceNormalGray();

        setBackgroundResource(R.drawable.calendar_day_unselected);

        isSelected = false;
    }

    @Override
    public void setBackgroudIfIncludePeriodStartToEnd() {
        setTypefaceBoldWhite();
        setBackgroundResource(R.drawable.calendar_day_include_in_period_start_end);
    }

    @Override
    public void setBackgroudIfDontIncludePeriodStartToEnd() {
        setTypefaceBoldWhite();
        setBackgroundResource(R.drawable.calendar_day_dont_include_in_period_start_end);
    }

    @Override
    public void setBackgroundIfSelectDateStartEqualDateEnd() {
        setTypefaceBoldWhite();
        setBackgroundResource(R.drawable.calendar_day_selected_start_and_end);
    }

    public void setDayItemClick(DayItemOnClickListener listener) {
        if (listener == null)
            return;

        dayItemOnClickListener = listener;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public boolean isDayIncludeToPeriodStartToEnd() {
        return isDayIncludeToPeriodStartToEnd;
    }

    public interface DayItemOnClickListener {
        void onClick(LocalDate dateTime, ModelVisibleMonths monthItem, DayController controller);
    }
}