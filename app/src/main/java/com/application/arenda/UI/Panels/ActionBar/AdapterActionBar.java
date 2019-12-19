package com.application.arenda.UI.Panels.ActionBar;

import android.view.ViewGroup;

public interface AdapterActionBar {
    int getIdPatternResource();
    void initializationComponentsActionBar(ViewGroup viewGroup);
    void initializationListenersActionBar(ViewGroup viewGroup);
}