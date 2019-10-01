package com.application.trust.StructureProject.Panels.ActionBar;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import com.application.trust.StructureProject.Panels.DrawPanel;

interface IActionBar {
    void stylePanelActionBar(Context context, DrawPanel drawPanel);
    void styleItemActionBar(Context context);
    void startListenerActionBar(AppCompatActivity activity, int idContainerContent);
}