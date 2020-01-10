package com.application.arenda.UI.Components.BottomNavigation;

import android.view.View;

import com.application.arenda.UI.DrawPanel;

interface BottomNavigation {
    void stylePanel(DrawPanel drawPanel, View view);
    void styleItems();
    void itemListener();
}