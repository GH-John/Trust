package com.application.arenda.Entities.Announcements.InsertAnnouncement;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.application.arenda.Entities.Announcements.Models.ModelInsertAnnouncement;

import io.reactivex.Observable;

public interface IInsertAnnouncement {
    Observable insertAnnouncement(@NonNull Context context, @NonNull String url, ModelInsertAnnouncement announcement);

    Observable insertPhoto(@NonNull Context context, int idAnnouncement, @NonNull Bitmap bitmap);
}