package com.application.arenda.UI.Components.CalendarView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.application.arenda.Entities.Utils.Utils;
import com.application.arenda.R;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

public class CalendarView extends FrameLayout {

    private static final int DAYS_COUNT = 42;

    private int textSize;

    private LocalDateTime currentMonth;

    private ImageButton btn_previous_month, btn_next_month;
    private TextView title_year, title_month, calendarDateStartRent, calendarTimeStartRent, calendarDateEndRent, calendarTimeEndRent;
    private GridView grid_container;
    private RadioButton calendarStartDateSelectorRent, calendarEndDateSelectorRent;
    private Button calendarBtnNowDay, calendarBtnReset;

    private CalendarAdapter adapter;

    private CalendarDayVH lastVHStartRent;
    private CalendarDayVH lastVHEndRent;

    private CalendarTimePicker timePicker;

    public CalendarView(Context context) {
        super(context);
    }

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initControl(context, attrs);
    }

    public CalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initControl(context, attrs);
    }

    private void initControl(Context context, AttributeSet attrs) {
//        AndroidThreeTen.init(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.calendar_layout, this);

        initUI();
        initGridCalendar();
        loadAttrs(attrs);
        initListeners();

        resetCalendar();
    }

    private void loadAttrs(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.CalendarView);

        try {
            textSize = ta.getInteger(R.styleable.CalendarView_calendarTextSize, 12);
        } finally {
            ta.recycle();
        }
    }

    private void initUI() {
        btn_previous_month = findViewById(R.id.calendar_btn_previous_month);
        btn_next_month = findViewById(R.id.calendar_btn_next_month);

        calendarBtnNowDay = findViewById(R.id.calendarBtnNowDay);
        calendarBtnReset = findViewById(R.id.calendarBtnReset);

        title_year = findViewById(R.id.calendar_title_year);
        title_month = findViewById(R.id.calendar_title_month);

        calendarDateStartRent = findViewById(R.id.calendarDateStartRent);
        calendarDateEndRent = findViewById(R.id.calendarDateEndRent);

        calendarTimeStartRent = findViewById(R.id.calendarTimeStartRent);
        calendarTimeEndRent = findViewById(R.id.calendarTimeEndRent);

        calendarStartDateSelectorRent = findViewById(R.id.calendarStartDateSelectorRent);
        calendarEndDateSelectorRent = findViewById(R.id.calendarEndDateSelectorRent);

        grid_container = findViewById(R.id.calendar_grid_container);

        initTimePicker();
    }

    private void initTimePicker() {
        timePicker = new CalendarTimePicker(getContext());

        timePicker.setBtnTimeSelectOnClick(v -> {
            if (calendarStartDateSelectorRent.isChecked())
                setStartPeriodTime(timePicker.getHour(), timePicker.getMinute());
            else
                setEndPeriodTime(timePicker.getHour(), timePicker.getMinute());
        });

        timePicker.setOnTimeChangeListener((view, hourOfDay, minute) -> {

        });
    }

    @SuppressLint("SetTextI18n")
    private void initGridCalendar() {
        adapter = new CalendarAdapter(getContext());

        adapter.setDayItemClickListener(vh -> {

            if (calendarStartDateSelectorRent.isChecked()) {
                if (lastVHStartRent != null) {
                    if (!lastVHStartRent.isCurrentDayOfMonth())
                        lastVHStartRent.unselectDay();
                    else
                        lastVHStartRent.unselectCurrentDay();

                    lastVHStartRent = null;
                }

                lastVHStartRent = vh;

                lastVHStartRent.selectDay();

                setStartPeriodDate(lastVHStartRent.getDateTime());

                timePicker.show();
            } else {
                if (lastVHEndRent != null) {
                    if (!lastVHEndRent.isCurrentDayOfMonth())
                        lastVHEndRent.unselectDay();
                    else
                        lastVHEndRent.unselectCurrentDay();

                    lastVHEndRent = null;
                }

                lastVHEndRent = vh;

                lastVHEndRent.selectDay();

                setEndPeriodDate(lastVHEndRent.getDateTime());

                timePicker.show();
            }
        });

        grid_container.setAdapter(adapter);
    }

    private void initListeners() {
        btn_next_month.setOnClickListener(v -> {
            currentMonth = currentMonth.plusMonths(1);
            updateCalendar(currentMonth, new ArrayList<>());
        });

        btn_previous_month.setOnClickListener(v -> {
            currentMonth = currentMonth.minusMonths(1);
            updateCalendar(currentMonth, new ArrayList<>());
        });

        calendarStartDateSelectorRent.setOnCheckedChangeListener((buttonView, isChecked) -> calendarEndDateSelectorRent.setChecked(!isChecked));

        calendarEndDateSelectorRent.setOnCheckedChangeListener((buttonView, isChecked) -> calendarStartDateSelectorRent.setChecked(!isChecked));

        calendarBtnNowDay.setOnClickListener(v -> resetCalendar());

        calendarBtnReset.setOnClickListener(v -> {
            resetCalendar();
            resetStartPeriod();
            resetEndPeriod();
        });
    }

    private void resetCalendar() {
        currentMonth = LocalDateTime.now();
        updateCalendar(currentMonth, new ArrayList<>());
    }

    public void updateCalendar(LocalDateTime currentVisibleMonth, List<String> events) {
        ArrayList<DayItem> cells = new ArrayList<>();
        LocalDateTime toDay = LocalDateTime.now();

        currentVisibleMonth = currentVisibleMonth.minusDays(currentVisibleMonth.getDayOfMonth() - 1);

        int monthBeginningCell = currentVisibleMonth.getDayOfWeek().getValue() - 1;

        currentVisibleMonth = currentVisibleMonth.minusDays(monthBeginningCell);

        while (cells.size() < DAYS_COUNT) {
            DayItem dayItem = new DayItem();
            dayItem.setDate(LocalDateTime.of(currentVisibleMonth.toLocalDate(), currentVisibleMonth.toLocalTime()));
            dayItem.setEvents(events);
            dayItem.setEventStateDrawable(R.drawable.ic_dot_selected);

            cells.add(dayItem);

            currentVisibleMonth = currentVisibleMonth.plusDays(1);
        }

        ((CalendarAdapter) grid_container.getAdapter()).replaceDayItems(cells, this.currentMonth);

        title_year.setText(String.valueOf(currentMonth.getYear()));
        title_month.setText(Utils.getMonthOfYear(getContext(), currentMonth.getMonth(), true));
    }

    private void setStartPeriodDate(LocalDateTime dateTime) {
        calendarDateStartRent.setText(dateTime.format(DateTimeFormatter.ofPattern("dd.MM")));
    }

    @SuppressLint("SetTextI18n")
    private void setStartPeriodTime(int hour, int minute) {
        calendarTimeStartRent.setText(Utils.convertTimeTo_24H_or_12h(hour, minute, DateFormat.is24HourFormat(getContext())));
    }

    private void setEndPeriodDate(LocalDateTime dateTime) {
        calendarDateEndRent.setText(dateTime.format(DateTimeFormatter.ofPattern("dd.MM")));
    }

    @SuppressLint("SetTextI18n")
    private void setEndPeriodTime(int hour, int minute) {
        calendarTimeEndRent.setText(Utils.convertTimeTo_24H_or_12h(hour, minute, DateFormat.is24HourFormat(getContext())));
    }

    private void resetStartPeriod() {
        calendarDateStartRent.setText(getResources().getString(R.string.text_three_dots));
        calendarTimeStartRent.setText(getResources().getString(R.string.text_three_dots));
    }

    private void resetEndPeriod() {
        calendarDateEndRent.setText(getResources().getString(R.string.text_three_dots));
        calendarTimeEndRent.setText(getResources().getString(R.string.text_three_dots));
    }
}