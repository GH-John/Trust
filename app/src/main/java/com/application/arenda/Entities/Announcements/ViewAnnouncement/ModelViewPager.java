package com.application.arenda.Entities.Announcements.ViewAnnouncement;


import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class ModelViewPager {
    private LinearLayout linearLayout;

    private Drawable drawableSelected;
    private Drawable drawableUnselected;

    private List<Uri> uriList = new ArrayList<>();

    public void setLinearLayout(LinearLayout linearLayout) {
        this.linearLayout = linearLayout;
    }

    public void setDrawableSelected(Drawable drawableSelected) {
        this.drawableSelected = drawableSelected;
    }

    public void setDrawableUnselected(Drawable drawableUnselected) {
        this.drawableUnselected = drawableUnselected;
    }

    public void setUriList(List<Uri> uriList) {
        this.uriList = uriList;
    }

    public List<Uri> getCollectionUri() {
        return uriList;
    }

    public LinearLayout getDotsLayout() {
        return linearLayout;
    }

    public Drawable getDotUnselected() {
        return drawableUnselected;
    }

    public Drawable getDotSelected() {
        return drawableSelected;
    }
}