package com.application.arenda.Entities.RecyclerView;

public interface ScrollCallBack {
    void onScrolledToStart();

    void onScrolled(int currentVisibleItems, int firstVisibleItem, int totalItems);

    void onScrolledToEnd();

    void onScrollState(int state, int currentVisibleItems, int firstVisibleItem, int totalItems);

    void currentPosition(int currentPos);
}