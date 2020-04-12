package com.application.arenda.Entities.Announcements.InsertAnnouncement.Categories;

import org.greenrobot.eventbus.Subscribe;

public class EventSendID {
    private int idSubcategory;
    private String name;

    public EventSendID(int idSubcategory, String name) {
        this.idSubcategory = idSubcategory;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Subscribe
    public void setSelectedSubcategory(int idSubcategory) {
        this.idSubcategory = idSubcategory;
    }

    public int getIdSubcategory() {
        return idSubcategory;
    }
}