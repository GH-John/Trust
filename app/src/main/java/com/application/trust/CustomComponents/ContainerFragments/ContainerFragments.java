package com.application.trust.CustomComponents.ContainerFragments;

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
    private Fragment containerFragments;

    private ObserverManager<Observable, Observer> observerManager;
    private ComponentLinkManager<Fragment, View, FragmentLink> componentLinkManager;

    public ContainerFragments(Context context) {
        this.context = context;
        inflateContainer(context);
    }

    private void inflateContainer(Context context) {
        idContainer = R.id.containerFragments;
        containerFragments = ((AppCompatActivity)context).getSupportFragmentManager().findFragmentById(idContainer);
    }

    @Override
    public void setManagers(ObserverManager observerManager, ComponentLinkManager componentLinkManager) {
        this.observerManager = observerManager;
        this.componentLinkManager = componentLinkManager;
        notifyObservers(containerFragments);
    }

    @Override
    public void notifyObservers(Fragment fragment) {
        observerManager.notifyObservers(this, fragment);
    }

    private void changeFragmentContainer(Fragment fragment) {
        containerFragments = ((AppCompatActivity)context).getSupportFragmentManager().findFragmentById(idContainer);
        if(!containerFragments.equals(fragment)) {
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