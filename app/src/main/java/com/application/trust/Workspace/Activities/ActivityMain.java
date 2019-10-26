package com.application.trust.Workspace.Activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.application.trust.CustomComponents.Container.ComponentLinkManager;
import com.application.trust.CustomComponents.Container.ContainerFragments;
import com.application.trust.CustomComponents.Container.FragmentLink;
import com.application.trust.CustomComponents.Panels.ActionBar.CustomActionBar;
import com.application.trust.CustomComponents.Panels.BottomNavigation.CustomBottomNavigation;
import com.application.trust.CustomComponents.Panels.SideBar.CustomSideBar;
import com.application.trust.Workspace.Fragments.FragmentAddAnnouncement;
import com.application.trust.Workspace.Fragments.FragmentAllAnnouncements;
import com.application.trust.Workspace.Fragments.FragmentUserAnnouncements;
import com.application.trust.Workspace.Fragments.FragmentUserProposals;
import com.application.trust.Workspace.Fragments.FragmentUserStatistics;
import com.application.trust.Patterns.Observable;
import com.application.trust.Patterns.Observer;
import com.application.trust.Patterns.ObserverManager;
import com.application.trust.R;

public class ActivityMain extends AppCompatActivity {

    private CustomSideBar customSideBar;
    private CustomActionBar customActionBar;
    private ContainerFragments containerFragments;
    private CustomBottomNavigation customBottomNavigation;

    private FragmentUserProposals fragmentUserProposals;
    private FragmentUserStatistics fragmentUserStatistics;
    private FragmentAddAnnouncement fragmentAddAnnouncement;
    private FragmentAllAnnouncements fragmentAllAnnouncements;
    private FragmentUserAnnouncements fragmentUserAnnouncements;

    private ObserverManager<Observable, Observer> observerManager;
    private ComponentLinkManager<Fragment, View, FragmentLink> componentLinkManager;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @SuppressLint({"NewApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeComponents();
        initializeFragments();
        initializeManagers();

        setManagersComponents();
    }

    private void initializeComponents() {
        customSideBar = findViewById(R.id.customSideBar);
        customActionBar = findViewById(R.id.customActionBar);
        containerFragments = new ContainerFragments(this);
        customBottomNavigation = findViewById(R.id.customBottomNavigation);
    }

    private void initializeFragments() {
        fragmentUserProposals = new FragmentUserProposals();
        fragmentUserStatistics = new FragmentUserStatistics();
        fragmentAddAnnouncement = new FragmentAddAnnouncement();
        fragmentAllAnnouncements = new FragmentAllAnnouncements();
        fragmentUserAnnouncements = new FragmentUserAnnouncements();
    }

    private void initializeManagers() {
        observerManager = new ObserverManager<>();
        observerManager.addObserver(containerFragments, customActionBar, customSideBar);
        observerManager.addObserver(customBottomNavigation, containerFragments);

        componentLinkManager = new ComponentLinkManager<>();

        componentLinkManager.addComponentLinks(customBottomNavigation,
                fragmentUserAnnouncements,
                fragmentAllAnnouncements,
                fragmentAddAnnouncement,
                fragmentUserStatistics,
                fragmentUserProposals);

        componentLinkManager.addComponentLinks(customActionBar,
                fragmentUserAnnouncements,
                fragmentAllAnnouncements,
                fragmentAddAnnouncement,
                fragmentUserStatistics,
                fragmentUserProposals);
    }

    private void setManagersComponents() {
        containerFragments.setManagers(observerManager, componentLinkManager);

        customBottomNavigation.setManagers(observerManager, componentLinkManager);
    }
}