package com.application.arenda.CustomComponents.DropDownList;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.application.arenda.ServerInteraction.AddAnnouncement.InflateCategories.AdapterDropDownList;

import java.util.Collection;

public interface IDropDownList {
    void setTitle(String title);

    void setDefaultTitle(String defaultTitle);

    void setAdapter(RecyclerView.Adapter adapter);

    void hideList();

    void expandList();

    View getProgressBar();

    void clearRecyclerView(AdapterDropDownList adapter);

    void refreshCollection(Collection collection);

    void pushToBackStack(Collection collection);

    Collection popToBackStack();

    boolean isEmptyToBackStack();

    boolean isContainsToBackStack(Collection collection);

    int CURRENT_SIZE_STACK();

    int MAX_SIZE_STACK();
}