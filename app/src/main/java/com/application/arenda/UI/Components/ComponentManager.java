package com.application.arenda.UI.Components;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ComponentManager {
    private static final Map<Observable, Set<Observer>> collection = new HashMap<>();

    public static synchronized void addLink(Observable observable, Observer... observers) {

        if (!collection.containsKey(observable)) {
            collection.put(observable, new HashSet<>(Arrays.asList(observers)));

            Log.d("ComponentManager", "Add to map new observable with observers");
        } else {
            Set<Observer> collectionObservers = collection.get(observable);
            collectionObservers.addAll(Arrays.asList(observers));
            collection.put(observable, collectionObservers);

            Log.d("ComponentManager", "Replace observable with observers");
        }
    }

    public static synchronized Set<Observable> getCollectionObservable(Observer observer) {
        Set<Observable> collectionObservable = new HashSet<>();
        Set<Observer> collectionObservers;

        for (Observable observable : collection.keySet()) {
            collectionObservers = collection.get(observable);

            if (collectionObservers.contains(observer)) {
                collectionObservable.add(observable);
            }
        }

        Log.d("ComponentManager", "Return collection observable by observer");
        return collectionObservable;
    }

    public static synchronized boolean containsObserver(Observable observable, Observer observer) {
        if (collection.containsKey(observable)) {
            Set<Observer> collectionObservers = collection.get(observable);
            return collectionObservers.contains(observer);
        }

        Log.d("ComponentManager", "Contains observer");
        return false;
    }

    public static synchronized Map<Observable, Set<Observer>> getCollection() {
        return collection;
    }

    public static synchronized void notifyObservers(Observable observable, Fragment fragment) {
        Set<Observer> collectionObservers = collection.get(observable);
        for (Observer observer : collectionObservers) {
            observer.update(fragment);
        }
    }

    public static synchronized void removeObservable(Observable observable) {
        collection.remove(observable);

        Log.d("ComponentManager", "Remove observable: " + observable.getClass().toString());
    }

    public static synchronized void removeObserver(Observer... observers) {
        Set<Observer> collectionObservers;
        for (Observable observable : collection.keySet()) {
            collectionObservers = collection.get(observable);
            for (Observer observer : observers) {
                collectionObservers.remove(observer);

                Log.d("ComponentManager", "Remove observer: " + observer.getClass().toString()
                        + " from observable: " + observable.getClass().toString());
            }
            collection.put(observable, collectionObservers);
        }
    }

    public static synchronized void removeObserverFromObservable(Observable observable, Observer... observers) {
        Set<Observer> collectionObservers = collection.get(observable);
        for (Observer observer : observers) {
            collectionObservers.remove(observer);

            Log.d("ComponentManager", "Remove observer: " + observer.getClass().toString()
                    + " from observable: " + observable.getClass().toString());
        }
        collection.put(observable, collectionObservers);
    }

    public synchronized void removeAllObserver() {
        this.collection.clear();

        Log.d("ComponentManager", "Clear list observers");
    }

    public static synchronized int getSize() {
        return collection.size();
    }

    public interface Observable {
        void notifyObservers(Object object);
    }

    public interface Observer {
        void update(@NonNull Object object);
    }
}