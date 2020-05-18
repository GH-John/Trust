package com.application.arenda.entities.room.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.application.arenda.entities.models.ModelPicture;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public abstract class DaoPicture implements BaseDao<ModelPicture> {
    @Query("select * from pictures where idAnnouncement = :idAnnouncement")
    public abstract Flowable<List<ModelPicture>> getPictures(long idAnnouncement);
}