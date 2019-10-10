package com.application.trust.Patterns;

import android.util.Log;

import androidx.fragment.app.Fragment;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ObserverManager<K extends Observable, V extends Observer> {
    private final Map<K, Set<V>> collection;

    public ObserverManager() {
        this.collection = new HashMap<>();

        Log.d("ObserverManager", "Constructor create empty map observers");
    }

    @SafeVarargs
    public ObserverManager(K observable, V... observers) {
        this.collection = new HashMap<>();
        this.collection.put(observable, new HashSet<>(Arrays.asList(observers)));

        Log.d("ObserverManager", "Constructor inflate map observers");
    }

    @SafeVarargs
    public final synchronized void addObserver(K observable, V... observers) {

        if (!this.collection.containsKey(observable)) {
            this.collection.put(observable, new HashSet<>(Arrays.asList(observers)));

            Log.d("ObserverManager", "Add to map new observable with observers");
        } else {
            Set<V> collectionObservers = this.collection.get(observable);
            collectionObservers.addAll(Arrays.asList(observers));
            this.collection.put(observable, collectionObservers);

            Log.d("ObserverManager", "Replace observable with observers");
        }
    }

    public synchronized Set<K> getCollectionObservable(V observer) {
        Set<K> collectionObservable = new HashSet<>();
        Set<V> collectionObservers;

        for (K observable : this.collection.keySet()) {
            collectionObservers = this.collection.get(observable);

            if (collectionObservers.contains(observer)) {
                collectionObservable.add(observable);
            }
        }

        Log.d("ObserverManager", "Return collection observable by observer");
        return collectionObservable;
    }

    public synchronized boolean containsObserver(K observable, V observer) {
        if (this.collection.containsKey(observable)) {
            Set<V> collectionObservers = this.collection.get(observable);
            return collectionObservers.contains(observer);
        }

        Log.d("ObserverManager", "Contains observer");
        return false;
    }

    public synchronized Map<K, Set<V>> getCollection() {
        return this.collection;
    }

    public synchronized void notifyObservers(K observable, Fragment fragment) {
        Set<V> collectionObservers = this.collection.get(observable);
        for (V observer : collectionObservers) {
            observer.update(fragment);
        }
    }

    public synchronized void removeObservable(K observable) {
        this.collection.remove(observable);

        Log.d("ObserverManager", "Remove observable: " + observable.getClass().toString());
    }

    @SafeVarargs
    public final synchronized void removeObserver(V... observers) {
        Set<V> collectionObservers;
        for (K observable : this.collection.keySet()) {
            collectionObservers = this.collection.get(observable);
            for (V observer : observers) {
                collectionObservers.remove(observer);

                Log.d("ObserverManager", "Remove observer: " + observer.getClass().toString()
                        + " from observable: " + observable.getClass().toString());
            }
            this.collection.put(observable, collectionObservers);
        }
    }

    @SafeVarargs
    public final synchronized void removeObserverFromObservable(K observable, V... observers) {
        Set<V> collectionObservers = this.collection.get(observable);
        for (V observer : observers) {
            collectionObservers.remove(observer);

            Log.d("ObserverManager", "Remove observer: " + observer.getClass().toString()
                    + " from observable: " + observable.getClass().toString());
        }
        this.collection.put(observable, collectionObservers);
    }

    public synchronized void removeAllObserver() {
        this.collection.clear();

        Log.d("ObserverManager", "Clear list observers");
    }
}