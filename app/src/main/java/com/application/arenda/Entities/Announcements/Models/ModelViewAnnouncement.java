package com.application.arenda.Entities.Announcements.Models;

import android.net.Uri;

import java.util.List;

public class ModelViewAnnouncement {
    private int idUser = 0;
    private int idAnnouncement = 0;

    private String name = "";
    private String description = "";
    private int idSubcategory = 0;

    private boolean isFavorite = false;

    private float rate = 0;
    private int countRent = 0;
    private boolean statusRent = false;

    private float costToBYN = 0.0f;
    private float costToUSD = 0.0f;
    private float costToEUR = 0.0f;

    private String address = "";
    private String placementDate = "";

    private String phone_1 = "";
    private String phone_2 = "";
    private String phone_3 = "";

    private boolean visiblePhone_1 = false;
    private boolean visiblePhone_2 = false;
    private boolean visiblePhone_3 = false;

    private List<Uri> uriCollection;

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getIdAnnouncement() {
        return idAnnouncement;
    }

    public void setIdAnnouncement(int idAnnouncement) {
        this.idAnnouncement = idAnnouncement;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIdSubcategory() {
        return idSubcategory;
    }

    public void setIdSubcategory(int idSubcategory) {
        this.idSubcategory = idSubcategory;
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

    public boolean isStatusRent() {
        return statusRent;
    }

    public void setStatusRent(boolean statusRent) {
        this.statusRent = statusRent;
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

    public boolean isVisiblePhone_1() {
        return visiblePhone_1;
    }

    public void setVisiblePhone_1(boolean visiblePhone_1) {
        this.visiblePhone_1 = visiblePhone_1;
    }

    public boolean isVisiblePhone_2() {
        return visiblePhone_2;
    }

    public void setVisiblePhone_2(boolean visiblePhone_2) {
        this.visiblePhone_2 = visiblePhone_2;
    }

    public boolean isVisiblePhone_3() {
        return visiblePhone_3;
    }

    public void setVisiblePhone_3(boolean visiblePhone_3) {
        this.visiblePhone_3 = visiblePhone_3;
    }

    public List<Uri> getUriCollection() {
        return uriCollection;
    }

    public void setUriCollection(List<Uri> uriCollection) {
        this.uriCollection = uriCollection;
    }

    public String getPlacementDate() {
        return placementDate;
    }

    public void setPlacementDate(String placementDate) {
        this.placementDate = placementDate;
    }
}