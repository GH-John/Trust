package com.application.arenda.CustomComponents.Panels.SideBar;

import android.view.MotionEvent;
import android.view.View;

public interface AdapterSideBar extends View.OnTouchListener {
    void setSideBar(SideBar sideBar);

    @Override
    boolean onTouch(View v, MotionEvent event);
}