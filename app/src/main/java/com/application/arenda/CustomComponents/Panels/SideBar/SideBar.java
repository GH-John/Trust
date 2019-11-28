package com.application.arenda.CustomComponents.Panels.SideBar;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;

import com.application.arenda.CustomComponents.DrawPanel;
import com.application.arenda.CustomComponents.Panels.SideBar.ItemList.AdapterItemList;

public interface SideBar {
    void initializationListeners();
    void styleItemSideBar(Context context);
    void stylePanelSideBar(Context context, DrawPanel drawPanel);
    void stylePanelItemList(Context context, DrawPanel drawPanel);
    void setAdapterItemList(Context context, AdapterItemList adapter);
    void hide();
    void expand();
    void startPosition();
    void swipeListener(View view, MotionEvent event);
}