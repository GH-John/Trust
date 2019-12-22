package com.application.arenda.Entities.Announcements.InsertAnnouncement.InflateDropDownList;

public class ModelItemContent {
    private int id;
    private String name;

    public ModelItemContent(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}