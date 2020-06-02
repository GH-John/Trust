package com.application.arenda.entities.models;

import com.application.arenda.entities.serverApi.client.CodeHandler;

public class ModelMessage implements IModel {
    private int type;
    private CodeHandler codeHandler;
    private String message;
    private String error;

    private long ID;

    public ModelMessage() {
    }

    public ModelMessage(int type, String message) {
        this.type = type;
        this.message = message;
    }

    public ModelMessage(int type, CodeHandler codeHandler, String message, String error) {
        this.type = type;
        this.codeHandler = codeHandler;
        this.message = message;
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public CodeHandler getCodeHandler() {
        return codeHandler;
    }

    public void setCodeHandler(CodeHandler codeHandler) {
        this.codeHandler = codeHandler;
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

    @Override
    public String toString() {
        return "ModelMessage{" +
                "type=" + type +
                ", codeHandler=" + codeHandler +
                ", message='" + message + '\'' +
                ", error='" + error + '\'' +
                ", ID=" + ID +
                '}';
    }
}