package com.application.arenda.entities.serverApi.chat;

public enum TypeMessage {
    CHAT_MINE(0),
    CHAT_PARTNER(1),
    USER_JOIN(2),
    USER_LEAVE(3);

    private int type = -1;

    TypeMessage(int type) {
        this.type = type;
    }

    public static TypeMessage get(int type) {
        switch (type) {
            case 0:
                return CHAT_MINE;
            case 1:
                return CHAT_PARTNER;
            case 2:
                return USER_JOIN;
            case 3:
                return USER_LEAVE;
        }

        return CHAT_MINE;
    }

    public int getCode() {
        return type;
    }

}
