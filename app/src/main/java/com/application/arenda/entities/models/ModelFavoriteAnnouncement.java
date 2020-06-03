package com.application.arenda.entities.models;

import android.net.Uri;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.threeten.bp.LocalDateTime;

@Entity(tableName = "favoriteAnnouncements")
public class ModelFavoriteAnnouncement implements IModel {

    @PrimaryKey
    @ColumnInfo(index = true, name = "idFavorite")
    @SerializedName("idFavorite")
    private long ID;

    @SerializedName("idAnnouncement")
    private long idAnnouncement = 0;

    @SerializedName("idUser")
    private long idUser = 0;

    @SerializedName("userLogo")
    private Uri userAvatar;

    @SerializedName("login")
    private String login = "";

    @SerializedName("name")
    private String name = "";

    @SerializedName("picture")
    private Uri picture;

    @SerializedName("isFavorite")
    private Boolean isFavorite = false;

    @SerializedName("hourlyCost")
    private float hourlyCost = 0.0f;

    @SerializedName("hourlyCurrency")
    private Currency hourlyCurrency;

    @SerializedName("dailyCost")
    private float dailyCost = 0.0f;

    @SerializedName("dailyCurrency")
    private Currency dailyCurrency;

    @SerializedName("address")
    private String address = "";

    @SerializedName("announcementCreated")
    private LocalDateTime created;

    @SerializedName("announcementUpdated")
    private LocalDateTime updated;

    public long getIdAnnouncement() {
        return idAnnouncement;
    }

    public void setIdAnnouncement(long idAnnouncement) {
        this.idAnnouncement = idAnnouncement;
    }

    public long getIdUser() {
        return idUser;
    }

    public void setIdUser(long idUser) {
        this.idUser = idUser;
    }

    public Uri getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(Uri userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Uri getPicture() {
        return picture;
    }

    public void setPicture(Uri picture) {
        this.picture = picture;
    }

    public Boolean getFavorite() {
        return isFavorite;
    }

    public void setFavorite(Boolean favorite) {
        isFavorite = favorite;
    }

    public float getHourlyCost() {
        return hourlyCost;
    }

    public void setHourlyCost(float hourlyCost) {
        this.hourlyCost = hourlyCost;
    }

    public Currency getHourlyCurrency() {
        return hourlyCurrency;
    }

    public void setHourlyCurrency(Currency hourlyCurrency) {
        this.hourlyCurrency = hourlyCurrency;
    }

    public float getDailyCost() {
        return dailyCost;
    }

    public void setDailyCost(float dailyCost) {
        this.dailyCost = dailyCost;
    }

    public Currency getDailyCurrency() {
        return dailyCurrency;
    }

    public void setDailyCurrency(Currency dailyCurrency) {
        this.dailyCurrency = dailyCurrency;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(LocalDateTime updated) {
        this.updated = updated;
    }

    @Override
    public long getID() {
        return ID;
    }

    @Override
    public void setID(long id) {
        ID = id;
    }
}