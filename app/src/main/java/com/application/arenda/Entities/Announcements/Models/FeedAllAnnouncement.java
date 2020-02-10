package com.application.arenda.Entities.Announcements.Models;

import java.util.List;

public class FeedAllAnnouncement {
    private List<ModelAllAnnouncement> models;

    public void setModels(List<ModelAllAnnouncement> models) {
        this.models = models;
    }

    public List<ModelAllAnnouncement> getModels(){
        return models;
    }
}
