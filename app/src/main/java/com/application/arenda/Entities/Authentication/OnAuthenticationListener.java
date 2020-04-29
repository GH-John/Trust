package com.application.arenda.Entities.Authentication;

import androidx.annotation.NonNull;

public interface OnAuthenticationListener {
    void onComplete(@NonNull IApiAuthentication.StatusAuthentication code);

    void onFailure(@NonNull Throwable t);
}