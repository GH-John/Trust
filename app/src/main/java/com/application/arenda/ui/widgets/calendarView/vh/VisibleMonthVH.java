package com.application.arenda.ui.widgets.calendarView.vh;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;

import com.application.arenda.R;
import com.application.arenda.ui.widgets.calendarView.models.KeyMonth;
import com.application.arenda.ui.widgets.calendarView.vh.DayVH.DayItemOnClickListener;
import com.application.arenda.ui.widgets.calendarView.models.ModelMonth;
import com.application.arenda.ui.widgets.calendarView.models.ModelVisibleMonths;

import org.threeten.bp.LocalDate;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VisibleMonthVH extends BaseMonthVH {
    @BindView(R.id.month_grid_container)
    GridView month;

    private ModelVisibleMonths monthItem;

    private VisibleMonthVH(@NonNull View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
//
//        Animation animation = AnimationUtils.loadAnimation(itemView.getContext(), R.anim.calendar_day_anim);
//        GridLayoutAnimationController controller = new GridLayoutAnimationController(animation, 0.03f, 0.01f);
//        month.setLayoutAnimation(controller);
    }

    public static VisibleMonthVH create(ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.calendar_vh_month, parent, false);

        return new VisibleMonthVH(view);
    }

    public ModelVisibleMonths getMonthItem() {
        return monthItem;
    }

    @Override
    public int getResourceLayoutId() {
        return R.layout.calendar_vh_month;
    }

    @Override
    public void onBind(ModelMonth model, LocalDate selectedDayStart, LocalDate selectedDayEnd) {
        monthItem = (ModelVisibleMonths) model;

        monthItem.getDayAdapter().onBindAdapter(monthItem.getItemList(), monthItem);

        month.setAdapter(monthItem.getDayAdapter());
    }

    public KeyMonth getKeyMonth() {
        return monthItem.getKeyMainMonth();
    }

    public void setDayItemClick(DayItemOnClickListener listener) {
        if (listener == null)
            return;

        monthItem.getDayAdapter().setDayItemClickListener(listener);
    }
}