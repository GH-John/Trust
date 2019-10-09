package com.application.trust.Patterns;

public interface Observable {
    void setManager(ObserverManager manager);

    void createLink();

    void notifyObservers();
}