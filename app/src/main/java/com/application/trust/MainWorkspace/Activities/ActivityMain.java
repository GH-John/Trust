package com.application.trust.MainWorkspace.Activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.application.trust.CustomComponents.ContainerFragments.ComponentLinkManager;
import com.application.trust.CustomComponents.ContainerFragments.ContainerFragments;
import com.application.trust.CustomComponents.ContainerFragments.FragmentLink;
import com.application.trust.CustomComponents.Panels.ActionBar.CustomActionBar;
import com.application.trust.CustomComponents.Panels.BottomNavigation.CustomBottomNavigation;
import com.application.trust.CustomComponents.Panels.SideBar.CustomSideBar;
import com.application.trust.MainWorkspace.Fragments.FragmentAddAnnouncement;
import com.application.trust.MainWorkspace.Fragments.FragmentAllAnnouncements;
import com.application.trust.MainWorkspace.Fragments.FragmentUserAnnouncements;
import com.application.trust.MainWorkspace.Fragments.FragmentUserProposals;
import com.application.trust.MainWorkspace.Fragments.FragmentUserStatistics;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializationComponents();
        initializationFragments();
        initializationManagers();

        setManagersComponents();
    }

    private void initializationComponents() {
        customSideBar = findViewById(R.id.customSideBar);
        customActionBar = findViewById(R.id.customActionBar);
        containerFragments = new ContainerFragments(this);
        customBottomNavigation = findViewById(R.id.customBottomNavigation);
    }

    private void initializationFragments() {
        fragmentUserProposals = new FragmentUserProposals();
        fragmentUserStatistics = new FragmentUserStatistics();
        fragmentAddAnnouncement = new FragmentAddAnnouncement();
        fragmentAllAnnouncements = new FragmentAllAnnouncements();
        fragmentUserAnnouncements = new FragmentUserAnnouncements();
    }

    private void initializationManagers() {
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