package com.application.arenda.Entities.Announcements.Models;

import android.net.Uri;

import com.application.arenda.Entities.Announcements.ViewAnnouncement.DialogFragment.ModelPhoneNumber;

import java.util.ArrayList;
import java.util.List;

public class ModelViewAnnouncement implements IModel {
    private long idUser = 0;
    private long idAnnouncement = 0;

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

    private List<ModelPhoneNumber> phoneNumbers = new ArrayList<>();

    private List<Uri> uriCollection = new ArrayList<>();

    public long getIdUser() {
        return idUser;
    }

    public void setIdUser(long idUser) {
        this.idUser = idUser;
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

    public List<ModelPhoneNumber> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(List<ModelPhoneNumber> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
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