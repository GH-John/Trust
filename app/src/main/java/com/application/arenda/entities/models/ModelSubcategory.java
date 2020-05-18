package com.application.arenda.entities.models;

public class ModelSubcategory implements IModel {
    private long id;

    private long idCategory;
    private String name;

    public ModelSubcategory() {
    }

    public ModelSubcategory(long id, long idCategory, String name) {
        this.id = id;
        this.idCategory = idCategory;
        this.name = name;
    }

    public long getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(long idCategory) {
        this.idCategory = idCategory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setID(long id) {
        this.id = id;
    }

    @Override
    public long getID() {
        return id;
    }
}
