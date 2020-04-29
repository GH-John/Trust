package com.application.arenda.Entities.Room.Dao;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

public interface BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertToRoom(T object);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertToRoom(Iterable<T> object);

    @Delete
    Completable deleteFromRoom(T object);

    @Delete
    Completable deleteFromRoom(Iterable<T> object);

    @Update
    Completable updateInRoom(T object);

    @Update
    Completable updateInRoom(Iterable<T> object);
}