package com.application.trust.StructureProject.Panels.SideBar.Btn;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import com.application.trust.StructureProject.Panels.DrawPanel;

interface IBtnAdd {
    void stylePanelBtnAdd(Context context, DrawPanel drawPanel);
    void styleItemBtnAdd(Context context);
    void startListenerBtnAdd(AppCompatActivity activity, int idContainerContent);
}
