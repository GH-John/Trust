package com.application.arenda.entities.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "users")
public class ModelUser implements IModel {
    @PrimaryKey
    @ColumnInfo(index = true)
    @SerializedName("idUser")
    private long idUser = 0;

    @SerializedName("token")
    private String token = "";

    @SerializedName("name")
    private String name = "";

    @SerializedName("lastName")
    private String lastName = "";

    @SerializedName("login")
    private String login = "";

    @SerializedName("email")
    private String email = "";

    @SerializedName("userLogo")
    private String userLogo = "";

    @SerializedName("address_1")
    private String address_1 = "";

    @SerializedName("address_2")
    private String address_2 = "";

    @SerializedName("address_3")
    private String address_3 = "";

    @SerializedName("phone_1")
    private String phone_1 = "";

    @SerializedName("phone_2")
    private String phone_2 = "";

    @SerializedName("phone_3")
    private String phone_3 = "";

    @SerializedName("accountType")
    private String accountType;

    @SerializedName("balance")
    private float balance = 0.0f;

    @SerializedName("rating")
    private float rating = 0.0f;

    @SerializedName("statusUser")
    private String statusUser = "";

    @SerializedName("created")
    private String created;

    @SerializedName("updated")
    private String updated;

    @Expose(deserialize = false, serialize = false)
    private boolean isCurrent;

    public long getIdUser() {
        return idUser;
    }

    public void setIdUser(long idUser) {
        this.idUser = idUser;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public boolean isCurrent() {
        return isCurrent;
    }

    public void setCurrent(boolean current) {
        isCurrent = current;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getStatusUser() {
        return statusUser;
    }

    public void setStatusUser(String statusUser) {
        this.statusUser = statusUser;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getAddress_1() {
        return address_1;
    }

    public void setAddress_1(String address_1) {
        this.address_1 = address_1;
    }

    public String getAddress_2() {
        return address_2;
    }

    public void setAddress_2(String address_2) {
        this.address_2 = address_2;
    }

    public String getAddress_3() {
        return address_3;
    }

    public void setAddress_3(String address_3) {
        this.address_3 = address_3;
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

    public String getUserLogo() {
        return userLogo;
    }

    public void setUserLogo(String userLogo) {
        this.userLogo = userLogo;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
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
        return idUser;
    }

    @Override
    public void setID(long id) {
        this.idUser = id;
    }
}
