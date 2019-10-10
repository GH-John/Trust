package com.application.trust.CustomComponents.Panels.ActionBar;

import android.view.ViewGroup;

public interface AdapterActionBar {
    int getIdPatternResource();
    void initializeItems(ViewGroup viewGroup);
    void initializeItemsListener(ViewGroup viewGroup);
}