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

    private Context context;
    private Fragment fragment;
    private final int idContainer = R.id.containerFragments;

    private ContainerFragments() {
    }

    public static ContainerFragments getInstance() {
        if (containerFragments == null)
            containerFragments = new ContainerFragments();

        return containerFragments;
    }

    public void start(Context context) {
        this.context = context;
        notifyObservers(getCurrentFragment(context));
    }

    public Fragment getCurrentFragment(Context context) {
        return ((AppCompatActivity) context).getSupportFragmentManager().findFragmentById(idContainer);
    }

    public void replaceFragmentInContainer(Fragment fragment) {
        this.fragment = getCurrentFragment(context);
        if (!this.fragment.equals(fragment)) {
            ((AppCompatActivity) context).getSupportFragmentManager()
                    .beginTransaction()
                    .replace(idContainer, fragment)
                    .addToBackStack(String.valueOf(fragment.getClass()))
                    .commit();

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