package com.application.arenda.Entities.Models;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.ArrayList;
import java.util.List;

public class ModelViewAnnouncement {
    @Embedded
    private ModelAnnouncement announcement;

    @Relation(entity = ModelPicture.class, parentColumn = "idAnnouncement", entityColumn = "idAnnouncement")
    private List<ModelPicture> pictures = new ArrayList<>();

    public ModelAnnouncement getAnnouncement() {
        return announcement;
    }

    public void setAnnouncement(ModelAnnouncement announcement) {
        this.announcement = announcement;
    }

    public List<ModelPicture> getPictures() {
        return pictures;
    }

    public void setPictures(List<ModelPicture> pictures) {
        this.pictures = pictures;
    }
}