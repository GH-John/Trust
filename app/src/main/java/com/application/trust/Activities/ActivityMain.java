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

public class ActivityMain extends AppCompatActivity{

    private CustomBottomNavigation customBottomNavigation;
    private CustomActionBar customActionBar;
    private CustomContainer customContainer;
    private CustomSideBar customSideBar;

    private FragmentAllAnnouncements fragmentAllAnnouncements;
    private FragmentUserAnnouncements fragmentUserAnnouncements;
    private FragmentAddAnnouncement fragmentAddAnnouncement;
    private FragmentUserProposals fragmentUserProposals;
    private FragmentUserStatistics fragmentUserStatistics;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @SuppressLint({"NewApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inflate();

        ObserverManager<Observable, Observer> observerManager = new ObserverManager<>();
        observerManager.addObserver(customContainer, customActionBar);
        customContainer.addFragments(fragmentAllAnnouncements,
                                        fragmentUserAnnouncements,
                                        fragmentAddAnnouncement,
                                        fragmentUserProposals,
                                        fragmentUserStatistics);
        customContainer.setManager(observerManager);
    }

    private void inflate(){
        customBottomNavigation = findViewById(R.id.customBottomNavigation);
        customActionBar = findViewById(R.id.customActionBar);
        customContainer = findViewById(R.id.customContainer);
        customSideBar = findViewById(R.id.customSideBar);

        fragmentAllAnnouncements = new FragmentAllAnnouncements();
        fragmentUserAnnouncements = new FragmentUserAnnouncements();
        fragmentAddAnnouncement = new FragmentAddAnnouncement();
        fragmentUserProposals = new FragmentUserProposals();
        fragmentUserStatistics = new FragmentUserStatistics();
    }
}