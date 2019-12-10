package com.application.arenda.CustomComponents.Panels.SideBar.ItemList;

import android.graphics.drawable.Drawable;

import androidx.fragment.app.Fragment;

class ContentItemList {
    private int idNamePanel;
    private int idIconPanel;
    private Drawable stylePanel;
    private Fragment fragment;

    public ContentItemList(Fragment fragment, int idNamePanel, int idIconPanel, Drawable stylePanel) {
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

    public Drawable getStylePanel() {
        return stylePanel;
    }

    public int getIdNamePanel() {
        return idNamePanel;
    }
}