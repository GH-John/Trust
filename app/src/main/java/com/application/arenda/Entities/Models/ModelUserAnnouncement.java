package com.application.arenda.Entities.Models;

import android.graphics.Bitmap;
import android.net.Uri;

public class ModelUserAnnouncement implements IModel {
    private String name = "";

    private float rate = 0;
    private int countRent = 0;
    private int countViewers = 0;
    private int countFavorites = 0;
    private String placementDate;

    private long idUser = 0;
    private long idAnnouncement = 0;

    private float costToBYN = 0.0f;
    private float costToUSD = 0.0f;
    private float costToEUR = 0.0f;

    private Bitmap bitmap;
    private Uri mainUriBitmap;

    private String address = "";

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

    public int getCountViewers() {
        return countViewers;
    }

    public void setCountViewers(int countViewers) {
        this.countViewers = countViewers;
    }

    public int getCountFavorites() {
        return countFavorites;
    }

    public void setCountFavorites(int countFavorites) {
        this.countFavorites = countFavorites;
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

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Uri getMainUriBitmap() {
        return mainUriBitmap;
    }

    public void setMainUriBitmap(Uri mainUriBitmap) {
        this.mainUriBitmap = mainUriBitmap;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public void setID(long id) {
        idAnnouncement = id;
    }

    @Override
    public long getID() {
        return idAnnouncement;
    }
}