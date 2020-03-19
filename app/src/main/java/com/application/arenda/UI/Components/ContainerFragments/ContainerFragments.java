package com.application.arenda.UI.Components.ContainerFragments;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.application.arenda.R;
import com.application.arenda.UI.Components.ComponentManager;

public final class ContainerFragments implements ComponentManager.Observer, ComponentManager.Observable {
    @SuppressLint("StaticFieldLeak")
    private static ContainerFragments containerFragments;
    private final int idContainer = R.id.containerFragments;
    private boolean isLoad = false;
    private Fragment fragment;

    private ContainerFragments() {
    }

    public static ContainerFragments getInstance() {
        if (containerFragments == null)
            containerFragments = new ContainerFragments();

        return containerFragments;
    }

    public void start(Context context) {
        fragment = getCurrentFragment(context);
        notifyObservers(fragment);
    }

    public Fragment getCurrentFragment(Context context) {
        return ((AppCompatActivity) context).getSupportFragmentManager().findFragmentById(idContainer);
    }

    public void replaceFragmentInContainer(Fragment fragment) {
        this.fragment = getCurrentFragment(this.fragment.getContext());
        if (!this.fragment.equals(fragment) && !isLoad) {
            isLoad = true;
            ((AppCompatActivity) this.fragment.getContext()).getSupportFragmentManager()
                    .beginTransaction()
                    .replace(idContainer, fragment)
                    .addToBackStack(String.valueOf(fragment.getClass()))
                    .commit();

            isLoad = false;

            notifyObservers(fragment);
        }
    }

    @Override
    public void notifyObservers(Object object) {
        if (object instanceof Fragment)
            ComponentManager.notifyObservers(this, (Fragment) object);
    }

    @Override
    public void update(@NonNull Object object) {
        if (object instanceof Fragment)
            replaceFragmentInContainer((Fragment) object);
    }
}