package com.application.arenda.MainWorkspace.Activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.application.arenda.UI.ContainerFragments.ComponentLinkManager;
import com.application.arenda.UI.ContainerFragments.ContainerFragments;
import com.application.arenda.UI.ContainerFragments.AdapterLink;
import com.application.arenda.UI.Panels.ActionBar.CustomActionBar;
import com.application.arenda.UI.Panels.BottomNavigation.CustomBottomNavigation;
import com.application.arenda.UI.Panels.SideBar.CustomSideBar;
import com.application.arenda.MainWorkspace.Fragments.FragmentInsertAnnouncement;
import com.application.arenda.MainWorkspace.Fragments.FragmentAllAnnouncements;
import com.application.arenda.MainWorkspace.Fragments.FragmentUserAnnouncements;
import com.application.arenda.MainWorkspace.Fragments.FragmentUserProposals;
import com.application.arenda.MainWorkspace.Fragments.FragmentUserStatistics;
import com.application.arenda.Patterns.Observable;
import com.application.arenda.Patterns.Observer;
import com.application.arenda.Patterns.ObserverManager;
import com.application.arenda.R;

public class ActivityMain extends AppCompatActivity {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private CustomSideBar customSideBar;
    private CustomActionBar customActionBar;
    private ContainerFragments containerFragments;
    private CustomBottomNavigation customBottomNavigation;
    private FragmentUserProposals fragmentUserProposals;
    private FragmentUserStatistics fragmentUserStatistics;
    private FragmentInsertAnnouncement fragmentInsertAnnouncement;
    private FragmentAllAnnouncements fragmentAllAnnouncements;
    private FragmentUserAnnouncements fragmentUserAnnouncements;
    private ObserverManager<Observable, Observer> observerManager;
    private ComponentLinkManager<Fragment, View, AdapterLink> componentLinkManager;

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
        fragmentInsertAnnouncement = new FragmentInsertAnnouncement();
        fragmentAllAnnouncements = new FragmentAllAnnouncements();
        fragmentUserAnnouncements = new FragmentUserAnnouncements();
    }

    private void initializationManagers() {
        observerManager = new ObserverManager<>();
        observerManager.addObserver(containerFragments, customActionBar, customSideBar);
        observerManager.addObserver(customBottomNavigation, containerFragments);

        componentLinkManager = new ComponentLinkManager<>();

        componentLinkManager.addLinks(customBottomNavigation,
                fragmentUserAnnouncements,
                fragmentAllAnnouncements,
                fragmentInsertAnnouncement,
                fragmentUserStatistics,
                fragmentUserProposals);

        componentLinkManager.addLinks(customActionBar,
                fragmentUserAnnouncements,
                fragmentAllAnnouncements,
                fragmentInsertAnnouncement,
                fragmentUserStatistics,
                fragmentUserProposals);
    }

    private void setManagersComponents() {
        containerFragments.setManagers(observerManager, componentLinkManager);

        customBottomNavigation.setManagers(observerManager, componentLinkManager);
    }
}