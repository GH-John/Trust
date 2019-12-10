package com.application.arenda.CustomComponents.ContainerFragments;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.application.arenda.Patterns.AdapterManager;
import com.application.arenda.Patterns.Observable;
import com.application.arenda.Patterns.Observer;
import com.application.arenda.Patterns.ObserverManager;
import com.application.arenda.R;

public class ContainerFragments implements Observer, Observable, AdapterManager {
    private int idContainer;
    private Context context;
    private Fragment containerFragments;

    private ObserverManager<Observable, Observer> observerManager;
    private ComponentLinkManager<Fragment, View, AdapterLink> componentLinkManager;

    public ContainerFragments(Context context) {
        this.context = context;
        inflateContainer(context);
    }

    private void inflateContainer(Context context) {
        idContainer = R.id.containerFragments;
        containerFragments = ((AppCompatActivity) context).getSupportFragmentManager().findFragmentById(idContainer);
    }

    @Override
    public void setManagers(ObserverManager observerManager, ComponentLinkManager componentLinkManager) {
        this.observerManager = observerManager;
        this.componentLinkManager = componentLinkManager;
        notifyObservers(containerFragments);
    }

    @Override
    public void notifyObservers(Object object) {
        observerManager.notifyObservers(this, (Fragment) object);
    }

    private void changeFragmentContainer(Fragment fragment) {
        containerFragments = ((AppCompatActivity) context).getSupportFragmentManager().findFragmentById(idContainer);
        if (!containerFragments.equals(fragment)) {
            ((AppCompatActivity) context).getSupportFragmentManager()
                    .beginTransaction()
                    .replace(idContainer, fragment)
                    .addToBackStack(String.valueOf(fragment.getClass()))
                    .commit();
        }
    }

    @Override
    public void update(@NonNull Object object) {
        changeFragmentContainer((Fragment) object);
        notifyObservers(object);
    }
}