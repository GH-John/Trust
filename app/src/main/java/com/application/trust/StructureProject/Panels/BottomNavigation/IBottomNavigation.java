package com.application.trust.StructureProject.Panels.BottomNavigation;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import com.application.trust.StructureProject.Panels.DrawPanel;

interface IBottomNavigation {
    void stylePanelBottomNavigation(Context context, DrawPanel drawPanel);
    void styleHideBottomNavigation(Context context);
    void styleItemBottomNavigation(Context context);
    void startListenerBottomNavigation(AppCompatActivity activity, int idContainerContent);
}