package com.application.arenda.ui.widgets;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import timber.log.Timber;

public class ComponentManager {
    private static final Map<Observable, Set<Observer>> collection = new HashMap<>();

    public static synchronized void addLink(Observable observable, Observer... observers) {

        if (!collection.containsKey(observable)) {
            collection.put(observable, new HashSet<>(Arrays.asList(observers)));

            Timber.tag("ComponentManager").d("Add to map new observable with observers");
        } else {
            Set<Observer> collectionObservers = collection.get(observable);
            collectionObservers.addAll(Arrays.asList(observers));
            collection.put(observable, collectionObservers);

            Timber.tag("ComponentManager").d("Replace observable with observers");
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

        Timber.d("Return collection observable by observer");
        return collectionObservable;
    }

    public static synchronized boolean containsObserver(Observable observable, Observer observer) {
        if (collection.containsKey(observable)) {
            Set<Observer> collectionObservers = collection.get(observable);
            return collectionObservers.contains(observer);
        }

        Timber.tag("ComponentManager").d("Contains observer");
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

        Timber.tag("ComponentManager").d("Remove observable: %s", observable.getClass().toString());
    }

    public static synchronized void removeObserver(Observer... observers) {
        Set<Observer> collectionObservers;
        for (Observable observable : collection.keySet()) {
            collectionObservers = collection.get(observable);
            for (Observer observer : observers) {
                collectionObservers.remove(observer);

                Timber.tag("ComponentManager").d("Remove observer: " + observer.getClass().toString() + " from observable: " + observable.getClass().toString());
            }
            collection.put(observable, collectionObservers);
        }
    }

    public static synchronized void removeObserverFromObservable(Observable observable, Observer... observers) {
        Set<Observer> collectionObservers = collection.get(observable);
        for (Observer observer : observers) {
            collectionObservers.remove(observer);

            Timber.tag("ComponentManager").d("Remove observer: " + observer.getClass().toString() + " from observable: " + observable.getClass().toString());
        }
        collection.put(observable, collectionObservers);
    }

    public static synchronized int getSize() {
        return collection.size();
    }

    public synchronized void removeAllObserver() {
        this.collection.clear();

        Timber.tag("ComponentManager").d("Clear list observers");
    }

    public interface Observable {
        void notifyObservers(Object object);
    }

    public interface Observer {
        void update(@NonNull Object object);
    }
}