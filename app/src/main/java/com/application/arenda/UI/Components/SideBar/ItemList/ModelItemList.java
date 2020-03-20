package com.application.arenda.UI.Components.SideBar.ItemList;

import android.graphics.drawable.Drawable;

import androidx.fragment.app.Fragment;

import com.application.arenda.Entities.Announcements.Models.IModel;

class ModelItemList implements IModel {
    private int idNamePanel;
    private int idIconPanel;
    private Drawable stylePanel;
    private Fragment fragment;

    public ModelItemList(Fragment fragment, int idNamePanel, int idIconPanel, Drawable stylePanel) {
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

    @Override
    public void setID(long id) {

    }

    @Override
    public long getID() {
        return 0;
    }
}