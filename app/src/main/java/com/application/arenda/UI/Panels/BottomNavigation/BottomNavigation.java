package com.application.arenda.UI.Panels.BottomNavigation;

import android.view.View;

import com.application.arenda.UI.ContainerFragments.ComponentLinkManager;
import com.application.arenda.UI.DrawPanel;

interface BottomNavigation {
    void stylePanel(DrawPanel drawPanel, View view);
    void hidePanel();
    void expandPanel();
    void styleItems();
    void itemListener(ComponentLinkManager componentLinkManager);
}