package com.application.arenda.entities.models;

public class ModelMessage implements IModel {
    private int type;
    private String message;
    private String userName;
    private String roomName;

    private long ID;

    public ModelMessage() {
    }

    public ModelMessage(int type, String message, String userName, String roomName) {
        this.type = type;
        this.message = message;
        this.userName = userName;
        this.roomName = roomName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    @Override
    public long getID() {
        return ID;
    }

    @Override
    public void setID(long id) {
        ID = id;
    }

    public enum Type {
        CHAT_MINE(0),
        CHAT_PARTNER(1),
        USER_JOIN(2),
        USER_LEAVE(3);

        private int type = -1;

        Type(int type) {
            this.type = type;
        }

        public static Type get(int type) {
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

        public int getType() {
            return type;
        }
    }
}
