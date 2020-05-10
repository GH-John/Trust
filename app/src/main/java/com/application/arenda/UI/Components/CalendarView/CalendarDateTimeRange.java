package com.application.arenda.UI.Components.CalendarView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.application.arenda.Entities.Utils.Utils;
import com.application.arenda.R;
import com.application.arenda.UI.Components.CalendarView.adapters.CalendarAdapter;
import com.application.arenda.UI.Components.CalendarView.models.ModelMonthItem;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class CalendarDateTimeRange extends FrameLayout {

    private static final int DAYS_COUNT = 42;
    @BindView(R.id.calendar_btn_previous_month)
    ImageButton btn_previous_month;
    @BindView(R.id.calendar_btn_next_month)
    ImageButton btn_next_month;
    @BindView(R.id.calendar_title_year)
    TextView title_year;
    @BindView(R.id.calendar_title_month)
    TextView title_month;
    @BindView(R.id.calendarDateStartRent)
    TextView calendarDateStartRent;
    @BindView(R.id.calendarTimeStartRent)
    TextView calendarTimeStartRent;
    @BindView(R.id.calendarDateEndRent)
    TextView calendarDateEndRent;
    @BindView(R.id.calendarTimeEndRent)
    TextView calendarTimeEndRent;
    @BindView(R.id.monthRecycler)
    RecyclerView monthRecycler;
    @BindView(R.id.calendarStartDateSelectorRent)
    RadioButton calendarStartDateSelectorRent;
    @BindView(R.id.calendarEndDateSelectorRent)
    RadioButton calendarEndDateSelectorRent;
    @BindView(R.id.calendarBtnNowDay)
    Button calendarBtnNowDay;
    @BindView(R.id.calendarBtnReset)
    Button calendarBtnReset;
    private int textSize;
    private LocalDateTime currentMonth;
    private CalendarAdapter recyclerAdapter;

    private CalendarTimePicker timePicker;
    private CalendarView calendarView;

    public CalendarDateTimeRange(Context context) {
        super(context);
    }

    public CalendarDateTimeRange(Context context, AttributeSet attrs) {
        super(context, attrs);
        initControl(context, attrs);
    }

    public CalendarDateTimeRange(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initControl(context, attrs);
    }

    private void initControl(Context context, AttributeSet attrs) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.calendar_layout, this);

        ButterKnife.bind(this, view);

        initUI();
        loadAttrs(attrs);
        initListeners();
    }

    private void loadAttrs(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.CalendarDateTimeRange);

        try {
            textSize = ta.getInteger(R.styleable.CalendarDateTimeRange_calendarTextSize, 12);
        } finally {
            ta.recycle();
        }
    }

    private void initUI() {
        recyclerAdapter = new CalendarAdapter();

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
        recyclerAdapter.setMonthCallBack(new CalendarAdapter.MonthCallBack() {

            @Override
            public void getSelectedDateTime(LocalDate dateTime) {
                if (calendarStartDateSelectorRent.isChecked()) {
                    setStartPeriodDate(dateTime);

                    timePicker.show();
                } else {
                    setEndPeriodDate(dateTime);

                    timePicker.show();
                }
            }

            @Override
            public void currentMonth(ModelMonthItem monthItem) {
                inflateHeader(monthItem.getDate());
            }

            @Override
            public void onError(Throwable throwable) {
                Timber.e(throwable);
            }
        });

        btn_next_month.setOnClickListener(v -> recyclerAdapter.nextMonth());

        btn_previous_month.setOnClickListener(v -> recyclerAdapter.previousMonth());

        calendarStartDateSelectorRent.setOnCheckedChangeListener((buttonView, isChecked) -> {
            recyclerAdapter.setSelectDayEnd(!isChecked);
            calendarEndDateSelectorRent.setChecked(!isChecked);
        });

        calendarEndDateSelectorRent.setOnCheckedChangeListener((buttonView, isChecked) -> {
            recyclerAdapter.setSelectDayStart(!isChecked);
            calendarStartDateSelectorRent.setChecked(!isChecked);
        });

        calendarBtnNowDay.setOnClickListener(v -> recyclerAdapter.goTodayMonth());

        calendarBtnReset.setOnClickListener(v -> {
            recyclerAdapter.resetSelectedDays();

            resetStartPeriod();
            resetEndPeriod();
        });
    }

    private void inflateHeader(LocalDate dateTime) {
        title_year.setText(String.valueOf(dateTime.getYear()));
        title_month.setText(Utils.getMonthOfYear(getContext(), dateTime.getMonth(), true));
    }

    private void setStartPeriodDate(LocalDate date) {
        calendarDateStartRent.setText(date.format(DateTimeFormatter.ofPattern("dd.MM")));
    }

    @SuppressLint("SetTextI18n")
    private void setStartPeriodTime(int hour, int minute) {
        calendarTimeStartRent.setText(Utils.convertTimeTo_24H_or_12h(hour, minute, DateFormat.is24HourFormat(getContext())));
    }

    private void setEndPeriodDate(LocalDate date) {
        calendarDateEndRent.setText(date.format(DateTimeFormatter.ofPattern("dd.MM")));
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