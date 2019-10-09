package com.application.trust.CustomComponents.Panels.SideBar.Btn;

import android.content.Context;

import com.application.trust.CustomComponents.Panels.DrawPanel;

interface IBtnAdd {
    void stylePanel(Context context, DrawPanel drawPanel);
    void styleItem(Context context);
    void startListener(int idContainerContent);
}