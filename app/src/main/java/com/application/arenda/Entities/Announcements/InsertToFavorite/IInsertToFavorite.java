package com.application.arenda.Entities.Announcements.InsertToFavorite;

import android.content.Context;

import androidx.annotation.NonNull;

import io.reactivex.Observable;

public interface IInsertToFavorite {
    Observable<Boolean> insertToFavorite(@NonNull Context context, @NonNull String url, String idAnnouncement);
}