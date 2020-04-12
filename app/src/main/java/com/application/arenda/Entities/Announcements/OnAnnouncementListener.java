package com.application.arenda.Entities.Announcements;

import androidx.annotation.NonNull;

public interface OnAnnouncementListener {
    void onComplete(@NonNull ApiAnnouncement.AnnouncementCodes code);

    void onFailure(@NonNull Throwable t);
}
