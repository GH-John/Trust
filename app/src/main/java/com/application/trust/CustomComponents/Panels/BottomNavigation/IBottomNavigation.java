package com.application.trust.CustomComponents.Panels.BottomNavigation;

import android.content.Context;

import com.application.trust.CustomComponents.Panels.DrawPanel;

interface IBottomNavigation {
    void stylePanel(Context context, DrawPanel drawPanel);
    void styleHide(Context context);
    void styleItem(Context context);
    void startListener(int idContainerContent);
}