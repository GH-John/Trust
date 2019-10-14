package com.application.trust.CustomComponents.Panels.SideBar;

import android.content.Context;
import android.view.MotionEvent;

import com.application.trust.CustomComponents.Panels.DrawPanel;
import com.application.trust.CustomComponents.Panels.SideBar.ItemList.AdapterItemList;

interface ISideBar {
    void initializeListeners();
    void styleItemSideBar(Context context);
    void stylePanelSideBar(Context context, DrawPanel drawPanel);
    void stylePanelItemList(Context context, DrawPanel drawPanel);
    void setAdapterItemList(Context context, AdapterItemList adapter);
    void hide();
    void expand();
    void startPosition();
    void swipeListener(MotionEvent event);
}