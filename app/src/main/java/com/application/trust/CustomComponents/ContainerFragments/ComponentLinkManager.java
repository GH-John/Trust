package com.application.trust.CustomComponents.ContainerFragments;

import android.util.Log;
import android.view.View;

import androidx.fragment.app.Fragment;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class ComponentLinkManager<F extends Fragment, V extends View, C extends FragmentLink> {
    private final Map<C, Map<F, V>> mapItemLinks;
    private final Map<C, Set<F>> mapComponentLinks;

    public ComponentLinkManager() {
        this.mapItemLinks = new HashMap<>();
        this.mapComponentLinks = new HashMap<>();
        Log.d("ComponentLinkManager", "Initialize constructor");
    }

    @SafeVarargs
    public final synchronized void addComponentLinks(C component, F... fragments){
        this.mapComponentLinks.put(component, new LinkedHashSet<>(Arrays.asList(fragments)));
        Log.d("ComponentLinkManager", "Add links " + component.getClass().toString() + " with fragments");
    }

    public synchronized Set<F> getCollectionFragmets(C component){
        Log.d("ComponentLinkManager", "Return collection fragments for component: " + component.getClass().toString());
        return this.mapComponentLinks.get(component);
    }

    public synchronized void addItemLinks(C component, Map<F, V> mapFragmentLinks){
        this.mapItemLinks.put(component, mapFragmentLinks);
        Log.d("ComponentLinkManager", "Add item links component: " + component.getClass().toString() + " with fragments");
    }

    public synchronized Map<F, V> getItemLinks(C component){
        Log.d("ComponentLinkManager", "Return item links for component: " + component.getClass().toString());
        return this.mapItemLinks.get(component);
    }
}