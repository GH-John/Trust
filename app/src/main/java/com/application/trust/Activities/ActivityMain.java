package com.application.trust.Activities;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.application.trust.CustomComponents.Container.CustomContainer;
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

    private CustomBottomNavigation customBottomNavigation;
    private CustomActionBar customActionBar;
    private CustomContainer customContainer;
    private CustomSideBar customSideBar;

    private FragmentUserAnnouncements fragmentUserAnnouncements;
    private FragmentAllAnnouncements fragmentAllAnnouncements;
    private FragmentAddAnnouncement fragmentAddAnnouncement;
    private FragmentUserStatistics fragmentUserStatistics;
    private FragmentUserProposals fragmentUserProposals;

    private ObserverManager<Observable, Observer> observerManager;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @SuppressLint({"NewApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inflate();

        customContainer.addFragments(fragmentAllAnnouncements,
                fragmentUserAnnouncements,
                fragmentAddAnnouncement,
                fragmentUserStatistics,
                fragmentUserProposals);
        observerManager.addObserver(customContainer, customActionBar);
        customContainer.setManager(observerManager);
    }

    private void inflate() {
        customBottomNavigation = findViewById(R.id.customBottomNavigation);
        customActionBar = findViewById(R.id.customActionBar);
        customContainer = findViewById(R.id.customContainer);
        customSideBar = findViewById(R.id.customSideBar);

        fragmentUserAnnouncements = new FragmentUserAnnouncements();
        fragmentAllAnnouncements = new FragmentAllAnnouncements();
        fragmentAddAnnouncement = new FragmentAddAnnouncement();
        fragmentUserStatistics = new FragmentUserStatistics();
        fragmentUserProposals = new FragmentUserProposals();

        observerManager = new ObserverManager<>();
    }
}