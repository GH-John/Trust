package com.application.arenda.Entities.Announcements.ViewAnnouncement;

import android.net.Uri;

import java.util.List;

public interface OnItemClickViewPager {
    void onClick(List<Uri> uriList, Uri selectedUri);
}