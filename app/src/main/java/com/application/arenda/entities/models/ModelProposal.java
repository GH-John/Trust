package com.application.arenda.entities.models;

import android.net.Uri;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.threeten.bp.LocalDateTime;

@Entity(tableName = "rent")
public class ModelProposal implements IModel {
    @PrimaryKey
    @ColumnInfo(index = true, name = "idRent")
    @SerializedName("idRent")
    private long ID = 0;

    @SerializedName("idAnnouncement")
    private long idAnnouncement = 0;

    @SerializedName("idUser")
    private long idUser = 0;

    @SerializedName("typeProposal")
    private TypeProposalAnnouncement typeProposal;

    @SerializedName("userLogo")
    private Uri userAvatar;

    @SerializedName("login")
    private String userLogin;

    @SerializedName("rentalStart")
    private LocalDateTime rentalStart;

    @SerializedName("rentalEnd")
    private LocalDateTime rentalEnd;

    @SerializedName("created")
    private LocalDateTime created;

    @SerializedName("updated")
    private LocalDateTime updated;

    @SerializedName("picture")
    private Uri picture;

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

    public TypeProposalAnnouncement getTypeProposal() {
        return typeProposal;
    }

    public void setTypeProposal(TypeProposalAnnouncement typeProposal) {
        this.typeProposal = typeProposal;
    }

    public Uri getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(Uri userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public LocalDateTime getRentalStart() {
        return rentalStart;
    }

    public void setRentalStart(LocalDateTime rentalStart) {
        this.rentalStart = rentalStart;
    }

    public LocalDateTime getRentalEnd() {
        return rentalEnd;
    }

    public void setRentalEnd(LocalDateTime rentalEnd) {
        this.rentalEnd = rentalEnd;
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

    public Uri getPicture() {
        return picture;
    }

    public void setPicture(Uri picture) {
        this.picture = picture;
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