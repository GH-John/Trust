package com.application.arenda.entities.serverApi.chat;

public class ResponseConnect {
    private long idRoom;

    public ResponseConnect() {
    }

    public ResponseConnect(long idRoom) {
        this.idRoom = idRoom;
    }

    public long getIdRoom() {
        return idRoom;
    }

    public void setIdRoom(long idRoom) {
        this.idRoom = idRoom;
    }
}