package com.application.trust.CustomComponents.Container;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class PluginFragmentManager<T extends PluginFragment> {
    Set<T> collection;

    public PluginFragmentManager() {
        this.collection = new HashSet<>();
    }

    @SafeVarargs
    public PluginFragmentManager(T... pluginFragments) {
        this.collection = new HashSet<>();
        collection.addAll(Arrays.asList(pluginFragments));
    }

    @SafeVarargs
    public final synchronized void addPluginFragment(T... pluginFragments){
        collection.addAll(Arrays.asList(pluginFragments));
    }
}