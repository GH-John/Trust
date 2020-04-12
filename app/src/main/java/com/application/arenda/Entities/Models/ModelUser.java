package com.application.arenda.Entities.Models;

import com.application.arenda.Entities.User.AccountType;

import java.util.ArrayList;
import java.util.List;

public class ModelUser implements IModel {
    private long id = 0;

    private String name = "";
    private String lastName = "";

    private String email = "";
    private String userPhoto = "";

    private List<ModelAddress> addresses = new ArrayList<>();
    private List<ModelPhoneNumber> phoneNumbers = new ArrayList<>();

    private AccountType accountType;
    private float balance = 0.0f;
    private float rating = 0.0f;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public List<ModelAddress> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<ModelAddress> addresses) {
        this.addresses = addresses;
    }

    public List<ModelPhoneNumber> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(List<ModelPhoneNumber> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    @Override
    public long getID() {
        return id;
    }

    @Override
    public void setID(long id) {
        this.id = id;
    }
}
