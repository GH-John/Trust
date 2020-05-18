package com.application.arenda.entities.room.dao;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.application.arenda.entities.models.ModelAnnouncement;
import com.application.arenda.entities.models.ModelViewAnnouncement;

import io.reactivex.Flowable;

@Dao
public abstract class DaoAnnouncement implements BaseDao<ModelAnnouncement> {
    @Query("select * from announcements where idAnnouncement = :idAnnouncement")
    public abstract Flowable<ModelAnnouncement> getAnnouncement(long idAnnouncement);

    @Transaction
    @Query("select * from announcements where idAnnouncement = :idAnnouncement")
    public abstract Flowable<ModelViewAnnouncement> getViewAnnouncement(long idAnnouncement);
}