package com.application.arenda.UI.Components.CalendarView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.application.arenda.Entities.Utils.Utils;
import com.application.arenda.R;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import timber.log.Timber;

public class CalendarView extends FrameLayout {

    private static final int DAYS_COUNT = 42;

    private int textSize;

    private LocalDateTime currentMonth;

    private ImageButton btn_previous_month, btn_next_month;
    private TextView title_year, title_month, calendarDateStartRent, calendarTimeStartRent, calendarDateEndRent, calendarTimeEndRent;
    private RecyclerView monthRecycler;
    private RadioButton calendarStartDateSelectorRent, calendarEndDateSelectorRent;
    private Button calendarBtnNowDay, calendarBtnReset;

    private MonthAdapter recyclerAdapter;

    private DayVH lastVHStartRent;
    private DayVH lastVHEndRent;

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
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.calendar_layout, this);

        initUI();
        loadAttrs(attrs);
        initListeners();
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

        monthRecycler = findViewById(R.id.monthRecycler);

        recyclerAdapter = new MonthAdapter();

        monthRecycler.setAdapter(recyclerAdapter);

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

    @SuppressLint("CheckResult")
    private void initListeners() {
        recyclerAdapter.setMonthCallBack(new MonthAdapter.MonthCallBack() {

            @Override
            public void currentMonth(ModelMonthItem monthItem) {
                inflateHeader(monthItem.getDateTime());
            }

            @Override
            public void onError(Throwable throwable) {
                Timber.e(throwable);
            }
        });

        recyclerAdapter.setDayItemClickListener(vh -> {
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

        btn_next_month.setOnClickListener(v -> recyclerAdapter.nextMonth());

        btn_previous_month.setOnClickListener(v -> recyclerAdapter.previousMonth());

        calendarStartDateSelectorRent.setOnCheckedChangeListener((buttonView, isChecked) -> calendarEndDateSelectorRent.setChecked(!isChecked));

        calendarEndDateSelectorRent.setOnCheckedChangeListener((buttonView, isChecked) -> calendarStartDateSelectorRent.setChecked(!isChecked));

//        calendarBtnNowDay.setOnClickListener(v -> resetCalendar());

        calendarBtnReset.setOnClickListener(v -> {
            resetStartPeriod();
            resetEndPeriod();
        });
    }

    private void inflateHeader(LocalDateTime dateTime) {
        title_year.setText(String.valueOf(dateTime.getYear()));
        title_month.setText(Utils.getMonthOfYear(getContext(), dateTime.getMonth(), true));
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