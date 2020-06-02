package com.application.arenda.entities.serverApi.chat;

public class SendMessage {
    private String userToken;
    private long idUser_To;
    private long idRoom;
    private String message;

    public SendMessage() {
    }

    public SendMessage(String userToken, long idUser_To, long idRoom, String message) {
        this.userToken = userToken;
        this.idUser_To = idUser_To;
        this.idRoom = idRoom;
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

    public long getIdRoom() {
        return idRoom;
    }

    public void setIdRoom(long idRoom) {
        this.idRoom = idRoom;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}