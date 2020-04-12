package com.application.arenda.Entities.Utils.Retrofit;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import io.reactivex.disposables.Disposable;

public class FileUploadService extends JobIntentService {
    private static final short SERVICE_ID = 104;
    private Disposable disposable;

    public static void enqueueWork(Context context, Intent intent) {
        enqueueWork(context, FileUploadService.class, SERVICE_ID, intent);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {

    }
}
