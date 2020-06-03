package com.application.arenda.entities.serverApi.chat;

public class ResponseConnect {
    private String room;

    public ResponseConnect() {
    }

    public ResponseConnect(String room) {
        this.room = room;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }
}