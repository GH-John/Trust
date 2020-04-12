package com.application.arenda.Entities.Models;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.persistence.DataQueryBuilder;

import java.util.List;

public class FavoriteAnnouncement extends BackendlessTable {
    private Boolean isFavorite = false;

    public static FavoriteAnnouncement findById(String id) {
        return Backendless.Data.of(FavoriteAnnouncement.class).findById(id);
    }

    public static void findByIdAsync(String id, AsyncCallback<FavoriteAnnouncement> callback) {
        Backendless.Data.of(FavoriteAnnouncement.class).findById(id, callback);
    }

    public static FavoriteAnnouncement findFirst() {
        return Backendless.Data.of(FavoriteAnnouncement.class).findFirst();
    }

    public static void findFirstAsync(AsyncCallback<FavoriteAnnouncement> callback) {
        Backendless.Data.of(FavoriteAnnouncement.class).findFirst(callback);
    }

    public static FavoriteAnnouncement findLast() {
        return Backendless.Data.of(FavoriteAnnouncement.class).findLast();
    }

    public static void findLastAsync(AsyncCallback<FavoriteAnnouncement> callback) {
        Backendless.Data.of(FavoriteAnnouncement.class).findLast(callback);
    }

    public static List<FavoriteAnnouncement> find(DataQueryBuilder queryBuilder) {
        return Backendless.Data.of(FavoriteAnnouncement.class).find(queryBuilder);
    }

    public static void findAsync(DataQueryBuilder queryBuilder, AsyncCallback<List<FavoriteAnnouncement>> callback) {
        Backendless.Data.of(FavoriteAnnouncement.class).find(queryBuilder, callback);
    }

    public Boolean isFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(Boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    public FavoriteAnnouncement save() {
        return Backendless.Data.of(FavoriteAnnouncement.class).save(this);
    }

    public void saveAsync(AsyncCallback<FavoriteAnnouncement> callback) {
        Backendless.Data.of(FavoriteAnnouncement.class).save(this, callback);
    }

    public Long remove() {
        return Backendless.Data.of(FavoriteAnnouncement.class).remove(this);
    }

    public void removeAsync(AsyncCallback<Long> callback) {
        Backendless.Data.of(FavoriteAnnouncement.class).remove(this, callback);
    }
}
