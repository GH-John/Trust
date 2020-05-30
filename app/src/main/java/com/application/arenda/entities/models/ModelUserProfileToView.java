package com.application.arenda.entities.models;

import android.net.Uri;

import com.google.gson.annotations.SerializedName;

import org.threeten.bp.LocalDateTime;

public class ModelUserProfileToView implements IModel {
    @SerializedName("idUser")
    private long ID = 0;

    @SerializedName("name")
    private String name = "";

    @SerializedName("lastName")
    private String lastName = "";

    @SerializedName("login")
    private String login = "";

    @SerializedName("userLogo")
    private Uri avatar;

    @SerializedName("accountType")
    private String accountType;

    @SerializedName("statusUser")
    private String statusUser = "";

    @SerializedName("countAnnouncementsUser")
    private int countAnnouncementsUser = 0;

    @SerializedName("countAllViewers")
    private int countAllViewers = 0;

    @SerializedName("countFollowers")
    private int countFollowers = 0;

    @SerializedName("countFollowing")
    private int countFollowing = 0;

    @SerializedName("created")
    private LocalDateTime created;

    @SerializedName("updated")
    private LocalDateTime updated;

    @SerializedName("isFollow")
    private Boolean isFollow;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Uri getAvatar() {
        return avatar;
    }

    public void setAvatar(Uri avatar) {
        this.avatar = avatar;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getStatusUser() {
        return statusUser;
    }

    public void setStatusUser(String statusUser) {
        this.statusUser = statusUser;
    }

    public int getCountAnnouncementsUser() {
        return countAnnouncementsUser;
    }

    public void setCountAnnouncementsUser(int countAnnouncementsUser) {
        this.countAnnouncementsUser = countAnnouncementsUser;
    }

    public int getCountAllViewers() {
        return countAllViewers;
    }

    public void setCountAllViewers(int countAllViewers) {
        this.countAllViewers = countAllViewers;
    }

    public int getCountFollowers() {
        return countFollowers;
    }

    public void setCountFollowers(int countFollowers) {
        this.countFollowers = countFollowers;
    }

    public int getCountFollowing() {
        return countFollowing;
    }

    public void setCountFollowing(int countFollowing) {
        this.countFollowing = countFollowing;
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

    public Boolean getFollow() {
        return isFollow;
    }

    public void setFollow(Boolean follow) {
        isFollow = follow;
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