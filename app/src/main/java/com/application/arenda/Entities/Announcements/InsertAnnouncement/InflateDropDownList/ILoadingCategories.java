package com.application.arenda.Entities.Announcements.InsertAnnouncement.InflateDropDownList;

import android.content.Context;

import java.util.List;

import io.reactivex.Observable;

public interface ILoadingCategories {
    Observable <List<ModelItemContent>> loadingCategories(final Context context);
    Observable <List<ModelItemContent>> loadingSubcategories(final Context context, final int idCategories);
}