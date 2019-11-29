package com.application.arenda.CustomComponents.Panels.BottomNavigation;

import android.view.View;

import com.application.arenda.CustomComponents.ContainerFragments.ComponentLinkManager;
import com.application.arenda.CustomComponents.DrawPanel;

interface BottomNavigation {
    void stylePanel(DrawPanel drawPanel, View view);
    void hidePanel();
    void expandPanel();
    void styleItems();
    void itemListener(ComponentLinkManager componentLinkManager);
}