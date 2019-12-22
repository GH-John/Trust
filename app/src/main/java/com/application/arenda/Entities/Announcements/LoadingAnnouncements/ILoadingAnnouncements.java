package com.application.arenda.Entities.Announcements.LoadingAnnouncements;

import android.content.Context;

import com.application.arenda.Entities.Announcements.Models.ViewModelAllAnnouncement;

import java.util.List;

import io.reactivex.Observable;

public interface ILoadingAnnouncements {

    Observable<List<ViewModelAllAnnouncement>> loadAllAnnouncemets(final Context context, final String url);

    Observable<List<ViewModelAllAnnouncement>> searchToAllAnnouncemets(final Context context, final String url, final int lastID, final String search);

    Observable<List<ViewModelAllAnnouncement>> loadUserAnnouncemets(final Context context, final String url);

    Observable<List<ViewModelAllAnnouncement>> searchToUserAnnouncemets(final Context context, final String url, final int lastID, final String search);
}