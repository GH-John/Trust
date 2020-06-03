package com.application.arenda.entities.serverApi.chat;

public class SendMessage {
    private String userToken;
    private long idUser_To;
    private String room;
    private String message;

    public SendMessage() {
    }

    public SendMessage(String userToken, long idUser_To, String room, String message) {
        this.userToken = userToken;
        this.idUser_To = idUser_To;
        this.room = room;
        this.message = message;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public long getIdUser_To() {
        return idUser_To;
    }

    public void setIdUser_To(long idUser_To) {
        this.idUser_To = idUser_To;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}