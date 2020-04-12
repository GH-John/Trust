package com.application.arenda.Entities.RecyclerView;

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

        if (scrollCallBack != null && firstVisibleItem == 0)
            scrollCallBack.onScrolledToStart();

        if (scrollCallBack != null)
            scrollCallBack.onScrolled(currentVisibleItems, firstVisibleItem, totalItems);

        if (!rvAdapter.isLoading() && (currentVisibleItems + firstVisibleItem) >= totalItems - countIgnoreItem && firstVisibleItem >= 0) {
            System.out.println("Status: " + currentVisibleItems + " + " + firstVisibleItem + " >= " + (totalItems - countIgnoreItem) + " && " + firstVisibleItem + ">= 0");
            System.out.println("LastItemID: " + rvAdapter.getLastItem().getObjectId());

            loadMoreData.loadMore(rvAdapter.getLastItem().getObjectId());
        }

        if (scrollCallBack != null && lastVisibleItem == totalItems)
            scrollCallBack.onScrolledToEnd();
    }

    public interface ScrollCallBack {
        void onScrolledToStart();

        void onScrolled(int currentVisibleItems, int firstVisibleItem, int totalItems);

        void onScrolledToEnd();
    }
}