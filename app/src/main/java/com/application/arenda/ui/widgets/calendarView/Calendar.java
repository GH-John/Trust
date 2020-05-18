package com.application.arenda.ui.widgets.calendarView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.application.arenda.R;
import com.application.arenda.ui.widgets.calendarView.adapters.CalendarAdapter;
import com.application.arenda.ui.widgets.calendarView.adapters.CalendarAdapter.MonthCallBack;
import com.application.arenda.ui.widgets.calendarView.models.ModelEvent;

import org.threeten.bp.LocalDate;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Single;

public class Calendar extends FrameLayout {
    @BindView(R.id.monthRecycler)
    RecyclerView monthRecycler;

    private CalendarAdapter recyclerAdapter;

    public Calendar(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public Calendar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public Calendar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public Calendar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.calendar_layout, this);

        ButterKnife.bind(this, view);

        initUI();
        loadAttrs(attrs);
    }

    private void initUI() {
        recyclerAdapter = new CalendarAdapter();

        monthRecycler.setAdapter(recyclerAdapter);
    }

    private void loadAttrs(AttributeSet attrs) {
        if (attrs == null)
            return;

        //add style
    }

    public void setMonthCallBack(MonthCallBack monthCallBack) {
        recyclerAdapter.setMonthCallBack(monthCallBack);
    }

    public void nextMonth() {
        recyclerAdapter.nextMonth();
    }

    public void previousMonth() {
        recyclerAdapter.previousMonth();
    }

    public void setSelectDayEnd(boolean b) {
        recyclerAdapter.setSelectDayEnd(b);
    }

    public void setSelectDayStart(boolean b) {
        recyclerAdapter.setSelectDayStart(b);
    }

    public void goTodayMonth() {
        recyclerAdapter.goTodayMonth();
    }

    public void resetSelectedDays() {
        recyclerAdapter.resetSelectedDays();
    }

    public void replaceEvents(List<ModelEvent> modelEvents) {
        recyclerAdapter.replaceEvents(modelEvents);
    }

    public Single<List<ModelEvent>> getEventsInclusiveBetween(LocalDate dateStart, LocalDate dateEnd) {
        return recyclerAdapter.getEventsInclusiveBetween(dateStart, dateEnd);
    }
}