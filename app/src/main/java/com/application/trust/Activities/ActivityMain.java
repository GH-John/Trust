package com.application.trust.Activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.application.trust.CustomComponents.Container.ContainerFragments;
import com.application.trust.CustomComponents.Container.FragmentLink;
import com.application.trust.CustomComponents.Container.ManagerFragmentLinks;
import com.application.trust.CustomComponents.Panels.ActionBar.CustomActionBar;
import com.application.trust.CustomComponents.Panels.BottomNavigation.CustomBottomNavigation;
import com.application.trust.CustomComponents.Panels.SideBar.CustomSideBar;
import com.application.trust.Fragments.FragmentAddAnnouncement;
import com.application.trust.Fragments.FragmentAllAnnouncements;
import com.application.trust.Fragments.FragmentUserAnnouncements;
import com.application.trust.Fragments.FragmentUserProposals;
import com.application.trust.Fragments.FragmentUserStatistics;
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
    private ManagerFragmentLinks<Fragment, View, FragmentLink> managerFragmentLinks;

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
        observerManager.addObserver(containerFragments, customActionBar);
        observerManager.addObserver(customBottomNavigation, containerFragments);
        observerManager.addObserver(containerFragments,customSideBar);

        managerFragmentLinks = new ManagerFragmentLinks<>();

        managerFragmentLinks.addComponentLinks(customBottomNavigation,
                fragmentUserAnnouncements,
                fragmentAllAnnouncements,
                fragmentAddAnnouncement,
                fragmentUserStatistics,
                fragmentUserProposals);

        managerFragmentLinks.addComponentLinks(customActionBar,
                fragmentUserAnnouncements,
                fragmentAllAnnouncements,
                fragmentAddAnnouncement,
                fragmentUserStatistics,
                fragmentUserProposals);
    }

    private void setManagersComponents() {
        containerFragments.setManagers(observerManager, managerFragmentLinks);

        customBottomNavigation.setManagers(observerManager, managerFragmentLinks);
    }
}