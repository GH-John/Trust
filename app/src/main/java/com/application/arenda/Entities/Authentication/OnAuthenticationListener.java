package com.application.arenda.Entities.Authentication;

import androidx.annotation.NonNull;

public interface OnAuthenticationListener {
    void onComplete(@NonNull ApiAuthentication.AuthenticationCodes code);

    void onFailure(@NonNull Throwable t);
}