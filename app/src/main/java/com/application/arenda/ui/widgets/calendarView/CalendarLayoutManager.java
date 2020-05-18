package com.application.arenda.ui.widgets.calendarView;

import android.content.Context;
import android.util.DisplayMetrics;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

public class CalendarLayoutManager extends LinearLayoutManager {
    private static final float MILLISECONDS_PER_INCH = 100f;

    public CalendarLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    @Override
    public void smoothScrollToPosition(
            RecyclerView recyclerView, RecyclerView.State state, int position) {
        final LinearSmoothScroller linearSmoothScroller =
                new LinearSmoothScroller(recyclerView.getContext()) {

                    @Override
                    protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                        return MILLISECONDS_PER_INCH / displayMetrics.densityDpi;
                    }
                };
        linearSmoothScroller.setTargetPosition(position);
        startSmoothScroll(linearSmoothScroller);
    }
}