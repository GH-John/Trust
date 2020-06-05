package com.application.arenda.entities.serverApi.chat;

public class SendMessageResponse extends ChatError {

    private long idMessage;
    private String message;

    public long getIdMessage() {
        return idMessage;
    }

    public void setIdMessage(long idMessage) {
        this.idMessage = idMessage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}