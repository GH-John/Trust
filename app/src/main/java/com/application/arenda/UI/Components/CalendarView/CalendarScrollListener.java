package com.application.arenda.UI.Components.CalendarView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.application.arenda.Entities.RecyclerView.ScrollCallBack;

public class CalendarScrollListener extends RecyclerView.OnScrollListener {
    private ScrollCallBack scrollCallBack;
    private LinearLayoutManager layoutManager;

    private int totalItems = 0;
    private int lastVisibleItem = 0;
    private int firstVisibleItem = 0;
    private int currentVisibleItems = 0;

    public CalendarScrollListener(LinearLayoutManager linearLayoutManager) {
        this.layoutManager = linearLayoutManager;
    }

    public void setScrollCallBack(ScrollCallBack scrollCallBack) {
        this.scrollCallBack = scrollCallBack;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        currentVisibleItems = layoutManager.getChildCount();
        totalItems = layoutManager.getItemCount();

        firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
        lastVisibleItem = layoutManager.findLastVisibleItemPosition();

        if (scrollCallBack != null)
            scrollCallBack.onScrolled(currentVisibleItems, firstVisibleItem, lastVisibleItem, totalItems, dx, dy);
    }

    @Override
    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//        SCROLL_STATE_IDLE: scrolling is not performed.
//        SCROLL_STATE_DRAGGING: the user drags a finger across the screen (or is executed programmatically).
//        SCROLL_STATE_SETTLING: user raised finger and animation now slows down

        if (scrollCallBack != null)
            scrollCallBack.onScrollState(newState, currentVisibleItems, firstVisibleItem, totalItems);

        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            if (scrollCallBack != null && firstVisibleItem == 0)
                scrollCallBack.onScrolledToStart();

            if (scrollCallBack != null)
                scrollCallBack.currentPosition(firstVisibleItem);

            if (scrollCallBack != null && lastVisibleItem == totalItems - 1)
                scrollCallBack.onScrolledToEnd();
        }
    }
}