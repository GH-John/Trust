package com.application.arenda.ui.widgets.actionBar;

import android.view.ViewGroup;

public interface AdapterActionBar {
    int getIdPatternResource();
    void initComponentsActionBar(ViewGroup viewGroup);
    void initListenersActionBar(ViewGroup viewGroup);
}