package com.application.arenda.ServerInteraction.LoadingCategoryTemplates;

import java.io.Serializable;

public interface AdapterCategoryTemplate extends Serializable {
    void initializationComponents();

    void initializationStyles();

    void initializationListeners();


}