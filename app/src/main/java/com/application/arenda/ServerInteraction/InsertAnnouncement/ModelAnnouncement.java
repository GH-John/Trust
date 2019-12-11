package com.application.arenda.ServerInteraction.InsertAnnouncement;

import android.graphics.Bitmap;
import android.net.Uri;

import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ModelAnnouncement {
    private String name = "";
    private String description = "";

    private int rating = 0;
    private int countRent = 0;
    private boolean statusRent = false;
    private String placementDate;

    private int idUser = 0;
    private int idSubcategory = 0;
    private int idAnnouncement = 0;

    private float costToBYN = 0.0f;
    private float costToUSD = 0.0f;
    private float costToEUR = 0.0f;

    private Bitmap bitmap;
    private Uri mainUriBitmap;
    private Collection<URL> urlCollection;
    private Map<Uri, Bitmap> mapBitmap = new HashMap<>();

    private String location = "";

    private String phone_1 = "";
    private String phone_2 = "";
    private String phone_3 = "";

    private boolean visiblePhone_1 = false;
    private boolean visiblePhone_2 = false;
    private boolean visiblePhone_3 = false;

    public Uri getMainUriBitmap() {
        return mainUriBitmap;
    }

    public void setMainUriBitmap(Uri mainUriBitmap) {
        this.mainUriBitmap = mainUriBitmap;
    }

    public int getIdAnnouncement() {
        return idAnnouncement;
    }

    public void setIdAnnouncement(int idAnnouncement) {
        this.idAnnouncement = idAnnouncement;
    }

    public Map<Uri, Bitmap> getMapBitmap() {
        return mapBitmap;
    }

    public void setMapBitmap(Map<Uri, Bitmap> mapBitmap) {
        this.mapBitmap = mapBitmap;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
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

    public String getPlacementDate() {
        return placementDate;
    }

    public void setPlacementDate(String placementDate) {
        this.placementDate = placementDate;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public Bitmap getMainBitmap() {
        return bitmap;
    }

    public void setMainBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Collection<URL> getUrlCollection() {
        return urlCollection;
    }

    public void setUrlCollection(Collection<URL> urlCollection) {
        this.urlCollection = urlCollection;
    }

    public int getIdSubcategory() {
        return idSubcategory;
    }

    public void setIdSubcategory(int idSubcategory) {
        this.idSubcategory = idSubcategory;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public boolean getVisiblePhone_1() {
        return visiblePhone_1;
    }

    public void setVisiblePhone_1(boolean visiblePhone_1) {
        this.visiblePhone_1 = visiblePhone_1;
    }

    public boolean getVisiblePhone_2() {
        return visiblePhone_2;
    }

    public void setVisiblePhone_2(boolean visiblePhone_2) {
        this.visiblePhone_2 = visiblePhone_2;
    }

    public boolean getVisiblePhone_3() {
        return visiblePhone_3;
    }

    public void setVisiblePhone_3(boolean visiblePhone_3) {
        this.visiblePhone_3 = visiblePhone_3;
    }
}