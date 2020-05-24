package com.application.arenda.entities.models;

import android.net.Uri;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "category")
public class ModelCategory implements IModel {
    @PrimaryKey
    @ColumnInfo(name = "idCategory")
    @SerializedName("idCategory")
    private long ID = 0;

    @SerializedName("iconUri")
    private Uri iconUri;

    @SerializedName("name")
    private String name = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Uri getIconUri() {
        return iconUri;
    }

    public void setIconUri(Uri iconUri) {
        this.iconUri = iconUri;
    }

    @Override
    public long getID() {
        return ID;
    }

    @Override
    public void setID(long id) {
        this.ID = id;
    }
}