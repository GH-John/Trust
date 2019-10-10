package com.application.trust.Patterns;

import androidx.fragment.app.Fragment;

import com.application.trust.CustomComponents.Container.ManagerFragmentLinks;

public interface Observable {
    void setManagers(ObserverManager observerManager, ManagerFragmentLinks managerFragmentLinks);

    void notifyObservers(Fragment fragment);
}