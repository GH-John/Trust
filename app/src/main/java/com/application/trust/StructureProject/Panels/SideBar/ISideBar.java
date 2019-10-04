package com.application.trust.StructureProject.Panels.SideBar;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import com.application.trust.StructureProject.Panels.DrawPanel;
import com.application.trust.StructureProject.Panels.SideBar.ItemList.ItemListAdapter;

interface ISideBar {
    void stylePanelSideBar(Context context, DrawPanel drawPanel);
    void stylePanelItemList(Context context, DrawPanel drawPanel);
    void styleItemSideBar(Context context);
    void setAdapterItemList(Context context, ItemListAdapter adapter);
    void startListenerSideBar(AppCompatActivity activity, int idContainerContent);
}