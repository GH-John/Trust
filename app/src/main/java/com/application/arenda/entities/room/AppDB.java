package com.application.arenda.entities.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.application.arenda.BuildConfig;
import com.application.arenda.entities.models.ModelAnnouncement;
import com.application.arenda.entities.models.ModelCategory;
import com.application.arenda.entities.models.ModelFavoriteAnnouncement;
import com.application.arenda.entities.models.ModelPicture;
import com.application.arenda.entities.models.ModelProposal;
import com.application.arenda.entities.models.ModelSubcategory;
import com.application.arenda.entities.models.ModelUser;
import com.application.arenda.entities.room.converters.CurrencyConverter;
import com.application.arenda.entities.room.converters.DateTimeConverter;
import com.application.arenda.entities.room.converters.PicturesConverter;
import com.application.arenda.entities.room.converters.UriConverter;
import com.application.arenda.entities.room.dao.DaoAnnouncement;
import com.application.arenda.entities.room.dao.DaoPicture;
import com.application.arenda.entities.room.dao.DaoUser;

import timber.log.Timber;

@Database(entities = {
        ModelUser.class, ModelAnnouncement.class,
        ModelPicture.class, ModelProposal.class,
        ModelCategory.class, ModelSubcategory.class,
        ModelFavoriteAnnouncement.class
},
        version = BuildConfig.APP_DB_VERSION)
@TypeConverters({UriConverter.class, PicturesConverter.class, DateTimeConverter.class, CurrencyConverter.class})
public abstract class AppDB extends RoomDatabase {
    private static AppDB instance;

    public synchronized static AppDB getInstance(Context context) {
        if (instance == null) {
            Timber.d("Creating new database instance");
            try {
                if (BuildConfig.DEBUG) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDB.class, BuildConfig.APP_DB_NAME)

//                          this replace database and user lose data

                            .fallbackToDestructiveMigration()
                            .build();
                } else {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDB.class, BuildConfig.APP_DB_NAME)
//                        .addMigrations() - use this for release
                            .build();
                }
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