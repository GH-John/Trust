package com.application.arenda.entities.announcements.viewAnnouncement;

import android.net.Uri;

import java.util.List;

public interface OnItemClickViewPager {
    void onClick(List<Uri> uriList, Uri selectedUri);
}