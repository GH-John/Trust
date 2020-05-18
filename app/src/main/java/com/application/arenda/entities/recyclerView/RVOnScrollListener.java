package com.application.arenda.entities.recyclerView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import timber.log.Timber;

public class RVOnScrollListener extends RecyclerView.OnScrollListener {
    private RVAdapter rvAdapter;
    private LoadMoreData loadMoreData;
    private ScrollCallBack scrollCallBack;
    private LinearLayoutManager layoutManager;


    private int totalItems = 0;
    private int countIgnoreItem = 2;
    private int lastVisibleItem = 0;
    private int firstVisibleItem = 0;
    private int currentVisibleItems = 0;

    public RVOnScrollListener(LinearLayoutManager linearLayoutManager) {
        this.layoutManager = linearLayoutManager;
    }

    public void setScrollCallBack(ScrollCallBack scrollCallBack) {
        this.scrollCallBack = scrollCallBack;
    }

    public void setOnLoadMoreData(LoadMoreData loadMoreData) {
        this.loadMoreData = loadMoreData;
    }

    public void setRVAdapter(RVAdapter adapter) {
        this.rvAdapter = adapter;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        currentVisibleItems = layoutManager.getChildCount();
        totalItems = layoutManager.getItemCount();

        firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
        lastVisibleItem = layoutManager.findLastVisibleItemPosition();

        Timber.tag("isLoading").d(String.valueOf(rvAdapter.isLoading()));

        Timber.tag("LAST_VISIBLE_ITEM").d(String.valueOf(lastVisibleItem));

        if (scrollCallBack != null)
            scrollCallBack.onScrolled(currentVisibleItems, firstVisibleItem, lastVisibleItem, totalItems, dx, dy);

        if (!rvAdapter.isLoading() && (currentVisibleItems + firstVisibleItem) >= totalItems - countIgnoreItem && firstVisibleItem >= 0) {
            System.out.println("Status: " + currentVisibleItems + " + " + firstVisibleItem + " >= " + (totalItems - countIgnoreItem) + " && " + firstVisibleItem + ">= 0");
            System.out.println("LastItemID: " + rvAdapter.getLastItem().getID());

            if (loadMoreData != null)
                loadMoreData.loadMore(rvAdapter.getLastItem().getID());
        }
    }

    @Override
    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//        SCROLL_STATE_IDLE: scrolling is not performed.
//        SCROLL_STATE_DRAGGING: the user drags a finger across the screen (or is executed programmatically).
//        SCROLL_STATE_SETTLING: user raised finger and animation now slows down

        if (scrollCallBack != null)
            scrollCallBack.onScrollState(newState, currentVisibleItems, firstVisibleItem, lastVisibleItem, totalItems);

        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            if (scrollCallBack != null && firstVisibleItem == 0)
                scrollCallBack.onScrolledToStart();

            if (scrollCallBack != null)
                scrollCallBack.onScrolledToCurrentPosition(firstVisibleItem);

            if (scrollCallBack != null && lastVisibleItem == totalItems - 1)
                scrollCallBack.onScrolledToEnd();
        }
    }
}