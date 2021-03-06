package com.application.arenda.MainWorkspace.Activities;

import android.app.Application;

import com.application.arenda.BuildConfig;

import timber.log.Timber;

public class AppController extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}