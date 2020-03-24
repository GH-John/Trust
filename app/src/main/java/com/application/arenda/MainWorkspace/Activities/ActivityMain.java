package com.application.arenda.MainWorkspace.Activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.application.arenda.R;
import com.application.arenda.UI.Components.ActionBar.CustomActionBar;
import com.application.arenda.UI.Components.BottomNavigation.CustomBottomNavigation;
import com.application.arenda.UI.Components.ComponentManager;
import com.application.arenda.UI.Components.ContainerFragments.ContainerFragments;
import com.application.arenda.UI.Components.SideBar.ContainerDrawerLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityMain extends AppCompatActivity {
    @Nullable
    @BindView(R.id.customActionBar)
    CustomActionBar customActionBar;

    @Nullable
    @BindView(R.id.customBottomNavigation)
    CustomBottomNavigation customBottomNavigation;

    private ContainerFragments containerFragments;
    private ContainerDrawerLayout containerDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        init();

        initializationManager();
    }

    private void init() {
        containerFragments = ContainerFragments.getInstance();
        containerDrawerLayout = ContainerDrawerLayout.getInstance(this);
    }

    private void initializationManager() {
        ComponentManager.addLink(containerFragments, customBottomNavigation, customActionBar, containerDrawerLayout);

        containerFragments.start(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        containerFragments.notifyObservers(containerFragments.getCurrentFragment(this));
    }
}