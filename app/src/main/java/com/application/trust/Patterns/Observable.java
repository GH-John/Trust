package com.application.trust.Patterns;

import androidx.fragment.app.Fragment;

import com.application.trust.CustomComponents.Container.ComponentLinkManager;

public interface Observable {
    void setManagers(ObserverManager observerManager, ComponentLinkManager componentLinkManager);

    void notifyObservers(Fragment fragment);
}