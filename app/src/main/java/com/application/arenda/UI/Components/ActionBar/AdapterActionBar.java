package com.application.arenda.UI.Components.ActionBar;

import android.view.ViewGroup;

public interface AdapterActionBar {
    int getIdPatternResource();
    void initComponentsActionBar(ViewGroup viewGroup);
    void initListenersActionBar(ViewGroup viewGroup);
}