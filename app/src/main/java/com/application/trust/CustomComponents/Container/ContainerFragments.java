package com.application.trust.CustomComponents.Container;

import android.content.Context;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.application.trust.Patterns.Observable;
import com.application.trust.Patterns.Observer;
import com.application.trust.Patterns.ObserverManager;
import com.application.trust.R;

public class ContainerFragments implements Observer, Observable {
    private int idContainer;
    private Context context;
    private Fragment container;

    private ObserverManager<Observable, Observer> observerManager;
    private ManagerFragmentLinks<Fragment, View, FragmentLink> managerFragmentLinks;

    public ContainerFragments(Context context) {
        this.context = context;
        inflateContainer(context);
    }

    private void inflateContainer(Context context) {
        idContainer = R.id.containerFragments;
        container = ((AppCompatActivity)context).getSupportFragmentManager().findFragmentById(idContainer);
    }

    @Override
    public void setManagers(ObserverManager observerManager, ManagerFragmentLinks managerFragmentLinks) {
        this.observerManager = observerManager;
        this.managerFragmentLinks = managerFragmentLinks;
    }

    @Override
    public void notifyObservers(Fragment fragment) {
        observerManager.notifyObservers(this, fragment);
    }

    private void changeFragmentContainer(Fragment fragment) {
        container = ((AppCompatActivity)context).getSupportFragmentManager().findFragmentById(idContainer);
        if(!container.equals(fragment)) {
            ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction()
                    .replace(idContainer, fragment)
                    .addToBackStack(String.valueOf(fragment.getClass()))
                    .commit();
        }
    }

    @Override
    public void update(Fragment fragment) {
        changeFragmentContainer(fragment);
        notifyObservers(fragment);
    }
}