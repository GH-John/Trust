package com.application.arenda.entities.serverApi.client;

public enum CodeHandler {

    HTTP_NOT_FOUND(404),
    INTERNAL_SERVER_ERROR(500),
    HTTP_VERSION_NOT_SUPPORTED(505),
    WEB_SERVER_IS_DOWN(521),

    NETWORK_ERROR(1000),

    SUCCESS(2000),
    UNSUCCESS(2001),

    NONE_REZULT(2002),
    NOT_CONNECT_TO_DB(2003),
    UNKNOW_ERROR(2004),
    PHP_INI_NOT_LOADED(2005),
    PICTURES_DONT_LOAD(2006),
    FILES_EMPTY(2007),

    ERROR_FOLLOW(2008),
    ERROR_UNFOLLOW(2009),
    ALLREADY_FOLLOW(2010),
    ALLREADY_UNFOLLOW(2011),
    PROPOSAL_NOT_FOUND(2012),

    USER_WITH_LOGIN_EXISTS(1800),
    USER_EXISTS(1801),
    USER_NOT_FOUND(1802),
    WRONG_PASSWORD(1803),
    WRONG_EMAIL_LOGIN(1804),
    WRONG_TOKEN(1805);

    private int code;

    CodeHandler(int code) {
        this.code = code;
    }

    public static CodeHandler get(int code) {
        switch (code) {
            case 2000:
                return SUCCESS;
            case 2001:
                return UNSUCCESS;
            case 2002:
                return NONE_REZULT;
            case 2003:
                return NOT_CONNECT_TO_DB;
            case 2004:
                return UNKNOW_ERROR;
            case 2005:
                return PHP_INI_NOT_LOADED;

            case 1800:
                return USER_WITH_LOGIN_EXISTS;
            case 1801:
                return USER_EXISTS;
            case 1802:
                return USER_NOT_FOUND;
            case 1803:
                return WRONG_PASSWORD;
            case 1804:
                return WRONG_EMAIL_LOGIN;
            case 1805:
                return WRONG_TOKEN;
        }

        return UNKNOW_ERROR;
    }

    public int getCode() {
        return code;
    }
}