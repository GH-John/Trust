package com.application.arenda.mainWorkspace.activities;

import android.app.Application;

import com.application.arenda.BuildConfig;
import com.facebook.stetho.Stetho;
import com.jakewharton.threetenabp.AndroidThreeTen;

import timber.log.Timber;

public class AppController extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        AndroidThreeTen.init(this);
        Stetho.initializeWithDefaults(this);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}