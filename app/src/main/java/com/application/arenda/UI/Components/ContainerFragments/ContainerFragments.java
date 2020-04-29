package com.application.arenda.UI.Components.ContainerFragments;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.application.arenda.R;
import com.application.arenda.UI.Components.ComponentManager;

import java.util.List;

import timber.log.Timber;

public final class ContainerFragments implements ComponentManager.Observer, ComponentManager.Observable {
    @SuppressLint("StaticFieldLeak")
    private static ContainerFragments containerFragments;
    private final int idContainer = R.id.containerFragments;

    private List<Fragment> fragments;

    private FragmentManager fragmentManager;

    private boolean isLoad = false;

    private ContainerFragments(Context context) {
        fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
        initListeners();
    }

    public static ContainerFragments getInstance(Context context) {
        if (containerFragments == null)
            containerFragments = new ContainerFragments(context);

        return containerFragments;
    }

    private void initListeners() {
        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            notifyObservers(getCurrentFragment());

            Timber.tag("FRAGMENT_MANAGER").i("Size - %s", getSupportFragmentManager().getFragments().size());

            fragments = getFragments();

            for (int i = 0; i < fragments.size(); i++) {
                Timber.tag("FRAGMENT_MANAGER").i("Fragment - %s", fragments.get(i).getClass().getSimpleName());
            }
        });
    }

    public Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentById(idContainer);
    }

    public FragmentManager getSupportFragmentManager() {
        return fragmentManager;
    }

    public void open(@NonNull Fragment fragment) {
        if (!isLoad && fragment != null) {
            if (!fragment.isVisible()) {
                Timber.tag("FRAGMENT_MANAGER").i("Visible - false");

                isLoad = true;
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(idContainer, fragment, fragment.getClass().getSimpleName())
                        .addToBackStack(fragment.getClass().getSimpleName())
                        .commit();

                isLoad = false;
            } else {
                Timber.tag("FRAGMENT_MANAGER").i("Visible - true");

                isLoad = true;
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(idContainer, fragment, fragment.getClass().getSimpleName())
                        .addToBackStack(fragment.getClass().getSimpleName())
                        .commit();

                isLoad = false;
            }
        }
    }

    public boolean isContains(Fragment fragment) {
        return fragment != null && getFragments().contains(fragment);
    }

    public List<Fragment> getFragments() {
        return getSupportFragmentManager().getFragments();
    }

    public int getBackStackEntryCount() {
        return getSupportFragmentManager().getBackStackEntryCount();
    }

    public void popBackStack() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void notifyObservers(Object object) {
        if (object instanceof Fragment)
            ComponentManager.notifyObservers(this, (Fragment) object);
    }

    @Override
    public void update(@NonNull Object object) {
        if (object instanceof Fragment)
            open((Fragment) object);
    }
}