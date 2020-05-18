package com.application.arenda.entities.recyclerView;

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

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }
}