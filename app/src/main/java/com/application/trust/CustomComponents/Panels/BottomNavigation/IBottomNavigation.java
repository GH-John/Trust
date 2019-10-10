package com.application.trust.CustomComponents.Panels.BottomNavigation;

import com.application.trust.CustomComponents.Container.ManagerFragmentLinks;
import com.application.trust.CustomComponents.Panels.DrawPanel;

interface IBottomNavigation {
    void stylePanel(DrawPanel drawPanel);
    void hidePanel();
    void expandPanel();
    void styleItems();
    void itemListener(ManagerFragmentLinks managerFragmentLinks);
}