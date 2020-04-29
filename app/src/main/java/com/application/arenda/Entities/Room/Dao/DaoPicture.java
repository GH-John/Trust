package com.application.arenda.Entities.Room.Dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.application.arenda.Entities.Models.ModelPicture;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public abstract class DaoPicture implements BaseDao<ModelPicture> {
    @Query("select * from pictures where idAnnouncement = :idAnnouncement")
    public abstract Flowable<List<ModelPicture>> getPictures(long idAnnouncement);
}