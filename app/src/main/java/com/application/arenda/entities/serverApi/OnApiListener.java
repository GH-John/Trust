package com.application.arenda.entities.serverApi;

import androidx.annotation.NonNull;

import com.application.arenda.entities.serverApi.client.CodeHandler;

public interface OnApiListener {
    void onComplete(@NonNull CodeHandler code);

    void onFailure(@NonNull Throwable t);
}