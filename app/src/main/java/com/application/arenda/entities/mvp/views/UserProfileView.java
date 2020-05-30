package com.application.arenda.entities.mvp.views;

import com.application.arenda.entities.models.ModelUser;
import com.application.arenda.entities.mvp.baseInterfaces.ViewError;
import com.application.arenda.entities.mvp.baseInterfaces.ViewProgress;

public interface UserProfileView extends ViewProgress, ViewError {
    void setProfile(ModelUser profile);

    void userNotFound();
}