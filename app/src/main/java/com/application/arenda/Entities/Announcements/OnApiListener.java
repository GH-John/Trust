package com.application.arenda.Entities.Announcements;

import androidx.annotation.NonNull;

import com.application.arenda.Entities.Utils.Retrofit.CodeHandler;

public interface OnApiListener {
    void onComplete(@NonNull CodeHandler code);

    void onFailure(@NonNull Throwable t);
}
