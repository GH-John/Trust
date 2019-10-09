package com.application.trust.CustomComponents.Panels.ActionBar;

import android.content.Context;

import com.application.trust.CustomComponents.Panels.DrawPanel;

interface IActionBar {
    void stylePanel(Context context, DrawPanel drawPanel);
    void styleItem(Context context);
    void startListener(int idContainerContent);
}