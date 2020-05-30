package com.application.arenda.entities.models;

import android.net.Uri;

import org.threeten.bp.LocalTime;

import java.util.ArrayList;
import java.util.List;

public class ModelInsertAnnouncement implements IModel {
    private long ID = 0;
    private int idSubcategory = 0;

    private String name = "";
    private String description = "";

    private float hourlyCost = 0.0f;
    private Currency hourlyCurrency;

    private float dailyCost = 0.0f;
    private Currency dailyCurrency;

    private Uri mainUriBitmap;
    private List<Uri> urisBitmap = new ArrayList<>();

    private String address = "";

    private String phone_1 = "";
    private String phone_2 = "";
    private String phone_3 = "";

    private int minTime = 1;
    private int minDay = 1;
    private int maxRentalPeriod = 1;

    private LocalTime timeOfIssueWith;
    private LocalTime timeOfIssueBy;

    private LocalTime returnTimeWith;
    private LocalTime returnTimeBy;

    private Boolean withSale = false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Currency getHourlyCurrency() {
        return hourlyCurrency;
    }

    public void setHourlyCurrency(Currency hourlyCurrency) {
        this.hourlyCurrency = hourlyCurrency;
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

    public float getHourlyCost() {
        return hourlyCost;
    }

    public void setHourlyCost(float hourlyCost) {
        this.hourlyCost = hourlyCost;
    }

    public List<Uri> getUrisBitmap() {
        return urisBitmap;
    }

    public void setUrisBitmap(List<Uri> urisBitmap) {
        this.urisBitmap = urisBitmap;
    }

    public Uri getMainUri() {
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

    public int getMinTime() {
        return minTime;
    }

    public void setMinTime(int minTime) {
        this.minTime = minTime;
    }

    public int getMinDay() {
        return minDay;
    }

    public void setMinDay(int minDay) {
        this.minDay = minDay;
    }

    public int getMaxRentalPeriod() {
        return maxRentalPeriod;
    }

    public void setMaxRentalPeriod(int maxRentalPeriod) {
        this.maxRentalPeriod = maxRentalPeriod;
    }

    public LocalTime getTimeOfIssueWith() {
        return timeOfIssueWith;
    }

    public void setTimeOfIssueWith(LocalTime timeOfIssueWith) {
        this.timeOfIssueWith = timeOfIssueWith;
    }

    public LocalTime getTimeOfIssueBy() {
        return timeOfIssueBy;
    }

    public void setTimeOfIssueBy(LocalTime timeOfIssueBy) {
        this.timeOfIssueBy = timeOfIssueBy;
    }

    public LocalTime getReturnTimeWith() {
        return returnTimeWith;
    }

    public void setReturnTimeWith(LocalTime returnTimeWith) {
        this.returnTimeWith = returnTimeWith;
    }

    public LocalTime getReturnTimeBy() {
        return returnTimeBy;
    }

    public void setReturnTimeBy(LocalTime returnTimeBy) {
        this.returnTimeBy = returnTimeBy;
    }

    public Boolean isWithSale() {
        return withSale;
    }

    public void setWithSale(Boolean withSale) {
        this.withSale = withSale;
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