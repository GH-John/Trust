package com.application.arenda.Entities.Room.Dao;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.application.arenda.Entities.Models.ModelAnnouncement;
import com.application.arenda.Entities.Models.ModelViewAnnouncement;

import io.reactivex.Completable;
import io.reactivex.Flowable;

@Dao
public abstract class DaoAnnouncement implements BaseDao<ModelAnnouncement> {
    @Query("select * from announcements where idAnnouncement = :idAnnouncement")
    public abstract Flowable<ModelAnnouncement> getAnnouncement(long idAnnouncement);

    @Transaction
    @Query("select * from announcements where idAnnouncement = :idAnnouncement")
    public abstract Flowable<ModelViewAnnouncement> getViewAnnouncement(long idAnnouncement);
}