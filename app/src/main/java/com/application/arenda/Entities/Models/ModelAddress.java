package com.application.arenda.Entities.Models;

public class ModelAddress implements IModel {
    private long id = 0;

    private String address = "";

    public ModelAddress(long id, String address) {
        this.id = id;
        this.address = address;
    }

    public String getAddress() {
        return address;
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