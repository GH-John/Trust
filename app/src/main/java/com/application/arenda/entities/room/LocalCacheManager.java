package com.application.arenda.entities.room;

import android.content.Context;

import com.application.arenda.entities.room.dao.DaoAnnouncement;
import com.application.arenda.entities.room.dao.DaoPicture;
import com.application.arenda.entities.room.dao.DaoUser;

public final class LocalCacheManager {
    private static LocalCacheManager manager;
    private static AppDB db;

    private LocalCacheManager() {
    }

    public static LocalCacheManager getInstance(Context context) {
        if (manager == null) {
            db = AppDB.getInstance(context);
            manager = new LocalCacheManager();
        }

        return manager;
    }

    public synchronized DaoUser users() {
        return db.daoUser();
    }

    public synchronized DaoAnnouncement announcements() {
        return db.daoAnnouncement();
    }

    public synchronized DaoPicture pictures() {
        return db.daoPicture();
    }
}