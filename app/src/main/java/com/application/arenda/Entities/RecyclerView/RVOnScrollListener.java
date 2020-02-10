package com.application.arenda.Entities.RecyclerView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RVOnScrollListener extends RecyclerView.OnScrollListener {
    private LoadMoreData loadMoreData;
    private LinearLayoutManager layoutManager;
    private RVAdapter rvAdapter;

    private int currentVisibleItems = 0;
    private int totalItems = 0;
    private int firstVisibleItem = 0;

    private int lastVisibleItem;

    public RVOnScrollListener(LinearLayoutManager linearLayoutManager) {
        this.layoutManager = linearLayoutManager;
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

//        if (!rvAdapter.isLoading() && (currentVisibleItems + firstVisibleItem <= totalItems)) {
        if (!rvAdapter.isLoading() && (currentVisibleItems - lastVisibleItem == 1)) {
            loadMoreData.loadMore(rvAdapter.getLastItem().getIdAnnouncement());
        }
    }
}