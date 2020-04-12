package com.application.arenda.Entities.Authentication;

public enum AuthenticationCodes {
    USER_SUCCESS_REGISTERED,
    USER_UNSUCCESS_REGISTERED,
    USER_EXISTS,

    USER_LOGGED,
    WRONG_PASSWORD,
    WRONG_EMAIL,
    WRONG_TOKEN,

    NETWORK_ERROR,
    NOT_CONNECT_TO_DB,
    UNKNOW_ERROR
}