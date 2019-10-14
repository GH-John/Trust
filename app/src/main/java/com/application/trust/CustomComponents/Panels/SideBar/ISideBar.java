package com.application.trust.CustomComponents.Panels.SideBar;

import android.content.Context;

import com.application.trust.CustomComponents.Panels.DrawPanel;
import com.application.trust.CustomComponents.Panels.SideBar.ItemList.AdapterItemList;

interface ISideBar {
    void stylePanelSideBar(Context context, DrawPanel drawPanel);
    void stylePanelItemList(Context context, DrawPanel drawPanel);
    void styleItemSideBar(Context context);
    void setAdapterItemList(Context context, AdapterItemList adapter);
    void startPosition();
    void initializeListeners();
}