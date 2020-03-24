package com.application.arenda.Entities.Announcements.ViewAnnouncement.DialogFragment;

import com.application.arenda.Entities.Announcements.Models.IModel;

public class ModelPhoneNumber implements IModel {
    private long id;

    private String number = "";

    public ModelPhoneNumber(long id, String number) {
        this.id = id;
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    @Override
    public long getID() {
        return id;
    }

    @Override
    public void setID(long id) {
        this.id = id;
    }
}