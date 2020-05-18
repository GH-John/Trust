package com.application.arenda.entities.user;

public enum AccountType {
    PRIVATE_PERSON("PRIVATE_PERSON"),
    BUSINESS_PERSON("BUSINESS_PERSON");

    private String type = "";

    AccountType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}