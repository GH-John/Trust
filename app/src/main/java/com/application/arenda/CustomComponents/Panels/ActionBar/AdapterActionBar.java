package com.application.arenda.CustomComponents.Panels.ActionBar;

import android.view.ViewGroup;

public interface AdapterActionBar {
    int getIdPatternResource();
    void initializationComponentsActionBar(ViewGroup viewGroup);
    void initializationListenersActionBar(ViewGroup viewGroup);
}