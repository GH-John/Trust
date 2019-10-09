package com.application.trust.CustomComponents.Panels.SideBar.ItemList;

import androidx.fragment.app.Fragment;

class ContentItemList {
    private int idNamePanel;
    private int idIconPanel;
    private PanelItemList stylePanel;
    private Fragment fragment;

    public ContentItemList(Fragment fragment, int idNamePanel, int idIconPanel, PanelItemList stylePanel) {
        this.fragment = fragment;
        this.idNamePanel = idNamePanel;
        this.idIconPanel = idIconPanel;
        this.stylePanel = stylePanel;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public int getIdIconPanel() {
        return idIconPanel;
    }

    public PanelItemList getStylePanel() {
        return stylePanel;
    }

    public int getIdNamePanel() {
        return idNamePanel;
    }
}