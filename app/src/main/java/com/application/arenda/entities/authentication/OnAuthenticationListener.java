package com.application.arenda.entities.authentication;

import androidx.annotation.NonNull;

public interface OnAuthenticationListener {
    void onComplete(@NonNull IApiAuthentication.StatusAuthentication code);

    void onFailure(@NonNull Throwable t);
}