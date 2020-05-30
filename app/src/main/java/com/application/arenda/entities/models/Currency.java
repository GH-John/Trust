package com.application.arenda.entities.models;

public enum Currency {
    USD("USD"),
    EUR("EUR"),
    BYN("BYN"),
    RUB("RUB");

    private String currency;

    Currency(String currency) {
        this.currency = currency;
    }

    public static Currency get(String currency) {
        switch (currency) {
            case "USD":
                return USD;
            case "EUR":
                return EUR;
            case "BYN":
                return BYN;
            case "RUB":
                return RUB;
        }

        return USD;
    }

    public String getCurrency() {
        return currency;
    }
}
