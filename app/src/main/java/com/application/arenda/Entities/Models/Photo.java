package com.application.arenda.Entities.Models;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.persistence.DataQueryBuilder;

import java.util.List;

public class Photo extends BackendlessTable {
    private String uri = "";
    private Boolean isMain = false;

    public static Photo findById(String id) {
        return Backendless.Data.of(Photo.class).findById(id);
    }

    public static void findByIdAsync(String id, AsyncCallback<Photo> callback) {
        Backendless.Data.of(Photo.class).findById(id, callback);
    }

    public static Photo findFirst() {
        return Backendless.Data.of(Photo.class).findFirst();
    }

    public static void findFirstAsync(AsyncCallback<Photo> callback) {
        Backendless.Data.of(Photo.class).findFirst(callback);
    }

    public static Photo findLast() {
        return Backendless.Data.of(Photo.class).findLast();
    }

    public static void findLastAsync(AsyncCallback<Photo> callback) {
        Backendless.Data.of(Photo.class).findLast(callback);
    }

    public static List<Photo> find(DataQueryBuilder queryBuilder) {
        return Backendless.Data.of(Photo.class).find(queryBuilder);
    }

    public static void findAsync(DataQueryBuilder queryBuilder, AsyncCallback<List<Photo>> callback) {
        Backendless.Data.of(Photo.class).find(queryBuilder, callback);
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Boolean getIsMain() {
        return isMain;
    }

    public void setIsMain(Boolean isMain) {
        this.isMain = isMain;
    }

    public Photo save() {
        return Backendless.Data.of(Photo.class).save(this);
    }

    public void saveAsync(AsyncCallback<Photo> callback) {
        Backendless.Data.of(Photo.class).save(this, callback);
    }

    public Long remove() {
        return Backendless.Data.of(Photo.class).remove(this);
    }

    public void removeAsync(AsyncCallback<Long> callback) {
        Backendless.Data.of(Photo.class).remove(this, callback);
    }
}
