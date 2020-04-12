package com.application.arenda.MainWorkspace.Activities;

import android.app.Application;

import com.application.arenda.BuildConfig;
import com.backendless.Backendless;
import com.jakewharton.threetenabp.AndroidThreeTen;

import timber.log.Timber;

public class AppController extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        initApi();
    }

    private void initApi() {
        AndroidThreeTen.init(this);

        Backendless.setUrl(BuildConfig.BackendlessURL);
        Backendless.initApp(this, BuildConfig.BackendlessAppID, BuildConfig.BackendlessApiKey);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}