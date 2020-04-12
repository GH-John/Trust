package com.application.arenda.UI.ContainerImg;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public interface Container {
    void setAdapter(AdapterContainer adapter);
    ViewGroup getContainer();
    View getInstanceFiller();
    void setInstanceCounter(TextView counter);
    void incrementToCounter();
    void decrementToCounter();
    boolean addToContainer(final View view);
    void removeFromContainer(final View v);
    void clearContainer();

    int CURRENT_SIZE_CONTAINER();
    int MAX_SIZE_CONTAINER();
}