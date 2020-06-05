package com.application.arenda.entities.serverApi.client

enum class CodeHandler(val code: Int) {
    HTTP_NOT_FOUND(404),
    INTERNAL_SERVER_ERROR(500),
    HTTP_VERSION_NOT_SUPPORTED(505),
    WEB_SERVER_IS_DOWN(521),
    NETWORK_ERROR(1000),

    SUCCESS(-2000),
    UNSUCCESS(-2001),
    NONE_REZULT(-2002),
    NOT_CONNECT_TO_DB(-2003),
    UNKNOW_ERROR(-2004),
    PHP_INI_NOT_LOADED(-2005),
    PICTURES_DONT_LOAD(-2006),
    FILES_EMPTY(-2007),
    ERROR_FOLLOW(-2008),
    ERROR_UNFOLLOW(-2009),
    ALLREADY_FOLLOW(-2010),
    ALLREADY_UNFOLLOW(-2011),
    PROPOSAL_NOT_FOUND(-2012),
    FAILED_CREATE_ROOM(-2013),
    CHAT_NOT_FOUND(-2014),
    RECIPIENT_NOT_FOUND(-2015),

    USER_WITH_LOGIN_EXISTS(-1800),
    USER_EXISTS(-1801),
    USER_NOT_FOUND(-1802),
    WRONG_PASSWORD(-1803),
    WRONG_EMAIL_LOGIN(-1804),
    WRONG_TOKEN(-1805);

    companion object {
        @JvmStatic
        operator fun get(code: Int): CodeHandler {
            when (code) {
                404 -> return HTTP_NOT_FOUND
                500 -> return INTERNAL_SERVER_ERROR
                505 -> return HTTP_VERSION_NOT_SUPPORTED
                521 -> return WEB_SERVER_IS_DOWN
                1000 -> return NETWORK_ERROR

                -2000 -> return SUCCESS
                -2001 -> return UNSUCCESS
                -2002 -> return NONE_REZULT
                -2003 -> return NOT_CONNECT_TO_DB
                -2004 -> return UNKNOW_ERROR
                -2005 -> return PHP_INI_NOT_LOADED
                -2006 -> return PICTURES_DONT_LOAD
                -2007 -> return FILES_EMPTY
                -2008 -> return ERROR_FOLLOW
                -2009 -> return ERROR_UNFOLLOW
                -2010 -> return ALLREADY_FOLLOW
                -2011 -> return ALLREADY_UNFOLLOW
                -2012 -> return PROPOSAL_NOT_FOUND
                -2013 -> return FAILED_CREATE_ROOM
                -2014 -> return CHAT_NOT_FOUND
                -2015 -> return RECIPIENT_NOT_FOUND

                -1800 -> return USER_WITH_LOGIN_EXISTS
                -1801 -> return USER_EXISTS
                -1802 -> return USER_NOT_FOUND
                -1803 -> return WRONG_PASSWORD
                -1804 -> return WRONG_EMAIL_LOGIN
                -1805 -> return WRONG_TOKEN
            }
            return UNKNOW_ERROR
        }
    }
}