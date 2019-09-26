package com.application.trust.StructureProject.Panels.BottomNavigation;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

interface IBottomNavigation {
    void stylePanelBottomNavigation(Context context, PanelBottomNavigation panelBottomNavigation);
    void styleHideBottomNavigation(Context context);
    void styleItemBottomNavigation(Context context);
    void startListenerBottomNavigation(AppCompatActivity activity, int idContainerContent);
}