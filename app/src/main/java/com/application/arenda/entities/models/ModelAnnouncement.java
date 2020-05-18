package com.application.arenda.entities.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "announcements")
public class ModelAnnouncement implements IModel {

    @PrimaryKey
    @ColumnInfo(index = true)
    @SerializedName("idAnnouncement")
    private long idAnnouncement = 0;

    @SerializedName("idUser")
    private long idUser = 0;

    @SerializedName("idSubcategory")
    private long idSubcategory = 0;

    @SerializedName("name")
    private String name = "";

    @SerializedName("description")
    private String description = "";

    @SerializedName("announcementRating")
    private float announcementRating = 0;

    @SerializedName("countRent")
    private int countRent = 0;

    @SerializedName("countReviews")
    private int countReviews = 0;

    @SerializedName("countViewers")
    private int countViewers = 0;

    @SerializedName("countFavorites")
    private int countFavorites = 0;

    @SerializedName("announcementCreated")
    private String announcementCreated;

    @SerializedName("announcementUpdated")
    private String announcementUpdated;

    @SerializedName("userLogo")
    private String userLogo;

    @SerializedName("login")
    private String login = "";

    @SerializedName("userRating")
    private float userRating = 0;

    @SerializedName("userCreated")
    private String userCreated;

    @SerializedName("countAnnouncementsUser")
    private int countAnnouncementsUser = 0;

    @SerializedName("isFavorite")
    private boolean isFavorite = false;

    @SerializedName("costToBYN")
    private float costToBYN = 0.0f;

    @SerializedName("costToUSD")
    private float costToUSD = 0.0f;

    @SerializedName("costToEUR")
    private float costToEUR = 0.0f;

    @SerializedName("address")
    private String address = "";

    @SerializedName("phone_1")
    private String phone_1 = "";

    @SerializedName("phone_2")
    private String phone_2 = "";

    @SerializedName("phone_3")
    private String phone_3 = "";

    @SerializedName("pictures")
    @Ignore
    private List<ModelPicture> pictures = new ArrayList<>();

    public long getIdAnnouncement() {
        return idAnnouncement;
    }

    public void setIdAnnouncement(long idAnnouncement) {
        this.idAnnouncement = idAnnouncement;
    }

    public long getIdSubcategory() {
        return idSubcategory;
    }

    public void setIdSubcategory(long idSubcategory) {
        this.idSubcategory = idSubcategory;
    }

    public String getUserCreated() {
        return userCreated;
    }

    public void setUserCreated(String userCreated) {
        this.userCreated = userCreated;
    }

    public String getAnnouncementUpdated() {
        return announcementUpdated;
    }

    public void setAnnouncementUpdated(String announcementUpdated) {
        this.announcementUpdated = announcementUpdated;
    }

    public int getCountAnnouncementsUser() {
        return countAnnouncementsUser;
    }

    public void setCountAnnouncementsUser(int countAnnouncementsUser) {
        this.countAnnouncementsUser = countAnnouncementsUser;
    }

    public float getUserRating() {
        return userRating;
    }

    public void setUserRating(float userRating) {
        this.userRating = userRating;
    }

    public List<ModelPicture> getPictures() {
        return pictures;
    }

    public void setPictures(List<ModelPicture> pictures) {
        this.pictures = pictures;
    }

    public int getCountFavorites() {
        return countFavorites;
    }

    public void setCountFavorites(int countFavorites) {
        this.countFavorites = countFavorites;
    }

    public int getCountViewers() {
        return countViewers;
    }

    public void setCountViewers(int countViewers) {
        this.countViewers = countViewers;
    }

    public String getPhone_1() {
        return phone_1;
    }

    public void setPhone_1(String phone_1) {
        this.phone_1 = phone_1;
    }

    public String getPhone_2() {
        return phone_2;
    }

    public void setPhone_2(String phone_2) {
        this.phone_2 = phone_2;
    }

    public String getPhone_3() {
        return phone_3;
    }

    public void setPhone_3(String phone_3) {
        this.phone_3 = phone_3;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCountReviews() {
        return countReviews;
    }

    public void setCountReviews(int countReviews) {
        this.countReviews = countReviews;
    }

    public String getUserLogo() {
        return userLogo;
    }

    public void setUserLogo(String userLogo) {
        this.userLogo = userLogo;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getAnnouncementRating() {
        return announcementRating;
    }

    public void setAnnouncementRating(float announcementRating) {
        this.announcementRating = announcementRating;
    }

    public int getCountRent() {
        return countRent;
    }

    public void setCountRent(int countRent) {
        this.countRent = countRent;
    }

    public String getAnnouncementCreated() {
        return announcementCreated;
    }

    public void setAnnouncementCreated(String announcementCreated) {
        this.announcementCreated = announcementCreated;
    }

    public long getIdUser() {
        return idUser;
    }

    public void setIdUser(long idUser) {
        this.idUser = idUser;
    }

    public float getCostToBYN() {
        return costToBYN;
    }

    public void setCostToBYN(float costToBYN) {
        this.costToBYN = costToBYN;
    }

    public float getCostToUSD() {
        return costToUSD;
    }

    public void setCostToUSD(float costToUSD) {
        this.costToUSD = costToUSD;
    }

    public float getCostToEUR() {
        return costToEUR;
    }

    public void setCostToEUR(float costToEUR) {
        this.costToEUR = costToEUR;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public long getID() {
        return idAnnouncement;
    }

    @Override
    public void setID(long id) {
        idAnnouncement = id;
    }
}