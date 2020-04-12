package com.application.arenda.Entities.Announcements.Categories;

import org.greenrobot.eventbus.Subscribe;

public class EventSendID {
    private String idSubcategory;
    private String name;

    public EventSendID(String idSubcategory, String name) {
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
    public void setSelectedSubcategory(String idSubcategory) {
        this.idSubcategory = idSubcategory;
    }

    public String getIdSubcategory() {
        return idSubcategory;
    }
}