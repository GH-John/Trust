package com.application.arenda.ui.widgets.calendarView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.application.arenda.R;
import com.application.arenda.entities.models.ModelPeriodRent;
import com.application.arenda.entities.utils.Utils;
import com.application.arenda.ui.widgets.calendarView.adapters.CalendarAdapter;
import com.application.arenda.ui.widgets.calendarView.models.ModelEvent;
import com.application.arenda.ui.widgets.calendarView.models.ModelVisibleMonths;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class CalendarRentPeriod extends FrameLayout {
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

    @BindView(R.id.calendarStartDateSelectorRent)
    RadioButton calendarStartDateSelectorRent;

    @BindView(R.id.calendarEndDateSelectorRent)
    RadioButton calendarEndDateSelectorRent;

    @BindView(R.id.calendarBtnNowDay)
    Button calendarBtnNowDay;

    @BindView(R.id.calendarBtnReset)
    Button calendarBtnReset;

    @BindView(R.id.rentCalendar)
    Calendar calendar;

    @BindView(R.id.textMessage)
    TextView textMessage;

    @BindView(R.id.textFindNearFreeDates)
    TextView textFindNearFreeDates;

    private LocalDateTime currentMonth;

    private CalendarTimePicker timePicker;

    private LocalDate dateStart, dateEnd;
    private LocalTime timeStart, timeEnd;

    private OnSelectRange rangeListener;

    public CalendarRentPeriod(Context context) {
        super(context);
        init(context, null);
    }

    public CalendarRentPeriod(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CalendarRentPeriod(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.calendar_rent_layout, this);

        ButterKnife.bind(this, view);

        initTimePicker();
        loadAttrs(attrs);
        initListeners();
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

    private void loadAttrs(AttributeSet attrs) {
        if (attrs == null)
            return;

        //style
    }

    @SuppressLint("CheckResult")
    private void initListeners() {
        calendar.setMonthCallBack(new CalendarAdapter.MonthCallBack() {

            @Override
            public void getSelectedDate(LocalDate date, List<ModelEvent> events, DayController controller) {
                if (calendarStartDateSelectorRent.isChecked())
                    setStartPeriodDate(date);
                else
                    setEndPeriodDate(date);


                checkPeriodRent();
                showTimePicker();
            }

            @Override
            public void currentMonth(ModelVisibleMonths monthItem) {
                inflateHeader(monthItem.getMainDate());
            }

            @Override
            public void onError(Throwable throwable) {
                Timber.e(throwable);
            }
        });

        btn_next_month.setOnClickListener(v -> calendar.nextMonth());

        btn_previous_month.setOnClickListener(v -> calendar.previousMonth());

        calendarStartDateSelectorRent.setOnCheckedChangeListener((buttonView, isChecked) -> {
            calendar.setSelectDayEnd(!isChecked);
            calendarEndDateSelectorRent.setChecked(!isChecked);
        });

        calendarEndDateSelectorRent.setOnCheckedChangeListener((buttonView, isChecked) -> {
            calendar.setSelectDayStart(!isChecked);
            calendarStartDateSelectorRent.setChecked(!isChecked);
        });

        calendarBtnNowDay.setOnClickListener(v -> calendar.goTodayMonth());

        calendarBtnReset.setOnClickListener(v -> {
            calendar.resetSelectedDays();

            resetStartPeriod();
            resetEndPeriod();
        });
    }

    private void checkPeriodRent() {
        if (dateStart != null && dateEnd != null) {
//            calendar.getEventsInclusiveBetween(dateStart, dateEnd)
//                    .subscribeOn(Schedulers.io())
//                    .toObservable()
//                    .flatMap(Observable::fromIterable)
//                    .filter(event -> {
//
//                    })
        }
    }

    private void showTimePicker() {
        if (calendarStartDateSelectorRent.isChecked()) {
            if (timeStart != null) {
                Timber.tag("Timepicker_inflate").d("TimeStart - " + timeStart.getHour() + ":" + timeStart.getMinute());
                timePicker.setHour(timeStart.getHour());
                timePicker.setMinute(timeStart.getMinute());
            }
            timePicker.show();
        } else {
            if (timeEnd != null) {
                Timber.tag("Timepicker_inflate").d("TimeEnd - " + timeEnd.getHour() + ":" + timeEnd.getMinute());
                timePicker.setHour(timeEnd.getHour());
                timePicker.setMinute(timeEnd.getMinute());
            }
            timePicker.show();
        }
    }

    public ModelPeriodRent getPeriodRent() {
        ModelPeriodRent periodRent = new ModelPeriodRent();
        periodRent.setDateStart(dateStart);
        periodRent.setDateEnd(dateEnd);
        periodRent.setTimeStart(timeStart);
        periodRent.setTimeEnd(timeEnd);

        return periodRent;
    }

    private void inflateHeader(LocalDate dateTime) {
        title_year.setText(String.valueOf(dateTime.getYear()));
        title_month.setText(Utils.getMonthOfYear(getContext(), dateTime.getMonth(), true));
    }

    private void setStartPeriodDate(LocalDate date) {
        dateStart = date;

        if (rangeListener != null)
            rangeListener.selectOnDateStart(dateStart);

        calendarDateStartRent.setText(date.format(DateTimeFormatter.ofPattern(Utils.DatePattern.dd_MM_yyyy.getPattern())));
    }

    @SuppressLint("SetTextI18n")
    private void setStartPeriodTime(int hour, int minute) {
        timeStart = Utils.convertToLocalTime(hour, minute);
        Timber.tag("TimeStart").d(timeStart.toString());

        if (rangeListener != null)
            rangeListener.selectOnTimeStart(timeStart);

        calendarTimeStartRent.setText(Utils.convertTimeTo_24H_or_12h(timeStart, DateFormat.is24HourFormat(getContext())));
    }

    private void setEndPeriodDate(LocalDate date) {
        dateEnd = date;

        if (rangeListener != null)
            rangeListener.selectOnDateEnd(dateEnd);

        calendarDateEndRent.setText(date.format(DateTimeFormatter.ofPattern(Utils.DatePattern.dd_MM_yyyy.getPattern())));
    }

    @SuppressLint("SetTextI18n")
    private void setEndPeriodTime(int hour, int minute) {
        timeEnd = Utils.convertToLocalTime(hour, minute);
        Timber.tag("TimeEnd").d(timeEnd.toString());

        if (rangeListener != null)
            rangeListener.selectOnTimeEnd(timeEnd);

        calendarTimeEndRent.setText(Utils.convertTimeTo_24H_or_12h(timeEnd, DateFormat.is24HourFormat(getContext())));
    }

    private void resetStartPeriod() {
        dateStart = null;
        timeStart = null;

        if (rangeListener != null)
            rangeListener.selectOnDateStart(dateStart);

        if (rangeListener != null)
            rangeListener.selectOnTimeStart(timeStart);

        calendarDateStartRent.setText(getResources().getString(R.string.text_three_dots));
        calendarTimeStartRent.setText(getResources().getString(R.string.text_three_dots));
    }

    private void resetEndPeriod() {
        dateEnd = null;
        timeEnd = null;

        if (rangeListener != null)
            rangeListener.selectOnDateEnd(dateEnd);

        if (rangeListener != null)
            rangeListener.selectOnTimeEnd(timeEnd);

        calendarDateEndRent.setText(getResources().getString(R.string.text_three_dots));
        calendarTimeEndRent.setText(getResources().getString(R.string.text_three_dots));
    }

    public void replaceEvents(List<ModelEvent> modelEvents) {
        calendar.replaceEvents(modelEvents);
    }

    public void setRangeListener(OnSelectRange rangeListener) {
        this.rangeListener = rangeListener;
    }

    public interface OnSelectRange {
        void selectOnDateStart(LocalDate date);

        void selectOnTimeStart(LocalTime time);

        void selectOnDateEnd(LocalDate date);

        void selectOnTimeEnd(LocalTime time);
    }
}