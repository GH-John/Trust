package com.application.trust.Activities;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.application.trust.R;
import com.application.trust.StructureProject.Panels.ActionBar.ActionBarView;
import com.application.trust.StructureProject.Panels.BottomNavigation.BottomNavigationView;
import com.application.trust.StructureProject.Panels.SideBar.SideBarView;

public class ActivityMain extends AppCompatActivity {

    private BottomNavigationView customBottomNavigation;
    private ActionBarView customActionBar;
    private SideBarView customSideBar;
    private Fragment containerContentDisplay;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        customBottomNavigation = findViewById(R.id.customBottomNavigation);
        customActionBar = findViewById(R.id.customActionBar);
        customSideBar = findViewById(R.id.customSideBar);

        customBottomNavigation.startListenerBottomNavigation(this, R.id.containerContentDisplay);
        customActionBar.startListenerActionBar(this, R.id.containerContentDisplay);
        customSideBar.startListenerSideBar(this, R.id.containerContentDisplay);
    }
}