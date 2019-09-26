package com.application.trust.StructureProject.Panels.ActionBar;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

interface IActionBar {
    void stylePanelActionBar(Context context, PanelActionBar panelActionBar);
    void styleItemActionBar(Context context);
    void startListenerActionBar(AppCompatActivity activity, int idContainerContent);
}