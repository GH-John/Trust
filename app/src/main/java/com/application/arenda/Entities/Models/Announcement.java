package com.application.arenda.Entities.Models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Announcement extends BackendlessTable {
    private String name = "";
    private String address = "";
    private String description = "";

    private List<Map> subcategories = new ArrayList<>();

    private Integer countRent = 0;
    private Integer countViewers = 0;
    private Integer countFavorites = 0;

    private String phone_1 = "";
    private String phone_2 = "";
    private String phone_3 = "";

    private Double costToBYN = 0.0;
    private Double costToUSD = 0.0;
    private Double costToEUR = 0.0;

    private Double profit = 0.0;
    private Double rating = 0.0;

    private String statusControl = "";
    private Boolean statusRent = false;

    public static Map convertToMap(Announcement announcement) {
        Map<String, Object> map = new HashMap();

        if(announcement == null)
            return map;

//        map.put("ownerId", announcement.getOwnerId());

        map.put("name", announcement.getName());
        map.put("address", announcement.getAddress());
        map.put("description", announcement.getDescription());

        map.put("phone_1", announcement.getPhone_1());
        map.put("phone_2", announcement.getPhone_2());
        map.put("phone_3", announcement.getPhone_3());

        map.put("costToBYN", announcement.getCostToBYN());
        map.put("costToUSD", announcement.getCostToUSD());
        map.put("costToEUR", announcement.getCostToEUR());

        return map;
    }

    public List<Map> getSubcategories() {
        return subcategories;
    }

    public void addSubcategory(String idSubcategory) {
        Map<String, Object> map = new HashMap<>();
        map.put("objectId", idSubcategory);

        subcategories.add(map);
    }

    public String getPhone_2() {
        return phone_2;
    }

    public void setPhone_2(String phone_2) {
        this.phone_2 = phone_2;
    }

    public Boolean getStatusRent() {
        return statusRent;
    }

    public void setStatusRent(Boolean statusRent) {
        this.statusRent = statusRent;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getCountRent() {
        return countRent;
    }

    public void setCountRent(Integer countRent) {
        this.countRent = countRent;
    }

    public Double getCostToUSD() {
        return costToUSD;
    }

    public void setCostToUSD(Double costToUSD) {
        this.costToUSD = costToUSD;
    }

    public Integer getCountFavorites() {
        return countFavorites;
    }

    public void setCountFavorites(Integer countFavorites) {
        this.countFavorites = countFavorites;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCountViewers() {
        return countViewers;
    }

    public void setCountViewers(Integer countViewers) {
        this.countViewers = countViewers;
    }

    public String getPhone_1() {
        return phone_1;
    }

    public void setPhone_1(String phone_1) {
        this.phone_1 = phone_1;
    }

    public String getStatusControl() {
        return statusControl;
    }

    public void setStatusControl(String statusControl) {
        this.statusControl = statusControl;
    }

    public String getPhone_3() {
        return phone_3;
    }

    public void setPhone_3(String phone_3) {
        this.phone_3 = phone_3;
    }

    public Double getCostToEUR() {
        return costToEUR;
    }

    public void setCostToEUR(Double costToEUR) {
        this.costToEUR = costToEUR;
    }

    public Double getCostToBYN() {
        return costToBYN;
    }

    public void setCostToBYN(Double costToBYN) {
        this.costToBYN = costToBYN;
    }

    public Double getProfit() {
        return profit;
    }

    public void setProfit(Double profit) {
        this.profit = profit;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }
}