package com.application.arenda.Patterns;

import com.application.arenda.CustomComponents.ContainerFragments.ComponentLinkManager;

public interface AdapterManager {
    void setManagers(ObserverManager observerManager, ComponentLinkManager componentLinkManager);
}