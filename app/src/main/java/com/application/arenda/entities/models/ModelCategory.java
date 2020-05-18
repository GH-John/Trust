package com.application.arenda.entities.models;

public class ModelCategory implements IModel {
    private long idCategories = 0;

    private String iconUri;
    private String name = "";

    public ModelCategory() {
    }

    public ModelCategory(long idCategories, String iconUri, String name) {
        this.idCategories = idCategories;
        this.iconUri = iconUri;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconUri() {
        return iconUri;
    }

    public void setIconUri(String uri) {
        this.iconUri = uri;
    }

    @Override
    public long getID() {
        return idCategories;
    }

    @Override
    public void setID(long id) {
        this.idCategories = id;
    }
}