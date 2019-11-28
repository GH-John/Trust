package com.application.arenda.CustomComponents.DropDownList;

import android.view.View;

import java.util.Collection;

public interface IDropDownList {
    void hideList();

    void expandList();

    View getProgressBar();

    void refreshList(Collection collection);
}