package com.application.arenda.Entities.RecyclerView;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RVLayoutManager extends LinearLayoutManager {
    private float MANUAL_SCROLL_SLOW_RATIO = 2f;

    private RecyclerView recyclerView;

    public RVLayoutManager(Context context) {
        super(context);
    }

    public RVLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public RVLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

//    @Override
//    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
//        int prevDelta = dy;
//        if (recyclerView.getScrollState() == SCROLL_STATE_DRAGGING)
//            dy = (int)(dy > 0 ? Math.max(dy * MANUAL_SCROLL_SLOW_RATIO, 1) : Math.min(dy * MANUAL_SCROLL_SLOW_RATIO, -1));
//
//        if (recyclerView.getScrollState() == SCROLL_STATE_DRAGGING)
//            dy = prevDelta;
//
//        return dy;
//    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }
}