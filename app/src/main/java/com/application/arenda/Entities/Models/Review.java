package com.application.arenda.Entities.Models;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.persistence.DataQueryBuilder;

import java.util.List;

public class Review extends BackendlessTable {
    private String review = "";
    private Double rating = 0.0;

    public static Review findById(String id) {
        return Backendless.Data.of(Review.class).findById(id);
    }

    public static void findByIdAsync(String id, AsyncCallback<Review> callback) {
        Backendless.Data.of(Review.class).findById(id, callback);
    }

    public static Review findFirst() {
        return Backendless.Data.of(Review.class).findFirst();
    }

    public static void findFirstAsync(AsyncCallback<Review> callback) {
        Backendless.Data.of(Review.class).findFirst(callback);
    }

    public static Review findLast() {
        return Backendless.Data.of(Review.class).findLast();
    }

    public static void findLastAsync(AsyncCallback<Review> callback) {
        Backendless.Data.of(Review.class).findLast(callback);
    }

    public static List<Review> find(DataQueryBuilder queryBuilder) {
        return Backendless.Data.of(Review.class).find(queryBuilder);
    }

    public static void findAsync(DataQueryBuilder queryBuilder, AsyncCallback<List<Review>> callback) {
        Backendless.Data.of(Review.class).find(queryBuilder, callback);
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Review save() {
        return Backendless.Data.of(Review.class).save(this);
    }

    public void saveAsync(AsyncCallback<Review> callback) {
        Backendless.Data.of(Review.class).save(this, callback);
    }

    public Long remove() {
        return Backendless.Data.of(Review.class).remove(this);
    }

    public void removeAsync(AsyncCallback<Long> callback) {
        Backendless.Data.of(Review.class).remove(this, callback);
    }
}