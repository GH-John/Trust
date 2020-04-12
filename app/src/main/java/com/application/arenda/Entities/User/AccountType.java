package com.application.arenda.Entities.User;

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