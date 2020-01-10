package com.application.arenda.Entities.Announcements.ViewAnnouncement;

import android.content.Context;

import com.application.arenda.Entities.Announcements.Models.ModelViewAnnouncement;

import io.reactivex.Observable;

public interface ILoadingViewAnnouncement {
    Observable<ModelViewAnnouncement> loadAnnouncement(final Context context, final String url, final int idAnnouncement);
}