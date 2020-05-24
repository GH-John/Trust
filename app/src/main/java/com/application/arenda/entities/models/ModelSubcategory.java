package com.application.arenda.entities.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "subcategories")
public class ModelSubcategory implements IModel {
    @PrimaryKey
    @ColumnInfo(name = "idSubcategory")
    @SerializedName("idSubcategory")
    private long ID;

    @SerializedName("idCategory")
    private long idCategory;

    @SerializedName("name")
    private String name;

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
        this.ID = id;
    }

    @Override
    public long getID() {
        return ID;
    }
}