package com.application.arenda.Entities.Models;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.persistence.DataQueryBuilder;

import java.util.Date;
import java.util.List;

public class Rent extends BackendlessTable {
    private Date rentalEnd;
    private Date rentalStart;

    public static Rent findById(String id) {
        return Backendless.Data.of(Rent.class).findById(id);
    }

    public static void findByIdAsync(String id, AsyncCallback<Rent> callback) {
        Backendless.Data.of(Rent.class).findById(id, callback);
    }

    public static Rent findFirst() {
        return Backendless.Data.of(Rent.class).findFirst();
    }

    public static void findFirstAsync(AsyncCallback<Rent> callback) {
        Backendless.Data.of(Rent.class).findFirst(callback);
    }

    public static Rent findLast() {
        return Backendless.Data.of(Rent.class).findLast();
    }

    public static void findLastAsync(AsyncCallback<Rent> callback) {
        Backendless.Data.of(Rent.class).findLast(callback);
    }

    public static List<Rent> find(DataQueryBuilder queryBuilder) {
        return Backendless.Data.of(Rent.class).find(queryBuilder);
    }

    public static void findAsync(DataQueryBuilder queryBuilder, AsyncCallback<List<Rent>> callback) {
        Backendless.Data.of(Rent.class).find(queryBuilder, callback);
    }

    public Date getRentalEnd() {
        return rentalEnd == null ? new Date() : rentalEnd;
    }

    public void setRentalEnd(Date rentalEnd) {
        this.rentalEnd = rentalEnd;
    }

    public Date getRentalStart() {
        return rentalStart == null ? new Date() : rentalStart;
    }

    public void setRentalStart(Date rentalStart) {
        this.rentalStart = rentalStart;
    }

    public Rent save() {
        return Backendless.Data.of(Rent.class).save(this);
    }

    public void saveAsync(AsyncCallback<Rent> callback) {
        Backendless.Data.of(Rent.class).save(this, callback);
    }

    public Long remove() {
        return Backendless.Data.of(Rent.class).remove(this);
    }

    public void removeAsync(AsyncCallback<Long> callback) {
        Backendless.Data.of(Rent.class).remove(this, callback);
    }
}
