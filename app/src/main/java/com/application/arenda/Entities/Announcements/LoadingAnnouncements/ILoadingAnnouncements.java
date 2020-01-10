package com.application.arenda.Entities.Announcements.LoadingAnnouncements;

import android.content.Context;

import com.application.arenda.Entities.Announcements.Models.ModelViewAnnouncement;
import com.application.arenda.Entities.Announcements.Models.ModelAllAnnouncement;
import com.application.arenda.Entities.Announcements.Models.ModelUserAnnouncement;

import java.util.List;

import io.reactivex.Observable;

public interface ILoadingAnnouncements {

    Observable<List<ModelAllAnnouncement>> loadAllAnnouncements(final Context context, final String url);

    Observable<List<ModelAllAnnouncement>> searchToAllAnnouncements(final Context context, final String url, final int lastID, final String search);

    Observable<List<ModelUserAnnouncement>> loadUserAnnouncements(final Context context, final String url);

    Observable<List<ModelUserAnnouncement>> searchToUserAnnouncements(final Context context, final String url, final int lastID, final String search);

    Observable<List<ModelViewAnnouncement>> loadViewAnnouncement(final Context context, final String url);
}