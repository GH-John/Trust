package com.application.arenda.Entities.Models;

import android.net.Uri;

import com.google.gson.annotations.SerializedName;

public class ModelAllAnnouncement implements IModel {
    @SerializedName("name")
    private String name = "";

    @SerializedName("rating")
    private float rate = 0;

    @SerializedName("countRent")
    private int countRent = 0;

    @SerializedName("countReviews")
    private int countReviews = 0;

    @SerializedName("placementDate")
    private String placementDate;

    @SerializedName("idUser")
    private long idUser = 0;

    @SerializedName("userLogo")
    private Uri userLogoUri;

    @SerializedName("login")
    private String login = "";

    @SerializedName("idAnnouncement")
    private long idAnnouncement = 0;

    @SerializedName("isFavorite")
    private boolean isFavorite = false;

    @SerializedName("costToBYN")
    private float costToBYN = 0.0f;

    @SerializedName("costToUSD")
    private float costToUSD = 0.0f;

    @SerializedName("costToEUR")
    private float costToEUR = 0.0f;

    @SerializedName("photoPath")
    private Uri mainUri;

    @SerializedName("address")
    private String address = "";

    public int getCountReviews() {
        return countReviews;
    }

    public void setCountReviews(int countReviews) {
        this.countReviews = countReviews;
    }

    public Uri getUserLogoUri() {
        return userLogoUri;
    }

    public void setUserLogoUri(Uri userLogoUri) {
        this.userLogoUri = userLogoUri;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Uri getMainUri() {
        return mainUri;
    }

    public void setMainUri(Uri mainUri) {
        this.mainUri = mainUri;
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

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public int getCountRent() {
        return countRent;
    }

    public void setCountRent(int countRent) {
        this.countRent = countRent;
    }

    public String getPlacementDate() {
        return placementDate;
    }

    public void setPlacementDate(String placementDate) {
        this.placementDate = placementDate;
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