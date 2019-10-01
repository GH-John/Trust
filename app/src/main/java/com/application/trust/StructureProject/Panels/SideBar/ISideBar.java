package com.application.trust.StructureProject.Panels.SideBar;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import com.application.trust.StructureProject.Panels.DrawPanel;

interface ISideBar {
    void stylePanelSideBar(Context context, DrawPanel drawPanel);
    void styleItemSideBar(Context context);
    void startListenerSideBar(AppCompatActivity activity, int idContainerContent);
}