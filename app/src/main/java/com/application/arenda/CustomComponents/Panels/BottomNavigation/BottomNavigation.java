package com.application.arenda.CustomComponents.Panels.BottomNavigation;

import com.application.arenda.CustomComponents.ContainerFragments.ComponentLinkManager;
import com.application.arenda.CustomComponents.DrawPanel;

interface BottomNavigation {
    void stylePanel(DrawPanel drawPanel);
    void hidePanel();
    void expandPanel();
    void styleItems();
    void itemListener(ComponentLinkManager componentLinkManager);
}