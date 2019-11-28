package com.application.arenda.ServerInteraction.AddAnnouncement.InflateCategories;

public class ItemContent {
    private int id;
    private String name;

    public ItemContent(int id, String name) {
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