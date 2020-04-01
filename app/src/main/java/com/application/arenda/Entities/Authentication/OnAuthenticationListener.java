package com.application.arenda.Entities.Authentication;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;

public interface OnAuthenticationListener {
    void onComplete(@NonNull Task task);

    void onFailure(@NonNull Exception e);
}
