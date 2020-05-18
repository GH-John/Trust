package com.application.arenda.entities.announcements.proposalsAnnouncement;

import androidx.fragment.app.Fragment;

public interface PagerItem {
    Integer getResourceTitle();

    Fragment getFragment();
}