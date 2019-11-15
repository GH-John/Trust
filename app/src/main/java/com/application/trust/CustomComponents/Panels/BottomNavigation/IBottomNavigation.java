package com.application.trust.CustomComponents.Panels.BottomNavigation;

import com.application.trust.CustomComponents.ContainerFragments.ComponentLinkManager;
import com.application.trust.CustomComponents.Panels.DrawPanel;

interface IBottomNavigation {
    void stylePanel(DrawPanel drawPanel);
    void hidePanel();
    void expandPanel();
    void styleItems();
    void itemListener(ComponentLinkManager componentLinkManager);
}