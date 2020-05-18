package com.application.arenda.entities.recyclerView;

public interface ScrollCallBack {
    void onScrolledToStart();

    void onScrolled(int currentVisibleItems, int firstVisibleItem, int lastVisibleItem, int totalItems, int dx, int dy);

    void onScrolledToEnd();

    void onScrollState(int state, int currentVisibleItems, int firstVisibleItem, int lastVisibleItem, int totalItems);

    void onScrolledToCurrentPosition(int currentPos);
}