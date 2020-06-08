package com.application.arenda.mainWorkspace.activities

import android.app.Application
import com.application.arenda.BuildConfig
import com.facebook.stetho.Stetho
import com.jakewharton.threetenabp.AndroidThreeTen
import timber.log.Timber
import timber.log.Timber.DebugTree

class AppController : Application() {
    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        Stetho.initializeWithDefaults(this)
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
    }
}