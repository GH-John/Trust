package com.application.arenda.entities.mvp.baseInterfaces;

import com.application.arenda.entities.serverApi.client.ApiHandler;

public interface ViewError {
    void onError(ApiHandler handler);
}
