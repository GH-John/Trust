package com.application.arenda.UI.Components.SideBar;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;

import com.application.arenda.UI.DrawPanel;
import com.application.arenda.UI.Components.SideBar.ItemList.AdapterItemList;

public interface SideBar {
    void initListeners();
    void styleItemSideBar(Context context);
    void stylePanelSideBar(Context context, DrawPanel drawPanel);
    void stylePanelItemList(Context context, DrawPanel drawPanel);
    void setAdapterItemList(Context context, AdapterItemList adapter);
    void hide();
    void expand();
    void startPosition();
    void swipeListener(View view, MotionEvent event);
}