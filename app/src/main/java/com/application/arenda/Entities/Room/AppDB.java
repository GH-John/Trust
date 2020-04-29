package com.application.arenda.Entities.Room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.application.arenda.BuildConfig;
import com.application.arenda.Entities.Models.ModelAnnouncement;
import com.application.arenda.Entities.Models.ModelPicture;
import com.application.arenda.Entities.Models.ModelUser;
import com.application.arenda.Entities.Room.Dao.DaoAnnouncement;
import com.application.arenda.Entities.Room.Dao.DaoPicture;
import com.application.arenda.Entities.Room.Dao.DaoUser;

import timber.log.Timber;

@Database(entities = {ModelUser.class, ModelAnnouncement.class, ModelPicture.class}, version = BuildConfig.APP_DB_VERSION)
public abstract class AppDB extends RoomDatabase {
    private static AppDB instance;

    public synchronized static AppDB getInstance(Context context) {
        if (instance == null) {
            Timber.d("Creating new database instance");
            try {
                instance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDB.class, BuildConfig.APP_DB_NAME)
                        .build();
            } catch (Throwable throwable) {
                Timber.e(throwable);
            }
        }
        Timber.d("Getting the database instance");
        return instance;
    }

    public abstract DaoUser daoUser();

    public abstract DaoAnnouncement daoAnnouncement();

    public abstract DaoPicture daoPicture();
}