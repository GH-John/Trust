package com.application.arenda.ui.widgets.bottomNavigation;

import android.view.View;

import com.application.arenda.ui.DrawPanel;

interface BottomNavigation {
    void stylePanel(DrawPanel drawPanel, View view);
    void styleItems();
    void itemListener();
}