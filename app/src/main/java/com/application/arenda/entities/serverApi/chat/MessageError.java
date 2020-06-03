package com.application.arenda.entities.serverApi.chat;

public class MessageError extends ChatError {

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}