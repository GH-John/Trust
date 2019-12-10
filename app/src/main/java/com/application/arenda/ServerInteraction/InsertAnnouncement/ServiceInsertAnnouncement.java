package com.application.arenda.ServerInteraction.InsertAnnouncement;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

public class ServiceInsertAnnouncement extends Service {
    private InsertAnnouncement insertAnnouncement;

    public ServiceInsertAnnouncement(@NotNull Context context, @NotNull ModelAnnouncement modelAnnouncement) {
        this.insertAnnouncement = new InsertAnnouncement(context, modelAnnouncement);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        insertAnnouncement.execute();
        Log.d(getClass().toString(), "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        insertAnnouncement.cancelRequest();
        Log.d(getClass().toString(), "onDestroy");
    }
}