package com.application.arenda.Entities.Announcements.ViewAnnouncement;


import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.LinearLayout;

import java.util.List;

public interface IDataViewPager {
    List<Uri> getCollectionUriBitmap();

    LinearLayout getDotsLayout();

    Drawable getDotUnselected();

    Drawable getDotSelected();
}