package com.application.arenda.Entities.Announcements.InsertAnnouncement.InflateDropDownList;

import android.content.Context;

import com.application.arenda.UI.DropDownList.DropDownList;

import java.util.List;

import io.reactivex.Observable;

public interface ILoadingCategories {
    Observable <List<DropDownList.ModelItemContent>> loadingCategories(final Context context);
    Observable <List<DropDownList.ModelItemContent>> loadingSubcategories(final Context context, final int idCategories);
}